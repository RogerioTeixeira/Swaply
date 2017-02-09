package com.rogerio.tex.swaply.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rogerio.tex.swaply.Presenter;
import com.rogerio.tex.swaply.PresenterFactory;
import com.rogerio.tex.swaply.PresenterLoader;

import butterknife.ButterKnife;

/**
 * Created by Rogerio Lavoro on 09/02/2017.
 */

public abstract class GenericMVPActivity<T extends Presenter> extends AppCompatActivity implements MVPView, LoaderManager.LoaderCallbacks<T> {

    private T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
        getSupportLoaderManager().initLoader(getIdLoaderId(), null, this);
    }

    protected abstract int getLayoutResource();

    protected abstract int getIdLoaderId();

    protected abstract PresenterFactory<T> getFactoryPresenter();

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

    @Override
    public void onShowToast(String msg) {

    }

    @Override
    public void onShowToast(String msg, int duration) {

    }

    @Override
    public void onShowSnackbar(String msg, View parentView) {

    }

    @Override
    public void onShowSnackbar(String msg, View parentView, int duration) {

    }

    @Override
    public void onShowSnackbar(Snackbar snackbar) {

    }

    @Override
    public void onShowDialogProgress(String msg) {

    }

    @Override
    public void hideDialogProgress() {

    }

    @Override
    public Loader<T> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<T>(this, getFactoryPresenter());
    }

    @Override
    public void onLoadFinished(Loader<T> loader, T presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<T> loader) {
        this.presenter = null;
    }
}
