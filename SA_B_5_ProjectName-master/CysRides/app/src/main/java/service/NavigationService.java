package service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import domain.UserInfo;

public interface NavigationService {

    /**
     * get intent of page being navigated to
     * @param item - page selected
     * @param c - context of app
     * @param intent - intent of current page
     * @return - page to be navigated to
     */
    Intent getNavigationIntent(@NonNull MenuItem item, Context c, Intent intent);

    /**
     * checks that user is connected to wifi
     * @param c context of app
     * @return true if user is not connected to internet
     */
    boolean checkInternetConnection(Context c);

    /**
     * hides menu items for specific users
     * @param menu - menu to be modified
     * @param userInfo - user's user info
     */
    void hideMenuItems(Menu menu, UserInfo userInfo);

    /**
     * hides admin buttons for non admins
     * @param menu - menu to be modified
     * @param userInfo - user's user info
     */
    void hideAdminButton(Menu menu, UserInfo userInfo);

    /**
     * creates an alert dialog if user tries to log out
     * @param c - context of app
     * @return alert dialog asking user to log out
     */
    AlertDialog.Builder logOutButton(Context c);
}
