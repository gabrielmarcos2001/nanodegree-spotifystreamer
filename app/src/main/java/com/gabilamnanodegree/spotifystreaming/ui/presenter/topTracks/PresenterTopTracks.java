package com.gabilamnanodegree.spotifystreaming.ui.presenter.topTracks;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.ui.view.ViewTopTracks;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.Presenter;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public interface PresenterTopTracks extends Presenter<ViewTopTracks> {

    /**
     * Searches for the top tracks by artist id
     * @param artistId
     */
    void searchTopTracksByArtist(String artistId);

    /**
     * A Track was selected by the user
     * @param position
     */
    void trackSelected(int position, AppArtist artist);

    /**
     * Sets the actions interface in case we want to handle actions from
     * outside the fragment
     * @param callback
     */
    void setInterface(PresenterTopTracksImp.TopTracksInterface callback);

}
