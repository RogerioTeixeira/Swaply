package com.rogerio.tex.validator.rule;

import android.text.TextUtils;

import com.rogerio.tex.validator.ValidationException;


/**
 * Created by rogerio on 18/12/2016.
 */

public class EmptyRule implements IRule<CharSequence> {

    protected String mMessageError;

    public EmptyRule(String messageError) {
        mMessageError = messageError;
    }

    @Override
    public void validate(CharSequence charSequence) throws ValidationException {
        if (TextUtils.isEmpty(charSequence)) {
            throw new ValidationException(mMessageError);
        }
    }
}
