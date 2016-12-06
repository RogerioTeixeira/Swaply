package com.rogerio.tex.swaply;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.rogerio.tex.swaply.provider.AuthProvider;
import com.rogerio.tex.swaply.provider.GoogleProvider;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements AuthProvider.AuthCallback {
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
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        authProviderHashMap = new HashMap<String, AuthProvider>();
        authProviderHashMap.put(GoogleAuthProvider.PROVIDER_ID, new GoogleProvider(this, this));

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
    public void onStop() {
        super.onStop();
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

    }

    @Override
    public void onFailure(Bundle extra) {

    }

    @OnClick({R.id.sign_in_button_facebook, R.id.sign_in_button_twitter, R.id.sign_in_button_google, R.id.button_skip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button_facebook:
                break;
            case R.id.sign_in_button_twitter:
                break;
            case R.id.sign_in_button_google:
                AuthProvider authProvider = authProviderHashMap.get(GoogleAuthProvider.PROVIDER_ID);
                if (authProvider != null) {
                    authProvider.startLogin();
                }
                break;
            case R.id.button_skip:
                break;
        }
    }
}
