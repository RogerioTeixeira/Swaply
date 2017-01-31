package com.rogerio.tex.swaply;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Rogerio Lavoro on 27/01/2017.
 */

public class TaskResult<T extends Parcelable> implements Parcelable {

    public static final Parcelable.Creator<TaskResult> CREATOR = new Parcelable.Creator<TaskResult>() {
        @Override
        public TaskResult createFromParcel(Parcel source) {
            return new TaskResult(source);
        }

        @Override
        public TaskResult[] newArray(int size) {
            return new TaskResult[size];
        }
    };
    private final static String TAG = "TaskResult";
    private boolean isSuccessful;
    private T result;
    private Exception exception;
    private boolean hasResolution;


    private TaskResult(boolean isSuccessful, T result, Exception exception, boolean hasResolution) {
        this.isSuccessful = isSuccessful;
        this.result = result;
        this.exception = exception;
        this.hasResolution = hasResolution;
    }

    protected TaskResult(Parcel in) {
        this.isSuccessful = in.readByte() != 0;
        if (in.readInt() == 0) {
            this.result = null;
        } else {
            String className = in.readString();
            ClassLoader classloader;
            try {
                classloader = Class.forName(className).getClassLoader();
                this.result = in.readParcelable(classloader);
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "read parcel", e);
            }
        }
        this.exception = (Exception) in.readSerializable();
        this.hasResolution = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSuccessful ? (byte) 1 : (byte) 0);

        if (this.result == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            dest.writeString(this.result.getClass().getName());
            dest.writeParcelable(this.result, flags);
        }

        dest.writeSerializable(this.exception);
        dest.writeByte(this.hasResolution ? (byte) 1 : (byte) 0);
    }

    public static class Builder<T extends Parcelable> {

        @NonNull
        private boolean isSuccessful;

        private T result;

        private Exception exception;

        private boolean hasResolution;

        private Builder() {

        }


        public static Builder create() {
            return new Builder();
        }

        public Builder setSuccessful(boolean successful) {
            isSuccessful = successful;
            return this;
        }

        public Builder setResult(T result) {
            this.result = result;
            return this;
        }

        public Builder setException(Exception exception) {
            this.exception = exception;
            return this;
        }

        public Builder setHasResolution(boolean hasResolution) {
            this.hasResolution = hasResolution;
            return this;
        }

        public TaskResult<T> build() {
            return new TaskResult<T>(isSuccessful, result, exception, hasResolution);
        }
    }
}
