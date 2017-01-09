package com.rogerio.tex.swaply.provider;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by Rogerio Lavoro on 09/01/2017.
 */

public class ProviderResponse implements Parcelable {
    public static final Parcelable.Creator<ProviderResponse> CREATOR = new Parcelable.Creator<ProviderResponse>() {
        @Override
        public ProviderResponse createFromParcel(Parcel source) {
            return new ProviderResponse(source);
        }

        @Override
        public ProviderResponse[] newArray(int size) {
            return new ProviderResponse[size];
        }
    };
    @Nullable
    private final String email;
    private final String token;
    private final String providerId;
    private final String secretKey;

    public ProviderResponse(String email, String token, String providerId, String secretKey) {
        this.email = email;
        this.token = token;
        this.providerId = providerId;
        this.secretKey = secretKey;
    }

    protected ProviderResponse(Parcel in) {
        this.email = in.readString();
        this.token = in.readString();
        this.providerId = in.readString();
        this.secretKey = in.readString();
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.token);
        dest.writeString(this.providerId);
        dest.writeString(this.secretKey);
    }
}
