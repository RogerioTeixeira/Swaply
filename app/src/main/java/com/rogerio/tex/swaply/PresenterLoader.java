package com.rogerio.tex.swaply;

import android.content.Context;
import android.support.v4.content.Loader;

/**
 * Created by Rogerio Lavoro on 09/02/2017.
 */

public class PresenterLoader<T extends Presenter> extends Loader<T> {

    private final PresenterFactory<T> presenterFactory;
    private T presenter;

    public PresenterLoader(Context context, PresenterFactory<T> presenterFactory) {
        super(context);
        this.presenterFactory = presenterFactory;
    }

    @Override
    protected void onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        onForceLoad();
    }

    @Override
    protected void onForceLoad() {
        presenter = presenterFactory.Create();
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter.onDestroyed();
        presenter = null;
    }
}
