package com.rogerio.tex.swaply.util.validator;

import android.support.design.widget.TextInputLayout;
import android.view.View;

/**
 * Created by rogerio on 18/12/2016.
 */

public abstract class BaseValidator {
    private TextInputLayout mContainer;

    public BaseValidator(TextInputLayout container) {
        mContainer = container;
    }

    protected View getContainer() {
        return mContainer;
    }

    public abstract String getErrorMessage();

    public abstract String getEmptyMessage();

    protected abstract boolean isValidate(CharSequence charSequence);


    public boolean validate(CharSequence charSequence) {
        if (getEmptyMessage() != null && (charSequence == null || charSequence.length() == 0)) {
            mContainer.setError(getEmptyMessage());
            return false;
        } else if (isValidate(charSequence)) {
            mContainer.setError("");
            return true;
        } else {
            mContainer.setError(getErrorMessage());
            return false;
        }
    }

}
