package com.rogerio.tex.swaply.view;

import android.support.v4.app.DialogFragment;

import com.rogerio.tex.swaply.view.helper.ActivityHelper;

/**
 * Created by rogerio on 06/01/2017.
 */

public class BaseDialog extends DialogFragment {
    private ActivityHelper helper;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (helper != null) {
            helper.dismissDialog();
        }

    }

    protected ActivityHelper getActivityHelper() {
        if (helper == null) {
            helper = new ActivityHelper(getActivity());
        }
        return helper;
    }
}
