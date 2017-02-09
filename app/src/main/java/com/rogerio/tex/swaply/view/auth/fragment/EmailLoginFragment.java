package com.rogerio.tex.swaply.view.auth.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.TaskFailureLogger;
import com.rogerio.tex.swaply.provider.UserResult;
import com.rogerio.tex.validator.Form;
import com.rogerio.tex.validator.FormValidationResult;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmailLoginFragment extends BaseEmaiFragment {

    private final static String TAG = "EmailLoginFragment";

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
                .addonCompleteValidationListener(new Form.onCompleteValidationListener() {
                    @Override
                    public void onFormValidationSuccessful(List<FormValidationResult> validationResults) {
                        String email = inputEmail.getText().toString();
                        String password = inputPassword.getText().toString();
                        loginWithPassword(email, password);
                    }

                    @Override
                    public void onFormValidationFailed(List<FormValidationResult> validationResults) {

                    }
                })
                .CreateForm();
    }

    private void loginWithPassword(String email, String password) {
        getActivityHelper().showLoadingDialog("");
        getActivityHelper().getFirebaseAuth().signInWithEmailAndPassword(email, password)
                .addOnFailureListener(new TaskFailureLogger(TAG, "Error signinWithEmail"))
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserResult response = UserResult.Builder.create()
                                .setEmail(authResult.getUser().getEmail())
                                .setName(authResult.getUser().getDisplayName())
                                .setProvideData(EmailAuthProvider.PROVIDER_ID)
                                .build();
                        listener.succesLogin(response);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getActivityHelper().dismissDialog();
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                });
    }

    @OnClick(R.id.btn_accedi)
    public void onClick() {
        Log.v("EmailLoginFragment", "email:" + inputEmail.toString());
        formValidation.validate();


    }

    public void setEmail(String email) {
        inputEmail.setText(email);
        inputPassword.requestFocus();

    }

}
