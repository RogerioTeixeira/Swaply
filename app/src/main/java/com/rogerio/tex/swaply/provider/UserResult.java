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

    public UserResult(String name, String email, String photoUrl, String provideData) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.provideData = provideData;
    }

    protected UserResult(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.photoUrl = in.readString();
        this.provideData = in.readString();
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
    }

    public static class Builder {
        private String name;
        private String email;
        private String photoUrl;
        private String provideData;

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

        public UserResult build() {
            return new UserResult(name, email, photoUrl, provideData);
        }
    }
}
