package com.gabilamnanodegree.spotifystreaming.ui.presenter;

import android.content.Context;

import com.gabilamnanodegree.spotifystreaming.ui.ViewBase;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public interface Presenter <T extends ViewBase> {

    void initialize();

    void onViewDestroy();

    void setView(T view);

    void setContext(Context context);
}
