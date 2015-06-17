package com.gabilamnanodegree.spotifystreaming.model.repository;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetArtistsByName;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public interface RepositoryArtist extends Repository {

    /**
     * Gets a list of artists by its name
     * This method is not async so it must be
     * called using an async task
     * We are using it from the GetArtistByName Interactor which works with
     * an Async Task
     * @param name of the artsis
     */
    List<AppArtist> get(String name);

    /**
     * Gets a list of artists asynchronously
     * The callback result is returned in the
     * main thread
     *
     * We are not currently using it, but could be used instead of the async task and
     * interactor
     *
     * @param name of the artist
     * @param callback
     */
    void getAsync(String name, GetArtistsByName.Callback callback);
}
