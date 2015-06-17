package com.gabilamnanodegree.spotifystreaming.ui.presenter;

import android.content.Context;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public class PresenterBase {

    protected Context mContext;

    public PresenterBase(Context mContext) {
        this.mContext = mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
