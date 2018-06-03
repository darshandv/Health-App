package com.myapp.darshan.healthie.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapp.darshan.healthie.R;

public class AdminActivity extends AppCompatActivity {

    public EditText tip_layout ;
    private String tip;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Button btn_sign_out;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        tip_layout = (EditText)findViewById(R.id.input_tip);
        btn_sign_out = (Button)findViewById(R.id.sign_out);
        tip = tip_layout.getText().toString();
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        mFirebaseInstance = FirebaseDatabase.getInstance();


    }

    public void signOut()
    {
        auth.signOut();
        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
        finish();
    }
}
