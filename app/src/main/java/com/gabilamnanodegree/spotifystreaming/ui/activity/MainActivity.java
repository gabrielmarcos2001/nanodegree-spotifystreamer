package com.gabilamnanodegree.spotifystreaming.ui.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetArtistsByNameInteractor;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetTopTracksForArtistInteractor;
import com.gabilamnanodegree.spotifystreaming.model.repository.RepositoryArtistImp;
import com.gabilamnanodegree.spotifystreaming.model.repository.RepositoryTracksImp;
import com.gabilamnanodegree.spotifystreaming.ui.SpotifyStreamerApplication;
import com.gabilamnanodegree.spotifystreaming.ui.components.ViewSearchArtistHeader;
import com.gabilamnanodegree.spotifystreaming.ui.fragment.SearchArtistFragment;
import com.gabilamnanodegree.spotifystreaming.ui.fragment.TopTracksFragment;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.searchArtist.PresenterSearchArtist;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.searchArtist.PresenterSearchArtistImp;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.topTracks.PresenterTopTracks;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.topTracks.PresenterTopTracksImp;

/**
 * Activity which hosts the Search Artist Fragment
 */
public class MainActivity extends BaseActivity {

    /**
     * We make the presenter static so it does not get recreated on configuration
     * changes - the presenter will be cleaned when the activity is destroyed.
     */
    private static PresenterSearchArtist mSearchArtistPresenter;
    private static PresenterTopTracks mTopTracksPresenter;

    private static final String FRAGMENT_SEARCH_ARTIST_TAG = "search_artist_fragment";
    private static final String FRAGMENT_TOP_TRACKS_TAG = "top_tracks_fragment";

    private ViewSearchArtistHeader mHeader;

    private SearchArtistFragment mSearchArtistFragment; // Reference to the search artist fragment
    private TopTracksFragment mTopTracksFragment; // Reference to the top tracks fragment

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If the app is running in a tablet it sets the dual container layout
        // for displaying the two fragments in the same activity
        if (SpotifyStreamerApplication.mIsLargeLayout) {
            setContentView(R.layout.activity_dual_container);
        }else {
            setContentView(R.layout.activity_single_container);
        }

        // Initialize the activity for tablet layouts
        if (SpotifyStreamerApplication.mIsLargeLayout) {

            // Gets a reference to the header view
            mHeader = (ViewSearchArtistHeader)findViewById(R.id.header);

            // Searches for an instance of the search artist fragment
            mSearchArtistFragment = (SearchArtistFragment)getSupportFragmentManager().findFragmentByTag(FRAGMENT_SEARCH_ARTIST_TAG);

            // Searches for an instance of the top tracks fragment
            mTopTracksFragment = (TopTracksFragment)getSupportFragmentManager().findFragmentByTag(FRAGMENT_TOP_TRACKS_TAG);

            // Handles the presenter for the search artist fragment
            if (mSearchArtistPresenter == null) {
                // We create a new Presenter, The Interactor and the Repository could be injected with a dependency injection framework
                mSearchArtistPresenter = new PresenterSearchArtistImp(this, new GetArtistsByNameInteractor(new RepositoryArtistImp(this)));
            }else {
                mSearchArtistPresenter.setContext(this);
            }

            mSearchArtistPresenter.setInterface(new PresenterSearchArtistImp.SearchArtistInterface() {
                @Override
                public void artistSelected(AppArtist artist) {
                    mTopTracksFragment.setmArtist(artist);
                }
            });

            // Handles the presenter for the top tracks fragment
            if (mTopTracksPresenter == null) {
                // We create a new Presenter, The Interactor and the Repository could be injected with a dependency injection framework
                mTopTracksPresenter = new PresenterTopTracksImp(this, new GetTopTracksForArtistInteractor(new RepositoryTracksImp(this)));
            }else {
                mTopTracksPresenter.setContext(this);
            }

            // Creates a new instance of the top tracks fragment if needed
            if (mTopTracksFragment == null) {
                mTopTracksFragment = (TopTracksFragment)TopTracksFragment.newInstance();
            }

            // sets the data to the fragment
            mTopTracksFragment.setmPresenter(mTopTracksPresenter);
            mTopTracksFragment.setmArtist(null);

            // Creates a new instance of the search artist fragment if needed
            if (mSearchArtistFragment == null) {
                mSearchArtistFragment = (SearchArtistFragment)SearchArtistFragment.newInstance();
            }

            mSearchArtistFragment.setmPresenter(mSearchArtistPresenter);

            mHeader.setmInterface(new ViewSearchArtistHeader.SearchArtistHeaderActions() {
                @Override
                public void artistNameChanged(String artistName) {

                    // Clears the list of tracks before sending the data to the
                    // search artist fragment
                    mTopTracksFragment.setmArtist(null);
                    mSearchArtistFragment.artistNameChanged(artistName);
                }
            });

            // Updates the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mSearchArtistFragment, FRAGMENT_SEARCH_ARTIST_TAG).commit();

            fragmentManager.beginTransaction()
                    .replace(R.id.container_2, mTopTracksFragment, FRAGMENT_TOP_TRACKS_TAG).commit();

        }else {

            mSearchArtistFragment = (SearchArtistFragment)getSupportFragmentManager().findFragmentByTag(FRAGMENT_SEARCH_ARTIST_TAG);
            mTopTracksFragment = (TopTracksFragment)getSupportFragmentManager().findFragmentByTag(FRAGMENT_TOP_TRACKS_TAG);

            if (mSearchArtistPresenter == null) {

                // We create a new Presenter, The Interactor and the Repository could be injected with a dependency injection framework
                mSearchArtistPresenter = new PresenterSearchArtistImp(this, new GetArtistsByNameInteractor(new RepositoryArtistImp(this)));
            }else {
                mSearchArtistPresenter.setContext(this);
            }

            if (mSearchArtistFragment == null) {
                mSearchArtistFragment = (SearchArtistFragment)SearchArtistFragment.newInstance();
            }

            mSearchArtistFragment.setmPresenter(mSearchArtistPresenter);

            // We let the presenter to handle the actions by itself
            mSearchArtistPresenter.setInterface(null);

            // Updates the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (mTopTracksFragment != null) {

                fragmentManager.beginTransaction().remove(mTopTracksFragment).commit();
                mTopTracksFragment = null;
                mTopTracksPresenter = null;

            }

            fragmentManager.beginTransaction()
                    .replace(R.id.container, mSearchArtistFragment, FRAGMENT_SEARCH_ARTIST_TAG).commit();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cleans up the presenter object
        if (isFinishing()) {
            mSearchArtistPresenter = null;
            mTopTracksPresenter = null;
        }
    }
}
