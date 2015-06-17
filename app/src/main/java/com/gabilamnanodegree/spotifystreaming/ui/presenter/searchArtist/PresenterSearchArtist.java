package com.gabilamnanodegree.spotifystreaming.ui.presenter.searchArtist;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.ui.view.ViewSearchByArtist;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.Presenter;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public interface PresenterSearchArtist extends Presenter<ViewSearchByArtist> {

    /**
     * Searches an artist by its name
     * @param name
     */
    void searchArtistByName(String name);

    /**
     * An Artist was selected by the user
     * @param artist
     */
    void artistSelected(AppArtist artist);

}
