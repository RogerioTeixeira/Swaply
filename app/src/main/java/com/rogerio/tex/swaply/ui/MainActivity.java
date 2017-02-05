package com.rogerio.tex.swaply.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.helper.ProfileHelper;
import com.rogerio.tex.swaply.helper.model.UserProfile;
import com.rogerio.tex.swaply.provider.UserResult;
import com.rogerio.tex.swaply.ui.auth.LoginActivity;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_PARAM_ID = "EXTRA_PARAM";
    public static final String TAG = "MainActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private CircleImageView imageProfile;
    private ImageView imageSfondo;
    private TextView nameProfile;
    private TextView emailProfile;
    private DatabaseReference refDatabase;
    private ProfileHelper profileHelper;
    private ValueEventListener valueEventListener;
    private UserProfile profile;


    public static void startActivity(Activity activity, UserResult response) {
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
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, imageProfile, "profile");
                ProfileActivity.startActivity(MainActivity.this, profile, options);
            }
        });

        imageSfondo = (ImageView) header.findViewById(R.id.image_sfondo);
        emailProfile = (TextView) header.findViewById(R.id.email_profile);
        nameProfile = (TextView) header.findViewById(R.id.name_profile);

        profileHelper = ProfileHelper.getInstance();
        refDatabase = profileHelper.getMyProfile();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v(TAG, "CambioDati");
                if (!dataSnapshot.exists()) {
                    LoginActivity.startActivity(MainActivity.this);
                } else {
                    profile = dataSnapshot.getValue(UserProfile.class);
                    updateNavigationHeader(profile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error read profile", databaseError.toException());
            }
        };
    }

    private void updateNavigationHeader(UserProfile prof) {
        Glide.with(MainActivity.this).
                load(prof.getPhotoUrl())
                .centerCrop()
                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageProfile);
        emailProfile.setText(prof.getEmail());
        nameProfile.setText(prof.getName());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "FirebaseListener attach");
        refDatabase.addValueEventListener(valueEventListener);

    }

    @Override

    public void onStop() {
        super.onStop();
        Log.v(TAG, "FirebaseListener detach");
        refDatabase.removeEventListener(valueEventListener);
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
                LoginActivity.startActivity(MainActivity.this);
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
