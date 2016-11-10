package com.rogerio.tex.swaply.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rogerio.tex.swaply.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.BindView;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterLoginFragment extends BaseLoginFragment {

    private static final String TWITTER_KEY = "vN1aPHy2T0Y8O4S6L58D6bktc";
    private static final String TWITTER_SECRET = "91MPS4g3wmHcWz1WdjHiN4ifxMDHXvgNmCTYuTMBR07ixXICwU";
    @BindView(R.id.sign_in_button_twitter)
    Button signInButtonTwitter;
    @BindView(R.id.disconnect_button_twitter)
    Button disconnectButtonTwitter;

    @BindView(R.id.twitter_login_button)
    TwitterLoginButton twitterLoginButton;
    private TwitterAuthClient client;

    public TwitterLoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_twitter_login;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(getContext(), new Twitter(authConfig));

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("TwitterKit", "login success");
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login failure");
            }
        });

    }


    @OnClick({R.id.sign_in_button_twitter, R.id.disconnect_button_twitter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button_twitter:
                signIn();
                break;
            case R.id.disconnect_button_twitter:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void signIn() {
        twitterLoginButton.performClick();
    }
}
