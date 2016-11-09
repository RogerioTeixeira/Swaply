package com.rogerio.tex.swaply;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.rogerio.tex.swaply.fragment.BaseLoginFragment;

public class LoginActivity extends AppCompatActivity implements BaseLoginFragment.LoginCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onConnected(Task<AuthResult> task) {
        Log.v("LoginActivity", "Chiamata onConnected");
    }
}
