package com.rogerio.tex.validator;

import android.widget.EditText;

/**
 * Created by rogerio on 28/12/2016.
 */

public class FormContext {
    public final EditText editText;
    public final CharSequence arg;
    public final ValidationTask validationTask;

    public FormContext(EditText editText, CharSequence arg, ValidationTask validationTask) {
        this.editText = editText;
        this.arg = arg;
        this.validationTask = validationTask;
    }
}
