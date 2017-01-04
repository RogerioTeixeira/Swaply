package com.rogerio.tex.validator;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;

import com.rogerio.tex.validator.rule.EmailRule;
import com.rogerio.tex.validator.rule.EmptyRule;
import com.rogerio.tex.validator.rule.PasswordRule;
import com.rogerio.tex.validator.rule.Rule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rogerio Lavoro on 19/12/2016.
 */

public class Form implements FormAsynctask.onCompleteFormAsynctask {
    private final Map<EditText, ValidationTask> mapParam = new HashMap<>();
    private final WeakReference<onCompleteValidationListener> callback;

    private Form(Map<EditText, ValidationTask> mapParam, onCompleteValidationListener callback) {
        this.callback = new WeakReference<>(callback);
        this.mapParam.putAll(mapParam);
    }

    @Nullable
    private TextInputLayout getTextInputLayout(@NonNull View view) {
        View currentView = view;
        for (int i = 0; i < 2; i++) {
            ViewParent parent = currentView.getParent();
            if (parent instanceof TextInputLayout) {
                return (TextInputLayout) parent;
            } else {
                currentView = (View) parent;
            }
        }
        return null;
    }

    private void setErrorMessage(@NonNull View view, String message) {

        TextInputLayout textInputLayout = getTextInputLayout(view);
        if (textInputLayout != null) {
            if (message == null) {
                textInputLayout.setError(message);
                textInputLayout.setErrorEnabled(false);
            } else {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(message);
            }

        } else if (view instanceof EditText) {
            ((EditText) view).setError(message);
        }

    }

    public void validate() {

        final List<FormContext> listFormContext = new ArrayList<>();
        for (EditText editText : mapParam.keySet()) {
            ValidationTask validationTask = mapParam.get(editText);
            CharSequence charSequence = editText.getText();
            listFormContext.add(new FormContext(editText, validationTask));
        }

        new FormAsynctask(this).execute(listFormContext);
    }

    /*
              Callbacks della FormAsynctask.onCompleteFormAsynctask
    */

    @Override
    public void onFormValidationSuccessful(List<FormValidationResult> validationResults) {
        setError(validationResults);
        callback.get().onFormValidationSuccessful(validationResults);
    }

    @Override
    public void onFormValidationFailed(List<FormValidationResult> validationResults) {
        setError(validationResults);
        callback.get().onFormValidationFailed(validationResults);
    }
    /*
          fine callbacks FormAsynctask.onCompleteFormAsynctask
     */

    private void setError(List<FormValidationResult> formValidationResults) {
        for (FormValidationResult validationResult : formValidationResults) {
            EditText editText = validationResult.getFormContext().editText;
            String errorMessage = null;
            if (!validationResult.isOk()) {
                errorMessage = validationResult.getException().getMessage();
            }
            setErrorMessage(editText, errorMessage);
        }
    }

    public interface onCompleteValidationListener {
        void onFormValidationSuccessful(List<FormValidationResult> validationResults);

        void onFormValidationFailed(List<FormValidationResult> validationResults);

    }

    public static class Builder {
        private final Map<EditText, ValidationTask> mapParam = new HashMap<>();
        private WeakReference<onCompleteValidationListener> listener;

        public Builder() {

        }

        public Builder addValidationTask(EditText editText, ValidationTask validationTask) {
            mapParam.put(editText, validationTask);
            return this;
        }


        public Builder addEmailValidationTask(String errorMessage, EditText editText, boolean verifyEmpty) {
            Rule<CharSequence> rule = new EmailRule(errorMessage);
            ValidationTask validationTask = createValidationTask(rule, verifyEmpty);
            addValidationTask(editText, validationTask);
            return this;
        }

        public Builder addEmailValidationTask(@StringRes int errorMessageResId, EditText editText, boolean verifyEmpty) {
            final Context context = editText.getContext();
            final String errorMessage = context.getResources().getString(errorMessageResId);
            addEmailValidationTask(errorMessage, editText, verifyEmpty);
            return this;
        }

        public Builder addPasswordValidationTask(String errorMessage, EditText editText, boolean verifyEmpty) {
            Rule<CharSequence> rule = new PasswordRule(errorMessage);
            ValidationTask validationTask = createValidationTask(rule, verifyEmpty);
            addValidationTask(editText, validationTask);
            return this;
        }

        public Builder addPasswordValidationTask(@StringRes int errorMessageResId, EditText editText, boolean verifyEmpty) {

            final Context context = editText.getContext();
            final String errorMessage = context.getResources().getString(errorMessageResId);
            addPasswordValidationTask(errorMessage, editText, verifyEmpty);
            return this;
        }

        public Builder addonCompleteValidationListener(onCompleteValidationListener listener) {
            this.listener = new WeakReference<>(listener);
            return this;
        }

        private ValidationTask createValidationTask(Rule rule, boolean verifyEmpty) {
            ValidationTask<CharSequence> task = new ValidationTask<>();
            if (verifyEmpty) {
                task.addRule(new EmptyRule(rule.getErrorMessage()));
            }
            task.addRule(rule);
            return task;
        }

        public Form CreateForm() {
            Form form = new Form(mapParam, listener.get());

            listener.clear();

            return form;
        }


    }


}
