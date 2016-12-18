package com.rogerio.tex.swaply.util.validator;

import android.support.design.widget.TextInputLayout;

import com.rogerio.tex.swaply.R;

/**
 * Created by rogerio on 18/12/2016.
 */

public class PasswordValidator extends BaseValidator {

    public PasswordValidator(TextInputLayout container) {
        super(container);
    }

    @Override
    public String getErrorMessage() {
        return getContainer().getResources().getString(R.string.validate_error_invalid_password);
    }

    @Override
    public String getEmptyMessage() {
        return getContainer().getResources().getString(R.string.validate_error_missing_password);
    }

    @Override
    protected boolean isValidate(CharSequence charSequence) {
        return charSequence.length() >= 6;

    }

}
