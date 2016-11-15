package com.rogerio.tex.swaply.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.WeakReference;

public abstract class BaseLoginFragment extends BaseFragment implements OnCompleteListener<AuthResult> {
    private FirebaseAuth mAuth;
    private WeakReference<LoginCallback> loginCallBackRef;

    public BaseLoginFragment() {
        // Required empty public constructor
    }

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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


    public void signInFirebase(final AuthCredential credential) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.isAnonymous()) {
                Log.v("LinkRich", "Link utenti");
                user.linkWithCredential(credential).addOnCompleteListener(this);
                return;
            }
        }
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
