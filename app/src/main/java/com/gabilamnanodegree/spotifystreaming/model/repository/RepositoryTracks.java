package com.gabilamnanodegree.spotifystreaming.model.repository;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetTopTracksForArtist;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public interface RepositoryTracks extends Repository {

    /**
     * Gets a list of top tracks by artist id
     * This method is not async so it must be
     * called using an async task
     *
     * We are using it from the GetTopTracksForArtist Interactor which works with
     * an Async Task
     * @param artistId of the artsit
     */
    List<AppTrack> get(String artistId);

    /**
     * Fetches the list of top tracks for an artist id
     * @param artistId
     * @param callback
     */
    void getAsync(String artistId, GetTopTracksForArtist.Callback callback);
}
