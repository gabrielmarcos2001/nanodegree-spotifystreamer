package com.gabilamnanodegree.spotifystreaming.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.gabilamnanodegree.spotifystreaming.R;

/**
 * Created by gabrielmarcos on 8/13/15.
 */
public class SettingsActivity extends PreferenceActivity {

    public static final String KEY_AUTO_PLAY = "pref_auto_play";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
