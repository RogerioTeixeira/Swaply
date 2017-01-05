package com.rogerio.tex.swaply;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;

/**
 * Created by Rogerio Lavoro on 05/01/2017.
 */

public class TaskFailureLogger implements OnFailureListener {
    private String tag;
    private String message;

    public TaskFailureLogger(String tag, String message) {
        this.tag = tag;
        this.message = message;
    }


    @Override
    public void onFailure(@NonNull Exception e) {
        Log.e(tag, message, e);
    }
}
