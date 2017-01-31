package com.rogerio.tex.swaply.helper;

import com.rogerio.tex.swaply.helper.firebase.FirebaseHelper;
import com.rogerio.tex.swaply.helper.model.UserProfile;
import com.rogerio.tex.swaply.provider.UserResult;

/**
 * Created by rogerio on 31/01/2017.
 */
public class ProfileHelper {
    private final static String USERS_PROFILE = "users_profiles";
    private static ProfileHelper ourInstance;
    private FirebaseHelper firebaseHelper;

    private ProfileHelper() {
        firebaseHelper = FirebaseHelper.getInstance();
    }

    public static ProfileHelper getInstance() {
        if (ourInstance == null)
            ourInstance = new ProfileHelper();
        return ourInstance;
    }

    public UserProfile createUserProfileFromUserResult(UserResult result) {
        UserProfile userProfile = new UserProfile();
        userProfile.setName(result.getName());
        userProfile.setEmail(result.getEmail());
        userProfile.setPhotoUrl(result.getPhotoUrl());
        return userProfile;
    }

    public void updateProfile(UserProfile user, String uid) {
        firebaseHelper.getReferences(USERS_PROFILE).child(uid).setValue(user);
    }

    public void updateProfile(UserResult result, String uid) {
        UserProfile profile = createUserProfileFromUserResult(result);
        updateProfile(profile, uid);
    }
}
