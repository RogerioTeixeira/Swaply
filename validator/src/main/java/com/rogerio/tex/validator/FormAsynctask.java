package com.rogerio.tex.validator;

import android.os.AsyncTask;

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
            try {
                validationTask.Validate(args);
            } catch (Exception e) {
                validationResultList.add(new FormValidationResult(formContext, e));
            }
        }
        return validationResultList;
    }

    @Override
    protected void onPostExecute(List<FormValidationResult> args) {
        if (args.size() == 0) {
            listener.get().onFormValidationSuccessful();
        } else {
            listener.get().onFormValidationFailed(args);
        }

        listener.clear();
    }

    public interface onCompleteFormAsynctask {
        void onFormValidationSuccessful();

        void onFormValidationFailed(List<FormValidationResult> errorValidations);

    }


}