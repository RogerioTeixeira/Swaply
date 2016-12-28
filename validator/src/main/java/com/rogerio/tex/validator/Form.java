package com.rogerio.tex.validator;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;

import com.rogerio.tex.validator.rule.EmailRule;
import com.rogerio.tex.validator.rule.EmptyRule;
import com.rogerio.tex.validator.rule.PasswordRule;
import com.rogerio.tex.validator.rule.Rule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogerio Lavoro on 19/12/2016.
 */

public class Form implements FormAsynctask.onCompleteFormAsynctask {
    private final List<FormContext> listFormContext = new ArrayList<>();
    private final WeakReference<onCompleteValidationListener> callback;

    public Form(List<FormContext> listFormContext, onCompleteValidationListener callback) {
        this.callback = new WeakReference<>(callback);
        this.listFormContext.addAll(listFormContext);
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
            Log.v("messaggioError", "inputLayout");
            textInputLayout.setError(message);
        } else if (view instanceof EditText) {
            Log.v("messaggioError", "Edittext");
            ((EditText) view).setError(message);
        }

    }

    public void validate() {
        new FormAsynctask(this).execute(listFormContext);
    }

    @Override
    public void onFormValidationSuccessful() {
        callback.get().onFormValidationSuccessful();
    }

    @Override
    public void onFormValidationFailed(List<FormValidationResult> errorValidations) {
        for (FormValidationResult validationResult : errorValidations) {
            EditText editText = validationResult.getFormContext().editText;
            String errorMessage = validationResult.getException().getMessage();
            setErrorMessage(editText, errorMessage);
        }
        callback.get().onFormValidationFailed(errorValidations);
    }

    public interface onCompleteValidationListener {
        void onFormValidationSuccessful();

        void onFormValidationFailed(List<FormValidationResult> errorValidations);

    }

    public static class Builder {
        private final WeakReference<Context> context;
        private final List<FormContext> listFormContext = new ArrayList<>();
        private WeakReference<onCompleteValidationListener> listener;

        public Builder(Context context) {
            this.context = new WeakReference<>(context);
        }

        public Builder addValidationTask(EditText editText, ValidationTask validationTask) {
            CharSequence arg = editText.getText();
            FormContext formContext = new FormContext(editText, arg, validationTask);
            listFormContext.add(formContext);
            return this;
        }


        public Builder addEmailValidationTask(String errorMessage, EditText editText, boolean verifyEmpty) {
            Rule<CharSequence> rule = new EmailRule(errorMessage);
            ValidationTask validationTask = createValidationTask(rule, verifyEmpty);
            addValidationTask(editText, validationTask);
            return this;
        }

        public Builder addEmailValidationTask(int errorMessageResId, EditText editText, boolean verifyEmpty) {
            String errorMessage = "";
            if (context != null && context.get() != null) {
                errorMessage = context.get().getResources().getString(errorMessageResId);
            }
            addEmailValidationTask(errorMessage, editText, verifyEmpty);
            return this;
        }

        public Builder addPasswordValidationTask(String errorMessage, EditText editText, boolean verifyEmpty) {
            Rule<CharSequence> rule = new PasswordRule(errorMessage);
            ValidationTask validationTask = createValidationTask(rule, verifyEmpty);
            addValidationTask(editText, validationTask);
            return this;
        }

        public Builder addPasswordValidationTask(int errorMessageResId, EditText editText, boolean verifyEmpty) {
            String errorMessage = "";
            if (context != null && context.get() != null) {
                errorMessage = context.get().getResources().getString(errorMessageResId);
            }
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
            Form form = new Form(listFormContext, listener.get());

            listener.clear();

            return form;
        }


    }


}
