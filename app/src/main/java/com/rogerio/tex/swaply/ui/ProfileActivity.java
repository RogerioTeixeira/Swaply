package com.rogerio.tex.swaply.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.adapter.ViewPagerAdapter;
import com.rogerio.tex.swaply.model.UserProfile;

import java.util.Locale;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {

    private static final String PARAM_EXTRA = "userProfile";
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 40;
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
    @BindView(R.id.image_profile_circle)
    CircleImageView imageProfileCircle;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.scroll)
    NestedScrollView scroll;
    @BindView(R.id.text_location)
    TextView textLocation;
    private int maxScrollSize;
    private boolean isAvatarShown = true;
    private UserProfile userProfile;

    public static void startActivity(Activity activity, UserProfile profile) {
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtra(PARAM_EXTRA, profile);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, UserProfile profile, ActivityOptionsCompat options) {
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtra(PARAM_EXTRA, profile);
        activity.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        maxScrollSize = appbarlayout.getTotalScrollRange();
        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (maxScrollSize == 0) {
                    maxScrollSize = appbarlayout.getTotalScrollRange();
                }

                int percentage = (Math.abs(verticalOffset)) * 100 / maxScrollSize;

                if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && isAvatarShown) {
                    isAvatarShown = false;
                    imageProfileCircle.animate().scaleX(0).scaleY(0).setDuration(200).start();
                }

                if (percentage < PERCENTAGE_TO_ANIMATE_AVATAR && !isAvatarShown) {
                    isAvatarShown = true;
                    imageProfileCircle.animate().scaleX(1).scaleY(1).setDuration(200).start();

                }
            }
        });

        userProfile = getIntent().getParcelableExtra(PARAM_EXTRA);
        if (userProfile != null) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadProfileData() {
        textName.setText(userProfile.getName());
        if (userProfile.getCountry() != null) {
            textLocation.setVisibility(View.VISIBLE);
            Locale loc = new Locale("", userProfile.getCountry());
            textLocation.setText(loc.getDisplayCountry());
        } else {
            textLocation.setVisibility(View.INVISIBLE);
        }

        Glide.with(this).
                load(userProfile.getPhotoUrl())
                .centerCrop()
                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageProfile);
        Glide.with(this).
                load(userProfile.getPhotoUrl())
                .centerCrop()
                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageProfileCircle);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profile;
    }

}
