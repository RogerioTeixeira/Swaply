package com.rogerio.tex.validator.rule;

/**
 * Created by Rogerio Lavoro on 20/12/2016.
 */

public abstract class AbstractRule implements Rule<CharSequence> {
    private String mMessageText;

    public AbstractRule(String message) {
        this.mMessageText = message;
    }


    @Override
    abstract public boolean isValid(CharSequence arg);

    @Override
    public String getErrorMessage() {
        return mMessageText;
    }


}
