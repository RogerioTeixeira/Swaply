package com.rogerio.tex.swaply.util.validator;

import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.util.Patterns;

import com.rogerio.tex.swaply.R;

/**
 * Created by rogerio on 18/12/2016.
 */

public class EmailValidator extends BaseValidator {

    public EmailValidator(TextInputLayout container) {
        super(container);
    }

    @Override
    public String getErrorMessage() {
        return getContainer().getResources().getString(R.string.validate_error_invalid_email);
    }

    @Override
    public String getEmptyMessage() {
        return getContainer().getResources().getString(R.string.validate_error_missing_email);
    }

    @Override
    protected boolean isValidate(CharSequence charSequence) {
        boolean valido = Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
        Log.v("controllo", "email:" + charSequence);
        Log.v("controllo", "esito:" + valido);
        return Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }

}
