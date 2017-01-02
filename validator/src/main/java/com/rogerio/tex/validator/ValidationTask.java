package com.rogerio.tex.validator;

import android.support.annotation.NonNull;
import android.util.Log;

import com.rogerio.tex.validator.rule.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogerio Lavoro on 21/12/2016.
 */

public class ValidationTask<T> {
    private final List<Rule<T>> rules = new ArrayList<>();
    private String errorMessage;


    public ValidationTask() {

    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void addRule(@NonNull final Rule<T> rule) {
        if (!rules.contains(rule)) {
            rules.add(rule);
        }

    }

    public void addAllRule(@NonNull final List<Rule<T>> rules) {
        this.rules.addAll(rules);
    }

    public void removeRule(@NonNull final Rule<T> rule) {
        rules.remove(rule);
    }

    public List<Rule<T>> getRules() {
        return rules;
    }

    public void Validate(final T arg) throws Exception {
        if (rules.isEmpty()) {
            throw new IllegalArgumentException("List rules is empty");
        }
        for (Rule<T> rule : rules) {
            Log.v("Validation", "argomento" + arg);
            if (!rule.isValid(arg)) {
                throw new ValidationException(rule.getErrorMessage());
            }
        }
    }



}
