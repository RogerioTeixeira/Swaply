package com.rogerio.tex.swaply.fragment;


import android.support.v4.app.Fragment;
import android.widget.Button;

import com.rogerio.tex.swaply.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnonymouslyLoginFragment extends BaseLoginFragment {


    @BindView(R.id.button_skip)
    Button buttonSkip;

    public AnonymouslyLoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_anonymously_login;
    }


    @OnClick(R.id.button_skip)
    public void onClick() {
        signInAnonymous();
    }
}
