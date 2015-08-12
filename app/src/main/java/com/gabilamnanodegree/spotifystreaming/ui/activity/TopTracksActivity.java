package com.gabilamnanodegree.spotifystreaming.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetTopTracksForArtistInteractor;
import com.gabilamnanodegree.spotifystreaming.model.repository.RepositoryTracksImp;
import com.gabilamnanodegree.spotifystreaming.ui.SpotifyStreamerApplication;
import com.gabilamnanodegree.spotifystreaming.ui.fragment.TopTracksFragment;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.PresenterBase;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.topTracks.PresenterTopTracks;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.topTracks.PresenterTopTracksImp;

/**
 * Created by gabrielmarcos on 6/1/15.
 *
 * Activity which hosts the Top Tracks Fragment
 *
 */
public class TopTracksActivity extends BaseActivity{

    private static PresenterTopTracks mPresenter;

    private static final String FRAGMENT_TAG = "top_tracks_fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle data = getIntent().getExtras();
        AppArtist artist = data.getParcelable("artist");

        setContentView(R.layout.activity_single_container);

        Fragment topTracksFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (mPresenter == null) {
            // We create a new Presenter, The Interactor and the Repository could be injected with a dependency injection framework
            mPresenter = new PresenterTopTracksImp(this, new GetTopTracksForArtistInteractor(new RepositoryTracksImp(this))); //new GetArtistsByNameInteractor(new RepositoryArtistImp(this)));
        }else {
            mPresenter.setContext(this);
        }

        if (topTracksFragment == null) {
            topTracksFragment = TopTracksFragment.newInstance();
        }

        // sets the data to the fragment
        ((TopTracksFragment)topTracksFragment).setmArtist(artist);
        ((TopTracksFragment)topTracksFragment).setmPresenter(mPresenter);

        // Updates the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, topTracksFragment, FRAGMENT_TAG).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cleans up the presenter object
        if (isFinishing()) {
            mPresenter = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // This activity should not be visible on tablet if we are in landscape mode
        if (SpotifyStreamerApplication.mIsLargeLayout && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            onBackPressed();
        }
    }
}
