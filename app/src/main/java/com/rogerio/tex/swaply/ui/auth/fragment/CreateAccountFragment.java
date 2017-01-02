package com.rogerio.tex.swaply.ui.auth.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.fragment.BaseFragment;
import com.rogerio.tex.validator.Form;
import com.rogerio.tex.validator.FormValidationResult;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountFragment extends BaseFragment {

    @BindView(R.id.input_email)
    TextInputEditText inputEmail;
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_name)
    TextInputEditText inputName;
    @BindView(R.id.input_layout_name)
    TextInputLayout inputLayoutName;
    @BindView(R.id.input_password)
    TextInputEditText inputPassword;
    @BindView(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;
    @BindView(R.id.btn_accedi)
    Button btnAccedi;
    @BindView(R.id.btn_avanti)
    Button btnAvanti;
    private final Form.onCompleteValidationListener listenerForm = new Form.onCompleteValidationListener() {
        @Override
        public void onFormValidationSuccessful(List<FormValidationResult> validationResults) {
            Toast.makeText(getContext(), "Validazione ok", Toast.LENGTH_LONG).show();
            btnAvanti.setVisibility(View.INVISIBLE);
            btnAccedi.setVisibility(View.VISIBLE);
            inputLayoutName.setVisibility(View.VISIBLE);
            inputLayoutPassword.setVisibility(View.VISIBLE);
            inputLayoutEmail.setFocusable(false);
            inputLayoutEmail.setFocusableInTouchMode(false);
            inputLayoutEmail.setEnabled(false);
        }

        @Override
        public void onFormValidationFailed(List<FormValidationResult> validationResults) {
            Toast.makeText(getContext(), "Validazione ko", Toast.LENGTH_LONG).show();
        }
    };
    @BindView(R.id.frame_button)
    FrameLayout frameButton;
    private Form formValidation;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_create_account;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v("Create", "onViewCreated");
        formValidation = new Form.Builder()
                .addEmailValidationTask(R.string.validate_error_invalid_email, inputEmail, true)
                .addonCompleteValidationListener(listenerForm)
                .CreateForm();

    }

    @OnClick({R.id.btn_accedi, R.id.btn_avanti})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_accedi:
                break;
            case R.id.btn_avanti:
                formValidation.validate();
                break;
        }
    }
}
