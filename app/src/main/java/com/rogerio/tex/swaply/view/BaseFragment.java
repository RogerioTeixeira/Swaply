package com.rogerio.tex.swaply.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rogerio.tex.swaply.view.helper.ActivityHelper;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {
    private ActivityHelper helper;

    public BaseFragment() {
        // Required empty public constructor
    }

    protected abstract int getFragmentLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

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
