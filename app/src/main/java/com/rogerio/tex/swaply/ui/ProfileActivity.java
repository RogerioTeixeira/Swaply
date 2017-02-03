package com.rogerio.tex.swaply.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.adapter.ViewPagerAdapter;
import com.rogerio.tex.swaply.helper.model.UserProfile;

import butterknife.BindView;

public class ProfileActivity extends BaseActivity {

    private static final String PARAM_EXTRA = "userProfile";
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.activity_email_auth)
    CoordinatorLayout activityEmailAuth;
    @BindView(R.id.image_profile)
    ImageView imageProfile;
    private UserProfile userProfile;

    public static void startActivity(Activity activity, UserProfile profile) {
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtra(PARAM_EXTRA, profile);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userProfile = getIntent().getParcelableExtra(PARAM_EXTRA);
        if (userProfile != null) {
            collapsingToolbar.setTitle(userProfile.getName());
            loadProfileData();
        }


        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(WishListFragment.newInstance("a", "b"), getResources().getString(R.string.title_wishlist));
        adapter.addFragment(WishListFragment.newInstance("a", "b"), getResources().getString(R.string.title_friends));
        pager.setAdapter(adapter);
        tablayout.setupWithViewPager(pager);
        tablayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadProfileData() {
        Glide.with(this).
                load(userProfile.getPhotoUrl())
                .centerCrop()
                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageProfile);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profile;
    }

}
