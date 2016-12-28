package com.rogerio.tex.validator.rule;

/**
 * Created by Rogerio Lavoro on 19/12/2016.
 */

public interface Rule<T> {
    boolean isValid(T arg);
    String getErrorMessage();

}
