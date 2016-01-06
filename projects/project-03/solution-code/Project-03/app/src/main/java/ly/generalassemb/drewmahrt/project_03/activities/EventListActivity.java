package ly.generalassemb.drewmahrt.project_03.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ly.generalassemb.drewmahrt.project_03.ItineraryContentProvider;
import ly.generalassemb.drewmahrt.project_03.ItinerarySQLiteOpenHelper;
import ly.generalassemb.drewmahrt.project_03.R;

public class EventListActivity extends AppCompatActivity {
    private ContentResolver mResolver;
    private Context mContext = this;
    private int mItineraryId;
    private SimpleCursorAdapter mCursorAdapter;
    private ContentValues tempValues;

    public static final String AUTHORITY = "ly.generalassemb.drewmahrt.project_03.provider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempValues = null;
                showInputDialog();
            }
        });

        mResolver = getContentResolver();
        mItineraryId = getIntent().getIntExtra("listId",-1);

        if(mItineraryId != -1){
            Cursor itineraryCursor = getContentResolver().query(Uri.parse(ItineraryContentProvider.ITINERARIES_CONTENT_URI+"/"+mItineraryId), null, null, null, null);
            itineraryCursor.moveToFirst();
            toolbar.setTitle(itineraryCursor.getString(itineraryCursor.getColumnIndex(ItinerarySQLiteOpenHelper.COL_LIST_TITLE)));

            int[] to = new int[]{
                    R.id.event_title_text_view,
                    R.id.event_date_text_view
            };

            String[] from = new String[]{
                    ItinerarySQLiteOpenHelper.COL_EVENT_TITLE,
                    ItinerarySQLiteOpenHelper.COL_EVENT_DATE
            };

            Cursor eventsCursor = getContentResolver().query(Uri.parse(ItineraryContentProvider.EVENTS_CONTENT_URI+"/"+mItineraryId),null,null,null,null);
            mCursorAdapter = new SimpleCursorAdapter(this, R.layout.event_list_item, eventsCursor, from, to, 0);

            ListView listView = (ListView) findViewById(R.id.events_list_view);
            TextView emptyTextView = (TextView) findViewById(R.id.empty_event_text_view);
            listView.setAdapter(mCursorAdapter);
            listView.setEmptyView(emptyTextView);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, EventListActivity.class);
                    Cursor selectedCursor = (Cursor) parent.getAdapter().getItem(position);
                    tempValues = new ContentValues();
                    tempValues.put(ItinerarySQLiteOpenHelper.COL_ID,selectedCursor.getInt(selectedCursor.getColumnIndex(ItinerarySQLiteOpenHelper.COL_ID)));
                    tempValues.put(ItinerarySQLiteOpenHelper.COL_LIST_ID,selectedCursor.getInt(selectedCursor.getColumnIndex(ItinerarySQLiteOpenHelper.COL_LIST_ID)));
                    tempValues.put(ItinerarySQLiteOpenHelper.COL_EVENT_TITLE,selectedCursor.getString(selectedCursor.getColumnIndex(ItinerarySQLiteOpenHelper.COL_EVENT_TITLE)));
                    tempValues.put(ItinerarySQLiteOpenHelper.COL_EVENT_DESCRIPTION,selectedCursor.getString(selectedCursor.getColumnIndex(ItinerarySQLiteOpenHelper.COL_EVENT_DESCRIPTION)));
                    tempValues.put(ItinerarySQLiteOpenHelper.COL_EVENT_LOCATION,selectedCursor.getString(selectedCursor.getColumnIndex(ItinerarySQLiteOpenHelper.COL_EVENT_LOCATION)));
                    tempValues.put(ItinerarySQLiteOpenHelper.COL_EVENT_DATE,selectedCursor.getString(selectedCursor.getColumnIndex(ItinerarySQLiteOpenHelper.COL_EVENT_DATE)));
                    showInputDialog();
                }
            });
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TableObserver observer = new TableObserver(null);
        mResolver.registerContentObserver(Uri.parse(ItineraryContentProvider.ITINERARIES_CONTENT_URI), true, observer);
        mResolver.registerContentObserver(Uri.parse(ItineraryContentProvider.EVENTS_CONTENT_URI), true, observer);

        //Force sync on activity create
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        Log.d("DREWTEST", "Requesting a sync");
        if(ItineraryListActivity.mCredential != null && ItineraryListActivity.mCredential.getSelectedAccountName() != null)
            ContentResolver.requestSync(ItineraryListActivity.mCredential.getSelectedAccount(), AUTHORITY, extras);
    }

    public void showInputDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.event_input_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Event");
        dialogBuilder.setMessage(null);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText titleEditText = (EditText) dialogView.findViewById(R.id.title_text);
                EditText locationEditText = (EditText) dialogView.findViewById(R.id.location_text);
                EditText descriptionEditText = (EditText) dialogView.findViewById(R.id.description_text);
                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.event_date_picker);
                int eventId = -1;
                if (tempValues != null) {
                    eventId = tempValues.getAsInteger(ItinerarySQLiteOpenHelper.COL_ID);
                }

                if (titleEditText.getText().toString().length() == 0 || locationEditText.getText().toString().length() == 0) {
                    Toast.makeText(mContext, "Please complete all fields", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues newValues = new ContentValues();
                    newValues.put(ItinerarySQLiteOpenHelper.COL_LIST_ID, mItineraryId);
                    newValues.put(ItinerarySQLiteOpenHelper.COL_EVENT_TITLE, titleEditText.getText().toString());
                    newValues.put(ItinerarySQLiteOpenHelper.COL_EVENT_LOCATION, locationEditText.getText().toString());
                    newValues.put(ItinerarySQLiteOpenHelper.COL_EVENT_DESCRIPTION, descriptionEditText.getText().toString());
                    newValues.put(ItinerarySQLiteOpenHelper.COL_EVENT_DATE, (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear());

                    if (tempValues == null) {
                        getContentResolver().insert(Uri.parse(ItineraryContentProvider.EVENTS_CONTENT_URI + "/" + mItineraryId), newValues);
                    } else {
                        getContentResolver().update(Uri.parse(ItineraryContentProvider.EVENTS_CONTENT_URI + "/" + mItineraryId + "/" + eventId), newValues, null, null);
                        tempValues = null;
                    }
                    Cursor cursor = getContentResolver().query(Uri.parse(ItineraryContentProvider.EVENTS_CONTENT_URI + "/" + mItineraryId), null, null, null, null);
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

        EditText titleEditText = (EditText) dialogView.findViewById(R.id.title_text);
        EditText locationEditText = (EditText) dialogView.findViewById(R.id.location_text);
        EditText descriptionEditText = (EditText) dialogView.findViewById(R.id.description_text);
        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.event_date_picker);
        Log.d("DREWTEST", "tempValues: " + tempValues);
        if(tempValues != null){
            Log.d("DREWTEST","Prefilling fields");
            titleEditText.setText(tempValues.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_TITLE));
            locationEditText.setText(tempValues.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_LOCATION));
            descriptionEditText.setText(tempValues.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_DESCRIPTION));
            String date = tempValues.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_DATE);
            String[] splitDate = date.split("/");
            datePicker.updateDate(Integer.parseInt(splitDate[2]),Integer.parseInt(splitDate[0])-1,Integer.parseInt(splitDate[1]));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.explore_button) {
           Intent intent = new Intent(this,PointsOfInterestActivity.class);
            startActivity(intent);
        }

        if(id == android.R.id.home){
            Intent intent = getIntent();
            this.setResult(RESULT_OK, intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        //itinerary.setToDoItems(mItemList);intent.putExtra("modifiedToDoList", itinerary);
        this.setResult(RESULT_OK, intent);
        finish();
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
            Log.d("DREWTEST", "Requesting a sync");
            ContentResolver.requestSync(ItineraryListActivity.mCredential.getSelectedAccount(), AUTHORITY, extras);
        }
    }
}
