package com.rogerio.tex.swaply.ui.auth.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
public class EmailLoginFragment extends BaseFragment {



    private final Form.onCompleteValidationListener listenerForm = new Form.onCompleteValidationListener() {
        @Override
        public void onFormValidationSuccessful(List<FormValidationResult> validationResults) {
            Toast.makeText(getContext(), "Validazione ok", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFormValidationFailed(List<FormValidationResult> validationResults) {
            Toast.makeText(getContext(), "Validazione ko", Toast.LENGTH_LONG).show();
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
        Log.v("Login", "onViewCreated");

        formValidation = new Form.Builder()
                .addEmailValidationTask(R.string.validate_error_invalid_email, inputEmail, true)
                .addPasswordValidationTask(R.string.validate_error_invalid_password, inputPassword, true)
                .addonCompleteValidationListener(listenerForm)
                .CreateForm();
    }

    @OnClick(R.id.btn_accedi)
    public void onClick() {
        Log.v("EmailLoginFragment", "email:" + inputEmail.toString());
        formValidation.validate();


    }

    /*class PrimeThread extends Thread {
        @Override
        public void run() {
            try {
                Task<ProviderQueryResult> curTask = getFirebaseAuth().fetchProvidersForEmail("rogerio.teixeiranunes@gmail.com");
                ProviderQueryResult result = Tasks.await(curTask);
                if (result.getProviders() != null)
                    Log.v("test Provider", result.getProviders().toString());
            } catch (Exception e) {
                Log.e("test Provider2", "Errore", e);
            }
        }

    }*/
}
