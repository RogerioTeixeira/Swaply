package com.rogerio.tex.swaply.ui.auth.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.fragment.BaseLoginFragment;
import com.rogerio.tex.validator.Form;
import com.rogerio.tex.validator.rule.EmailRule;
import com.rogerio.tex.validator.rule.PasswordRule;

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

    private Form mFormValidation;


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
        String msgErrorMail = getResources().getString(R.string.validate_error_invalid_email);
        String msgErrorPassword = getResources().getString(R.string.validate_error_invalid_password);
        mFormValidation = new Form();
        mFormValidation.addValidationRule(new EmailRule(msgErrorMail), inputLayoutEmail);
        mFormValidation.addValidationRule(new PasswordRule(msgErrorPassword, 0), inputLayoutPassword);

    }

    @OnClick(R.id.btn_accedi)
    public void onClick() {
        mFormValidation.validate();
        if (mFormValidation.isSuccess()) {
            Toast.makeText(getContext(), "Validazione ok", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Validazione ko", Toast.LENGTH_LONG).show();
        }
    }
}
