package com.rogerio.tex.swaply.provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.rogerio.tex.swaply.ui.auth.EmailAuthActivity;

/**
 * Created by rogerio on 07/01/2017.
 */

public class EmailProvider extends AuthProvider {

    private static final String TAG = "EmailProvider";

    public EmailProvider(AppCompatActivity activity) {
        super(activity);
    }

    public EmailProvider(AppCompatActivity activity, AuthCallback authCallback) {
        super(activity, authCallback);

    }

    @Override
    public void onStop() {

    }

    @Override
    public String getProviderId() {
        return EmailAuthProvider.PROVIDER_ID;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EmailAuthActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                AuthResponse response = EmailAuthActivity.getResultData(data);
                authCallback.onSuccess(response);
            } else {
                authCallback.onFailure(new Bundle());
            }
        }
    }

    @Override
    public void startLogin() {
        EmailAuthActivity.startActivity(appCompatActivity);
    }

    @Override
    public AuthCredential createAuthCredential(AuthResponse response) {
        return null;
    }
}
