package com.rogerio.tex.swaply;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by rogerio on 21/01/2017.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
