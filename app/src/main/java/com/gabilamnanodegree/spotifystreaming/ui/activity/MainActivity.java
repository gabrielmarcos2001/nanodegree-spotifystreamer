package com.gabilamnanodegree.spotifystreaming.ui.activity;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.cache.SPCacheImp;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetArtistsByNameInteractor;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetTopTracksForArtistInteractor;
import com.gabilamnanodegree.spotifystreaming.model.repository.RepositoryArtistImp;
import com.gabilamnanodegree.spotifystreaming.model.repository.RepositoryTracksImp;
import com.gabilamnanodegree.spotifystreaming.ui.SpotifyStreamerApplication;
import com.gabilamnanodegree.spotifystreaming.ui.components.ViewMainHeader;
import com.gabilamnanodegree.spotifystreaming.ui.fragment.FragmentMusicPlayer;
import com.gabilamnanodegree.spotifystreaming.ui.fragment.SearchArtistFragment;
import com.gabilamnanodegree.spotifystreaming.ui.fragment.TopTracksFragment;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.searchArtist.PresenterSearchArtist;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.searchArtist.PresenterSearchArtistImp;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.topTracks.PresenterTopTracks;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.topTracks.PresenterTopTracksImp;

import java.util.List;

/**
 * Activity which hosts the Main Fragments
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

    private ViewMainHeader mHeader; // Header used for large layouts on landscape mode

    private SearchArtistFragment mSearchArtistFragment; // Reference to the search artist fragment
    private TopTracksFragment mTopTracksFragment; // Reference to the top tracks fragment

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Landscape mode is only for large devices running in landscape
        boolean mLandscapeMode;

        // If the app is running in a tablet it sets the dual container layout
        // for displaying the two fragments in the same activity
        if (SpotifyStreamerApplication.mIsLargeLayout && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_dual_container);
            mLandscapeMode = true;
        }else {
            setContentView(R.layout.activity_single_container);
            mLandscapeMode = false;
        }

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

        // Actions interface for the top tracks fragment
        mTopTracksPresenter.setInterface(new PresenterTopTracksImp.TopTracksInterface() {
            @Override
            public void trackSelected(AppArtist artist, List<AppTrack> tracks, int position) {

                FragmentMusicPlayer musicPlayerFragment = FragmentMusicPlayer.newInstance();

                // Sends the Data to the player fragment
                musicPlayerFragment.setArtist(artist);
                musicPlayerFragment.setmTracks(tracks);
                musicPlayerFragment.setmInitialTrackIndex(position);

                // Starts the music player dialog fragment
                musicPlayerFragment.show(getSupportFragmentManager(), "music_player");

            }
        });

        // sets presenter to control the fragment logic
        mTopTracksFragment.setmPresenter(mTopTracksPresenter);

        // Initialize the view with a null artist - this will be populated
        // once the user selects an artist
        mTopTracksFragment.setmArtist(null);

        // Creates a new instance of the search artist fragment if needed
        if (mSearchArtistFragment == null) {
            mSearchArtistFragment = (SearchArtistFragment)SearchArtistFragment.newInstance();
        }

        // Sets the presenter to control the fragment logic
        mSearchArtistFragment.setmPresenter(mSearchArtistPresenter);

        // Initialize the activity for tablet layouts
        if (mLandscapeMode) {

            // Gets a reference to the header view
            mHeader = (ViewMainHeader)findViewById(R.id.header);

            mSearchArtistPresenter.setInterface(new PresenterSearchArtistImp.SearchArtistInterface() {

                @Override
                public void artistSelected(AppArtist artist) {

                    // Since the fragment is already visible we just need to set the artist
                    // in order to update the displayed data
                    mTopTracksFragment.setmArtist(artist);
                }
            });

            mHeader.setmInterface(new ViewMainHeader.MainHeaderActions() {

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
            fragmentManager.popBackStackImmediate();

            // Since the top tracks fragment could be in a different container ( if we are using the app in portrait )
            // then we need to remove the fragment before adding it to a new container
            fragmentManager.beginTransaction().remove(mTopTracksFragment).commit();
            fragmentManager.executePendingTransactions();

            // Adds the search artist fragment to the first container
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mSearchArtistFragment, FRAGMENT_SEARCH_ARTIST_TAG).commit();

            // Adds the top tracks fragment to the seconf container
            fragmentManager.beginTransaction()
                    .replace(R.id.container_2, mTopTracksFragment, FRAGMENT_TOP_TRACKS_TAG).commit();

        }else {

            mSearchArtistPresenter.setInterface(new PresenterSearchArtistImp.SearchArtistInterface() {

                @Override
                public void artistSelected(AppArtist artist) {

                    // sets the data to the fragment
                    mTopTracksFragment.setmArtist(artist);

                    // Replaces the main container with the top tracks fragment - and adds it to the back stack in
                    // order to be able to navigate back
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();

                    ft.setCustomAnimations(R.anim.slide_in_horizontal, R.anim.slide_out_left);
                    ft.replace(R.id.container, mTopTracksFragment, FRAGMENT_TOP_TRACKS_TAG).commit();

                }
            });

            AppArtist selectedArtist = new SPCacheImp().getSelectedArtist();

            // We have an artist selected - so we should be displaying the top tracks fragment
            if (selectedArtist != null) {

                mTopTracksFragment.setmArtist(selectedArtist);

                // Updates the main content by replacing fragments
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Removes the top tracks fragment
                fragmentManager.beginTransaction().remove(mTopTracksFragment).commit();
                fragmentManager.executePendingTransactions();

                fragmentManager.beginTransaction()
                            .replace(R.id.container, mTopTracksFragment, FRAGMENT_TOP_TRACKS_TAG).commit();

            }else {

                // Updates the main content by replacing fragments
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mSearchArtistFragment, FRAGMENT_SEARCH_ARTIST_TAG).commit();
            }
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

    @Override
    public void onBackPressed() {

        if (SpotifyStreamerApplication.mIsLargeLayout && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            super.onBackPressed();
        }else {

            // If we are running on a phone or tablet but in portrait and we have selected artist data means
            // that we are currently on the top tracks fragment so we replace the fragment with the one
            // for searching artists by name - in other case we just process the back button as normal

            AppArtist selectedArtist = new SPCacheImp().getSelectedArtist();

            if (selectedArtist != null) {

                new SPCacheImp().clearSelectedArtist();

                // Updates the main content by replacing fragments
                FragmentManager fragmentManager = getSupportFragmentManager();

                android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();

                ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_horizontal);
                ft.replace(R.id.container, mSearchArtistFragment, FRAGMENT_SEARCH_ARTIST_TAG).commit();

            } else {
                super.onBackPressed();
            }
        }

    }
}
