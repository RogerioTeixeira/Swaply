package com.rogerio.tex.validator;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rogerio Lavoro on 19/12/2016.
 */

public class Form {
    private final Map<EditText, ValidationTask<CharSequence>> tasks = new HashMap<>();
    private onCompleteValidationListener callback;

    public Form(onCompleteValidationListener callback) {
        this.callback = callback;
    }

    public void addValidationTask(EditText editText, ValidationTask validationTask) {
        if (!tasks.containsKey(editText)) {
            tasks.put(editText, validationTask);
        }
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
        final List<FormValidationResult> validationResultList = new ArrayList<>();
        Log.v("messaggioError", "task:" + tasks.size());
        for (EditText editText : tasks.keySet()) {
            ValidationTask<CharSequence> validationTask = tasks.get(editText);
            CharSequence args = editText.getText().toString();
            setErrorMessage(editText, "");
            try {
                if (!validationTask.isValid(args)) {
                    String errorMessage = validationTask.getErrorMessage();
                    Log.v("messaggioError", "errorMessage:" + errorMessage);
                    validationResultList.add(new FormValidationResult(editText, errorMessage));
                    setErrorMessage(editText, errorMessage);
                }
            } catch (Exception e) {
                callback.onFormValidationException(e);
                return;
            }

        }
        if (validationResultList.isEmpty()) {
            callback.onFormValidationSuccessful();
        } else {
            callback.onFormValidationFailed(validationResultList);
        }
    }



    public interface onCompleteValidationListener {
        void onFormValidationSuccessful();

        void onFormValidationFailed(List<FormValidationResult> errorValidations);

        void onFormValidationException(Exception e);
    }


}
