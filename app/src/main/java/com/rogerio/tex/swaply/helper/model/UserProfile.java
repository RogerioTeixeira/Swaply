package com.rogerio.tex.swaply.helper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rogerio on 26/01/2017.
 */
@IgnoreExtraProperties
public class UserProfile implements Parcelable {
    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel source) {
            return new UserProfile(source);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };
    private static final byte IS_NULL = 0;
    private static final byte IS_NO_NULL = 1;
    public String email;
    public String name;
    public String photoUrl;
    public Map<String, Boolean> wishList;
    public Map<String, Boolean> friendsList;

    public UserProfile(String email, String name, String photoUrl, Map<String, Boolean> wishList, Map<String, Boolean> friendsList) {
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
        this.wishList = wishList;
        this.friendsList = friendsList;
    }

    public UserProfile() {
    }

    protected UserProfile(Parcel in) {
        this.email = in.readString();
        this.name = in.readString();
        this.photoUrl = in.readString();
        if (in.readByte() == IS_NO_NULL) {
            int wishListSize = in.readInt();
            this.wishList = new HashMap<String, Boolean>(wishListSize);
            for (int i = 0; i < wishListSize; i++) {
                String key = in.readString();
                Boolean value = (Boolean) in.readValue(Boolean.class.getClassLoader());
                this.wishList.put(key, value);
            }
        }
        if (in.readByte() == IS_NO_NULL) {
            int friendsListSize = in.readInt();
            this.friendsList = new HashMap<String, Boolean>(friendsListSize);
            for (int i = 0; i < friendsListSize; i++) {
                String key = in.readString();
                Boolean value = (Boolean) in.readValue(Boolean.class.getClassLoader());
                this.friendsList.put(key, value);
            }
        }

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Map<String, Boolean> getwishList() {
        return this.wishList;
    }

    public void setwishList(Map<String, Boolean> wishList) {
        this.wishList = wishList;
    }

    public Map<String, Boolean> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(Map<String, Boolean> friendsList) {
        this.friendsList = friendsList;
    }

    public void AddItemWishList(String wishList, Boolean bool) {
        if (this.wishList == null) {
            this.wishList = new HashMap<>();
        }
        this.wishList.put(wishList, bool);
    }

    public void AddItemFriendsList(String friend, Boolean bool) {
        if (this.friendsList == null) {
            this.friendsList = new HashMap<>();
        }
        this.wishList.put(friend, bool);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeString(this.photoUrl);
        if (this.wishList == null) {
            dest.writeByte(IS_NULL);
        } else {
            dest.writeByte(IS_NO_NULL);
            dest.writeInt(this.wishList.size());
            for (Map.Entry<String, Boolean> entry : this.wishList.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeValue(entry.getValue());
            }
        }

        if (this.friendsList == null) {
            dest.writeByte(IS_NULL);
        } else {
            dest.writeByte(IS_NO_NULL);
            dest.writeInt(this.friendsList.size());
            for (Map.Entry<String, Boolean> entry : this.friendsList.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeValue(entry.getValue());
            }
        }
    }
}
