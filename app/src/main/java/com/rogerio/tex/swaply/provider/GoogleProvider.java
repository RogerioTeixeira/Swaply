package com.rogerio.tex.swaply.provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.rogerio.tex.swaply.R;

/**
 * Created by Rogerio Lavoro on 02/12/2016.
 */

public class GoogleProvider extends AbstractProvider implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoogleProvider";
    private static final int RC_SIGN_IN = 20;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private AppCompatActivity activity;

    public GoogleProvider(AppCompatActivity activity, AuthCallback authCallback) {
        super(activity, authCallback);
        this.activity = activity;
        initGoogleService();
    }

    private void initGoogleService() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .enableAutoManage(activity, this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public String getProviderId() {
        return GoogleAuthProvider.PROVIDER_ID;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                authCallback.onSuccess(createProviderResponse(account));
            } else {
                authCallback.onFailure(new Bundle());
            }

        }
    }

    private AuthResponse createProviderResponse(GoogleSignInAccount account) {
        AuthResponse response = AuthResponse.Builder.create()
                .setToken(account.getIdToken())
                .setProviderId(getProviderId())
                .setEmail(account.getEmail())
                .setName(account.getDisplayName())
                .setPhotoUrl(account.getPhotoUrl().getPath())
                .setSuccessful(true)
                .build();
        return response;
    }

    @Override
    public void startLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public AuthCredential createAuthCredential(AuthResponse response) {
        return GoogleAuthProvider.getCredential(response.getToken(), null);
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
    }


}
