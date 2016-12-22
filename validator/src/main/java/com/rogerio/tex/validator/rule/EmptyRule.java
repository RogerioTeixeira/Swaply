package com.rogerio.tex.validator.rule;

import android.text.TextUtils;


/**
 * Created by rogerio on 18/12/2016.
 */

public class EmptyRule extends AbstractRule {

    public EmptyRule(String messageError) {
        super(messageError);
    }

    public EmptyRule(int messageErrorResId) {
        super(messageErrorResId);
    }

    @Override
    public boolean isValid(CharSequence charSequence) {
        return !TextUtils.isEmpty(charSequence);

    }
}
