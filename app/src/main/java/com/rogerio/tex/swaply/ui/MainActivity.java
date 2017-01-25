package com.rogerio.tex.swaply.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.provider.AuthResponse;
import com.rogerio.tex.swaply.ui.auth.LoginActivity;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_PARAM_ID = "EXTRA_PARAM";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private CircleImageView imageProfile;
    private TextView nameProfile;
    private TextView emailProfile;


    public static void startActivity(Activity activity, AuthResponse response) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(EXTRA_PARAM_ID, response);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        View header = navigationView.getHeaderView(0);
        imageProfile = (CircleImageView) header.findViewById(R.id.image_profile);
        emailProfile = (TextView) header.findViewById(R.id.email_profile);
        nameProfile = (TextView) header.findViewById(R.id.name_profile);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.v("UserProf", "onAuthState:" + user);
                if (user == null) {
                    LoginActivity.startActivity(MainActivity.this);
                } else {
                    updateNavigationHeader(user);
                }

            }
        };
    }

    private void updateNavigationHeader(FirebaseUser user) {
        Log.v("userProfile", "PhotoUrl:" + user.getPhotoUrl());
        Glide.with(this).
                load(user.getPhotoUrl())
                .centerCrop()
                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageProfile);
        emailProfile.setText(user.getEmail());
        nameProfile.setText(user.getDisplayName());
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_exit:
                getActivityHelper().getFirebaseAuth().signOut();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
