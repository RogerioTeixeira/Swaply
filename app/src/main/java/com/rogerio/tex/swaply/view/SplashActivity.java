package com.rogerio.tex.swaply.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rogerio.tex.swaply.view.auth.LoginActivity;

public class SplashActivity extends BaseActivity {
    private ImageView image;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = getActivityHelper().getFirebaseAuth();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    LoginActivity.startActivity(SplashActivity.this);
                } else {
                    MainActivity.startActivity(SplashActivity.this);
                }
                finish();
            }
        };

    }

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            firebaseAuth.removeAuthStateListener(authListener);
        }
    }

}
