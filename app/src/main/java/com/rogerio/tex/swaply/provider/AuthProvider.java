package com.rogerio.tex.swaply.provider;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.AuthCredential;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public abstract class AuthProvider extends ContextWrapper {

    protected AuthCallback authCallback;

    public AuthProvider(Context context, AuthCallback authCallback) {
        super(context);
        this.authCallback = authCallback;
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
