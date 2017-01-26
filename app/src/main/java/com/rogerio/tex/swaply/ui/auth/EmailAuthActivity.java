package com.rogerio.tex.swaply.ui.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.provider.AuthProvider;
import com.rogerio.tex.swaply.provider.AuthResponse;
import com.rogerio.tex.swaply.provider.LoginProviderManager;
import com.rogerio.tex.swaply.ui.BaseActivity;
import com.rogerio.tex.swaply.ui.auth.fragment.CreateAccountFragment;
import com.rogerio.tex.swaply.ui.auth.fragment.EmailAuthFragment;
import com.rogerio.tex.swaply.ui.auth.fragment.EmailLoginFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EmailAuthActivity extends BaseActivity implements EmailAuthFragment.EmailAuthListener, AuthProvider.AuthCallback {

    public static final String EXTRA_PARAM_ID = "EXTRA_PARAM";
    public static final int RESULT_COLLISION = 30;
    public static final int REQUEST_CODE = 100;
    private static final int INDEX_TAB_LOGIN = 0;
    private static final int INDEX_TAB_CREATE = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.pager)
    ViewPager pager;

    private LoginProviderManager providerManager;

    public static Intent createResultIntent(AuthResponse response) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAM_ID, response);
        return intent;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, EmailAuthActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static AuthResponse getResultData(Intent intent) {
        AuthResponse response = intent.getParcelableExtra(EXTRA_PARAM_ID);
        return response;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EmailLoginFragment(), getResources().getString(R.string.title_toolbar_login));
        adapter.addFragment(new CreateAccountFragment(), getResources().getString(R.string.title_toolbar_create));
        pager.setAdapter(adapter);
        tablayout.setupWithViewPager(pager);
        setToobarTitle(tablayout.getSelectedTabPosition());
        tablayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setToobarTitle(tab.getPosition());
            }
        });

        providerManager = LoginProviderManager.createInstance();

    }

    public void setToobarTitle(int tabIdex) {
        switch (tabIdex) {
            case INDEX_TAB_LOGIN:
                getSupportActionBar().setTitle(getResources().getString(R.string.title_toolbar_login));
                break;
            case INDEX_TAB_CREATE:
                getSupportActionBar().setTitle(getResources().getString(R.string.title_toolbar_create));
                break;
        }

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_email_auth;
    }


    @Override
    public void onExistingEmailUser(AuthResponse response) {
        Log.v("onExistingEmailUser", "onExistingEmailUser");
        pager.setCurrentItem(0);
        ViewPagerAdapter adapter = (ViewPagerAdapter) pager.getAdapter();
        if (adapter.getItem(0) instanceof EmailLoginFragment) {
            EmailLoginFragment fragment = (EmailLoginFragment) adapter.getItem(0);
            fragment.setEmail(response.getUser().getEmail());
        }
    }

    @Override
    public void onExistingIdpUser(AuthResponse response) {
        String providerId = response.getProviderId();
        providerManager.startLogin(providerId, this, this);
    }

    @Override
    public void succesLogin(AuthResponse response) {
        getActivityHelper().finishActivity(Activity.RESULT_OK, createResultIntent(response));
    }

    @Override
    public void onSuccess(AuthResponse response) {
        getActivityHelper().finishActivity(Activity.RESULT_OK, createResultIntent(response));
    }

    @Override
    public void onFailure(Bundle extra) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        providerManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        providerManager.onStop();
    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
