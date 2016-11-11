package com.rogerio.tex.swaply.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.TwitterAuthProvider;
import com.rogerio.tex.swaply.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import butterknife.BindView;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;



public class TwitterLoginFragment extends BaseLoginFragment {

    // private static final String TWITTER_KEY = "vN1aPHy2T0Y8O4S6L58D6bktc";
    // private static final String TWITTER_SECRET = "91MPS4g3wmHcWz1WdjHiN4ifxMDHXvgNmCTYuTMBR07ixXICwU";

    @BindView(R.id.sign_in_button_twitter)
    Button signInButtonTwitter;
    @BindView(R.id.disconnect_button_twitter)
    Button disconnectButtonTwitter;

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
        String twitter_key = getResources().getString(R.string.twitter_key);
        String twitter_secret = getResources().getString(R.string.twitter_secret);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitter_key, twitter_secret);
        Fabric.with(getContext(), new Twitter(authConfig));

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        client.onActivityResult(requestCode, resultCode, data);
    }

    public void signIn() {
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
        client = new TwitterAuthClient();
        client.authorize(getActivity(), new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                TwitterSession session = twitterSessionResult.data;
                AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                        session.getAuthToken().secret);
                signinFirebase(credential);
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
