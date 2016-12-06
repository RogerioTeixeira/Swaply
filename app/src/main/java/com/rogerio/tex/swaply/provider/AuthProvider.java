package com.rogerio.tex.swaply.provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;

import java.lang.ref.WeakReference;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public abstract class AuthProvider {

    protected WeakReference<AppCompatActivity> weakActivity;
    protected WeakReference<AuthCallback> weakAuthCallback;

    public AuthProvider(AppCompatActivity activity) {
        weakActivity = new WeakReference<AppCompatActivity>(activity);
    }

    public AuthProvider(AppCompatActivity activity, AuthCallback authCallback) {
        this(activity);
        weakAuthCallback = new WeakReference<AuthCallback>(authCallback);
    }

    protected void onDestroy() {
        if (weakActivity != null) {
            weakActivity.clear();
            weakActivity = null;
        }
        if (weakAuthCallback != null) {
            weakAuthCallback.clear();
            weakAuthCallback = null;
        }
    }

    public abstract void onStop();

    public abstract String getProviderId();

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public abstract void startLogin();

    public interface AuthCallback {
        void onSuccess(AuthCredential credential);

        void onFailure(Bundle extra);
    }
}
