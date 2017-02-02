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
import com.rogerio.tex.swaply.TaskResult;

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


    public FacebookProvider(AppCompatActivity activity, OnCompleteListener<TaskResult<UserResult>> listener) {
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
        final AccessToken accessToken = loginResult.getAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                if (response.getError() != null) {
                    finish(response.getError().getException());
                } else if (object == null) {
                    Exception exception = new IllegalStateException("Facebook data response is null");
                    finish(exception);
                } else {
                    try {
                        Log.v("JsonStub", object.toString());
                        String email = object.getString("email");
                        String name = object.getString("name");
                        String picture = object.getJSONObject("picture").getJSONObject("data").getString("url");
                        UserResult user = UserResult.Builder.create()
                                .setEmail(email)
                                .setName(name)
                                .setToken(accessToken.getToken())
                                .setPhotoUrl(picture)
                                .setProvideData(getProviderId())
                                .build();
                        finish(user);
                    } catch (JSONException exception) {
                        finish(exception);
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(500).height(500)");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private TaskResult<UserResult> createTaskResult(UserResult result) {
        TaskResult<UserResult> task = TaskResult.Builder.create()
                .setResult(result)
                .setSuccessful(true)
                .build();
        return task;
    }

    private TaskResult<UserResult> createTaskResult(Exception e) {
        TaskResult<UserResult> task = TaskResult.Builder.create()
                .setException(e)
                .setSuccessful(false)
                .build();
        return task;
    }

    @Override
    public void onCancel() {
        Log.v(TAG, "Facebook login cancel");
    }

    @Override
    public void onError(FacebookException error) {
        Log.e(TAG, "Error facebook login", error);
        finish(error);
    }

    @Override
    public AuthCredential createAuthCredential(UserResult response) {
        return FacebookAuthProvider.getCredential(response.getToken());
    }

    @Override
    public void onStop() {

    }

}
