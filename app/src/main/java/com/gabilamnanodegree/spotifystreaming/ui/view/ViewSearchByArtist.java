package com.gabilamnanodegree.spotifystreaming.ui.view;

import android.support.v4.app.ActivityOptionsCompat;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.ui.ViewBase;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public interface ViewSearchByArtist extends ViewBase {

    /**
     * Displays a list of artists
     * @param appArtists
     */
    void showArtistsList(List<AppArtist> appArtists);

    /**
     * Displays a message for empty results
     */
    void showEmptyListMessage();

    /**
     * Displays a message for empty query
     */
    void showEmptyQueryMessage();

    /**
     * Displays a standar error message
     * @param error
     */
    void showErrorMessage(String error);

    /**
     * Displays a loader
     */
    void showLoader();

    /**
     * Hides the loader
     */
    void hideLoader();

}
