package com.rogerio.tex.swaply.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rogerio.tex.swaply.R;

public class FacebookLoginFragment extends BaseLoginFragment {


    public FacebookLoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FacebookLoginFragment newInstance() {
        FacebookLoginFragment fragment = new FacebookLoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getFragmentLayout(){
        return R.layout.fragment_facebook_login;
    }

}
