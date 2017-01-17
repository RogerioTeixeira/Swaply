package com.rogerio.tex.swaply.provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.rogerio.tex.swaply.ui.auth.EmailAuthActivity;
import com.rogerio.tex.swaply.ui.auth.ResultEmailActivity;

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
        Log.v(TAG, "Email rquest request:" + requestCode);
        if (requestCode == EmailAuthActivity.REQUEST_CODE) {
            ResultEmailActivity resultEmailActivity;
            switch (resultCode) {
                case EmailAuthActivity.RESULT_OK:
                    resultEmailActivity = EmailAuthActivity.getResultEmailActivity(data);
                    if (resultEmailActivity != null) {
                        //    authCallback.onSuccess(new AuthResponse(resultEmailActivity.getEmail(), null, resultEmailActivity.getProvideId(), null));
                    }
                    break;
                case EmailAuthActivity.RESULT_COLLISION:
                    resultEmailActivity = EmailAuthActivity.getResultEmailActivity(data);
                    if (resultEmailActivity != null) {
                        authCallback.loginWith(resultEmailActivity.getProvideId());
                    }
                    break;
                default:
                    authCallback.onFailure(new Bundle());
                    break;
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
