package com.rogerio.tex.swaply.provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.TwitterAuthProvider;
import com.rogerio.tex.swaply.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public class TwitterProvider extends AuthProvider {

    private static final String TAG = "TwitterProvider";
    private TwitterAuthClient client;


    public TwitterProvider(AppCompatActivity activity) {
        super(activity);
        initialize();

    }

    public TwitterProvider(AppCompatActivity activity, AuthCallback authCallback) {
        super(activity, authCallback);
        initialize();
    }

    private void initialize() {
        AppCompatActivity activity = appCompatActivity;
        String twitter_key = activity.getResources().getString(R.string.twitter_key);
        String twitter_secret = activity.getResources().getString(R.string.twitter_secret);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitter_key, twitter_secret);
        Fabric.with(activity.getApplicationContext(), new Twitter(authConfig));

    }

    @Override
    public String getProviderId() {
        return TwitterAuthProvider.PROVIDER_ID;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE == requestCode) {
            client.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void startLogin() {
        Activity activity = appCompatActivity;
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
        Log.v(TAG, "twitter prima login");
        client = new TwitterAuthClient();
        client.authorize(activity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                Log.v(TAG, "twitter success");
                TwitterSession session = twitterSessionResult.data;
                AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                        session.getAuthToken().secret);
                // authCallback.onSuccess(credential);
            }

            @Override
            public void failure(TwitterException e) {
                Log.e(TAG, "Error twitter login", e);
                authCallback.onFailure(new Bundle());
            }
        });


    }

    @Override
    public AuthCredential createAuthCredential(AuthResponse response) {
        return TwitterAuthProvider.getCredential(response.getToken(), response.getSecretKey());
    }


    @Override
    public void onStop() {
        onDestroy();
        Log.v(TAG, "twitter ondestroy");
    }

}
