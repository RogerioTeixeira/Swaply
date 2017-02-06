package com.rogerio.tex.swaply.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.rogerio.tex.swaply.helper.firebase.FirebaseHelper;
import com.rogerio.tex.swaply.model.UserProfile;
import com.rogerio.tex.swaply.provider.UserResult;

/**
 * Created by rogerio on 31/01/2017.
 */
public class ProfileHelper {
    private final static String USERS_PROFILE = "users_profiles";
    private final static String USERS_PROFILE_TIME_VAR = "timeVar";
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
        DatabaseReference ref = firebaseHelper.getReferences(USERS_PROFILE).child(uid);
        ref.setValue(user);
        ref.child(USERS_PROFILE_TIME_VAR).setValue(ServerValue.TIMESTAMP);
    }

    public void updateProfile(UserResult result, String uid) {
        UserProfile profile = createUserProfileFromUserResult(result);
        updateProfile(profile, uid);
    }

    public DatabaseReference getMyProfile() {
        FirebaseUser user = getMyFirebaseUser();
        if (user != null) {
            return getProfile(user.getUid());
        }

        return null;
    }

    public DatabaseReference getProfile(String uid) {
        return firebaseHelper.getReferences(USERS_PROFILE).child(uid);
    }

    public FirebaseUser getMyFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void updateMyProfile(UserResult result) {
        UserProfile profile = createUserProfileFromUserResult(result);
        updateMyProfile(profile);
    }

    public void updateMyProfile(UserProfile profile) {
        String uid = getMyFirebaseUser().getUid();
        updateProfile(profile, uid);
    }
}
