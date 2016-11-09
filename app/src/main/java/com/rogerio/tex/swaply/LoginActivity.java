package com.rogerio.tex.swaply;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.rogerio.tex.swaply.fragment.BaseLoginFragment;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity implements BaseLoginFragment.LoginCallback {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "vN1aPHy2T0Y8O4S6L58D6bktc";
    private static final String TWITTER_SECRET = "91MPS4g3wmHcWz1WdjHiN4ifxMDHXvgNmCTYuTMBR07ixXICwU";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onConnected(Task<AuthResult> task) {
        Log.v("LoginActivity", "Chiamata onConnected");
    }
}
