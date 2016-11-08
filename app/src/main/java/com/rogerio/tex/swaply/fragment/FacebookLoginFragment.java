package com.rogerio.tex.swaply.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.rogerio.tex.swaply.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FacebookLoginFragment extends BaseLoginFragment {


    @BindView(R.id.sign_in_button_facebook)
    Button signInButtonFacebook;
    @BindView(R.id.disconnect_button_facebook)
    Button disconnectButtonFacebook;

    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private List<String> permission;
    private WeakReference<BaseLoginFragment.LoginCallback> loginCallBack;

    public FacebookLoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FacebookLoginFragment newInstance() {
        FacebookLoginFragment fragment = new FacebookLoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();
        permission = new ArrayList<String>();
        permission.add("email");
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getContext(), "Login riuscito:" + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                BaseLoginFragment.LoginCallback callback;
                if (loginCallBack != null && (callback = loginCallBack.get()) != null) {
                    AccessToken accessToken = loginResult.getAccessToken();
                    AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
                    callback.onConnected(credential);
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "Login cancellato", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), "Login ko:" + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseLoginFragment.LoginCallback) {
            BaseLoginFragment.LoginCallback mLoginCallback = (BaseLoginFragment.LoginCallback) context;
            loginCallBack = new WeakReference<LoginCallback>(mLoginCallback);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (loginCallBack != null) {
            loginCallBack.clear();
            loginCallBack = null;
        }
    }

    @Override
    protected int getFragmentLayout() {

        return R.layout.fragment_facebook_login;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.sign_in_button_facebook, R.id.disconnect_button_facebook})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button_facebook:
                signinFacebook();
                break;
            case R.id.disconnect_button_facebook:
                break;
        }
    }

    public void signinFacebook() {
        loginManager.logInWithReadPermissions(this, permission);
    }
}
