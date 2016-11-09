package com.rogerio.tex.swaply.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rogerio.tex.swaply.R;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;


public abstract class BaseLoginFragment extends Fragment {
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private WeakReference<LoginCallback> loginCallBackRef;

    public BaseLoginFragment() {
        // Required empty public constructor
    }

    protected abstract int getFragmentLayout();



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

    public void signinFirebase(final AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            LoginCallback activityCallback;
                            if (loginCallBackRef != null && (activityCallback = loginCallBackRef.get()) != null) {
                                activityCallback.onConnected(task);
                            }
                        } else {
                            Log.v("Login", "Chiamata firebase auth ko");
                        }
                    }
                });
    }

    public void signOutFirebase() {
        mAuth.signOut();
    }

    public interface LoginCallback {
        void onConnected(Task<AuthResult> task);
    }


}
