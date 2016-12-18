package com.rogerio.tex.swaply.ui.auth.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.fragment.BaseLoginFragment;
import com.rogerio.tex.swaply.util.validator.EmailValidator;
import com.rogerio.tex.swaply.util.validator.PasswordValidator;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmailLoginFragment extends BaseLoginFragment {


    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;
    @BindView(R.id.btn_accedi)
    Button btnAccedi;

    private EmailValidator mEmailValidator;
    private PasswordValidator mPasswordValidator;

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
        mEmailValidator = new EmailValidator(inputLayoutEmail);
        mPasswordValidator = new PasswordValidator(inputLayoutPassword);
    }

    @OnClick(R.id.btn_accedi)
    public void onClick() {
        boolean mailValid = mEmailValidator.validate(inputEmail.getText().toString());
        boolean passwordValid = mPasswordValidator.validate(inputPassword.getText().toString());
    }
}
