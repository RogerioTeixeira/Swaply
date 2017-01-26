package com.rogerio.tex.swaply.firebase.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

/**
 * Created by rogerio on 26/01/2017.
 */
@IgnoreExtraProperties
public class UserProfile {
    public String email;
    public String name;
    public String photoUrl;
    Map<String, Boolean> contacts;

    public UserProfile(String email, String name, String photoUrl, Map<String, Boolean> contacts) {
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
        this.contacts = contacts;
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

    public Map<String, Boolean> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, Boolean> contacts) {
        this.contacts = contacts;
    }
}
