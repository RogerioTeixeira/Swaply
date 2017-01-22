package com.rogerio.tex.swaply.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.rogerio.tex.swaply.R;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override

    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {


    }
}
