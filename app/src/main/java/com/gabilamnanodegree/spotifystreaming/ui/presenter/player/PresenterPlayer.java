package com.gabilamnanodegree.spotifystreaming.ui.presenter.player;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.Presenter;
import com.gabilamnanodegree.spotifystreaming.ui.view.ViewPlayer;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/23/15.
 */
public interface PresenterPlayer extends Presenter<ViewPlayer> {


    /**
     * Button Next track was clicked
     */
    void nextClicked();

    /**
     * Button Prev track was clicked
     */
    void prevClicked();

    /**
     * Button play was clicked
     */
    void playClicked();

    /**
     * Button pause was clicked
     */
    void pauseClicked();

    /**
     * Seek progress bar was updated
     * @param pogress
     */
    void seekTo(int pogress);

    /**
     * Share Button was clicked
     */
    void shareClicked();

    /**
     * Sets the list of tracks
     * @param tracks
     */
    void setData(List<AppTrack> tracks, int selectedIndex, AppArtist artist);

}
