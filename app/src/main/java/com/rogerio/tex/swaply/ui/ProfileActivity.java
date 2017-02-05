package com.rogerio.tex.swaply.ui;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.adapter.ViewPagerAdapter;
import com.rogerio.tex.swaply.helper.model.UserProfile;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {

    private static final String PARAM_EXTRA = "userProfile";
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
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

        GoogleApiClient mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.v("Prova", "errore");
                    }
                })
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.v("Prova", "connesione ok");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
        imageProfileCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Log.v("Prova", "Prima del lancio");
                    startActivityForResult(builder.build(ProfileActivity.this), 20);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });


        // Start the Intent by requesting a result, identified by a request code.


        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String localeCountry = mTelephonyManager.getNetworkCountryIso();
        if (localeCountry != null) {
            Locale loc = new Locale("", localeCountry);
            Log.d("locationManager", "User is from " + loc.getDisplayCountry());
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.v("locationManager", "onLocationChanged");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.v("locationManager", "onStatusChanged");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.v("locationManager", "onProviderEnabled");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.v("locationManager", "onProviderDisabled");
                }
            });
        } catch (SecurityException e) {
            Log.e("LocationError", "Errore autorizzazione", e);
        }
        List<String> matchingProviders = locationManager.getAllProviders();
        for (String provider : matchingProviders) {
            try {
                Location location = locationManager.getLastKnownLocation(provider);
                Log.v("Location", "provider:" + provider);
                if (location != null) {
                    Log.v("Location", "risultato long:" + location.getLongitude());
                    Log.v("Location", "risultato lat:" + location.getLatitude());
                }
            } catch (SecurityException e) {
                Log.e("LocationError", "Errore autorizzazione", e);
            }

        }
        Log.v("Paese", "Country1:" + matchingProviders.toString());

        Log.v("Paese", "Country:" + mTelephonyManager.getNetworkCountryIso());

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
