package ly.generalassemb.drewmahrt.project_03.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import ly.generalassemb.drewmahrt.project_03.R;
import ly.generalassemb.drewmahrt.project_03.model.Venue;

public class HighlightLocationService extends IntentService {
    private final static String CLIENT_ID = "WCNGAKJUMPP4Q00BIOEDDHMY5D0MDTLDCSJ21PBSSX25IGZS";
    private final static String CLIENT_SECRET = "WR3J250LLLFZAEOKMZ0W2TC00LU1KTYGIR2RIUBWWQY0LCRC";

    private static final String TAG = "HighlightLocationService";

    public HighlightLocationService() {
        super("HighlightLocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        double latitude = intent.getDoubleExtra("latExtra",0);
        double longitude = intent.getDoubleExtra("lngExtra",0);

        makeCall("https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20130815&ll=" + latitude + "," + longitude);

    }

    private void makeCall(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, "", new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        parseFoursquare(response);
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

    private void parseFoursquare(JSONObject jsonObject) {

        ArrayList<Venue> temp = new ArrayList();
        Venue poi;
        try {
            // make an jsonObject in order to parse the response
            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("venues")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("venues");

                    if(jsonArray.length() > 0){
                        Random r = new Random();
                        int i = r.nextInt(jsonArray.length()-1);
                        String name = jsonArray.getJSONObject(i).getString("name");
                        double lat = Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("location").getString("lat"));
                        double lng = Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("location").getString("lng"));

                        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d", lat, lng,18);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

                        Notification notification = new Notification.Builder(this)
                                .setContentTitle("Explore "+ name +"!")
                                .setContentText("Click here to find this exciting location near you.").setSmallIcon(R.mipmap.ic_launcher)
                                .setContentIntent(pIntent)
                                .build();
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;

                        notificationManager.notify(0, notification);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
