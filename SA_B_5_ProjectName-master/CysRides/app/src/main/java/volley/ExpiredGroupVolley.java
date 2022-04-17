package volley;

import android.content.Context;

import domain.Group;

interface ExpiredGroupVolley {

    /**
     * creates an expired group
     * @param group - group that has expired
     * @param context - context of app
     */
    void createExpiredGroup(Group group, Context context);

    /**
     * creates an expired group by their ride id
     * @param groupId - ID of group
     * @param isOffer - true if ride type is offer
     * @param id - ID of ride
     * @param context - context of app
     */
    void createExpiredGroupByRideId(final int groupId, boolean isOffer, int id, Context context);
}

