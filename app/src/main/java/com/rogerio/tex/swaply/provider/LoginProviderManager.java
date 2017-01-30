package com.rogerio.tex.swaply.provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.rogerio.tex.swaply.OnCompleteListener;
import com.rogerio.tex.swaply.TaskFailureLogger;
import com.rogerio.tex.swaply.TaskResult;

import java.util.HashMap;

/**
 * Created by rogerio on 18/01/2017.
 */

public class LoginProviderManager {

    private static String ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL = "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL";
    private final HashMap<String, AbstractProvider> authProviderHashMap = new HashMap<>();
    private LoginProviderManager() {

    }

    public static LoginProviderManager createInstance() {
        return new LoginProviderManager();
    }

    public static AuthCredential createAuthCredential(AuthResponse response) {
        String providerId = response.getProviderId();
        switch (providerId) {
            case FacebookAuthProvider.PROVIDER_ID:
                return FacebookAuthProvider.getCredential(response.getToken());
            case GoogleAuthProvider.PROVIDER_ID:
                return GoogleAuthProvider.getCredential(response.getToken(), null);
            case TwitterAuthProvider.PROVIDER_ID:
                return TwitterAuthProvider.getCredential(response.getToken(), response.getSecretKey());
            default:
                return null;
        }
    }

    public void startLogin(String providerId, AppCompatActivity activity, OnCompleteListener<TaskResult<UserResult>> listener) {

        if (authProviderHashMap.containsKey(providerId)) {
            authProviderHashMap.get(providerId).startLogin();
        } else {
            AbstractProvider provider = null;
            switch (providerId) {
                case EmailAuthProvider.PROVIDER_ID:
                    provider = new EmailProvider(activity, listener);
                    break;
                case FacebookAuthProvider.PROVIDER_ID:
                    provider = new FacebookProvider(activity, listener);
                    break;
                case GoogleAuthProvider.PROVIDER_ID:
                    provider = new GoogleProvider(activity, listener);
                    break;
                case TwitterAuthProvider.PROVIDER_ID:
                    provider = new TwitterProvider(activity, listener);
                    break;
                default:
                    throw new IllegalArgumentException("Provider id not valid");
            }
            authProviderHashMap.put(provider.getProviderId(), provider);
            provider.startLogin();

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (final AbstractProvider authProvider : authProviderHashMap.values()) {
            authProvider.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void onStop() {
        for (final AbstractProvider authProvider : authProviderHashMap.values()) {
            authProvider.onStop();
        }
    }

    private void signInFirebaseWithCredential(AuthCredential authCredential) {
        FirebaseAuth.getInstance().signInWithCredential(authCredential)
                .addOnFailureListener(new TaskFailureLogger("AbstractProvider", "Error signInWithCredential"))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        singInHandlerException(e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                    }
                });

    }

    private void singInHandlerException(Exception exception) {
        if (exception instanceof FirebaseAuthUserCollisionException) {
            FirebaseAuthUserCollisionException ex = (FirebaseAuthUserCollisionException) exception;
            if (ex.getErrorCode().equalsIgnoreCase(ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL)) {
                return;
            }
        }

    }

}
