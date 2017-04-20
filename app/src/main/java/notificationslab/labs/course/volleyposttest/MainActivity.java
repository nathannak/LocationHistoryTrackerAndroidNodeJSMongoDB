package notificationslab.labs.course.volleyposttest;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //list of ID's to delete
    List<String> deleteIds = new ArrayList<>();

    //to store pairs of lat and lng
    List<Map<String, Double>>  latlngList = new ArrayList<>();
    //
    static boolean mapsActivityStarted = false;
    BroadcastReceiver mMessageReceiver;
    ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create progressbar
        proDialog = ProgressDialog.show(this, "loading content", "please wait ...");

        RequestQueue queue = Volley.newRequestQueue(this);

        //get request

        //ToDO uncomment this, ge tthe url from nathan.nakhjavani@gmail.com
        //final String urlGet ="url goes here";

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //display response
                        Log.v("myapp", "json response: " + response.toString());


                        JSONArray data = null;

                        try {

                            data = response.getJSONArray("data");
                            Log.v("myapp", "data array: " + data.toString());

                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                        }

                        //
                        deleteIds.clear();

                        // assume i only need THREE latest entries
                        if (data.length() - 3 > 0) {
                            for (int i = 0; i < data.length() - 3; i++) {
                                //delete entry
                                try {
                                    // Handle ID
                                    JSONObject j = (JSONObject) (data.get(i));
                                    Log.v("myapp", "j: " + j.toString());

                                    deleteIds.add(j.getString("_id"));
                                    Log.v("myapp", "j.getString: " + j.getString("_id"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        //

                        //print deleteIds

                        for (int i = 0; i < deleteIds.size(); i++) {
                            Log.v("myapp", "deleteId element " + i + " is: " + deleteIds.get(i));
                        }

                        //delete items in DeleteId - maintaining clean backend
                        RequestQueue finalqueue = Volley.newRequestQueue(MainActivity.this);

                        for (int i = 0; i < deleteIds.size(); i++) {
                            //
                            String urlDelete = urlGet + "user/" + deleteIds.get(i);

                            StringRequest dr = new StringRequest(Request.Method.DELETE, urlDelete,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            // response
                                            Log.v("myapp", "delete successful, response is " + response);
                                            //
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // error.
                                            Log.v("myapp", "on error response");
                                        }
                                    }
                            );
                            finalqueue.add(dr);
                        }
                        //

                        //get again

                        //prepare the Request
                        JsonObjectRequest getRequest1 = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response)
                                    {

                                        //
                                        JSONArray data = null;
                                        //

                                        try
                                        {

                                            data = response.getJSONArray("data");

                                            Log.v("myapp", "data array - gettig again: " + data.toString());

                                        }
                                        catch (JSONException | NullPointerException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        //
                                        for (int i = 0; i < data.length(); i++) {

                                            try {
                                                JSONObject j = (JSONObject) (data.get(i));
                                                Log.v("myapp", "j: - getting again " + j.toString());

                                                // Handle Lat and Lng
                                                HashMap<String,Double> hm = new HashMap<>();

                                                hm.put("lat"+i+1, j.getDouble("lat"));
                                                Log.v("myapp", "lat: - getting again " + j.getDouble("lat"));

                                                hm.put("lng"+i+1, j.getDouble("lng"));
                                                Log.v("myapp", "lng: - getting again " + j.getDouble("lng"));

                                                latlngList.add(hm);
                                                //

                                                //
                                                Intent intent = new Intent("custom-event-name");
                                                // You can also include some extra data.
                                                intent.putExtra("message", "This is my message!");
                                                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
                                                //

                                                //
                                            } catch (JSONException | NullPointerException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        //

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.v("myapp", "on error response");
                                    }
                                }
                        );

                        // add it to the RequestQueue
                        finalqueue.add(getRequest1);
                        //
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("myapp", "on error response");
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(getRequest);
        //

//      //post
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String urlPost = "https://quiet-bayou-21078.herokuapp.com/user";
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, urlPost,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.d("Response", response);
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        Log.d("Error.Response", "on error response");
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("first_name", "ALI");
//                params.put("last_name", "Nakhjavani");
//                params.put("email_address", "me@yahoo.com");
//                params.put("career", "student");
//
//                return params;
//            }
//        };
//        queue.add(postRequest);
//    }
//    //

        //end of onCreate
    }

    //

    @Override
    protected void onResume()
    {
        //
        super.onResume();
        //
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(!mapsActivityStarted) {
                    //
                    proDialog.dismiss();
                    //
                    String message = intent.getStringExtra("message");
                    Log.d("receiver", "Got message: " + message);
                    //

                    //
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);

                    // i am interested in last THREE locations right now
                    for (int j = 0; j < latlngList.size(); j++) {

                        i.putExtra("lat"+(j+1), latlngList.get(j).get("lat" + j + 1));
                        i.putExtra("lng"+(j+1), latlngList.get(j).get("lng" + j + 1));

                        Log.v("myapp", "retreived lat" + (j + 1) + " is: " + latlngList.get(j).get("lat" + j + 1));
                        Log.v("myapp", "retreived lng" + (j + 1) + " is: " + latlngList.get(j).get("lng" + j + 1));

                    }
                    //
                    i.putExtra("size",latlngList.size());
                    //
                    startActivity(i);
                    mapsActivityStarted = true;
                    //
                }
            }

        };
        //
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-event-name"));
        //
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mMessageReceiver);
    }
}