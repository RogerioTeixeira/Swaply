package com.rogerio.tex.swaply;

/**
 * Created by Rogerio Lavoro on 09/02/2017.
 */

public interface Presenter<V> {
    void onViewAttached(V view);

    void onViewDetached();

    void onDestroyed();
}
