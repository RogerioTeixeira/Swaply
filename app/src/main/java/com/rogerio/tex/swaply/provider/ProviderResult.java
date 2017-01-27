package com.rogerio.tex.swaply.provider;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rogerio on 17/01/2017.
 */

public class ProviderResult implements Parcelable {
    public static final Parcelable.Creator<ProviderResult> CREATOR = new Parcelable.Creator<ProviderResult>() {
        @Override
        public ProviderResult createFromParcel(Parcel source) {
            return new ProviderResult(source);
        }

        @Override
        public ProviderResult[] newArray(int size) {
            return new ProviderResult[size];
        }
    };
    private String name;
    private String email;
    private String photoUrl;
    private String provideData;

    public ProviderResult(String name, String email, String photoUrl, String provideData) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.provideData = provideData;
    }

    protected ProviderResult(Parcel in) {
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
}
