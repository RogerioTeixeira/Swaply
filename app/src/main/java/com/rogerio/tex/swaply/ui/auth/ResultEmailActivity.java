package com.rogerio.tex.swaply.ui.auth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rogerio on 07/01/2017.
 */

public class ResultEmailActivity implements Parcelable {
    public static final Parcelable.Creator<ResultEmailActivity> CREATOR = new Parcelable.Creator<ResultEmailActivity>() {
        @Override
        public ResultEmailActivity createFromParcel(Parcel source) {
            return new ResultEmailActivity(source);
        }

        @Override
        public ResultEmailActivity[] newArray(int size) {
            return new ResultEmailActivity[size];
        }
    };
    private String email;
    private String password;
    private String provideId;

    public ResultEmailActivity(String email, String password, String provideId) {
        this.email = email;
        this.password = password;
        this.provideId = provideId;
    }

    protected ResultEmailActivity(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
        this.provideId = in.readString();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProvideId() {
        return provideId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.provideId);
    }
}
