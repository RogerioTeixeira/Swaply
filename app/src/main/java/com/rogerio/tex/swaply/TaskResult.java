package com.rogerio.tex.swaply;

/**
 * Created by Rogerio Lavoro on 27/01/2017.
 */

public class TaskResult<T> {

    private boolean isSuccessful;
    private T result;
    private Exception exception;
    private boolean hasResolution;

    public TaskResult(boolean isSuccessful, T result, Exception exception, boolean hasResolution) {
        this.isSuccessful = isSuccessful;
        this.result = result;
        this.exception = exception;
        this.hasResolution = hasResolution;
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    public T getResult() {
        return this.result;
    }

    public Exception getException() {
        return this.exception;
    }

    public boolean hasResolution() {
        return this.hasResolution;
    }

}
