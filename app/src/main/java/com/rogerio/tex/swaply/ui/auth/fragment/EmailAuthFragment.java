package com.rogerio.tex.swaply.ui.auth.fragment;

import android.content.Context;

import com.rogerio.tex.swaply.provider.AuthResponse;
import com.rogerio.tex.swaply.ui.BaseFragment;

/**
 * Created by Rogerio Lavoro on 18/01/2017.
 */

public abstract class EmailAuthFragment extends BaseFragment {

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
        void onExistingEmailUser(AuthResponse response);

        /**
         * Email entered belongs to an existing IDP user.
         */
        void onExistingIdpUser(AuthResponse response);

        /**
         * Email entered does not beling to an existing user.
         */
        void succesLogin(AuthResponse response);

    }
}
