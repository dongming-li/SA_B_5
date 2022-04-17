package volley;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import domain.GOR;
import domain.Offer;
import service.Callback;
import domain.Group;

public class GroupVolleyImpl extends AsyncTask<Void, Void, JSONArray> implements GroupVolley {

    private Group group;
    private Context currentContext;
    private Callback callback;
    private OfferVolleyImpl ovi;
    private ArrayList<GOR> gors;

    private RequestVolleyImpl rvi;

    /**
     * Default constructor
     */
    public GroupVolleyImpl(){}

    /**
     * Constructor that sets context and data callback
     * @param currentContext - context of app
     * @param c - callback to send data back to
     */
    public GroupVolleyImpl(Context currentContext, Callback c) {
        this.currentContext = currentContext;
        callback = c;
    }

    /**
     * Adds a group to the database
     * @param context - context for mySingleton class
     * @param g - Group to be added
     */
    @Override
    public void createGroup(Context context, Group g) {
        String createGroupUrl =     "http://proj-309-sa-b-5.cs.iastate.edu/createGroup_TEST.php";
        this.group = g;
        if(this.group.getType().equals("OFFER")) {
         this.ovi = new OfferVolleyImpl();
        }else{
            this.rvi = new RequestVolleyImpl();
        }
        currentContext = context;
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, createGroupUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //return group number of group we just created
                        //add group number to offer table
                        //0 is Groupid and 1 is tripid
                        String[] splitResponse = response.split(" ");
                        Log.d("GroupID, OfferID: ", splitResponse[0] + splitResponse[1]);
                        if(group.getType().equals("OFFER")) {
                            ovi.giveOfferGroup(currentContext, Integer.parseInt(splitResponse[1]), Integer.parseInt(splitResponse[0]));
                        }else{
                            rvi.giveRequestGroup(currentContext,Integer.parseInt(splitResponse[1]), Integer.parseInt(splitResponse[0]));
                        }
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
                if(group.getGroupMembers().get(0) != null) {
                    params.put("user", group.getGroupMembers().get(0));
                }else{
                    params.put("user", group.getGroupMembers().get(1));
                }
                if(group.getType().equals("OFFER")) {
                    params.put("id", Integer.toString(group.getOfferId()));
                }else{
                    params.put("id", Integer.toString(group.getRequestID()));
                }
                params.put("type", group.getType());
                return params;
            }
        };

        MySingleton.getInstance(currentContext).addToRequestQueue(stringRequest);
    }

    /**
     * Returns a group from the database with the group id
     * @param currentContext - context for mySingleton class
     * @param groupNum - id of the Group to be returned
     */
    @Override
    public void getGroup(final Context currentContext, final int groupNum) {
        String getGroupUrl =        "http://proj-309-sa-b-5.cs.iastate.edu/getGroup.php";
        this.currentContext = currentContext;
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, getGroupUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jarr = new JSONArray(response);
                            JSONObject jGroup = jarr.getJSONObject(0);
                            int groupNum = jGroup.getInt("ID");

                            ArrayList<String> members = new ArrayList<>();
                            String rider = jGroup.getString("DRIVER");
                            members.add(rider);
                            rider = jGroup.getString("RIDER_1");
                            members.add(rider);
                            rider = jGroup.getString("RIDER_2");
                            members.add(rider);
                            rider = jGroup.getString("RIDER_3");
                            members.add(rider);
                            rider = jGroup.getString("RIDER_4");
                            members.add(rider);
                            rider = jGroup.getString("RIDER_5");
                            members.add(rider);
                            rider = jGroup.getString("RIDER_6");
                            members.add(rider);
                            rider = jGroup.getString("RIDER_7");
                            members.add(rider);

                            String  offerNum = jGroup.getString("OFFER_ID");
                            if(!offerNum.equals("null")){
                                group = new Group(groupNum, members,Integer.parseInt(offerNum), Integer.MIN_VALUE);
                            }else {
                                group = new Group(groupNum, members, Integer.MIN_VALUE, Integer.parseInt(jGroup.getString("REQUEST_ID")));
                            }
                            ArrayList<Group> groupList = new ArrayList<>();
                            groupList.add(group);
                            callback.call(groupList);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(currentContext, "Error...", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("group_id", Integer.toString(groupNum));
                return params;
            }
        };

        MySingleton.getInstance(currentContext).addToRequestQueue(stringRequest);
    }

    /**
     * Adds a rider to a group in the database
     * @param context - context for mySingleton class
     * @param g - Group the rider is to be added to
     * @param netID - netID of the rider to be added
     */
    @Override
    public void addRider(Context context, final Group g, final String netID) {
        String addRiderUrl =        "http://proj-309-sa-b-5.cs.iastate.edu/addRider_TEST.php";
        currentContext = context;
        this.group = g;

        //check if going to overflow
        ArrayList<String> gMembers = group.getGroupMembers();
        int tempsize = 0;
        for (String s : gMembers) {
            if(!s.equals("null")){
                tempsize += 1;
            }
        }
        if(group.getGroupMembers().get(0) == null || group.getGroupMembers().get(0).equals("null")){
            tempsize+=1;
        }
        if(tempsize>7){
            Toast.makeText(currentContext, "Sorry, this group is full", Toast.LENGTH_SHORT).show();
            return;
        }
        final int size = tempsize;
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, addRiderUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        if(response.equals("You are already in this group")){
                            Toast.makeText(currentContext, response, Toast.LENGTH_SHORT).show();
                        }
                        if(response.equals("Data insertion success...")){
                            Toast.makeText(currentContext, "You are now in this group", Toast.LENGTH_SHORT).show();

                        }
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
                params.put("rider", netID);
                params.put("rider_num", Integer.toString(size));
                params.put("id", Integer.toString(group.getId()));
                return params;
            }
        };

        MySingleton.getInstance(currentContext).addToRequestQueue(stringRequest);
    }
    //adds user to a group as a driver

    /**
     * Adds a driver to a group in the database
     * @param context - context for mySingleton class
     * @param g - Group the driver is to be added to
     * @param netID - netID of the driver to be added
     */
    @Override
    public void addDriver(Context context, final Group g, final String netID) {
        String addDriverUrl = "http://proj-309-sa-b-5.cs.iastate.edu/addDriver_TEST.php";
        currentContext = context;
        this.group = g;
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, addDriverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("You are already in this group")){
                            Toast.makeText(currentContext, response, Toast.LENGTH_SHORT).show();
                        }
                        if(response.equals("Group already has a driver")){
                            Toast.makeText(currentContext, response, Toast.LENGTH_SHORT).show();
                        }
                        if(response.equals("Data insertion success...")){
                            Toast.makeText(currentContext, "You are now the driver of this group", Toast.LENGTH_SHORT).show();

                        }
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
                params.put("driver", netID);
                params.put("id", Integer.toString(group.getId()));

                return params;
            }
        };

        MySingleton.getInstance(currentContext).addToRequestQueue(stringRequest);
    }

    /**
     * Sets up a connection to the server
     * @param voids - Execute() takes no parameters
     * @return json array for postExecute
     */
    @Override
    protected JSONArray doInBackground(Void... voids) {
        String getGroupsAndRidesUrl = "http://proj-309-sa-b-5.cs.iastate.edu/getGroupsAndRides.php";
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(getGroupsAndRidesUrl);
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

        return array;
    }

    /**
     * returns all groups in the database
     * @param jsonArray - json array containing GOR data
     */
    @Override
    protected void onPostExecute(JSONArray jsonArray){
        try{
            Log.d("Groups", jsonArray.toString());
            gors = new ArrayList<>();
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonGOR = jsonArray.getJSONObject(i);
                Group group;
                Offer offer;
                domain.Request request;
                ArrayList<String> members = new ArrayList<>();

                String stringGroupID = jsonGOR.getString("ID");
                int groupID = Integer.parseInt(stringGroupID);
                members.add(jsonGOR.getString("DRIVER"));
                members.add(jsonGOR.getString("RIDER_1"));
                members.add(jsonGOR.getString("RIDER_2"));
                members.add(jsonGOR.getString("RIDER_3"));
                members.add(jsonGOR.getString("RIDER_4"));
                members.add(jsonGOR.getString("RIDER_5"));
                members.add(jsonGOR.getString("RIDER_6"));
                members.add(jsonGOR.getString("RIDER_7"));
                String stringOfferID = jsonGOR.getString("OFFER_ID");
                int requestID = 0;
                int offerID= 0;
                group = null;
                if(!stringOfferID.equals("null")) {
                    offerID = Integer.parseInt(stringOfferID);
                    group = new Group(groupID, members, offerID, 0);
                }
                String stringRequstID = jsonGOR.getString("REQUEST_ID");
                if(!stringRequstID.equals("null")) {
                    requestID = Integer.parseInt(stringRequstID);
                    group = new Group(groupID, members, 0, requestID);
                }

                if(offerID != 0){
                    String email = jsonGOR.getString("OFFER_EMAIL");
                    String stringDestination = jsonGOR.getString("OFFER_DESTINATION");
                    String destinationName = getLocationName(stringDestination);
                    LatLng destLatLng = getLatLngFromDatabase(stringDestination);
                    String stringStart = jsonGOR.getString("OFFER_START");
                    String startName = getLocationName(stringStart);
                    LatLng startLatLng = getLatLngFromDatabase(stringStart);
                    String stringDate = jsonGOR.getString("OFFER_DATETIME");
                    Date date =  new Date();
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(stringDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    offer = new Offer(0, offerID, email, destinationName, destLatLng, startName, startLatLng, null, date);
                    request = null;
                }else{
                    String email = jsonGOR.getString("REQUEST_EMAIL");
                    String stringDestination = jsonGOR.getString("REQUEST_DESTINATION");
                    String destinationName = getLocationName(stringDestination);
                    LatLng destLatLng = getLatLngFromDatabase(stringDestination);
                    String stringStart = jsonGOR.getString("REQUEST_START");
                    String startName = getLocationName(stringStart);
                    LatLng startLatLng = getLatLngFromDatabase(stringStart);
                    String stringDate = jsonGOR.getString("REQUEST_DATETIME");
                    Date date =  new Date();
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(stringDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    request = new domain.Request(0, requestID, email, destinationName, destLatLng, startName, startLatLng, null, date);
                    offer = null;
                }
                gors.add(new GOR(group, offer, request));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        callback.call(gors);
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
