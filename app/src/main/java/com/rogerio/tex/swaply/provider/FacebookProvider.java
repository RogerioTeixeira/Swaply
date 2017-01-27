package com.rogerio.tex.swaply.provider;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.rogerio.tex.swaply.OnCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public class FacebookProvider extends AbstractProvider implements FacebookCallback<LoginResult> {

    private static final String TAG = "FacebookProvider";
    private CallbackManager callbackManager;
    private AppCompatActivity activity;


    public FacebookProvider(AppCompatActivity activity, OnCompleteListener<ProviderResult> listener) {
        super(activity, listener);
        this.activity = activity;
        initFacebook();
    }

    private void initFacebook() {
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
                    authCallback.onFailure(new Bundle());
                    return;
                }
                if (object == null) {
                    authCallback.onFailure(new Bundle());
                } else {
                    try {
                        String email = object.getString("email");
                        String name = object.getString("name");
                        String picture = object.getString("picture");
                        AuthResponse authResponse = AuthResponse.Builder.create()
                                .setToken(loginResult.getAccessToken().getToken())
                                .setProviderId(getProviderId())
                                .setSuccessful(true)
                                .setEmail(email)
                                .setName(name)
                                .setPhotoUrl(picture)
                                .build();

                        authCallback.onSuccess(authResponse);
                    } catch (JSONException e) {
                        authCallback.onFailure(new Bundle());
                    }
                }


            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onCancel() {
        Log.v(TAG, "Facebook login cancel");
        finish();
    }

    @Override
    public void onError(FacebookException error) {
        Log.e(TAG, "Error facebook login", error);
        finish();
    }

    @Override
    public AuthCredential createAuthCredential(AuthResponse response) {
        return FacebookAuthProvider.getCredential(response.getToken());
    }

    @Override
    public void onStop() {

    }

}
