package volley;

import android.content.Context;

public interface RequestVolley {

    /**
     * Add request to the database
     * @param context of app
     * @param request data
     * @param destName ride destination
     * @param startName ride start location
     */
    void createRequest(Context context, domain.Request request, String destName, String startName);

    /**
     * Remove request from database
     * @param context app context
     * @param id of ride request
     */
    void deleteRequest(final Context context, final int id);

    /**
     * gives specified request the given groupID
     * @param context of app
     * @param requestId id of ride request
     * @param groupId id of group
     */
    void giveRequestGroup(Context context,final int requestId, final int groupId);
}
