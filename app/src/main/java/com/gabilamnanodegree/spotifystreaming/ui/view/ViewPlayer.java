package com.gabilamnanodegree.spotifystreaming.ui.view;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.ui.ViewBase;


/**
 * Created by gabrielmarcos on 6/23/15.
 */
public interface ViewPlayer extends ViewBase {

    void selectTrackByIndex(int index);
    void updateProgress(int progress, String totalDuration, String currentDuration);

}
