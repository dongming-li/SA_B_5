package service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import cysrides.cysrides.LoginActivity;
import cysrides.cysrides.R;

public class NotificationServiceImpl implements NotificationService {

    /**
     * sends notification about ride to user
     * @param context - context of app
     */
    @Override
    public void showRideNotification(Context context) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, LoginActivity.class), 0);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_calendar)
                .setTicker("Cy's Rides")
                .setContentTitle("Upcoming trip with Cy's Rides")
                .setContentText("")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

}
