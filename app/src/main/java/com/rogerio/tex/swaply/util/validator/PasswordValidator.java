package com.rogerio.tex.swaply.util.validator;


/**
 * Created by rogerio on 18/12/2016.
 */

public class PasswordValidator extends TextValidator {
    private int mMinLen;

    public PasswordValidator(String messageError, int minlen) {
        super(messageError);
        mMinLen = minlen;

    }
    @Override
    public void validate(CharSequence charSequence) throws ValidationException {
        super.validate(charSequence);
        if (charSequence.length() < mMinLen) {
            throw new ValidationException(mMessageError);
        }

    }

}
