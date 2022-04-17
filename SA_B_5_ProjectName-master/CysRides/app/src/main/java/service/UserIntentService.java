package service;

import android.content.Context;
import android.content.Intent;

import domain.UserInfo;

public interface UserIntentService {

    /**
     * creates new page intent
     * @param context - context of app
     * @param cls - current class
     * @param userInfo - user's user info
     * @return intent of app page
     */
    Intent createIntent(Context context, Class<?> cls, UserInfo userInfo);

    /**
     * returns user info from page intent
     * @param intent - intent of current page
     * @return - current user's user info
     */
    UserInfo getUserFromIntent(Intent intent);
}
