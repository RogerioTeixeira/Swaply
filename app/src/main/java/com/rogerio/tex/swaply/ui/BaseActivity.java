package com.rogerio.tex.swaply.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected ActivityHelper helper;

    protected abstract int getLayoutResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        helper = new ActivityHelper(this);
        ButterKnife.bind(this);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.dismissDialog();
    }


}
