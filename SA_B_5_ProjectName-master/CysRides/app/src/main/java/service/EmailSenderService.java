package service;

import android.content.Context;

import domain.UserInfo;

public interface EmailSenderService {

    /**
     * sends email to user
     * @param user - user that receives email
     * @param currentContext - context of app
     */
    void sendEmail(UserInfo user, Context currentContext);

}
