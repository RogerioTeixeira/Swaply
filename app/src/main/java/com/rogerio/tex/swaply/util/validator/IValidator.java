package com.rogerio.tex.swaply.util.validator;

/**
 * Created by Rogerio Lavoro on 19/12/2016.
 */

public interface IValidator<T> {
    void validate(T arg) throws ValidationException;

}
