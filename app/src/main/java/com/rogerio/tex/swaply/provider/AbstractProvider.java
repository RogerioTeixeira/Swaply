package com.rogerio.tex.swaply.provider;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.AuthCredential;
import com.rogerio.tex.swaply.OnCompleteListener;
import com.rogerio.tex.swaply.TaskResult;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public abstract class AbstractProvider extends ContextWrapper {

    protected OnCompleteListener<TaskResult<UserResult>> listener;

    public AbstractProvider(Context context, OnCompleteListener<TaskResult<UserResult>> listener) {
        super(context);
        this.listener = listener;
    }

    public abstract void onStop();

    public abstract String getProviderId();

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public abstract void startLogin();

    public abstract AuthCredential createAuthCredential(AuthResponse response);

    protected void finish(@Nullable UserResult result, @Nullable Exception exception) {
        if (listener != null) {
            boolean successful = true;
            if (exception != null) {
                successful = false;
            }
            TaskResult<UserResult> task = TaskResult.Builder.create()
                    .setResult(result)
                    .setException(exception)
                    .setSuccessful(successful)
                    .build();
            listener.onComplete(task);
        }
    }

    protected void finish(@NonNull UserResult result) {
        this.finish(result, null);
    }

    protected void finish(@NonNull Exception exception) {
        this.finish(null, exception);
    }


}
