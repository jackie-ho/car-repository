package ly.generalassemb.drewmahrt.project_03.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ly.generalassemb.drewmahrt.project_03.R;
import ly.generalassemb.drewmahrt.project_03.model.Venue;

public class PointsOfInterestActivity extends AppCompatActivity {
    private final static String CLIENT_ID = "WCNGAKJUMPP4Q00BIOEDDHMY5D0MDTLDCSJ21PBSSX25IGZS";
    private final static String CLIENT_SECRET = "WR3J250LLLFZAEOKMZ0W2TC00LU1KTYGIR2RIUBWWQY0LCRC";
    private final static int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 100;
    private List<Venue> mVenues;
    private ArrayAdapter mArrayAdapter;
    private ListView mPoiListView;
    private double mLatitude;
    private double mLongitude;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_of_interest);
        setTitle("Explore!");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Discovering places near you!");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        //Permission checking for 6.0+
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mLongitude = location.getLongitude();
            mLatitude = location.getLatitude();
            makeCall("https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20130815&ll=" + mLatitude + "," + mLongitude);
        }

        mPoiListView = (ListView) findViewById(R.id.poi_list_view);

        mPoiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venue selectedVenue = mVenues.get(position);
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d", selectedVenue.getLatitude(), selectedVenue.getLongitude(),20);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    mLongitude = location.getLongitude();
                    mLatitude = location.getLatitude();
                    makeCall("https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20130815&ll=" + mLatitude + "," + mLongitude);
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

    private void makeCall(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, "", new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mVenues = parseFoursquare(response);

                        mArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, mVenues) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                                text1.setTextColor(Color.BLACK);
                                text2.setTextColor(Color.BLACK);

                                text1.setText(mVenues.get(position).getName());
                                text2.setText(mVenues.get(position).getCategory());
                                return view;
                            }
                        };
                        mPoiListView.setAdapter(mArrayAdapter);
                        progressDialog.hide();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private ArrayList parseFoursquare(JSONObject jsonObject) {

        ArrayList<Venue> temp = new ArrayList();
        Venue poi;
        try {
            // make an jsonObject in order to parse the response
            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("venues")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("venues");

                    for (int i = 0;i<jsonArray.length();i++) {
                        poi = new Venue();
                        if (jsonArray.getJSONObject(i).getJSONArray("categories").length() > 0) {
                            poi.setCategory(jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).getString("name"));
                        }
                        poi.setName(jsonArray.getJSONObject(i).getString("name"));
                        if(jsonArray.getJSONObject(i).getJSONObject("location").has("city"))
                            poi.setCity(jsonArray.getJSONObject(i).getJSONObject("location").getString("city"));
                        poi.setLatitude(Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("location").getString("lat")));
                        poi.setLongitude(Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("location").getString("lng")));
                        temp.add(poi);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
        return temp;
    }
}
