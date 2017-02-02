package com.rogerio.tex.swaply.helper.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rogerio on 26/01/2017.
 */
@IgnoreExtraProperties
public class UserProfile {
    public String email;
    public String name;
    public String photoUrl;
    public Map<String, Boolean> wishList;

    public UserProfile(String email, String name, String photoUrl, Map<String, Boolean> wishList) {
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
        this.wishList = wishList;
    }

    public UserProfile() {
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

    public void setwishList(Map<String, Boolean> contacts) {
        this.wishList = contacts;
    }

    public void AddItemWishList(String wishList, Boolean bool) {
        if (this.wishList == null) {
            this.wishList = new HashMap<>();
        }
        this.wishList.put(wishList, bool);
    }

}
