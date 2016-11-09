package com.rogerio.tex.swaply.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rogerio.tex.swaply.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterLoginFragment extends BaseLoginFragment {


    public TwitterLoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {

        return R.layout.fragment_twitter_login;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_twitter_login, container, false);
    }

}
