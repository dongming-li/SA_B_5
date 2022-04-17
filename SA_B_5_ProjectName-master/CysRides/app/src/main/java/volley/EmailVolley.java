package volley;

import android.content.Context;

public interface EmailVolley {

    /**
     * Sends email to user
     * @param toData - email address to send to
     * @param fromData - email address of app
     * @param subjectData - email subject
     * @param messageData - email message
     * @param context - context of app
     */
    void sendEmail(String toData, String fromData, String subjectData, String messageData, Context context);
}
