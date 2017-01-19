package com.rogerio.tex.swaply.provider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;

import java.util.HashMap;

/**
 * Created by rogerio on 18/01/2017.
 */

public class ProviderManager {

    private final HashMap<String, AuthProvider> authProviderHashMap = new HashMap<>();

    private ProviderManager() {

    }

    public static ProviderManager createInstance() {
        return new ProviderManager();
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

    public void startLogin(String providerId, AppCompatActivity activity, AuthProvider.AuthCallback authCallback) {

        if (authProviderHashMap.containsKey(providerId)) {
            authProviderHashMap.get(providerId).startLogin();
        } else {
            AuthProvider provider = null;
            switch (providerId) {
                case EmailAuthProvider.PROVIDER_ID:
                    provider = new EmailProvider(activity, authCallback);
                    break;
                case FacebookAuthProvider.PROVIDER_ID:
                    provider = new FacebookProvider(activity, authCallback);
                    break;
                case GoogleAuthProvider.PROVIDER_ID:
                    provider = new GoogleProvider(activity, authCallback);
                    break;
                case TwitterAuthProvider.PROVIDER_ID:
                    provider = new TwitterProvider(activity, authCallback);
                    break;
            }
            if (provider != null) {
                authProviderHashMap.put(provider.getProviderId(), provider);
                provider.startLogin();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (final AuthProvider authProvider : authProviderHashMap.values()) {
            authProvider.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void onStop() {
        for (final AuthProvider authProvider : authProviderHashMap.values()) {
            authProvider.onStop();
        }
    }

}
