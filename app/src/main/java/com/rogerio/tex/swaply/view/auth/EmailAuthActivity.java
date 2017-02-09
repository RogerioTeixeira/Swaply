package com.rogerio.tex.swaply.view.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.rogerio.tex.swaply.OnCompleteListener;
import com.rogerio.tex.swaply.R;
import com.rogerio.tex.swaply.TaskResult;
import com.rogerio.tex.swaply.adapter.ViewPagerAdapter;
import com.rogerio.tex.swaply.provider.LoginProviderManager;
import com.rogerio.tex.swaply.provider.UserResult;
import com.rogerio.tex.swaply.view.BaseActivity;
import com.rogerio.tex.swaply.view.auth.fragment.BaseEmaiFragment;
import com.rogerio.tex.swaply.view.auth.fragment.CreateAccountFragment;
import com.rogerio.tex.swaply.view.auth.fragment.EmailLoginFragment;

import butterknife.BindView;

public class EmailAuthActivity extends BaseActivity implements BaseEmaiFragment.EmailAuthListener {

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

    public static Intent createResultIntent(UserResult result) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAM_ID, result);
        return intent;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, EmailAuthActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static UserResult getResultData(Intent intent) {
        UserResult response = intent.getParcelableExtra(EXTRA_PARAM_ID);
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
    public void onExistingEmailUser(UserResult result) {
        Log.v("onExistingEmailUser", "onExistingEmailUser");
        pager.setCurrentItem(0);
        ViewPagerAdapter adapter = (ViewPagerAdapter) pager.getAdapter();
        if (adapter.getItem(0) instanceof EmailLoginFragment) {
            EmailLoginFragment fragment = (EmailLoginFragment) adapter.getItem(0);
            fragment.setEmail(result.getEmail());
        }
    }

    @Override
    public void onExistingIdpUser(UserResult result) {
        String providerId = result.getProvideData();
        providerManager.startLogin(providerId, this, new OnCompleteListener<TaskResult<UserResult>>() {
            @Override
            public void onComplete(TaskResult<UserResult> args) {
                if (args.isSuccessful()) {
                    getActivityHelper().finishActivity(Activity.RESULT_OK, createResultIntent(args.getResult()));
                } else {
                    Toast.makeText(EmailAuthActivity.this, args.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void succesLogin(UserResult result) {
        getActivityHelper().finishActivity(Activity.RESULT_OK, createResultIntent(result));
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
}
