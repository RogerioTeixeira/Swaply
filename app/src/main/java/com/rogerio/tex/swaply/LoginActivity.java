package com.rogerio.tex.swaply;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rogerio.tex.swaply.fragment.BaseLoginFragment;
import com.twitter.sdk.android.core.TwitterAuthConfig;


public class LoginActivity extends AppCompatActivity implements BaseLoginFragment.LoginCallback {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("verificaAuth", "Resume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("verificaAuth", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_login_twitter);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onConnected(Task<AuthResult> task) {
        Log.v("LoginActivity", "Chiamata onConnected");
    }
}
