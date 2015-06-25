package com.gabilamnanodegree.spotifystreaming.ui.presenter.topTracks;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetTopTracksForArtist;
import com.gabilamnanodegree.spotifystreaming.model.repository.RepositoryTracks;
import com.gabilamnanodegree.spotifystreaming.ui.fragment.FragmentMusicPlayer;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.player.PresenterPlayer;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.player.PresenterPlayerImp;
import com.gabilamnanodegree.spotifystreaming.ui.view.ViewTopTracks;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.PresenterBase;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 *
 * Implementation of the Top Tracks Presenter interface
 * This Presenter handles the logic for the TopTracks View
 *
 */
public class PresenterTopTracksImp extends PresenterBase implements PresenterTopTracks, GetTopTracksForArtist.Callback {

    private ViewTopTracks mView;
    private GetTopTracksForArtist mGetTopTracksInteractor;
    private List<AppTrack> mTracks;

    /**
     * Constructor
     * @param mContext
     */
    public PresenterTopTracksImp(Context mContext, GetTopTracksForArtist interactor) {
        super(mContext);
        this.mGetTopTracksInteractor = interactor;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onViewDestroy() {
        this.mView = null;
    }

    @Override
    public void searchTopTracksByArtist(String artistId) {

        if (mView == null) return;

        mView.showLoader();
        this.mGetTopTracksInteractor.execute(artistId,this);
    }

    @Override
    public void trackSelected(int position, AppArtist artist) {

        FragmentMusicPlayer musicPlayerFragment = FragmentMusicPlayer.newInstance();

        // Sends the Data to the player fragment
        musicPlayerFragment.setArtist(artist);
        musicPlayerFragment.setmTracks(mTracks);
        musicPlayerFragment.setmInitialTrackIndex(position);

        mView.openMusicPlayer(musicPlayerFragment);

    }

    @Override
    public void setView(ViewTopTracks view) {
        this.mView = view;
    }

    @Override
    public void onTopTracksFetched(List<AppTrack> appTracks) {

        this.mTracks = appTracks;

        if (mView == null) return;

        this.mView.showTracksList(appTracks);

        mView.hideLoader();

        if (appTracks.size() == 0) {
            mView.showEmptyListMessage();
        }
    }

    @Override
    public void onError(String error) {

        if (mView == null) return;

        this.mView.showErrorMessage(error);
        mView.hideLoader();
    }
}
