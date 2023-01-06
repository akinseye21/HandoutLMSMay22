package com.example.handoutlms;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationGenerator {

    public static String myNotification;

    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 9;

    public static  void openActivityNotification(Context context){
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, AlmostDoneOnline.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);
        nc.setSmallIcon(R.drawable.logo);
        nc.setAutoCancel(true);
        nc.setContentTitle("Handout Notification");
        nc.setContentText(myNotification);

        nm.notify(NOTIFICATION_ID_OPEN_ACTIVITY, nc.build());
    }

    public void setString(String notification){
        myNotification = notification;
    }
}
