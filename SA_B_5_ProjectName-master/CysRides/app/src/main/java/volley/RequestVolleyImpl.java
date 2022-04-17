package volley;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import service.Callback;


public class RequestVolleyImpl extends AsyncTask<Void, Void, JSONArray> implements RequestVolley {

    private GroupVolleyImpl groupVolley = new GroupVolleyImpl();
    private ExpiredGroupVolley expiredGroupVolley = new ExpiredGroupVolleyImpl();

    private domain.Request newRequest;
    private Context currentContext;
    private String destinationName;
    private String startName;
    private ArrayList<domain.Request> requests;
    private Callback callback;
    private Calendar current = Calendar.getInstance();

    /**
     * Default constructor initializes current date to be determine if a ride needs to be deleted
     */
    public RequestVolleyImpl() {}

    /**
     * constructor that stores caller data
     * @param c context of app
     * @param o callback to send data to
     */
    public RequestVolleyImpl(Context c, Callback o) {
        this();
        currentContext = c;
        callback = o;
    }

    /**
     * Add request to the database
     * @param context of app
     * @param request data
     * @param destination ride destination
     * @param start ride start location
     */
    @Override
    public void createRequest(Context context, domain.Request request, String destination, String start) {
        String createRequestUrl = "http://proj-309-sa-b-5.cs.iastate.edu/createRequest.php";
        newRequest = request;
        currentContext = context;
        destinationName = destination;
        startName = start;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, createRequestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        newRequest.getGroup().setRequestID(Integer.parseInt(response));
                        groupVolley.createGroup(currentContext, newRequest.getGroup());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(currentContext, "Error...",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("numBags", newRequest.getNumBags()+"");
                params.put("email", newRequest.getEmail());
                params.put("destination", destinationName);
                params.put("start", startName);
                params.put("description", newRequest.getDescription());
                params.put("datetime", String.format("'%s'", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(newRequest.getDate())));
                return params;
            }
        };

        /* push request to database */
        MySingleton.getInstance(currentContext).addToRequestQueue(stringRequest);
    }

    /**
     * Remove request from database
     * @param context app context
     * @param id of ride request
     */
    @Override
    public void deleteRequest(final Context context, final int id) {
        String deleteRequestUrl = "http://proj-309-sa-b-5.cs.iastate.edu/deleteRequest.php";
        currentContext = context;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteRequestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(currentContext, "Error...",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id+"");
                return params;
            }
        };

        /* push request to database */
        MySingleton.getInstance(currentContext).addToRequestQueue(stringRequest);
    }

    /**
     * gives specified request the given groupID
     * @param context of app
     * @param requestId id of ride request
     * @param groupId id of group
     */
    public void giveRequestGroup(Context context, final int requestId, final int groupId){
        String giveRequestGroupUrl = "http://proj-309-sa-b-5.cs.iastate.edu/giveRequestGroup.php";
        currentContext = context;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, giveRequestGroupUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(currentContext, "Error...",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("request_id", Integer.toString(requestId));
                params.put("group_id", Integer.toString(groupId));
                return params;
            }
        };

        MySingleton.getInstance(currentContext).addToRequestQueue(stringRequest);
    }

    /**
     * Method that parses data pulled from database
     * @param jsonArray of ride request data pulled from database
     */
    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        try{
            /* parse ride request data and place in arraylist */
            requests = new ArrayList<>();
            for(int i=0; i < jsonArray.length();i++){
                Log.d("JSON",jsonArray.toString());
                JSONObject jsonRequest = jsonArray.getJSONObject(i);

                String stringId = jsonRequest.getString("ID");
                int id = Integer.parseInt(stringId);
                String stringCost = jsonRequest.getString("NUM_BAGS");
                int numBags = Integer.parseInt(stringCost);
                String email = jsonRequest.getString("REQUEST_EMAIL");

                String stringDestination = jsonRequest.getString("REQUEST_DESTINATION");
                String destinationName = getLocationName(stringDestination);
                LatLng destLatLng = getLatLngFromDatabase(stringDestination);

                String stringStart = jsonRequest.getString("REQUEST_START");
                String startName = getLocationName(stringStart);
                LatLng startLatLng = getLatLngFromDatabase(stringStart);

                String description = jsonRequest.getString("REQUEST_DESCRIPTION");
                String stringDate = jsonRequest.getString("REQUEST_DATETIME");
                Date date =  new Date();

                int group_id = jsonRequest.getInt("REQUEST_GROUP_ID");

                Calendar compare = Calendar.getInstance();
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(stringDate);
                    compare.setTime(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                domain.Request request = new domain.Request(numBags, id, email, destinationName,
                        destLatLng, startName, startLatLng, description, date, group_id,
                        this.currentContext);

                /* if request is expired, just delete it */
                if(compare.compareTo(current) < 0) {
                    expiredGroupVolley.createExpiredGroupByRideId(group_id, false, id, currentContext);
                }
                else {
                    requests.add(request);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        /* send list of ride requests back to caller */
        callback.call(requests);
    }

    /**
     * Gets all request data from database
     * @param aVoid nothing
     * @return JSONArray of all ride offer data
     */
    @Override
    protected JSONArray doInBackground(Void... aVoid) {
        String getRequestsUrl = "http://proj-309-sa-b-5.cs.iastate.edu/getRequest.php";
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();

        try {
            /* pull data from database */
            URL url = new URL(getRequestsUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            /* disconnect from server */
            try {
                if(null != urlConnection) {
                    urlConnection.disconnect();
                }
            }catch(NullPointerException e) {
                e.printStackTrace();
            }
        }

        Log.d("String", result.toString());
        JSONArray array = null;
        try {
            array = new JSONArray(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* return data from database */
        return array;
    }



    /**
     * Returns the name of a location
     * @param location puled from database
     * @return name of the location
     */
    private String getLocationName(String location) {
        String[] splitDestination = location.split(" lat/lng: ");
        return splitDestination[0];
    }

    /**
     * Returns the LatLng of a location
     * @param location pulled from database
     * @return LatLng of location
     */
    private LatLng getLatLngFromDatabase(String location) {
        String[] splitDestination = location.split(" lat/lng: ");
        String latLong = splitDestination[1];
        latLong = latLong.replace("(","");
        latLong = latLong.replace(")","");
        String[] splitLatLong = latLong.split(",");
        double latitude = Double.parseDouble(splitLatLong[0]);
        double longitude = Double.parseDouble(splitLatLong[1]);
        return new LatLng(latitude, longitude);
    }

}
