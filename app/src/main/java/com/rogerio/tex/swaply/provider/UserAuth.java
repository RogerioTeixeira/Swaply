package com.rogerio.tex.swaply.provider;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rogerio on 17/01/2017.
 */

public class UserAuth implements Parcelable {
    public static final Parcelable.Creator<UserAuth> CREATOR = new Parcelable.Creator<UserAuth>() {
        @Override
        public UserAuth createFromParcel(Parcel source) {
            return new UserAuth(source);
        }

        @Override
        public UserAuth[] newArray(int size) {
            return new UserAuth[size];
        }
    };
    private String name;
    private String email;
    private String photoUrl;

    public UserAuth(String name, String email, String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    protected UserAuth(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.photoUrl = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.photoUrl);
    }
}
