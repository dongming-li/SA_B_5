package cysrides.cysrides;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import domain.Ban;
import domain.Offer;
import service.FragmentImpl;

public class ViewBannedUser extends FragmentImpl {

    private Ban ban;

    /**
     * Required empty public constructor
     */
    public ViewBannedUser() {
        // Required empty public constructor
    }

    /**
     * initializes data to be displayed
     * @param inflater inflates the fragment
     * @param container fragment view
     * @param savedInstanceState app info
     * @return fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_banned_user, container, false);
        setAdminTextInfo(v);

        return v;
    }

    /**
     * Sets fragment data
     * @param ban data
     * @param <T> offer
     */
    @Override
    public <T> void setData(T ban) {
        this.ban = (Ban) ban;
    }

    /**
     * This method doesn't do anything, non-admins can't see banned users
     * @param v view to be set
     */
    @Override
    protected void setNonAdminTextInfo(View v) {
        TextView info = v.findViewById(R.id.ban);
        info.setText(R.string.debug);
    }

    /**
     * set data to be displayed for admins
     * @param v view to be set
     */
    @Override
    protected void setAdminTextInfo(View v) {
        TextView info = v.findViewById(R.id.ban);
        info.setText(ban.toString());
    }
}