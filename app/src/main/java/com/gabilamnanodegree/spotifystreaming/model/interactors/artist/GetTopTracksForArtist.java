package com.gabilamnanodegree.spotifystreaming.model.interactors.artist;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.model.interactors.Interactor;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/15/15.
 */
public interface GetTopTracksForArtist extends Interactor{

    interface Callback {
        void onTopTracksFetched(List<AppTrack> appTracks);
        void onError(String error);
    }

    /**
     * Executes the interactor
     * This is an asynchronous operation so the
     * result is returned using a callback
     *
     * @param artistId
     * @param callback
     */
    void execute(String artistId, Callback callback);
}
