package volley;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EmailVolleyImpl implements EmailVolley {

    private Context currentContext;
    private String to, from, subject, message;

    /**
     * Sends email to user
     * @param toData - email address to send to
     * @param fromData - email address of app
     * @param subjectData - email subject
     * @param messageData - email message
     * @param context - context of app
     */
    public void sendEmail(String toData, String fromData, String subjectData, String messageData, Context context)    {
        String sendEmailUrl = "http://proj-309-sa-b-5.cs.iastate.edu/sendEmail.php";
        currentContext = context;
        to = toData;
        from = fromData;
        subject = subjectData;
        message = messageData;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sendEmailUrl,
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
                params.put("to", to);
                params.put("from", from);
                params.put("subject", subject);
                params.put("message", message);
                return params;
            }
        };

        MySingleton.getInstance(currentContext).addToRequestQueue(stringRequest);

    }
}
