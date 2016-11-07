package com.rogerio.tex.swaply.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.texsoft.calisthenicssingle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GoogleLoginFragment extends BaseLoginFragment implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener {

    private static final int RC_SIGN_IN = 0;
    private static final String TAG_LOG = GoogleLoginFragment.class.getName();

    @BindView(R.id.sign_in_button_google)
    SignInButton signInButtonGoogle;
    @BindView(R.id.disconnect_button_google)
    Button disconnectButtonGoogle;


 //   private OnFragmentInteractionListener mListener;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    public GoogleLoginFragment() {
        // Required empty public constructor
    }

    public static GoogleLoginFragment newInstance() {
        GoogleLoginFragment fragment = new GoogleLoginFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout(){
        return 1;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_google_login, container, false);
        ButterKnife.bind(this, view);
        signInButtonGoogle.setSize(SignInButton.SIZE_STANDARD);
        signInButtonGoogle.setColorScheme(SignInButton.COLOR_LIGHT);
        signInButtonGoogle.setOnClickListener(this);
        setGooglePlusButtonText(signInButtonGoogle, "Accedi con google");

        return view;


    }

    protected void setGooglePlusButtonText(SignInButton signInButton,
                                           String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(15);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setText(buttonText);
                return;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(TAG_LOG, "Connessione API google fallita");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v(TAG_LOG, "Connessione API google ok");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG_LOG, "Connessione API google sospesa");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                updateStateButton(true);

            } else {
                Log.v(TAG_LOG, "Autenticazione google ko:" + result.getStatus().getStatusMessage());
                updateStateButton(false);
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
