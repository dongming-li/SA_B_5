package service;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

public interface ActivityService {

    /**
     * creates snackbar if wifi is not connected
     * @param context of app
     * @param v current page
     * @return snackbar with option to enable wifi
     */
    Snackbar setupConnection(final Context context, View v);
}
