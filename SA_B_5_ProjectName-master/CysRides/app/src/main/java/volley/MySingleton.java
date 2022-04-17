package volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context context;

    /**
     * constructor creates new singleton instance
     * @param context - context of app
     */
    private MySingleton(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * returns instance of singleton
     * @param context - context of app
     * @return instance of singleton
     */
    public static synchronized MySingleton getInstance(Context context) {
        if(mInstance ==null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    /**
     * returns the request queue
     * @return request queue
     */
    private RequestQueue getRequestQueue() {
        if(requestQueue==null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * adds data to request queue
     * @param request - data to be sent to database
     * @param <T> - type of data to be sent
     */
    public <T>void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }
}
