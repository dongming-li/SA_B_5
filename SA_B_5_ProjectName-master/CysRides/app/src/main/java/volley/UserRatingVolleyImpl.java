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
import java.util.Map;

import service.Callback;

public class UserRatingVolleyImpl extends AsyncTask<Void, Void, JSONArray> implements UserRatingVolley {

    private String addRatingUrl = "http://proj-309-sa-b-5.cs.iastate.edu/addRating.php";
    private String getRatingUrl = "http://proj-309-sa-b-5.cs.iastate.edu/getRating.php";
    private Context currentContext;
    private Callback callback;
    private String netID;
    private String rating;
    private String numberRatings;
    private ArrayList<String> ratings;

    public UserRatingVolleyImpl(){};
    public UserRatingVolleyImpl(Callback call){
        callback = call;
    }

    /**
     * Updates a rating in the database
     * @param context - context of app
     * @param netID - user's netID
     * @param userRating - user's rating
     * @param numberRatings - amount of user ratings
     */
    @Override
    public void addRating(Context context, final String netID, final float userRating, final float numberRatings){

        currentContext = context;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, addRatingUrl,
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
                params.put("rating", userRating + "");
                params.put("number_ratings", numberRatings + "");
                params.put("netID", netID);
                return params;
            }
        };

        MySingleton.getInstance(currentContext).addToRequestQueue(stringRequest);

    }

    /**
     * Returns all ratings from the ratings table
     * @param jsonArray - array of ratings from database
     */
    @Override
    public void onPostExecute(JSONArray jsonArray) {
        try{
            ratings = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                Log.d("JSON", jsonArray.toString());
                JSONObject jsonRating = jsonArray.getJSONObject(i);

                netID = jsonRating.getString("RATINGS_NETID");
                rating = jsonRating.getString("RATING");
                numberRatings = jsonRating.getString("NUMBER_RATINGS");

                ratings.add(netID);
                ratings.add(rating);
                ratings.add(numberRatings);

                Log.d("size", ratings.size() + "");

            }

        } catch (Exception e){
            e.printStackTrace();
        }
        callback.call(ratings);
    }

    /**
     * Part of the asynchronous process of grabbing a list of users from the database. Reads the strings from the
       JSONObjects received from the database and adds them to the JSONArray.
     * @param aVoid - nothing
     * @return - JSONArray of user ratings
     */
    @Override
    protected JSONArray doInBackground(Void... aVoid) {
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(getRatingUrl);
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
