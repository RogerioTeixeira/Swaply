package com.rogerio.tex.swaply.ui.auth;


import android.app.Activity;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.provider.AuthProvider;
import com.rogerio.tex.swaply.provider.AuthResponse;
import com.rogerio.tex.swaply.provider.EmailProvider;
import com.rogerio.tex.swaply.provider.FacebookProvider;
import com.rogerio.tex.swaply.provider.GoogleProvider;
import com.rogerio.tex.swaply.provider.TwitterProvider;
import com.rogerio.tex.swaply.ui.BaseActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements AuthProvider.AuthCallback {
    public static final int REQUEST_CODE = 102;
    private static final String TAG_LOG = "LoginActivity";
    private static final String EXTRA_PARAM_ID = "EXTRA_AUTH_PARAM";

    @BindView(R.id.sign_in_button_facebook)
    Button signInButtonFacebook;
    @BindView(R.id.sign_in_button_twitter)
    Button signInButtonTwitter;
    @BindView(R.id.sign_in_button_google)
    Button signInButtonGoogle;
    @BindView(R.id.button_skip)
    Button buttonSkip;
    @BindView(R.id.sign_in_button_email)
    Button signInButtonEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private HashMap<String, AuthProvider> authProviderHashMap;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static AuthResponse getResultData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_PARAM_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        authProviderHashMap = new HashMap<String, AuthProvider>();
        authProviderHashMap.put(GoogleAuthProvider.PROVIDER_ID, new GoogleProvider(this, this));
        authProviderHashMap.put(FacebookAuthProvider.PROVIDER_ID, new FacebookProvider(this, this));
        authProviderHashMap.put(TwitterAuthProvider.PROVIDER_ID, new TwitterProvider(this, this));
        authProviderHashMap.put(EmailAuthProvider.PROVIDER_ID, new EmailProvider(this, this));
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
            Log.v(TAG_LOG, "rimanda:" + authProvider.getProviderId());
            authProvider.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void loginWith(String providerId) {
        authProviderHashMap.get(providerId).startLogin();
    }

    @Override
    public void onSuccess(final AuthResponse response) {
        if (response.getProviderId() == EmailAuthProvider.PROVIDER_ID) {
            finish(response);
        } else {
            AuthProvider authProvider = authProviderHashMap.get(response.getProviderId());
            AuthCredential authCredential = authProvider.createAuthCredential(response);
            Log.v(TAG_LOG, "Onsucces");
            if (authCredential != null) {
                helper.showLoadingDialog("");
                mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish(response);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                handlerUserCollisionException(response.getEmail());
                            }
                        }
                    }
                });
            }
        }
    }

    private void finish(AuthResponse response) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAM_ID, response);
        helper.finishActivity(Activity.RESULT_OK, intent);
    }

    private void handlerUserCollisionException(String email) {
        CompleteListener<AuthResponse> listener = new CompleteListener<AuthResponse>() {
            @Override
            public void onComplete(AuthResponse response) {
                authProviderHashMap.get(response.getProviderId()).startLogin();
            }
        };

        CollisionAccountHandler collisionAccountHandler = new CollisionAccountHandler(helper);
        collisionAccountHandler.show(email, getSupportFragmentManager(), listener);

    }

    @Override
    public void onFailure(Bundle extra) {
        helper.dismissDialog();

    }

    @OnClick({R.id.sign_in_button_facebook, R.id.sign_in_button_twitter, R.id.sign_in_button_google, R.id.button_skip, R.id.sign_in_button_email})
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
            case R.id.sign_in_button_email:
                authProvider = authProviderHashMap.get(EmailAuthProvider.PROVIDER_ID);
                break;
            case R.id.button_skip:
                break;
        }

        if (authProvider != null) {
            // showProgressDialog();
            authProvider.startLogin();
        }
    }

    @Override
    public void onBackPressed() {

    }

}
