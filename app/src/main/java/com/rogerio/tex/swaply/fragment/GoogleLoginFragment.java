package com.rogerio.tex.swaply.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.rogerio.tex.swaply.R;

import butterknife.BindView;
import butterknife.OnClick;


public class GoogleLoginFragment extends BaseLoginFragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    private static final String TAG_LOG = GoogleLoginFragment.class.getName();

    @BindView(R.id.sign_in_button_google)
    Button signInButtonGoogle;
    @BindView(R.id.disconnect_button_google)
    Button disconnectButtonGoogle;



    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    public GoogleLoginFragment() {

    }

    public static GoogleLoginFragment newInstance() {
        GoogleLoginFragment fragment = new GoogleLoginFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout(){

        return R.layout.fragment_google_login;
    }

    // implemetazione GoogleApiClient.ConnectionCallbacks

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(TAG_LOG, "Connection google api failed. Error code:" + connectionResult.getErrorCode());


    }
    // fine GoogleApiClient.ConnectionCallbacks


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("ApiclientTest", "Creata finestra");
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .enableAutoManage(getActivity(), this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Log.v("ApiclientTest", "Connessione");
        //   mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        //   Log.v("ApiclientTest", "Disconnect");
        //    mGoogleApiClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                Log.v("Verifica", "Token:" + account.getIdToken());
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                signinFirebase(credential);
            }
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void revokeAccess(){
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateStateButton(false);
                    }
                });
    }

    private void updateStateButton(boolean signedIn) {
        if(signedIn) {
            signInButtonGoogle.setVisibility(View.GONE);
            disconnectButtonGoogle.setVisibility(View.VISIBLE);
        } else {
            signInButtonGoogle.setVisibility(View.VISIBLE);
            disconnectButtonGoogle.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.sign_in_button_google, R.id.disconnect_button_google})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button_google:
                signIn();
                break;
            case R.id.disconnect_button_google:
                break;
        }
    }


}
