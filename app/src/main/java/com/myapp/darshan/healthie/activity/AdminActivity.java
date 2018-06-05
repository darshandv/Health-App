package com.myapp.darshan.healthie.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.darshan.healthie.R;
import com.myapp.darshan.healthie.app.Config;

public class AdminActivity extends AppCompatActivity {

    public EditText tip_layout ;
    private String tip;
    private String tip_data;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Button btn_sign_out;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        tip_layout = (EditText)findViewById(R.id.input_tip);
        btn_sign_out = (Button)findViewById(R.id.sign_out);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tips");

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                    finish();
                }

            }
        };

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tip = tip_layout.getText().toString();

                if (!tip.trim().equals(""))
                {
                    FirebaseDatabase.getInstance().getReference().child("Tips")
                            .setValue(tip);
                }

                // Clear the input
                tip_layout.setText("");
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tip_data = dataSnapshot.getValue(String.class);

                Toast.makeText(getApplicationContext(),"You received : "+tip_data,
                        Toast.LENGTH_SHORT).show();

                createNotification(tip_data);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failure", "Failed to read value.", error.toException());
            }
        });



        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        mFirebaseInstance = FirebaseDatabase.getInstance();


    }

    private void createNotification(String messageBody) {
        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", messageBody);

        PendingIntent resultIntent = PendingIntent.getActivity(this , 0, pushNotification, PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Healthie")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mNotificationBuilder.build());
    }

    public void signOut()
    {
        auth.signOut();
        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
        finish();
    }
}
