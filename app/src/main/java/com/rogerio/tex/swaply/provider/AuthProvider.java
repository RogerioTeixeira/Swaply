package com.rogerio.tex.swaply.provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.firebase.auth.AuthCredential;

import java.lang.ref.WeakReference;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public abstract class AuthProvider {

    protected WeakReference<FragmentActivity> weakActivity;
    protected WeakReference<AuthCallback> weakAuthCallback;

    public AuthProvider(FragmentActivity activity) {
        weakActivity = new WeakReference<FragmentActivity>(activity);
    }

    public AuthProvider(FragmentActivity activity, AuthCallback authCallback) {
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
