package com.rogerio.tex.swaply.provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public abstract class AuthProvider {

    protected AppCompatActivity appCompatActivity;
    protected AuthCallback authCallback;

    public AuthProvider(AppCompatActivity activity) {
        appCompatActivity = activity;
    }

    public AuthProvider(AppCompatActivity activity, AuthCallback authCallback) {
        this(activity);
        this.authCallback = authCallback;
    }

    protected void onDestroy() {
        if (appCompatActivity != null) {
            appCompatActivity = null;
        }
        if (authCallback != null) {
            authCallback = null;
        }
    }

    public abstract void onStop();

    public abstract String getProviderId();

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public abstract void startLogin();

    public abstract AuthCredential createAuthCredential(AuthResponse response);

    public interface AuthCallback {
        void onSuccess(AuthResponse response);

        void onFailure(Bundle extra);
    }
}
