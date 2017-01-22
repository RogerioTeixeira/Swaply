package com.rogerio.tex.swaply;

import android.app.Application;
import android.util.Log;

import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by rogerio on 21/01/2017.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        //Use it only in debug builds
        if (BuildConfig.DEBUG) {
            Log.v("debugAttivo", "Inizia");
            AndroidDevMetrics.initWith(this);
        }
    }

}
