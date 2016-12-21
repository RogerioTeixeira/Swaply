package com.rogerio.tex.validator;

import android.view.View;

/**
 * Created by Rogerio Lavoro on 20/12/2016.
 */

public class ValidationResult {
    private String mErrorMessage;
    private View mViewError;

    public ValidationResult(View view, String errorMessage) {
        mViewError = view;
        mErrorMessage = errorMessage;
    }

    public String getMessageError() {
        return mErrorMessage;
    }

}
