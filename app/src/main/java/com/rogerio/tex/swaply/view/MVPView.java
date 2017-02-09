package com.rogerio.tex.swaply.view;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Rogerio Lavoro on 09/02/2017.
 */

public interface MVPView {
    void onShowToast(String msg);

    void onShowToast(String msg, int duration);

    void onShowSnackbar(String msg, View parentView);

    void onShowSnackbar(String msg, View parentView, int duration);

    void onShowSnackbar(Snackbar snackbar);

    void onShowDialogProgress(String msg);

    void hideDialogProgress();
}
