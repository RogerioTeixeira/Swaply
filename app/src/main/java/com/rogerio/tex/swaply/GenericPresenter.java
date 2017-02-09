package com.rogerio.tex.swaply;

import com.rogerio.tex.swaply.view.GenericMVPActivity;

/**
 * Created by Rogerio Lavoro on 09/02/2017.
 */

public class GenericPresenter<T extends GenericMVPActivity> implements Presenter<T> {

    private T view;

    @Override
    public void onViewAttached(T view) {
        this.view = view;
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onDestroyed() {

    }
}
