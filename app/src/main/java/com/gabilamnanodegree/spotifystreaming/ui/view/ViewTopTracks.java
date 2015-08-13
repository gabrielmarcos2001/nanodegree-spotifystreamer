package com.gabilamnanodegree.spotifystreaming.ui.view;

import android.support.v4.app.DialogFragment;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.ui.ViewBase;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public interface ViewTopTracks extends ViewBase {

    /**
     * Displays a list of tracks
     * @param appTracks
     */
    void showTracksList(List<AppTrack> appTracks);

    /**
     * Displays a message for no results
     */
    void showEmptyListMessage();

    /**
     * Displays a standard error message
     * @param error
     */
    void showErrorMessage(String error);

    /**
     * Shows a loader
     */
    void showLoader();

    /**
     * Hides the loader
     */
    void hideLoader();

    /**
     * Opens the music player
     * @param fragment
     */
    void openMusicPlayer(DialogFragment fragment);
}
