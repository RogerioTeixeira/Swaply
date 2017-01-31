package com.rogerio.tex.swaply.ui.auth;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rogerio.tex.swaply.OnCompleteListener;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.TaskResult;
import com.rogerio.tex.swaply.helper.ProfileHelper;
import com.rogerio.tex.swaply.helper.firebase.FirebaseHelper;
import com.rogerio.tex.swaply.helper.model.UserProfile;
import com.rogerio.tex.swaply.provider.LoginProviderManager;
import com.rogerio.tex.swaply.provider.UserResult;
import com.rogerio.tex.swaply.ui.BaseActivity;
import com.rogerio.tex.swaply.ui.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements OnCompleteListener<TaskResult<UserResult>> {
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
    private LoginProviderManager providerManager;

    public static void startActivityForResult(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    public static UserResult getResultData(Intent intent) {
        return intent.getParcelableExtra(EXTRA_PARAM_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        providerManager = LoginProviderManager.createInstance();
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
    public void onComplete(TaskResult<UserResult> args) {
        AuthCredential authCredential = LoginProviderManager.createAuthCredential(args.getResult());
        final UserResult result = args.getResult();
        if (authCredential != null) {
            getActivityHelper().showLoadingDialog("");
            FirebaseAuth.getInstance().signInWithCredential(authCredential)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            checkAlreadyAuthenticated(authResult.getUser().getUid(), result);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                handlerUserCollisionException(result.getEmail());
                            }
                        }
                    });
        }
    }


    private void checkAlreadyAuthenticated(final String uid, final UserResult result) {
        final FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();
        firebaseHelper.getUserReferences(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.v("Firebaseprof", dataSnapshot.getValue().toString());

                } else {
                    ProfileHelper profile = ProfileHelper.getInstance();
                    UserProfile userProfile = profile.createUserProfileFromUserResult(result);
                    Log.v("FirebaseprofVal", userProfile.getPhotoUrl());
                    profile.updateProfile(userProfile, uid);
                }
                finishLogin();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void finishLogin() {
        if (getCallingActivity() != null) {
            Intent intent = new Intent();
            getActivityHelper().finishActivity(Activity.RESULT_OK, intent);
        } else {
            MainActivity.startActivity(this);
        }
    }

    private void handlerUserCollisionException(String email) {
        OnCompleteListener<TaskResult<UserResult>> listener = new OnCompleteListener<TaskResult<UserResult>>() {
            @Override
            public void onComplete(TaskResult<UserResult> response) {
                String providerId = response.getResult().getProvideData();
                providerManager.startLogin(providerId, LoginActivity.this, LoginActivity.this);
            }
        };
        CollisionAccountHandler collisionAccountHandler = new CollisionAccountHandler();
        collisionAccountHandler.show(email, getSupportFragmentManager(), listener);

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
