package volley;

import android.content.Context;

import domain.Message;

interface MessageVolley {

    /**
     * creates message to be sent to group
     * @param context - context of app
     * @param message - message to be sent
     */
    void createMessage(final Context context, final Message message);
  
}
