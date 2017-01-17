package com.rogerio.tex.swaply.ui.auth.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.TaskFailureLogger;
import com.rogerio.tex.swaply.provider.AuthResponse;
import com.rogerio.tex.swaply.ui.BaseFragment;
import com.rogerio.tex.swaply.ui.auth.CollisionAccountHandler;
import com.rogerio.tex.swaply.ui.auth.CompleteListener;
import com.rogerio.tex.swaply.ui.auth.EmailAuthActivity;
import com.rogerio.tex.validator.Form;
import com.rogerio.tex.validator.FormValidationResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountFragment extends BaseFragment implements CompleteListener<AuthResponse> {

    private final static String TAG = CreateAccountFragment.class.getSimpleName();

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
    @BindView(R.id.btn_crea_account)
    Button btnCreaAccount;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private Form formValidation;
    private CreateEmailListener listener;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    private void CreateAccountMail(final String email, final String password) {
        helper.showLoadingDialog("");
        helper.getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(new TaskFailureLogger(TAG, "Errore creazione account email"))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        helper.dismissDialog();
                        handlerException(e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        helper.dismissDialog();
                        if (listener != null) {
                            listener.onNewUser(new AuthResponse(authResult.getUser().getEmail(), null, EmailAuthProvider.PROVIDER_ID, null));
                        }
                    }
                });
    }

    private void handlerException(Exception e) {
        if (e instanceof FirebaseAuthUserCollisionException) {
            CollisionAccountHandler collisionAccountHandler = new CollisionAccountHandler(helper);
            collisionAccountHandler.show(inputEmail.getText().toString(), getFragmentManager(), this);
        } else {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onComplete(AuthResponse response) {
        if (listener != null) {
            if (response.getProviderId() == EmailAuthProvider.PROVIDER_ID) {
                listener.onExistingEmailUser(response);
            } else {
                listener.onExistingIdpUser(response);
            }
        }
    }

    private void finish(int resultCode, String providerId) {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        Intent intent = EmailAuthActivity.createResultIntent(providerId, email, password);
        helper.finishActivity(resultCode, intent);
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
                .addPasswordValidationTask(R.string.validate_error_invalid_password, inputPassword, true)
                .addonCompleteValidationListener(new Form.onCompleteValidationListener() {
                    @Override
                    public void onFormValidationSuccessful(List<FormValidationResult> validationResults) {

                        String email = inputEmail.getText().toString();
                        String password = inputPassword.getText().toString();
                        CreateAccountMail(email, password);
                    }

                    @Override
                    public void onFormValidationFailed(List<FormValidationResult> validationResults) {

                    }
                })
                .CreateForm();

    }

    @OnClick({R.id.btn_crea_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_crea_account:
                formValidation.validate();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateEmailListener) {
            listener = (CreateEmailListener) context;
        }
    }

    interface CreateEmailListener {

        /**
         * Email entered belongs to an existing email user.
         */
        void onExistingEmailUser(AuthResponse response);

        /**
         * Email entered belongs to an existing IDP user.
         */
        void onExistingIdpUser(AuthResponse response);

        /**
         * Email entered does not beling to an existing user.
         */
        void onNewUser(AuthResponse response);

    }

}
