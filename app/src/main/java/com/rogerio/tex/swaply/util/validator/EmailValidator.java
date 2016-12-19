package com.rogerio.tex.swaply.util.validator;

import android.util.Patterns;

/**
 * Created by rogerio on 18/12/2016.
 */

public class EmailValidator extends TextValidator {

    public EmailValidator(String messageError) {
        super(messageError);
    }
    @Override
    public void validate(CharSequence charSequence) throws ValidationException {
        super.validate(charSequence);
        if (!Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()) {
            throw new ValidationException(mMessageError);
        }

    }

}
