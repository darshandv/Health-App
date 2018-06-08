package com.myapp.darshan.healthie.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.darshan.healthie.R;
import com.myapp.darshan.healthie.app.Config;

/**
 * Created by darshan on 8/6/18.
 */

public class ReceiverAll extends BroadcastReceiver {
    String tip_data;
    private DatabaseReference mDatabase;
    private String test;
    private SharedPreferences sharedPref;
    @Override
    public void onReceive(final Context context, Intent intent) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tips");
        sharedPref = context.getSharedPreferences(String.valueOf(R.string.sharedPreferenceForTip), Context.MODE_PRIVATE);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tip_data = dataSnapshot.getValue(String.class);

                test = sharedPref.getString(String.valueOf(R.string.saved_high_score_default_key), "No name defined");

                Toast.makeText(context,"You received : "+test,
                        Toast.LENGTH_SHORT).show();
                if (!tip_data.equals(test)){
                    createNotification(context, tip_data);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(String.valueOf(R.string.saved_high_score_default_key), tip_data);
                    editor.commit();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failure", "Failed to read value.", error.toException());
            }
        });
    }

    private void createNotification(Context context, String messageBody) {
        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", messageBody);

        PendingIntent resultIntent = PendingIntent.getActivity( context, 0, pushNotification, PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Healthie")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
