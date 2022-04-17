package service;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;

public class RefreshServiceImpl implements RefreshService {

    private SwipeRefreshLayout refresh;
    private ArrayAdapter<String> adapter;

    /**
     * determines when to stop refreshing page
     * @param r - page being refreshed
     * @param a - data being refreshed
     */
    @Override
    public void stopRefreshing(SwipeRefreshLayout r, ArrayAdapter<String> a) {
        refresh = r;
        adapter = a;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        }, 1000);
    }
}
