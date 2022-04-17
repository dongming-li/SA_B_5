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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.Callback;
import domain.Offer;
import domain.UserInfo;
import domain.UserType;

public class UserVolleyImpl extends AsyncTask<Void, Void, JSONArray> implements UserVolley {

    private String createUserUrl = "http://proj-309-sa-b-5.cs.iastate.edu/createUser.php";
    private String getUserUrl = "http://proj-309-sa-b-5.cs.iastate.edu/getUser.php";
    private UserInfo currentUser;
    private Context currentContext;
    private ArrayList<UserInfo> users;
    private Callback callback;

    public UserVolleyImpl(Callback call){
        callback = call;
    }

    /**
     * Creates a user row in the database based on the information given in the UserInfo object.
     * Sends a map over of the fields of the current user to the createUser php file.
     * @param context context of app
     * @param user - user data
     */
    @Override
    public void createUser(Context context, final UserInfo user) {
        currentUser = user;
        currentContext = context;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, createUserUrl,
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
                params.put("netID", currentUser.getNetID());
                params.put("userPassword", currentUser.getPassword());
                params.put("confirmationCode", currentUser.getConfirmationCode());
                params.put("firstName", currentUser.getFirstName());
                params.put("lastName", currentUser.getLastName());
                params.put("venmo", currentUser.getVenmoName());
                params.put("profileDescription", currentUser.getProfileDescription());
                params.put("userType", currentUser.getUserType().toString());
                params.put("userRating", currentUser.ratingToString(currentUser.getUserRating()));
                params.put("dateJoined", currentUser.getDateJoined());
                return params;
            }
        };

        MySingleton.getInstance(currentContext).addToRequestQueue(stringRequest);
    }

    /*
    Part of the asynchronous process of grabbing a list of users from the database. For the given
    JSONArray, it parses JSONObjects from it and then creates UserInfo objects based on the Strings in
    the JSONObject. Adds each individual user to the users ArrayList. Calls the callback function on
    the users ArrayList.
     */

    /**
     * Part of the asynchronous process of grabbing a list of users from the database. For the given
     * JSONArray, it parses JSONObjects from it and then creates UserInfo objects based on the Strings in
     * the JSONObject. Adds each individual user to the users ArrayList. Calls the callback function on
     * the users ArrayList.
     * @param jsonArray of userData from database
     */
    @Override
    public void onPostExecute(JSONArray jsonArray) {
        try{
            users = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                Log.d("JSON", jsonArray.toString());
                JSONObject jsonUser = jsonArray.getJSONObject(i);

                String netID = jsonUser.getString("NETID");
                String userPassword = jsonUser.getString("PASSWORD");
                String confirmationCode = jsonUser.getString("CONFIRMATION_CODE");
                String firstName = jsonUser.getString("FIRST_NAME");
                String lastName = jsonUser.getString("LAST_NAME");
                String venmo = jsonUser.getString("VENMO");
                String profileDescription = jsonUser.getString("PROFILE_DESCRIPTION");
                String userType = jsonUser.getString("USER_TYPE");
                UserType type = UserType.valueOf(userType);
                float userRating = (float) jsonUser.getDouble("USER_RATING");
                String dateJoined = jsonUser.getString("DATE_JOINED");
                String banReason = jsonUser.getString("REASON");

                List<Offer> offers = new ArrayList<>();
                List<domain.Request> requests = new ArrayList<>();

                UserInfo user = new UserInfo(netID, userPassword, confirmationCode, firstName, lastName,
                        venmo, profileDescription, type, userRating, offers, requests);

                if(!banReason.equals("null")) {
                    user.setIsBanned(true);
                    user.setBanReason(banReason);
                }

                user.setDateJoined(dateJoined);

                users.add(user);
                Log.d("size", users.size() + "");

            }

        } catch (Exception e){
            e.printStackTrace();
        }
        callback.call(users);
    }

    /*
    Part of the asynchronous process of grabbing a list of users from the database. Reads the strings from the
    JSONObjects received from the database and adds them to the JSONArray.
     */

    /**
     * Part of the asynchronous process of grabbing a list of users from the database. Reads the strings from the
     * JSONObjects received from the database and adds them to the JSONArray
     * @param aVoid - nothing
     * @return JSONArray of user data from database
     */
    @Override
    protected JSONArray doInBackground(Void... aVoid) {
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(getUserUrl);
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

}
