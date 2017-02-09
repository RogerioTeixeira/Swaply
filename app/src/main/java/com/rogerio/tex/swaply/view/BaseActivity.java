package com.rogerio.tex.swaply.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rogerio.tex.swaply.view.helper.ActivityHelper;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityHelper helper;

    protected abstract int getLayoutResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutResource() != 0) {
            setContentView(getLayoutResource());
            ButterKnife.bind(this);
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (helper != null) {
            helper.dismissDialog();
        }
    }

    protected ActivityHelper getActivityHelper() {
        if (helper == null) {
            helper = new ActivityHelper(this);
        }
        return helper;
    }


}
