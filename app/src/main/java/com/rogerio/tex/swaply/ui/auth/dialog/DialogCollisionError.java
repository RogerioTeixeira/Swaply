package com.rogerio.tex.swaply.ui.auth.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.rogerio.tex.swaply.R;

/**
 * Created by rogerio on 07/01/2017.
 */

public class DialogCollisionError extends DialogFragment {

    private final static String TAG = "DialogCollisionError";
    private static final String EMAIL_PARAM = "email";
    private static final String PROVIDER_PARAM = "provider_id";
    private DialogClickListener listener;

    public DialogCollisionError setDialogClickListener(DialogClickListener listener) {
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
        String mail = getArguments().getString(EMAIL_PARAM);
        final String providerId = getArguments().getString(PROVIDER_PARAM);
        String providerName = getProviderName(providerId);
        String testo = String.format(getResources().getString(R.string.message_email_collision), mail, providerName);
        builder.setMessage(testo)
                .setTitle("Attenzione")
                .setPositiveButton("Accedi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onYesClick(providerId);
                    }
                })
                .setNegativeButton("Cancella", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onNoClick();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();

    }

    public interface DialogClickListener {
        void onYesClick(String providerId);

        void onNoClick();
    }

    public static class Builder {
        private final String email;
        private final String providerId;
        private DialogClickListener listener;

        public Builder(String email, String providerId) {
            this.email = email;
            this.providerId = providerId;
        }

        public Builder setDialogClickListener(DialogClickListener listener) {
            this.listener = listener;
            return this;
        }

        public DialogCollisionError create() {
            DialogCollisionError fragment = new DialogCollisionError();
            Bundle args = new Bundle();
            args.putString(EMAIL_PARAM, email);
            args.putString(PROVIDER_PARAM, providerId);
            fragment.setArguments(args);
            fragment.setDialogClickListener(listener);
            return fragment;
        }

    }


}