package com.rogerio.tex.swaply.provider;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public class FacebookProvider extends AuthProvider implements FacebookCallback<LoginResult> {

    private static final String TAG = "FacebookProvider";
    private CallbackManager callbackManager;


    public FacebookProvider(AppCompatActivity activity) {
        super(activity);
        initFacebook();

    }

    public FacebookProvider(AppCompatActivity activity, AuthCallback authCallback) {
        super(activity, authCallback);
        initFacebook();
    }

    private void initFacebook() {
        AppCompatActivity activity = weakActivity.get();
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

    }

    @Override
    public String getProviderId() {
        return FacebookAuthProvider.PROVIDER_ID;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startLogin() {
        Activity activity = weakActivity.get();
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, this);
        List<String> permission = new ArrayList<String>();
        permission.add("email");
        permission.add("public_profile");
        loginManager.logInWithReadPermissions(activity, permission);
    }


    @Override
    public void onSuccess(final LoginResult loginResult) {
        if (weakAuthCallback != null) {
            AuthCallback mAuthCallback = weakAuthCallback.get();
            AccessToken accessToken = loginResult.getAccessToken();
            AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
            mAuthCallback.onSuccess(credential);
        }
    }

    @Override
    public void onCancel() {
        Log.v(TAG, "Facebook login cancel");
    }

    @Override
    public void onError(FacebookException error) {
        Log.e(TAG, "Error facebook login", error);
    }

    @Override
    public void onStop() {
        onDestroy();
    }

}
