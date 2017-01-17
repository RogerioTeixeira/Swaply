package com.rogerio.tex.swaply.ui.helper;

import android.app.Activity;
import android.content.Intent;


/**
 * Created by rogerio on 07/01/2017.
 */

public class ActivityHelper extends BaseHelper {
    protected Activity activity;

    public ActivityHelper(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void finishActivity(int resultCode, Intent intent) {
        activity.setResult(resultCode, intent);
        activity.finish();
    }
}
