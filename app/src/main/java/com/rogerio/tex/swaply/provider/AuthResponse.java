package com.rogerio.tex.swaply.provider;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rogerio Lavoro on 09/01/2017.
 */

public class AuthResponse implements Parcelable {
    public static final Parcelable.Creator<AuthResponse> CREATOR = new Parcelable.Creator<AuthResponse>() {
        @Override
        public AuthResponse createFromParcel(Parcel source) {
            return new AuthResponse(source);
        }

        @Override
        public AuthResponse[] newArray(int size) {
            return new AuthResponse[size];
        }
    };
    private static final byte PRESENT = 1;
    private static final byte PRESENT_NO = 0;
    private final String token;
    private final String providerId;
    private final String secretKey;
    private final UserResult user;
    private final Exception exception;
    private final boolean isSuccessful;

    public AuthResponse(String token, String providerId, String secretKey, UserResult user, Exception exception, boolean isSuccessful) {
        this.token = token;
        this.providerId = providerId;
        this.secretKey = secretKey;
        this.user = user;
        this.exception = exception;
        this.isSuccessful = isSuccessful;
    }

    protected AuthResponse(Parcel in) {
        this.token = in.readString();
        this.providerId = in.readString();
        this.secretKey = in.readString();
        this.isSuccessful = in.readByte() != 0;
        if (in.readByte() == PRESENT) {
            this.user = in.readParcelable(UserResult.class.getClassLoader());
        } else {
            this.user = null;
        }

        if (in.readByte() == PRESENT) {
            this.exception = (Exception) in.readSerializable();
        } else {
            this.exception = null;
        }
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

    public UserResult getUser() {
        return user;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.providerId);
        dest.writeString(this.secretKey);
        dest.writeByte(this.isSuccessful ? (byte) 1 : (byte) 0);
        if (this.user == null) {
            dest.writeByte(PRESENT_NO);
        } else {
            dest.writeByte(PRESENT);
            dest.writeParcelable(this.user, flags);
        }

        if (this.exception == null) {
            dest.writeByte(PRESENT_NO);
        } else {
            dest.writeByte(PRESENT);
            dest.writeSerializable(this.exception);
        }

    }

    public static final class Builder {
        private String token;
        private String providerId;
        private String secretKey;
        private UserResult user;
        private Exception exception;
        private boolean isSuccessful;
        private String name;
        private String email;
        private String photoUrl;

        private Builder() {

        }

        public static Builder create() {
            return new Builder();
        }

        public Builder setProviderId(String providerId) {
            this.providerId = providerId;
            return this;
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

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setSecretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public Builder setUser(UserResult user) {
            this.user = user;
            return this;
        }

        public Builder setException(Exception exception) {
            this.exception = exception;
            return this;
        }

        public Builder setSuccessful(boolean successful) {
            isSuccessful = successful;
            return this;
        }

        public AuthResponse build() {
            if (this.user == null) {
                if (this.email != null || this.name != null || this.photoUrl != null) {
                    this.user = new UserResult(this.name, this.email, this.photoUrl);
                }
            }
            return new AuthResponse(this.token, this.providerId, this.secretKey, this.user, this.exception, this.isSuccessful);
        }
    }
}
