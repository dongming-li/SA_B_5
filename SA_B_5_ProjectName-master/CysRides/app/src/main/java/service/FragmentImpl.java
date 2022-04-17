package service;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class FragmentImpl extends Fragment {

    /**
     * Locks side drawer every time fragment is created
     * @param context of app
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((DrawerLock)getActivity()).lockDrawer(true);
    }

    /**
     * Unlocks side drawer every time fragment is destroyed
     */
    @Override
    public void onDetach() {
        super.onDetach();
        ((DrawerLock)getActivity()).lockDrawer(false);
    }

    /**
     * Sets the data to be displayed
     * @param data information to be displayed
     * @param <T> ride offer or ride request
     */
    public abstract <T> void setData(T data);

    /**
     * Sets text for non-admins
     * @param v view to be set
     */
    protected abstract void setNonAdminTextInfo(View v);

    /**
     * Sets text for non-admins
     * @param v view to be set
     */
    protected abstract void setAdminTextInfo(View v);
}
