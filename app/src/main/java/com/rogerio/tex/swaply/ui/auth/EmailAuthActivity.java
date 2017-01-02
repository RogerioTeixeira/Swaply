package com.rogerio.tex.swaply.ui.auth;

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
import com.rogerio.tex.swaply.ui.BaseActivity;
import com.rogerio.tex.swaply.ui.auth.fragment.CreateAccountFragment;
import com.rogerio.tex.swaply.ui.auth.fragment.EmailLoginFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EmailAuthActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EmailLoginFragment(), "Accedi");
        adapter.addFragment(new CreateAccountFragment(), "Crea Account");
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pager.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tablayout.setupWithViewPager(pager);
        setToobarTitle(tablayout.getSelectedTabPosition());
        tablayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setToobarTitle(tab.getPosition());
            }
        });

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

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                return new EmailLoginFragment();
            } else {
                return new CreateAccountFragment();
            }


            //return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            Log.v("AdapterFra", "object:" + object.toString());
            return POSITION_NONE;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
