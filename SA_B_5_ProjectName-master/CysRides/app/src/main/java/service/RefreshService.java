package service;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;

public interface RefreshService {
    /**
     * determines when to stop refreshing page
     * @param r - page being refreshed
     * @param a - data being refreshed
     */
    void stopRefreshing(SwipeRefreshLayout r, ArrayAdapter<String> a);
}
