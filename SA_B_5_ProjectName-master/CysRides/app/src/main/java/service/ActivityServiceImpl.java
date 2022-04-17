package service;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.design.widget.Snackbar;
import android.view.View;

public class ActivityServiceImpl implements ActivityService {

    /**
     * Creates a snackbar if wifi is not connected
     * @param context of app
     * @param v current page
     * @return snackbar with option to enable wifi
     */
    @Override
    public Snackbar setupConnection(final Context context, View v) {
        Snackbar snackbar = Snackbar.make(v, "Cy's Rides Requires\nInternet Connection", Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Connect WIFI", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);
            }
        });
        return snackbar;
    }
}
