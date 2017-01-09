package com.rogerio.tex.swaply.ui.auth;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.rogerio.tex.swaply.TaskFailureLogger;
import com.rogerio.tex.swaply.ui.ActivityHelper;
import com.rogerio.tex.swaply.ui.auth.dialog.DialogCollisionError;

/**
 * Created by rogerio on 07/01/2017.
 */

public class CollisionAccountHandler {
    private final static String TAG = "CollisionAccountHandler";

    final ActivityHelper helper;

    public CollisionAccountHandler(ActivityHelper helper) {
        this.helper = helper;

    }


    public void show(final String email, final FragmentManager fm, @NonNull final CompleteListener<String> listener) {
        FirebaseAuth firebaseAuth = helper.getFirebaseAuth();
        helper.showLoadingDialog("");
        firebaseAuth.fetchProvidersForEmail(email)
                .addOnFailureListener(new TaskFailureLogger(TAG, "Errore fetch provider"))
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.isSuccessful()) {
                            String providerId = task.getResult().getProviders().get(0);
                            helper.dismissDialog();
                            DialogFragment dialogFragment = new DialogCollisionError.Builder(email, providerId)
                                    .setDialogClickListener(listener)
                                    .create();
                            dialogFragment.show(fm, "DialogCollision");
                        }
                    }
                });
    }


}
