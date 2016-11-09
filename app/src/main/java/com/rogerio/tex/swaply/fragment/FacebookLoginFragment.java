package com.rogerio.tex.swaply.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    private static final String TAG_LOG = FacebookLoginFragment.class.getName();

    @BindView(R.id.sign_in_button_facebook)
    Button signInButtonFacebook;
    @BindView(R.id.disconnect_button_facebook)
    Button disconnectButtonFacebook;

    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private WeakReference<BaseLoginFragment.LoginCallback> loginCallBack;


    public FacebookLoginFragment() {

    }


    public static FacebookLoginFragment newInstance() {
        FacebookLoginFragment fragment = new FacebookLoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                BaseLoginFragment.LoginCallback callback;
                if (loginCallBack != null && (callback = loginCallBack.get()) != null) {
                    AccessToken accessToken = loginResult.getAccessToken();
                    AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
                    signinFirebase(credential);
                }
            }

            @Override
            public void onCancel() {
                Log.v(TAG_LOG, "Login facebook cancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG_LOG, "Login facebook error", error);
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
        List<String> permission = new ArrayList<String>();
        permission.add("email");
        loginManager.logInWithReadPermissions(this, permission);
    }

    public void signOutFacebook() {
        loginManager.logOut();
        signOutFirebase();
    }

}
