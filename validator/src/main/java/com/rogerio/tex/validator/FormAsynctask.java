package com.rogerio.tex.validator;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rogerio on 28/12/2016.
 */

public class FormAsynctask extends AsyncTask<List<FormContext>, Void, List<FormValidationResult>> {

    private final WeakReference<onCompleteFormAsynctask> listener;

    public FormAsynctask(onCompleteFormAsynctask listener) {
        this.listener = new WeakReference<onCompleteFormAsynctask>(listener);
    }

    @Override
    protected List<FormValidationResult> doInBackground(List<FormContext>... params) {
        final List<FormContext> listFormContext = params[0];
        final List<FormValidationResult> validationResultList = new ArrayList<>();
        for (FormContext formContext : listFormContext) {
            ValidationTask<CharSequence> validationTask = formContext.validationTask;
            CharSequence args = formContext.arg;
            Log.v("TaskForm", "Argomento:" + args);
            Exception exception = null;
            try {
                validationTask.Validate(args);
            } catch (Exception e) {
                exception = e;
            }
            validationResultList.add(new FormValidationResult(formContext, exception));
        }
        return validationResultList;
    }

    @Override
    protected void onPostExecute(List<FormValidationResult> args) {
        boolean isError = false;
        for (FormValidationResult validationResult : args) {
            if (validationResult.getException() != null) {
                isError = true;
                break;
            }
        }
        if (!isError) {
            listener.get().onFormValidationSuccessful(args);
        } else {
            listener.get().onFormValidationFailed(args);
        }

        listener.clear();
    }

    public interface onCompleteFormAsynctask {
        void onFormValidationSuccessful(List<FormValidationResult> validationResults);

        void onFormValidationFailed(List<FormValidationResult> validationResults);

    }


}