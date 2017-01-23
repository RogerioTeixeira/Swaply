package com.rogerio.tex.swaply.ui.auth;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.TaskFailureLogger;
import com.rogerio.tex.swaply.provider.AuthProvider;
import com.rogerio.tex.swaply.provider.AuthResponse;
import com.rogerio.tex.swaply.provider.ProviderManager;
import com.rogerio.tex.swaply.ui.BaseActivity;
import com.rogerio.tex.swaply.ui.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements AuthProvider.AuthCallback {
    public static final int REQUEST_CODE = 102;
    private static final String TAG = "LoginActivity";
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
    private FirebaseUser user;
    private ProviderManager providerManager;

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
        providerManager = ProviderManager.createInstance();
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
        providerManager.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        providerManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(final AuthResponse response) {
        if (response.getProviderId().equalsIgnoreCase(EmailAuthProvider.PROVIDER_ID)) {
            finish(response);
        } else {
            AuthCredential authCredential = ProviderManager.createAuthCredential(response);

            if (authCredential != null) {
                getActivityHelper().showLoadingDialog("");
                getActivityHelper().getFirebaseAuth().signInWithCredential(authCredential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUserProfile(task.getResult().getUser(), response);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                handlerUserCollisionException(response.getUser().getEmail());
                            }
                        }
                    }
                });
            }
        }
    }

    private void updateUserProfile(final FirebaseUser user, final AuthResponse response) {
        Uri uriPhoto = Uri.parse(response.getUser().getPhotoUrl());
        UserProfileChangeRequest changeNameRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(response.getUser().getName())
                .setPhotoUri(uriPhoto)
                .build();
        user.updateProfile(changeNameRequest)
                .addOnFailureListener(new TaskFailureLogger(TAG, "Error update profile"))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish(response);
                    }
                });
    }



    private void finish(AuthResponse response) {
        if (getCallingActivity() != null) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_PARAM_ID, response);
            getActivityHelper().finishActivity(Activity.RESULT_OK, intent);
        } else {
            MainActivity.startActivity(this, response);
        }
    }

    private void handlerUserCollisionException(String email) {
        CompleteListener<AuthResponse> listener = new CompleteListener<AuthResponse>() {
            @Override
            public void onComplete(AuthResponse response) {
                String providerId = response.getProviderId();
                providerManager.startLogin(providerId, LoginActivity.this, LoginActivity.this);
            }
        };

        CollisionAccountHandler collisionAccountHandler = new CollisionAccountHandler(getActivityHelper());
        collisionAccountHandler.show(email, getSupportFragmentManager(), listener);

    }

    @Override
    public void onFailure(Bundle extra) {
        getActivityHelper().dismissDialog();

    }

    @OnClick({R.id.sign_in_button_facebook, R.id.sign_in_button_twitter, R.id.sign_in_button_google, R.id.button_skip, R.id.sign_in_button_email})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button_facebook:
                providerManager.startLogin(FacebookAuthProvider.PROVIDER_ID, this, this);
                break;
            case R.id.sign_in_button_twitter:
                providerManager.startLogin(TwitterAuthProvider.PROVIDER_ID, this, this);
                break;
            case R.id.sign_in_button_google:
                providerManager.startLogin(GoogleAuthProvider.PROVIDER_ID, this, this);
                break;
            case R.id.sign_in_button_email:
                providerManager.startLogin(EmailAuthProvider.PROVIDER_ID, this, this);
                break;
            case R.id.button_skip:
                finishAffinity();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}
