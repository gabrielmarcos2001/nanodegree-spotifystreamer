package com.gabilamnanodegree.spotifystreaming.model.interactors.artist;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.interactors.Interactor;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/4/15.
 */
public interface GetArtistsByName extends Interactor {

    interface Callback {
        void onArtistsFetched(List<AppArtist> appArtists);
        void onError(String error);
    }

    /**
     * Executes the interactor
     * This is an asynchronous operation so the
     * result is returned using a callback
     *
     * @param artistName
     * @param callback
     */
    void execute(String artistName, Callback callback);
}
