package com.rogerio.tex.swaply.util;

import android.support.design.widget.TextInputLayout;

import com.rogerio.tex.swaply.util.validator.IValidator;
import com.rogerio.tex.swaply.util.validator.ValidationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rogerio Lavoro on 19/12/2016.
 */

public class FormValidation {
    private Map<IValidator<CharSequence>, TextInputLayout> validatorMap;
    private boolean mIsValid;

    public FormValidation() {
        validatorMap = new HashMap<>();
        mIsValid = true;
    }

    public void addValidationRule(IValidator<CharSequence> validator, TextInputLayout inputLayout) {
        validatorMap.put(validator, inputLayout);
    }

    public boolean isSuccess() {
        return mIsValid;
    }

    public void validate() {
        mIsValid = true;
        for (IValidator<CharSequence> validator : validatorMap.keySet()) {
            TextInputLayout inputLayout = validatorMap.get(validator);
            inputLayout.setError(null);
            CharSequence arg = inputLayout.getEditText().getText();
            try {
                validator.validate(arg);
            } catch (ValidationException e) {
                mIsValid = false;
                inputLayout.setError(e.getMessage());
            }
        }
    }
}
