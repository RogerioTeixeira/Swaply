package com.rogerio.tex.swaply.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by rogerio on 06/01/2017.
 */

public class BaseDialog extends DialogFragment {
    protected FragmentHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        helper = new FragmentHelper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.dismissDialog();

    }
}
