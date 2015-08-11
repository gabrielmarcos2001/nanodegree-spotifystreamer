package com.gabilamnanodegree.spotifystreaming.ui;

import android.app.Application;

import com.gabilamnanodegree.spotifystreaming.R;

/**
 * Created by gabrielmarcos on 8/11/15.
 */
public class SpotifyStreamerApplication extends Application {

    public static boolean mIsLargeLayout = false;

    @Override
    public void onCreate() {
        super.onCreate();

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

    }
}
