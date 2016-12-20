package com.rogerio.tex.validator.rule;


/**
 * Created by rogerio on 18/12/2016.
 */

public class PasswordRule extends AbstractRule {
    private int mMinLen = -1;

    public PasswordRule(String messageError) {
        super(messageError);
    }

    public PasswordRule(int messageErrorResId) {
        super(messageErrorResId);
    }

    @Override
    public boolean isValid(CharSequence charSequence) {
        return (charSequence.length() < mMinLen);
    }

}
