package com.gabilamnanodegree.spotifystreaming.ui.presenter.topTracks;

import android.content.Context;

import com.gabilamnanodegree.spotifystreaming.model.cache.SPCacheImp;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetTopTracksForArtist;
import com.gabilamnanodegree.spotifystreaming.ui.fragment.FragmentMusicPlayer;
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

    public interface TopTracksInterface {
        void trackSelected(AppArtist artist, List<AppTrack> tracks, int position);
    }

    private TopTracksInterface mInterface;
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

        if (mInterface != null) {

            mInterface.trackSelected(artist, mTracks, position);

        }else {

            FragmentMusicPlayer musicPlayerFragment = FragmentMusicPlayer.newInstance();

            // Sends the Data to the player fragment
            musicPlayerFragment.setArtist(artist);
            musicPlayerFragment.setmTracks(mTracks);
            musicPlayerFragment.setmInitialTrackIndex(position);


            mView.openMusicPlayer(musicPlayerFragment);
        }

    }

    @Override
    public void setView(ViewTopTracks view) {
        this.mView = view;

        mTracks = new SPCacheImp().getTracks();

        if (this.mView != null && mTracks != null) this.mView.showTracksList(mTracks);
    }

    @Override
    public void onTopTracksFetched(List<AppTrack> appTracks) {

        new SPCacheImp().setTracks(appTracks);

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

    @Override
    public void setInterface(TopTracksInterface callback) {
        this.mInterface = callback;
    }

}
