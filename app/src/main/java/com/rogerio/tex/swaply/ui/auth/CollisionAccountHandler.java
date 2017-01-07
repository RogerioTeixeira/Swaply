package com.rogerio.tex.swaply.ui.auth;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.rogerio.tex.swaply.TaskFailureLogger;
import com.rogerio.tex.swaply.ui.BaseHelper;
import com.rogerio.tex.swaply.ui.auth.dialog.DialogCollisionError;

/**
 * Created by rogerio on 07/01/2017.
 */

public class CollisionAccountHandler implements DialogCollisionError.DialogClickListener {
    private final static String TAG = "CollisionAccountHandler";

    final BaseHelper helper;

    public CollisionAccountHandler(BaseHelper helper) {
        this.helper = helper;

    }

    @Override
    public void onYesClick(String providerId) {
        Toast.makeText(helper.getContext(), "Yes click:" + providerId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNoClick() {
        Toast.makeText(helper.getContext(), "No click:", Toast.LENGTH_LONG).show();
    }

    public void show(final String email, final FragmentManager fm) {
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
                                    .setDialogClickListener(CollisionAccountHandler.this)
                                    .create();
                            dialogFragment.show(fm, "DialogCollision");
                        }
                    }
                });
    }


}
