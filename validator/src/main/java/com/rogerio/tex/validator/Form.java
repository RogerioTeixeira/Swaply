package com.rogerio.tex.validator;


import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.rogerio.tex.validator.rule.IRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rogerio Lavoro on 19/12/2016.
 */

public class Form {
    private final Map<View, List<IRule<CharSequence>>> validatorMap = new HashMap<>();
    private final List<ValidationResult> mValidationResultList = new ArrayList<>();
    private Context mContext;
    private onCompleteValidationListener mCallback;

    public Form(Context context, onCompleteValidationListener callback) {
        mContext = context;
        mCallback = callback;
    }

    public void addValidationRule(EditText view, IRule<CharSequence>... validator) {

        validatorMap.put(view, Arrays.asList(validator));
    }

    public void addValidationRule(TextInputLayout view, IRule<CharSequence>... validator) {

        validatorMap.put(view, Arrays.asList(validator));
    }

    public void validate() {
        for (View view : validatorMap.keySet()) {
            List<IRule<CharSequence>> list = validatorMap.get(view);
            EditText editText = null;
            if (view instanceof TextInputLayout) {
                editText = ((TextInputLayout) view).getEditText();
            } else if (view instanceof EditText) {
                editText = (EditText) view;
            }
            if (editText != null) {
                CharSequence args = editText.getText().toString();
            }

        }
    }

    private List<String> executeValidate(CharSequence args, List<IRule<CharSequence>> list) {
        List<String> messageError = new ArrayList<>();
        for (IRule<CharSequence> rule : list) {
            if (!rule.isValid(args)) {
                messageError.add(rule.getErrorMessage());
            }
        }
        return messageError;
    }

    public interface onCompleteValidationListener {
        void onFormValidationSuccessful();

        void onFormValidationFailed(List<ValidationResult> errorValidations, View v);

        void onFormValidationException(Exception e);
    }

}
