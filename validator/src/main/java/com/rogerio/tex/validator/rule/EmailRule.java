package com.rogerio.tex.validator.rule;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

/**
 * Created by rogerio on 18/12/2016.
 */

public class EmailRule extends AbstractRule {


    public EmailRule(String messageError) {
        super(messageError);
    }


    @Override
    public boolean isValid(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()) {
            try {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String email = charSequence.toString();
                Task<ProviderQueryResult> curTask = mAuth.fetchProvidersForEmail(email);
                ProviderQueryResult result = Tasks.await(curTask);
            } catch (Exception e) {
                Log.e("test Provider2", "Errore", e);
                return false;
            }

        } else {
            return false;
        }

        return true;
    }

}
