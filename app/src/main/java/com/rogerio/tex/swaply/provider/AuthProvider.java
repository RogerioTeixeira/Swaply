package com.rogerio.tex.swaply.provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.firebase.auth.AuthCredential;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public abstract class AuthProvider {

    protected Activity mActivity;

    public AuthProvider(FragmentActivity activity) {
        mActivity = activity;
    }

    public abstract String getProviderId();

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public abstract void startLogin(Activity activity);

    public abstract void setAuthenticationCallback(AuthCallback callback);

    public abstract void onStop();

    interface AuthCallback {
        void onSuccess(AuthCredential credential);

        void onFailure(Bundle extra);
    }
}
