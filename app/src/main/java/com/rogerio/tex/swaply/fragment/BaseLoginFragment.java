package com.rogerio.tex.swaply.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rogerio.tex.swaply.R;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;


public abstract class BaseLoginFragment extends Fragment implements OnCompleteListener<AuthResult> {
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private WeakReference<LoginCallback> loginCallBackRef;

    public BaseLoginFragment() {
        // Required empty public constructor
    }

    protected abstract int getFragmentLayout();

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(),container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginCallback) {
            LoginCallback mLoginCallback = (LoginCallback) context;
            loginCallBackRef = new WeakReference<LoginCallback>(mLoginCallback);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (loginCallBackRef != null) {
            loginCallBackRef.clear();
            loginCallBackRef = null;
        }
    }

    public LoginCallback getCallback() {
        if (loginCallBackRef != null) {
            return loginCallBackRef.get();
        }
        return null;
    }


    protected void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void signInFirebase(final AuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        hideProgressDialog();
        if (task.isSuccessful()) {
            Toast.makeText(getContext(), "login success", Toast.LENGTH_SHORT).show();
            LoginCallback activityCallback;
            if ((activityCallback = getCallback()) != null) {
                activityCallback.onConnected(task);
            }
        } else {
            Toast.makeText(getContext(), "login ko", Toast.LENGTH_SHORT).show();
        }
    }

    public void signInAnonymous() {
        showProgressDialog();
        mAuth.signInAnonymously().addOnCompleteListener(this);
    }

    public void signOutFirebase() {
        mAuth.signOut();
    }

    public interface LoginCallback {
        void onConnected(Task<AuthResult> task);
    }


}
