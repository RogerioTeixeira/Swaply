package com.rogerio.tex.validator.rule;


import android.text.TextUtils;

/**
 * Created by rogerio on 18/12/2016.
 */

public class PasswordRule extends AbstractRule {
    private final int MIN_LEN = 6;

    public PasswordRule(String messageError) {
        super(messageError);
    }


    @Override
    public boolean isValid(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return true;
        } else return charSequence.length() >= MIN_LEN;
    }

}
