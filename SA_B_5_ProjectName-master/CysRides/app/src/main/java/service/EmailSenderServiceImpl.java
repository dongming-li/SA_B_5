package service;

import android.content.Context;

import domain.UserInfo;
import volley.EmailVolley;
import volley.EmailVolleyImpl;

public class EmailSenderServiceImpl implements EmailSenderService {

    /**
     * sends confirmation email to user
     * @param user - user that receives email
     * @param currentContext - context of app
     */
    @Override
    public void sendEmail(UserInfo user, Context currentContext)
    {
        EmailVolley emailVolley = new EmailVolleyImpl();
        emailVolley.sendEmail(user.getNetID(), "cysrides@iastate.edu", "Welcome to Cy's Rides!",
                ("Here's your confirmation code: " + user.getConfirmationCode()), currentContext);

    }
}