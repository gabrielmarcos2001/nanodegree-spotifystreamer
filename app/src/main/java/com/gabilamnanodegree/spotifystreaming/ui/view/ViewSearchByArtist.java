package com.gabilamnanodegree.spotifystreaming.ui.view;

import android.support.v4.app.ActivityOptionsCompat;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.ui.ViewBase;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public interface ViewSearchByArtist extends ViewBase {

    void showArtistsList(List<AppArtist> appArtists);
    void showEmptyListMessage();
    void showEmptyQueryMessage();
    void showErrorMessage(String error);
    void showLoader();
    void hideLoader();

}
