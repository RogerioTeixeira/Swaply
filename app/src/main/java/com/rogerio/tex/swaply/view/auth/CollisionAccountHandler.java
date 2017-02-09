package com.rogerio.tex.swaply.view.auth;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.TwitterAuthProvider;
import com.rogerio.tex.swaply.OnCompleteListener;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.TaskFailureLogger;
import com.rogerio.tex.swaply.TaskResult;
import com.rogerio.tex.swaply.provider.UserResult;

/**
 * Created by rogerio on 07/01/2017.
 */

public class CollisionAccountHandler {
    private final static String TAG = "CollisionAccountHandler";

    public CollisionAccountHandler() {


    }

    public void show(final String email, final AppCompatActivity activity, @NonNull final OnCompleteListener<TaskResult<UserResult>> listener) {
        show(email, activity.getSupportFragmentManager(), listener);
    }

    public void show(final String email, final Fragment fragment, @NonNull final OnCompleteListener<TaskResult<UserResult>> listener) {
        show(email, fragment.getFragmentManager(), listener);
    }

    public void show(final String email, final FragmentManager fm, @NonNull final OnCompleteListener<TaskResult<UserResult>> listener) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.fetchProvidersForEmail(email)
                .addOnFailureListener(new TaskFailureLogger(TAG, "Errore fetch provider"))
                .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.isSuccessful()) {
                            String providerId = task.getResult().getProviders().get(0);
                            DialogCollisionError.Make(email, providerId)
                                    .setDialogClickListener(listener)
                                    .show(fm, "DialogCollision");

                        } else {
                            TaskResult<UserResult> taskResult = TaskResult.Builder.create()
                                    .setSuccessful(false)
                                    .setException(task.getException())
                                    .build();

                            listener.onComplete(taskResult);
                        }
                    }
                });
    }

    public static class DialogCollisionError extends DialogFragment {

        private final static String TAG = "DialogCollisionError";
        private static final String EMAIL_PARAM = "email";
        private static final String PROVIDER_PARAM = "provider_id";
        private OnCompleteListener<TaskResult<UserResult>> listener;

        public static DialogCollisionError Make(String email, String providerId) {
            DialogCollisionError fragment = new DialogCollisionError();
            Bundle args = new Bundle();
            args.putString(EMAIL_PARAM, email);
            args.putString(PROVIDER_PARAM, providerId);
            fragment.setArguments(args);
            return fragment;
        }

        public DialogCollisionError setDialogClickListener(OnCompleteListener<TaskResult<UserResult>> listener) {
            this.listener = listener;
            return this;
        }

        private String getProviderName(String providerId) {
            String providerName = null;
            switch (providerId) {
                case GoogleAuthProvider.PROVIDER_ID:
                    providerName = getResources().getString(R.string.provider_name_google);
                    break;
                case FacebookAuthProvider.PROVIDER_ID:
                    providerName = getResources().getString(R.string.provider_name_facebook);
                    break;
                case TwitterAuthProvider.PROVIDER_ID:
                    providerName = getResources().getString(R.string.provider_name_twitter);
                    break;
                case EmailAuthProvider.PROVIDER_ID:
                    providerName = getResources().getString(R.string.provider_name_Email);
                    break;
            }

            return providerName;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final String mail = getArguments().getString(EMAIL_PARAM);
            final String providerId = getArguments().getString(PROVIDER_PARAM);
            String providerName = getProviderName(providerId);
            String testo = String.format(getResources().getString(R.string.message_email_collision), mail, providerName);
            builder.setMessage(testo)
                    .setTitle("Attenzione")
                    .setPositiveButton("Accedi", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (listener != null) {
                                UserResult user = UserResult.Builder.create()
                                        .setEmail(mail)
                                        .setProvideData(providerId)
                                        .build();
                                TaskResult<UserResult> task = TaskResult.Builder.create()
                                        .setResult(user)
                                        .setSuccessful(true)
                                        .build();
                                listener.onComplete(task);
                            }
                        }
                    })
                    .setNegativeButton("Cancella", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();

        }
    }
}
