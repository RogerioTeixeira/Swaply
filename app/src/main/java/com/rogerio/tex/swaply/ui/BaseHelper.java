package com.rogerio.tex.swaply.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Rogerio Lavoro on 04/01/2017.
 */

public class BaseHelper {
    private final Context context;
    private ProgressDialog progressDialog;

    public BaseHelper(Context context) {
        this.context = context;
    }

    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }


    public void showLoadingDialog(String message) {
        dismissDialog();
        progressDialog = ProgressDialog.show(context, "", message, true);
    }

    public void showLoadingDialog(@StringRes int stringResource) {
        showLoadingDialog(context.getString(stringResource));
    }

    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }

    public void finishActivity(Activity activity, int resultCode, Intent intent) {
        activity.setResult(resultCode, intent);
        activity.finish();
    }

    public Context getContext() {
        return context;
    }


}
