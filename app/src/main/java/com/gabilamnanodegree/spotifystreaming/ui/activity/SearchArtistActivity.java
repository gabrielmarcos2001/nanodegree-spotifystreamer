package com.gabilamnanodegree.spotifystreaming.ui.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetArtistsByNameInteractor;
import com.gabilamnanodegree.spotifystreaming.model.repository.RepositoryArtistImp;
import com.gabilamnanodegree.spotifystreaming.ui.fragment.SearchArtistFragment;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.PresenterBase;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.searchArtist.PresenterSearchArtist;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.searchArtist.PresenterSearchArtistImp;

/**
 * Activity which hosts the Search Artist Fragment
 */
public class SearchArtistActivity extends BaseActivity {

    /**
     * We make the presenter static so it does not get recreated on configuration
     * changes - the presenter will be cleaned when the activity is destroyed.
     */
    private static PresenterSearchArtist mPresenter;

    private static final String FRAGMENT_TAG = "search_artist_fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_container);

        Fragment searchArtistFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (mPresenter == null) {

            // We create a new Presenter, The Interactor and the Repository could be injected with a dependency injection framework
            mPresenter = new PresenterSearchArtistImp(this, new GetArtistsByNameInteractor(new RepositoryArtistImp(this)));
        }else {
            mPresenter.setContext(this);
        }

        if (searchArtistFragment == null) {
            searchArtistFragment = SearchArtistFragment.newInstance();
        }

        ((SearchArtistFragment)searchArtistFragment).setmPresenter(mPresenter);

        // Updates the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, searchArtistFragment, FRAGMENT_TAG).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cleans up the presenter object
        if (isFinishing()) {
            mPresenter = null;
        }
    }
}
