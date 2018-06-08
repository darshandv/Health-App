package com.myapp.darshan.healthie.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.myapp.darshan.healthie.R;

/**
 * Created by darshan on 7/6/18.
 */

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    public void showNotification(Context context) {
        Intent intent = new Intent(context, AdminActivity.class);
        PendingIntent resultIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Healthie")
                .setContentText("You need to send a tip today")
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mNotificationBuilder.build());
    }
}
