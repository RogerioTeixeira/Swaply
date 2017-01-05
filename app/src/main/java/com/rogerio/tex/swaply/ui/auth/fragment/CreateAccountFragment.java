package com.rogerio.tex.swaply.ui.auth.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.TaskFailureLogger;
import com.rogerio.tex.swaply.fragment.BaseFragment;
import com.rogerio.tex.validator.Form;
import com.rogerio.tex.validator.FormValidationResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountFragment extends BaseFragment {

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


    public CreateAccountFragment() {
        // Required empty public constructor
    }


    /*
    @Override
    public void onFormValidationSuccessful(List<FormValidationResult> validationResults) {
        Toast.makeText(getContext(), "Validazione ok", Toast.LENGTH_LONG).show();
        Snackbar.make(coordinatorLayout, "Validazione ok", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();
    }



    @Override
    public void onFormValidationFailed(List<FormValidationResult> validationResults) {
        Toast.makeText(getContext(), "Validazione ko", Toast.LENGTH_LONG).show();

        Snackbar snackbar = Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();

        DialogFragment newFragment = new AlertAccount();
        newFragment.show(getFragmentManager(), "missiles");

    }*/


    private void CreateAccountMail(String mail, String password) {
        helper.getFirebaseAuth().signInWithEmailAndPassword(mail, password)
                .addOnFailureListener(new TaskFailureLogger(TAG, "Errore creazione account email"))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handlerException(e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                    }
                });
    }

    private void handlerException(Exception e) {
        if (e instanceof FirebaseAuthUserCollisionException) {
            DialogFragment newFragment = new AlertAccount();
            newFragment.show(getFragmentManager(), "DialogCollision");
        }
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
                .addonCompleteValidationListener(new Form.onCompleteValidationListener() {
                    @Override
                    public void onFormValidationSuccessful(List<FormValidationResult> validationResults) {
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

    public static class AlertAccount extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("L'indirizzo mail risulta già registrato tramite Google")
                    .setTitle("Attenzione")
                    .setPositiveButton("Accedi", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton("Cancella", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();

        }
    }
}
