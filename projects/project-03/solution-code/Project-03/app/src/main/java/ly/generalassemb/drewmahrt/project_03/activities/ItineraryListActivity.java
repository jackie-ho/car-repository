package ly.generalassemb.drewmahrt.project_03.activities;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;

import java.util.Arrays;

import ly.generalassemb.drewmahrt.project_03.services.HighlightLocationService;
import ly.generalassemb.drewmahrt.project_03.ItineraryContentProvider;
import ly.generalassemb.drewmahrt.project_03.ItinerarySQLiteOpenHelper;
import ly.generalassemb.drewmahrt.project_03.R;

public class ItineraryListActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 100;
    private Context mContext = this;

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "ly.generalassemb.drewmahrt.project_03.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "ly.generalassemb.drewmahrt.project_03.auth";
    // A content resolver for accessing the provider
    private ContentResolver mResolver;

    private SimpleCursorAdapter mCursorAdapter;

    private Intent mServiceIntent;

    //Google Drive Authentication
    public static GoogleAccountCredential mCredential;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 15;
    private static final String[] SCOPES = {DriveScopes.DRIVE_APPDATA,"https://www.googleapis.com/auth/drive.appfolder"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mResolver = getContentResolver();
        TableObserver observer = new TableObserver(null);
        mResolver.registerContentObserver(Uri.parse(ItineraryContentProvider.ITINERARIES_CONTENT_URI), true, observer);
        mResolver.registerContentObserver(Uri.parse(ItineraryContentProvider.EVENTS_CONTENT_URI), true, observer);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        int[] to = new int[]{
                R.id.itinerary_title_text_view,
                R.id.itinerary_date_text_view
        };

        String[] from = new String[]{
                ItinerarySQLiteOpenHelper.COL_LIST_TITLE,
                ItinerarySQLiteOpenHelper.COL_LIST_STARTING_DATE
        };

        //Permission checking for 6.0+
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);
        }else{
            // Initialize credentials and service object.
            SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
            mCredential = GoogleAccountCredential.usingOAuth2(
                    getApplicationContext(), Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff());
            String acctName = settings.getString(PREF_ACCOUNT_NAME,null);
            mCredential.setSelectedAccountName(acctName);
        }

        permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mServiceIntent = new Intent(getApplicationContext(), HighlightLocationService.class);
            mServiceIntent.putExtra("latExtra",location.getLatitude());
            mServiceIntent.putExtra("lngExtra",location.getLongitude());
            getApplicationContext().startService(mServiceIntent);
        }

        Cursor cursor = getContentResolver().query(Uri.parse(ItineraryContentProvider.ITINERARIES_CONTENT_URI), null, null, null, null);
        mCursorAdapter = new SimpleCursorAdapter(this, R.layout.itinerary_list_item, cursor, from, to, 0);

        ListView listView = (ListView) findViewById(R.id.itinerary_list_view);
        TextView emptyTextView = (TextView) findViewById(R.id.empty_list_text_view);
        listView.setAdapter(mCursorAdapter);
        listView.setEmptyView(emptyTextView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(mContext, EventListActivity.class);
                Cursor selectedCursor = (Cursor) parent.getAdapter().getItem(position);
                int listId = selectedCursor.getInt(selectedCursor.getColumnIndex(ItinerarySQLiteOpenHelper.COL_ID));
                intent.putExtra("listId", listId);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor itineraryCursor = (Cursor)parent.getAdapter().getItem(position);
                String title = itineraryCursor.getString(itineraryCursor.getColumnIndex(ItinerarySQLiteOpenHelper.COL_LIST_TITLE));
                String location = itineraryCursor.getString(itineraryCursor.getColumnIndex(ItinerarySQLiteOpenHelper.COL_LIST_STARTING_LOC));
                String date = itineraryCursor.getString(itineraryCursor.getColumnIndex(ItinerarySQLiteOpenHelper.COL_LIST_STARTING_DATE));
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Join me on my trip to "+location+" on "+date+"! #"+title);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            }
        });

        //Force sync on create
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED,true);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        if(mCredential != null && mCredential.getSelectedAccountName() != null)
            ContentResolver.requestSync(mCredential.getSelectedAccount(), AUTHORITY, extras);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_ACCOUNTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Initialize credentials and service object.
                    SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                    mCredential = GoogleAccountCredential.usingOAuth2(
                            getApplicationContext(), Arrays.asList(SCOPES))
                            .setBackOff(new ExponentialBackOff());
                    String acctName = settings.getString(PREF_ACCOUNT_NAME,null);
                    mCredential.setSelectedAccountName(acctName);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    mServiceIntent = new Intent(getApplicationContext(), HighlightLocationService.class);
                    mServiceIntent.putExtra("latExtra",location.getLatitude());
                    mServiceIntent.putExtra("lngExtra",location.getLongitude());
                    getApplicationContext().startService(mServiceIntent);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onPause() {
        SharedPreferences previewSizePref = getSharedPreferences("PREF", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = previewSizePref.edit();
        super.onPause();
    }

    public void showInputDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.itinerary_input_dialog, null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Itinerary");
        dialogBuilder.setMessage(null);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText titleEditText = (EditText) dialogView.findViewById(R.id.title_text);
                EditText locationEditText = (EditText) dialogView.findViewById(R.id.location_text);
                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

                if (titleEditText.getText().toString().length() == 0 || locationEditText.getText().toString().length() == 0 || datePicker.toString().length() == 0) {
                    Toast.makeText(mContext, "Please enter a list title, date, and location.", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(ItinerarySQLiteOpenHelper.COL_LIST_TITLE, titleEditText.getText().toString());
                    values.put(ItinerarySQLiteOpenHelper.COL_LIST_STARTING_LOC, locationEditText.getText().toString());
                    values.put(ItinerarySQLiteOpenHelper.COL_LIST_STARTING_DATE, (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear());

                    getContentResolver().insert(Uri.parse(ItineraryContentProvider.ITINERARIES_CONTENT_URI), values);
                    Cursor cursor = getContentResolver().query(Uri.parse(ItineraryContentProvider.ITINERARIES_CONTENT_URI), null, null, null, null);
                    mCursorAdapter.swapCursor(cursor);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.explore_button) {
            Intent intent = new Intent(this,PointsOfInterestActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    //NO ACCOUNT CHOSEN
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    chooseAccount();
                }
                break;
        }
    }

    private void refreshResults() {
        if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if(!isDeviceOnline()) {
            Toast.makeText(this,"No network connection available.",Toast.LENGTH_LONG).show();
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                ItineraryListActivity.this,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }


    private void chooseAccount() {
        startActivityForResult(
                mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (isGooglePlayServicesAvailable()) {
                refreshResults();
            } else {
                Toast.makeText(this, "Google Play Services required: " +
                        "after installing, close and relaunch this app.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class TableObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public TableObserver(Handler handler) {
            super(handler);
        }

        /*
         * Define a method that's called when data in the
         * observed content provider changes.
         * This method signature is provided for compatibility with
         * older platforms.
         */
        @Override
        public void onChange(boolean selfChange) {
            /*
             * Invoke the method signature available as of
             * Android platform version 4.1, with a null URI.
             */
            onChange(selfChange, null);
        }

        /*
         * Define a method that's called when data in the
         * observed content provider changes.
         */
        @Override
        public void onChange(boolean selfChange, Uri changeUri) {
            /*
             * Ask the framework to run your sync adapter.
             * To maintain backward compatibility, assume that
             * changeUri is null.
             */
            Bundle extras = new Bundle();
            ContentResolver.requestSync(mCredential.getSelectedAccount(), AUTHORITY, extras);
        }
    }
}
