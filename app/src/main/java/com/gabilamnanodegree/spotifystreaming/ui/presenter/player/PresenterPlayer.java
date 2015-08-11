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
     * Plays the track specified
     * @param track
     */
    void playTrack(AppTrack track);

    /**
     * Stops the current track
     */
    void stopCurrentTrack();

    /**
     * Pauses the current track
     */
    void pauseCurrentTrack();

    /**
     * Resumes the current track
     */
    void resumeCurrentTrack();

    /**
     * Seek progress bar was updated
     * @param pogress
     */
    void seekTo(int pogress);

    /**
     * Share Button was clicked
     */
    void shareTrack(AppTrack track);

    /**
     * Returns the current playing track - null if none
     * @return
     */
    AppTrack getCurrentPlayingTrack();

}
