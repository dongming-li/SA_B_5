package service;

import android.content.Context;

import domain.Group;
import volley.GroupVolley;
import volley.GroupVolleyImpl;

public class GroupServiceImpl implements GroupService{

    private GroupVolley groupVolley = new GroupVolleyImpl();

    /**
     * adds rider to ride
     * @param context - context of app
     * @param g - ride group
     * @param netid - user's netID
     */
    @Override
    public void addRider(Context context, Group g, String netid){groupVolley.addRider(context,g,netid);}

    /**
     * adds driver to ride
     * @param context - context of app
     * @param g - ride group
     * @param netid - user's netID
     */
    @Override
    public void addDriver(Context context, Group g, String netid){groupVolley.addDriver(context, g, netid);}

    /**
     * returns ride group
     * @param context - context of app
     * @param groupNum - group ID
     */
    @Override
    public void getGroup(Context context, int groupNum){groupVolley.getGroup(context, groupNum);}
}
