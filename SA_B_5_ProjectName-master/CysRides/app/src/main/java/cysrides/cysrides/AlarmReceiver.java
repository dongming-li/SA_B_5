package cysrides.cysrides;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import service.NotificationService;
import service.NotificationServiceImpl;

public class AlarmReceiver extends BroadcastReceiver {

    NotificationService notificationService = new NotificationServiceImpl();

    /**
     * Handles notifications for received alarms
     * @param context of app
     * @param intent of page
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        notificationService.showRideNotification(context);

    }
}