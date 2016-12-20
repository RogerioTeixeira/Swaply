package com.rogerio.tex.validator.rule;

import android.util.Patterns;


/**
 * Created by rogerio on 18/12/2016.
 */

public class EmailRule extends AbstractRule {

    public EmailRule(String messageError) {
        super(messageError);
    }

    public EmailRule(int messageErrorResId) {
        super(messageErrorResId);
    }

    @Override
    public boolean isValid(CharSequence charSequence) {
        return Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }

}
