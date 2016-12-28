package com.rogerio.tex.validator;

/**
 * Created by Rogerio Lavoro on 20/12/2016.
 */

public class FormValidationResult {
    private final FormContext formContext;
    private final Exception exception;
    private final boolean isOk;


    public FormValidationResult(FormContext formContext, Exception exception) {
        this.exception = exception;
        this.formContext = formContext;
        isOk = exception == null;
    }

    public boolean isOk() {
        return isOk;
    }

    public FormContext getFormContext() {
        return formContext;
    }

    public Exception getException() {
        return exception;
    }
}
