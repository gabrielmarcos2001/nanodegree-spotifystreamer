package com.gabilamnanodegree.spotifystreaming.ui.view;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.ui.ViewBase;


/**
 * Created by gabrielmarcos on 6/23/15.
 */
public interface ViewPlayer extends ViewBase {

    /**
     * Updates the song progress indicator
     * @param progress
     * @param totalDuration
     * @param currentDuration
     */
    void updateProgress(int progress, String totalDuration, String currentDuration);

    /**
     * Intilize the view in a status where the song
     * ins playing
     */
    void initInPlayingMode();

    /**
     * Initialize the view in a status
     * where the song is not playing
     */
    void initInStopMode();

    /**
     * Asks the view for the currently selected track
     * @return
     */
    AppTrack getSelectedTrack();

}
