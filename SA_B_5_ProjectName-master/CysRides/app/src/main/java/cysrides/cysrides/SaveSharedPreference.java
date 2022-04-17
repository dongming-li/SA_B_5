package cysrides.cysrides;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    private static final String USERNAME_PASSWORD = "username";

    /**
     * Method that returns user's shared preference data
     * @param context of app
     * @return saved user preferences
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * method that sets the current user's username and password
     * @param context of app
     * @param userName - user's username
     */
    static void setUsernamePassword(Context context, String userName) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(USERNAME_PASSWORD, userName);
        editor.apply();
    }

    /**
     * Method that returns the current user's username and password
     * @param context of app
     * @return user's password
     */
    static String getUsernamePassword(Context context) {
        return getSharedPreferences(context).getString(USERNAME_PASSWORD, "");
    }

    /**
     * Method that clears current username and password data
     */
    public static void clearUsernamePassword(Context context) {
        Editor editor = getSharedPreferences(context).edit();
        editor.clear(); //clear all stored data
        editor.apply();
    }
}
