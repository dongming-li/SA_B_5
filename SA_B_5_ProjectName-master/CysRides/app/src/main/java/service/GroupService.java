package service;

import android.content.Context;

import domain.Group;

public interface GroupService {

    /**
     * adds rider to ride
     * @param context - context of app
     * @param g - ride group
     * @param netid - user's netID
     */
    void addRider(Context context, Group g, String netid);

    /**
     * adds driver to ride
     * @param context - context of app
     * @param g - ride group
     * @param netid - user's netID
     */
    void addDriver(Context context, Group g, String netid);

    /**
     * returns ride group
     * @param context - context of app
     * @param groupNum - group ID
     */
    void getGroup(Context context, int groupNum);

}
