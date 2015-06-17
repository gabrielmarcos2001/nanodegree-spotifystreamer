package com.gabilamnanodegree.spotifystreaming.ui.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public class BaseFragment extends Fragment {

    private Toast mToast;

    /**
     * Displays an Error message using a Toast
     * @param error
     */
    public void showErrorMessage(String error) {

        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT);
        mToast.show();
    }


}
