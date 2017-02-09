package com.rogerio.tex.swaply;

/**
 * Created by Rogerio Lavoro on 09/02/2017.
 */

public interface PresenterFactory<T extends Presenter> {
    T Create();
}
