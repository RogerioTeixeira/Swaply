package com.rogerio.tex.validator.rule;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;


/**
 * Created by rogerio on 18/12/2016.
 */

public class EmailRule extends AbstractRule {


    public EmailRule(String messageError) {
        super(messageError);
    }


    @Override
    public boolean isValid(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence) && !Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()) {
            Log.v("EmailRule", "mai non valida" + charSequence);
            return false;
        }
        Log.v("EmailRule", "mai valida" + charSequence);
        return true;
    }
}
