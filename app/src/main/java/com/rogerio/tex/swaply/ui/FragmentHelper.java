package com.rogerio.tex.swaply.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Rogerio Lavoro on 04/01/2017.
 */

public class FragmentHelper extends BaseHelper {

    private Fragment fragment;

    public FragmentHelper(Fragment fragment) {
        super(fragment.getContext());
        this.fragment = fragment;
    }

    public void finish(int resultCode, Intent intent) {
        finishActivity(fragment.getActivity(), resultCode, intent);
    }
}
