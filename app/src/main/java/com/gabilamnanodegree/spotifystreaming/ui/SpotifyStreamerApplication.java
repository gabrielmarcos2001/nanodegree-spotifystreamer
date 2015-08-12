package com.gabilamnanodegree.spotifystreaming.ui;

import android.app.Application;

import com.gabilamnanodegree.spotifystreaming.R;

/**
 * Created by gabrielmarcos on 8/11/15.
 */
public class SpotifyStreamerApplication extends Application {

    public static boolean mIsLargeLayout = false;
    public static final String DEFAULT_COUNTRY = "US"; // Sorry for this hardcode :(

    private static SpotifyStreamerApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

    }

    public static SpotifyStreamerApplication getInstance() {
        return INSTANCE;
    }
}
