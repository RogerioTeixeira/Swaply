package com.rogerio.tex.validator;

import android.widget.EditText;

import com.rogerio.tex.validator.rule.IRule;

import java.util.List;

/**
 * Created by Rogerio Lavoro on 21/12/2016.
 */

public class ValidationTask {
    private List<IRule<CharSequence>> Mrules;
    private EditText MeditText;

    public ValidationTask(EditText editText, List<IRule<CharSequence>> rules) {
        Mrules = rules;
        MeditText = editText;
    }

    public EditText getEditText() {
        return MeditText;
    }

    public List<IRule<CharSequence>> getRules() {
        return Mrules;
    }


}
