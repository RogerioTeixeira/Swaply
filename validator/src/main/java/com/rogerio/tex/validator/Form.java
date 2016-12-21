package com.rogerio.tex.validator;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;

import com.rogerio.tex.validator.rule.IRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rogerio Lavoro on 19/12/2016.
 */

public class Form {
    private final List<ValidationTask> mTasks = new ArrayList<>();
    private final List<ValidationResult> mValidationResultList = new ArrayList<>();
    private Context mContext;
    private onCompleteValidationListener mCallback;

    public Form(Context context, onCompleteValidationListener callback) {
        mContext = context;
        mCallback = callback;
    }

    public void addValidationRule(EditText editText, IRule<CharSequence>... validator) {
        List<IRule<CharSequence>> list = Arrays.asList(validator);
        mTasks.add(new ValidationTask(editText, list));
    }


    public void validate() {
        for (EditText editText : validatorMap.keySet()) {
            List<IRule<CharSequence>> list = validatorMap.get(editText);
            CharSequence args = editText.getText().toString();
            for (IRule<CharSequence> rule : list) {
                if (!rule.isValid(args)) {
                    String errorMessage = rule.getErrorMessage();
                    ValidationResult validationResult = new ValidationResult(editText, errorMessage);
                    mCallback.onFormValidationFailed(validationResult);
                    return;
                }
            }
        }
    }



    public interface onCompleteValidationListener {
        void onFormValidationSuccessful();

        void onFormValidationFailed(List<ValidationResult> errorValidations);

        void onFormValidationException(Exception e);
    }

    public static class ValidationAsyncTask extends AsyncTask<ValidationTask, Integer, List<ValidationResult>> {

        private onCompleteValidationListener mListener;

        public ValidationAsyncTask(onCompleteValidationListener listener) {
            mListener = listener;
        }

        @Override
        protected List<ValidationResult> doInBackground(ValidationTask... tasks) {
            for (ValidationTask task : tasks) {
                List<IRule<CharSequence>> rules = task.getRules();
                EditText editText = task.getEditText();
                CharSequence arg = editText.getText().toString();
                for (IRule<CharSequence> rule : rules) {
                    rule.isValid(editText.getText()) {

                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(List<ValidationResult> results) {

        }


    }


}
