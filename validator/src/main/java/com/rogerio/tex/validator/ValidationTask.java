package com.rogerio.tex.validator;

import android.content.Context;
import android.support.annotation.NonNull;

import com.rogerio.tex.validator.rule.IRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogerio Lavoro on 21/12/2016.
 */

public class ValidationTask<T> {
    private final List<IRule<T>> rules = new ArrayList<>();
    private String errorMessage;
    private Context context;

    public ValidationTask(final Context context) {
        this.context = context;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void addRule(@NonNull final IRule<T> rule) {
        if (!rules.contains(rule)) {
            rules.add(rule);
        }

    }

    public void addAllRule(@NonNull final List<IRule<T>> rules) {
        this.rules.addAll(rules);
    }

    public void removeRule(@NonNull final IRule<T> rule) {
        rules.remove(rule);
    }

    public List<IRule<T>> getRules() {
        return rules;
    }

    public boolean isValid(final T arg) throws Exception {
        if (rules.isEmpty()) {
            throw new IllegalArgumentException("List rules is empty");
        }
        for (IRule<T> rule : rules) {
            if (!rule.isValid(arg)) {
                int ErrorMessageResId = rule.getErrorMessageResId();
                if (ErrorMessageResId != 0) {
                    errorMessage = context.getResources().getString(ErrorMessageResId);
                } else {
                    errorMessage = rule.getErrorMessage();
                }
                return false;
            }
        }
        return true;
    }



}
