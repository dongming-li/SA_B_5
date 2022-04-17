package volley;

import android.content.Context;

import domain.UserInfo;

public interface UserVolley {

    /**
     * Creates a user row in the database based on the information given in the UserInfo object.
     * Sends a map over of the fields of the current user to the createUser php file.
     * @param context context of app
     * @param user - user data
     */
    void createUser(Context context, UserInfo user);
}
