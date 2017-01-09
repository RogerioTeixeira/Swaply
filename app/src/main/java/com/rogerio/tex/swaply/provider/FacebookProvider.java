package com.rogerio.tex.swaply.provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FacebookAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

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
        AppCompatActivity activity = appCompatActivity;
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
        Activity activity = appCompatActivity;
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, this);
        List<String> permission = new ArrayList<String>();
        permission.add("email");
        permission.add("public_profile");
        loginManager.logInWithReadPermissions(activity, permission);
    }


    @Override
    public void onSuccess(final LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    Log.e(TAG, "Received Facebook error: " + response.getError().getErrorMessage());
                    authCallback.onFailure(new Bundle());
                    return;
                }
                if (object == null) {
                    Log.w(TAG, "Received null response from Facebook GraphRequest");
                    authCallback.onFailure(new Bundle());
                } else {
                    try {
                        String email = object.getString("email");
                        String name = object.getString("first_name") + " " + object.getString("last_name");

                        authCallback.onSuccess(new ProviderResponse(email, loginResult.getAccessToken().getToken(), getProviderId(), name));
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Exception reading from Facebook GraphRequest", e);
                        authCallback.onFailure(new Bundle());
                    }
                }


            }
        });

    }

    @Override
    public void onCancel() {
        Log.v(TAG, "Facebook login cancel");
        authCallback.onFailure(new Bundle());
    }

    @Override
    public void onError(FacebookException error) {
        Log.e(TAG, "Error facebook login", error);
        authCallback.onFailure(new Bundle());
    }

    @Override
    public void onStop() {
        onDestroy();
    }

}
