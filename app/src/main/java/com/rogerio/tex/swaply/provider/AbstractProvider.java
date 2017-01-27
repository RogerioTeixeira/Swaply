package com.rogerio.tex.swaply.provider;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.firebase.auth.AuthCredential;
import com.rogerio.tex.swaply.OnCompleteListener;
import com.rogerio.tex.swaply.TaskResult;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public abstract class AbstractProvider extends ContextWrapper {

    protected OnCompleteListener<TaskResult<ProviderResult>> listener;

    public AbstractProvider(Context context, OnCompleteListener<TaskResult<ProviderResult>> listener) {
        super(context);
        this.listener = listener;
    }

    public abstract void onStop();

    public abstract String getProviderId();

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public abstract void startLogin();

    public abstract AuthCredential createAuthCredential(AuthResponse response);

    protected void finish(ProviderResult result) {
        if (listener != null) {
            TaskResult<ProviderResult> task = new TaskResult<>(true, result, null, false);
            listener.onComplete(task);

        }
    }


}
