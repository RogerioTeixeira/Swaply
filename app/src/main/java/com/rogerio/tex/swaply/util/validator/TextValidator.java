package com.rogerio.tex.swaply.util.validator;

import android.text.TextUtils;


/**
 * Created by rogerio on 18/12/2016.
 */

public class TextValidator implements IValidator<CharSequence> {

    protected String mMessageError;

    public TextValidator(String messageError) {
        mMessageError = messageError;
    }

    @Override
    public void validate(CharSequence charSequence) throws ValidationException {
        if (TextUtils.isEmpty(charSequence)) {
            throw new ValidationException(mMessageError);
        }
    }
}
