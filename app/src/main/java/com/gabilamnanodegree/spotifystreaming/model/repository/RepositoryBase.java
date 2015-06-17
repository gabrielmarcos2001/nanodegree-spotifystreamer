package com.gabilamnanodegree.spotifystreaming.model.repository;

import android.content.Context;

/**
 * Created by gabrielmarcos on 6/2/15.
 */
public abstract class RepositoryBase {

    protected Context mContext;

    public RepositoryBase(Context mContext) {
        this.mContext = mContext;
    }
}
