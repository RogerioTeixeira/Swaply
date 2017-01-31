package com.rogerio.tex.swaply.provider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.TwitterAuthProvider;
import com.rogerio.tex.swaply.OnCompleteListener;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.TaskResult;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public class TwitterProvider extends AbstractProvider {

    private static final String TAG = "TwitterProvider";
    private TwitterAuthClient client;
    private AppCompatActivity activity;

    public TwitterProvider(AppCompatActivity activity, OnCompleteListener<TaskResult<UserResult>> listener) {
        super(activity, listener);
        this.activity = activity;
        initialize();
    }

    private void initialize() {
        String twitter_key = getResources().getString(R.string.twitter_key);
        String twitter_secret = getResources().getString(R.string.twitter_secret);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitter_key, twitter_secret);
        Fabric.with(this, new Twitter(authConfig));

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
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
        Log.v(TAG, "twitter prima login");
        client = new TwitterAuthClient();
        client.authorize(activity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                Log.v(TAG, "twitter success");
                TwitterSession session = twitterSessionResult.data;
                getUserData(session);
            }

            @Override
            public void failure(TwitterException e) {
                Log.e(TAG, "Error twitter login", e);
                finish(e);
            }
        });
    }

    public void getUserData(final TwitterSession session) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        twitterApiClient.getAccountService().verifyCredentials(false, false).enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {

                String name = userResult.data.name;
                String photoUrlNormalSize = userResult.data.profileImageUrl;
                String mail = userResult.data.email;
                String photoUrlBiggerSize = userResult.data.profileImageUrl.replace("_normal", "_bigger");
                String photoUrlMiniSize = userResult.data.profileImageUrl.replace("_normal", "_mini");
                Log.v(TAG, "getUserData-name:" + name);
                Log.v(TAG, "getUserData-photo:" + photoUrlNormalSize);

                UserResult result = UserResult.Builder.create()
                        .setProvideData(getProviderId())
                        .setToken(session.getAuthToken().token)
                        .setSecretKey(session.getAuthToken().secret)
                        .setName(name)
                        .setPhotoUrl(photoUrlNormalSize)
                        .setProvideData(getProviderId())
                        .build();
                finish(result);
            }

            @Override
            public void failure(TwitterException e) {
                Log.e(TAG, "getUserData", e);
                finish(e);
            }
        });

    }

    @Override
    public AuthCredential createAuthCredential(UserResult response) {
        return TwitterAuthProvider.getCredential(response.getToken(), response.getSecretKey());
    }


    @Override
    public void onStop() {
        Log.v(TAG, "twitter ondestroy");
    }

}
