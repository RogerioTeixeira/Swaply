package com.rogerio.tex.swaply.ui.auth.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.fragment.BaseLoginFragment;
import com.rogerio.tex.validator.Form;
import com.rogerio.tex.validator.FormValidationResult;
import com.rogerio.tex.validator.ValidationTask;
import com.rogerio.tex.validator.rule.EmailRule;
import com.rogerio.tex.validator.rule.EmptyRule;
import com.rogerio.tex.validator.rule.IRule;
import com.rogerio.tex.validator.rule.PasswordRule;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmailLoginFragment extends BaseLoginFragment {


    private final Form.onCompleteValidationListener listenerForm = new Form.onCompleteValidationListener() {
        @Override
        public void onFormValidationSuccessful() {
            Toast.makeText(getContext(), "Validazione ok", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFormValidationFailed(List<FormValidationResult> errorValidations) {
            Toast.makeText(getContext(), "Validazione ko", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFormValidationException(Exception e) {
            Toast.makeText(getContext(), "Validazione exception:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    @BindView(R.id.input_email)
    TextInputEditText inputEmail;
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_password)
    TextInputEditText inputPassword;
    @BindView(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;
    @BindView(R.id.btn_accedi)
    Button btnAccedi;
    private Form formValidation;


    public EmailLoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_email_login;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IRule<CharSequence> emailRule = new EmailRule(getResources().getString(R.string.validate_error_invalid_email));
        IRule<CharSequence> emptyRule = new EmptyRule(getResources().getString(R.string.validate_error_missing_default));
        IRule<CharSequence> passwordRule = new PasswordRule(getResources().getString(R.string.validate_error_invalid_password));

        ValidationTask<CharSequence> emailTask = new ValidationTask<>(getContext().getApplicationContext());
        emailTask.addRule(emptyRule);
        emailTask.addRule(emailRule);

        ValidationTask<CharSequence> passwordTask = new ValidationTask<>(getContext().getApplicationContext());
        passwordTask.addRule(emptyRule);
        passwordTask.addRule(passwordRule);


        formValidation = new Form(listenerForm);
        formValidation.addValidationTask(inputEmail, emailTask);
        formValidation.addValidationTask(inputPassword, passwordTask);



    }

    @OnClick(R.id.btn_accedi)
    public void onClick() {
        formValidation.validate();

    }
}
