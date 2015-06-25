package com.gabilamnanodegree.spotifystreaming.ui.view;

import android.support.v4.app.DialogFragment;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.ui.ViewBase;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public interface ViewTopTracks extends ViewBase {

    void showTracksList(List<AppTrack> appTracks);
    void showEmptyListMessage();
    void showErrorMessage(String error);
    void showLoader();
    void hideLoader();
    void openMusicPlayer(DialogFragment fragment);
}
