package com.rogerio.tex.validator;

import android.view.View;

import java.util.List;

/**
 * Created by Rogerio Lavoro on 20/12/2016.
 */

public class ValidationResult {
    private List<String> mErrorList;
    private View mViewError;

    public ValidationResult(View view, List<String> errorList) {
        mViewError = view;
        mErrorList = errorList;
    }

    public List<String> getMessageErrorList() {
        return mErrorList;
    }

}
