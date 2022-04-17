package volley;

import android.content.Context;

import domain.Group;

public interface GroupVolley {

    /**
     * creates group
     * @param context - context of app
     * @param g - group data
     */
    void createGroup(Context context, Group g);

    /**
     * Adds rider to group
     * @param context - context of app
     * @param group - group data
     * @param netID - user's netID
     */
    void addRider(Context context, Group group, String netID);

    /**
     * adds driver to ride
     * @param context - context of app
     * @param group - group data
     * @param netID - user's netID
     */
    void addDriver(Context context, Group group, String netID);

    /**
     * returns group data
     * @param context - context of app
     * @param groupNum - group ID
     */
    void getGroup(final Context context, final int groupNum);
}
