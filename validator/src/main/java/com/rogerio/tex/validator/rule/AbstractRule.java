package com.rogerio.tex.validator.rule;

/**
 * Created by Rogerio Lavoro on 20/12/2016.
 */

public abstract class AbstractRule implements IRule<CharSequence> {
    private String mMessageText;
    private int mMessageResId;

    public AbstractRule(String message) {
        this.mMessageText = message;
    }

    public AbstractRule(int message) {
        this.mMessageResId = message;
    }

    @Override
    abstract public boolean isValid(CharSequence arg);

    @Override
    public String getErrorMessage() {
        return mMessageText;
    }

    @Override
    public int getErrorMessageResId() {
        return mMessageResId;
    }

}
