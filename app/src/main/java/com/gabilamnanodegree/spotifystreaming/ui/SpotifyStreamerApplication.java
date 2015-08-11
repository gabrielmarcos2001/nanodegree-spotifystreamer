package com.gabilamnanodegree.spotifystreaming.ui;

import android.app.Application;

import com.gabilamnanodegree.spotifystreaming.R;

/**
 * Created by gabrielmarcos on 8/11/15.
 */
public class SpotifyStreamerApplication extends Application {

    public static boolean mIsLargeLayout = false;
    public static final String DEFAULT_COUNTRY = "US"; // Sorry for this hardcode :(

    @Override
    public void onCreate() {
        super.onCreate();

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

    }
}
