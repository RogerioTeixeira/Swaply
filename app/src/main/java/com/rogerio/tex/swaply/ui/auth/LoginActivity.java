package com.rogerio.tex.swaply.ui.auth;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.provider.AuthProvider;
import com.rogerio.tex.swaply.provider.FacebookProvider;
import com.rogerio.tex.swaply.provider.GoogleProvider;
import com.rogerio.tex.swaply.provider.TwitterProvider;
import com.rogerio.tex.swaply.ui.BaseActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements AuthProvider.AuthCallback, OnCompleteListener<AuthResult> {
    private static final String TAG_LOG = "LoginActivity";
    @BindView(R.id.sign_in_button_facebook)
    Button signInButtonFacebook;
    @BindView(R.id.sign_in_button_twitter)
    Button signInButtonTwitter;
    @BindView(R.id.sign_in_button_google)
    Button signInButtonGoogle;
    @BindView(R.id.button_skip)
    Button buttonSkip;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private HashMap<String, AuthProvider> authProviderHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        authProviderHashMap = new HashMap<String, AuthProvider>();
        authProviderHashMap.put(GoogleAuthProvider.PROVIDER_ID, new GoogleProvider(this, this));
        authProviderHashMap.put(FacebookAuthProvider.PROVIDER_ID, new FacebookProvider(this, this));
        authProviderHashMap.put(TwitterAuthProvider.PROVIDER_ID, new TwitterProvider(this, this));
        Log.v(TAG_LOG, "Creazione");

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG_LOG, "chiusura");
        for (final AuthProvider authProvider : authProviderHashMap.values()) {
            authProvider.onStop();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (final AuthProvider authProvider : authProviderHashMap.values()) {
            authProvider.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSuccess(AuthCredential credential) {
        Log.v(TAG_LOG, "Login effettuato:" + credential.getProvider());
        signinFirebase(credential);
    }

    @Override
    public void onFailure(Bundle extra) {
        hideProgressDialog();

    }

    private void signinFirebase(AuthCredential credential) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.isAnonymous()) {
            Log.v(TAG_LOG, "Login firebase link anonymous");
            user.linkWithCredential(credential).addOnCompleteListener(this);
            return;
        }

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        hideProgressDialog();
        if (task.isSuccessful()) {
            Log.v(TAG_LOG, "Login firebase effettuato:" + task.getResult().getUser().getUid());
            finish();
        } else {
            Log.e(TAG_LOG, "Login firebase ko", task.getException());
        }


    }

    private void signInAnonymous() {
        showProgressDialog();
        mAuth.signInAnonymously().addOnCompleteListener(this);
    }

    @OnClick({R.id.sign_in_button_facebook, R.id.sign_in_button_twitter, R.id.sign_in_button_google, R.id.button_skip})
    public void onClick(View view) {
        AuthProvider authProvider = null;
        switch (view.getId()) {
            case R.id.sign_in_button_facebook:
                authProvider = authProviderHashMap.get(FacebookAuthProvider.PROVIDER_ID);
                break;
            case R.id.sign_in_button_twitter:
                authProvider = authProviderHashMap.get(TwitterAuthProvider.PROVIDER_ID);
                break;
            case R.id.sign_in_button_google:
                authProvider = authProviderHashMap.get(GoogleAuthProvider.PROVIDER_ID);
                break;
            case R.id.button_skip:
                signInAnonymous();
                break;
        }

        if (authProvider != null) {
            showProgressDialog();
            authProvider.startLogin();
        }
    }

    @Override
    public void onBackPressed() {
        signInAnonymous();
    }
}
