package com.rogerio.tex.swaply.view.auth.fragment;

import android.content.Context;

import com.rogerio.tex.swaply.provider.UserResult;
import com.rogerio.tex.swaply.view.BaseFragment;

/**
 * Created by Rogerio Lavoro on 18/01/2017.
 */

public abstract class BaseEmaiFragment extends BaseFragment {

    protected EmailAuthListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EmailAuthListener) {
            listener = (EmailAuthListener) context;
        } else {
            throw new IllegalStateException("Activity must implement EmailAuthListener");
        }
    }

    public interface EmailAuthListener {
        /**
         * Email entered belongs to an existing email user.
         */
        void onExistingEmailUser(UserResult result);

        /**
         * Email entered belongs to an existing IDP user.
         */
        void onExistingIdpUser(UserResult result);

        /**
         * Email entered does not beling to an existing user.
         */
        void succesLogin(UserResult result);

    }
}
