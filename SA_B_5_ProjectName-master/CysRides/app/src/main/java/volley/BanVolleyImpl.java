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
import domain.Ban;


public class BanVolleyImpl extends AsyncTask<Void, Void, JSONArray> implements BanVolley {

    private Context currentContext;
    private String email;
    private String reason;
    private ArrayList<Ban> bans;
    private Callback callback;

    public BanVolleyImpl() { }

    public BanVolleyImpl(Callback o) {
        callback = o;
    }

    /**
     * Creates a ban in the database
     * @param context of app
     * @param ban - user ban
     */
    @Override
    public void createBan(Context context, Ban ban) {
        String createBanUrl = "http://proj-309-sa-b-5.cs.iastate.edu/createBan.php";
        currentContext = context;
        email = ban.getEmail();
        reason = ban.getReason();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, createBanUrl,
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
                params.put("email", email);
                params.put("reason", reason);
                return params;
            }
        };

        MySingleton.getInstance(currentContext).addToRequestQueue(stringRequest);
    }

    /**
     * Returns an array of all bans from the database
     * @param jsonArray of user bans
     */
    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        try{
            bans = new ArrayList<>();
            for(int i=0; i < jsonArray.length();i++){
                Log.d("JSON",jsonArray.toString());
                JSONObject jsonOffer = jsonArray.getJSONObject(i);

                String email = jsonOffer.getString("EMAIL");
                String reason = jsonOffer.getString("REASON");

                Ban ban = new Ban(email, reason);
                bans.add(ban);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        callback.call(bans);
    }

    /**
     * Sets up the connection to the server
     * @param aVoid - nothing
     * @return json Array of bans
     */
    @Override
    protected JSONArray doInBackground(Void... aVoid) {
        String getBansUrl = "http://proj-309-sa-b-5.cs.iastate.edu/getBan.php";
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(getBansUrl);
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
