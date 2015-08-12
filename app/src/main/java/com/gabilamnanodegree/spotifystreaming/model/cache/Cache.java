package com.gabilamnanodegree.spotifystreaming.model.cache;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;

import java.util.List;

/**
 * Created by gabrielmarcos on 8/12/15.
 */
public interface Cache {

    /**
     * Caches the selected artist by the user
     * @param artist
     */
    void setSelectedArtist(AppArtist artist);

    /**
     * Returns the cached selected artist by the user
     * @return
     */
    AppArtist getSelectedArtist();

    /**
     * Caches the search terms
     * @param searchTerms
     */
    void setSearchTerms(String searchTerms);

    /**
     * Returns the search terms
     * @return
     */
    String getSearchTerms();

    /**
     * Caches the tracks
     * @param tracks
     */
    void setTracks(List<AppTrack> tracks);

    /**
     * Returns the cached tracks
     * @return
     */
    List<AppTrack> getTracks();

    /**
     * Caches the fetched artists
     * @param artists
     */
    void setArtistsFetched(List<AppArtist> artists);

    /**
     * Returns the fetched artists
     * @return
     */
    List<AppArtist> getArtistsFetched();

    /**
     * Clears the selected artist from cache
     */
    void clearSelectedArtist();

    /**
     * Clears the fetched artists from cache
     */
    void clearFetchedArtists();

}
