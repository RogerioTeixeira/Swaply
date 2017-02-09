package com.rogerio.tex.swaply.provider;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.rogerio.tex.swaply.OnCompleteListener;
import com.rogerio.tex.swaply.TaskResult;
import com.rogerio.tex.swaply.view.auth.EmailAuthActivity;

/**
 * Created by rogerio on 07/01/2017.
 */

public class EmailProvider extends AbstractProvider {

    private static final String TAG = "EmailProvider";
    private AppCompatActivity activity;


    public EmailProvider(AppCompatActivity activity, OnCompleteListener<TaskResult<UserResult>> listener) {
        super(activity, listener);
        this.activity = activity;

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
            TaskResult<UserResult> task;
            if (resultCode == Activity.RESULT_OK) {
                UserResult response = EmailAuthActivity.getResultData(data);
                task = TaskResult.Builder.create()
                        .setSuccessful(true)
                        .setResult(response)
                        .build();
            } else {
                task = TaskResult.Builder.create()
                        .setSuccessful(false)
                        .build();
            }
            listener.onComplete(task);
        }
    }

    @Override
    public void startLogin() {
        EmailAuthActivity.startActivity(activity);
    }

    @Override
    public AuthCredential createAuthCredential(UserResult response) {
        return null;
    }
}
