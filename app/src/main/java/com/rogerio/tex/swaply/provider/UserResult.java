package com.rogerio.tex.swaply.provider;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rogerio on 17/01/2017.
 */

public class UserResult implements Parcelable {
    public static final Parcelable.Creator<UserResult> CREATOR = new Parcelable.Creator<UserResult>() {
        @Override
        public UserResult createFromParcel(Parcel source) {
            return new UserResult(source);
        }

        @Override
        public UserResult[] newArray(int size) {
            return new UserResult[size];
        }
    };
    private String name;
    private String email;
    private String photoUrl;
    private String provideData;
    private String token;
    private String secretKey;

    public UserResult(String name, String email, String photoUrl, String provideData, String token, String secretKey) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.provideData = provideData;
        this.token = token;
        this.secretKey = secretKey;

    }

    protected UserResult(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.photoUrl = in.readString();
        this.provideData = in.readString();
        this.token = in.readString();
        this.secretKey = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getProvideData() {
        return provideData;
    }

    public String getToken() {
        return token;
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
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.photoUrl);
        dest.writeString(this.provideData);
        dest.writeString(this.token);
        dest.writeString(this.secretKey);
    }

    public static class Builder {
        private String name;
        private String email;
        private String photoUrl;
        private String provideData;
        private String token;
        private String secretKey;


        private Builder() {

        }

        public static Builder create() {
            return new Builder();
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public Builder setProvideData(String provideData) {
            this.provideData = provideData;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setSecretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public UserResult build() {
            return new UserResult(name, email, photoUrl, provideData, token, secretKey);
        }
    }
}
