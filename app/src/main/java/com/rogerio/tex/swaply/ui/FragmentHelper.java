package com.rogerio.tex.swaply.ui;

import android.support.v4.app.Fragment;

/**
 * Created by Rogerio Lavoro on 04/01/2017.
 */

public class FragmentHelper extends ActivityHelper {

    private Fragment fragment;

    public FragmentHelper(Fragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
    }
}
