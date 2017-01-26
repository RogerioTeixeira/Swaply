package com.rogerio.tex.swaply.firebase.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by rogerio on 26/01/2017.
 */
public class FirebaseHelper {
    private final static String USERS_PATH = "users";
    private static FirebaseHelper ourInstance;
    private DatabaseReference dataReference;

    private FirebaseHelper() {
        dataReference = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseHelper getInstance() {
        if (ourInstance == null) {
            ourInstance = new FirebaseHelper();
        }
        return ourInstance;
    }

    public DatabaseReference getUserReferences(String uid) {
        return dataReference.getRoot().child(USERS_PATH).child(uid);
    }


}
