package com.rogerio.tex.swaply.view.auth.fragment;


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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.rogerio.tex.swaply.OnCompleteListener;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.TaskFailureLogger;
import com.rogerio.tex.swaply.TaskResult;
import com.rogerio.tex.swaply.provider.UserResult;
import com.rogerio.tex.swaply.view.auth.CollisionAccountHandler;
import com.rogerio.tex.validator.Form;
import com.rogerio.tex.validator.FormValidationResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountFragment extends BaseEmaiFragment {

    private final static String TAG = "CreateAccountFragment";

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


    public CreateAccountFragment() {
        // Required empty public constructor
    }

    private void CreateAccountMail(final String email, final String password, final String name) {
        getActivityHelper().showLoadingDialog("");
        getActivityHelper().getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(new TaskFailureLogger(TAG, "Errore creazione account email"))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            handlerCollisionException(email);
                        } else {
                            getActivityHelper().dismissDialog();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserResult response = UserResult.Builder.create()
                                .setProvideData(EmailAuthProvider.PROVIDER_ID)
                                .setEmail(email)
                                .setName(name)
                                .build();
                        final FirebaseUser user = authResult.getUser();
                        updateUserProfile(user, response);

                    }
                });
    }

    private void updateUserProfile(final FirebaseUser user, final UserResult response) {
        String name = response.getName();
        Log.v("Updateprof", "Update name:" + name);
        UserProfileChangeRequest changeNameRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(changeNameRequest)
                .addOnFailureListener(new TaskFailureLogger(TAG, "Error update profile"))
                .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.v(TAG, "Update profile ok");
                        } else {
                            Log.e(TAG, "Update profile ko", task.getException());
                        }
                        listener.succesLogin(response);
                    }
                });
    }

    private void handlerCollisionException(String email) {
        CollisionAccountHandler collisionAccountHandler = new CollisionAccountHandler();
        collisionAccountHandler.show(email, getFragmentManager(), new OnCompleteListener<TaskResult<UserResult>>() {
            @Override
            public void onComplete(TaskResult<UserResult> response) {
                if (response.isSuccessful()) {
                    if (listener != null) {
                        if (response.getResult().getProvideData().equalsIgnoreCase(EmailAuthProvider.PROVIDER_ID)) {
                            listener.onExistingEmailUser(response.getResult());
                        } else {
                            listener.onExistingIdpUser(response.getResult());
                        }
                    }
                } else {
                    Toast.makeText(getContext(), response.getException().getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error handler Collision", response.getException());
                }
            }
        });
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
                        String name = inputName.getText().toString();
                        CreateAccountMail(email, password, name);
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

}
