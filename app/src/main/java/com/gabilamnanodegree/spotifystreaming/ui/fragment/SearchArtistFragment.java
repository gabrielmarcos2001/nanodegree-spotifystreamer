package com.gabilamnanodegree.spotifystreaming.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.ui.SpotifyStreamerApplication;
import com.gabilamnanodegree.spotifystreaming.ui.adapter.ArtistAdapter;
import com.gabilamnanodegree.spotifystreaming.ui.components.ViewEmptyList;
import com.gabilamnanodegree.spotifystreaming.ui.components.ViewSearchArtistHeader;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.searchArtist.PresenterSearchArtist;
import com.gabilamnanodegree.spotifystreaming.ui.view.ViewSearchByArtist;
import com.gabilamnanodegree.spotifystreaming.utils.UtilsDpi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public class SearchArtistFragment extends FragmentBaseListWithHeader implements ViewSearchByArtist, AdapterView.OnItemClickListener, ViewSearchArtistHeader.SearchArtistHeaderActions, AbsListView.OnScrollListener {

    private PresenterSearchArtist mPresenter;
    private ArtistAdapter mAdapter;

    private String mArtistName = "";

    public static Fragment newInstance() {
        return new SearchArtistFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_artist, container, false);

        this.mEmptyView = (ViewEmptyList)rootView.findViewById(R.id.empty_view);
        this.mListView = (ListView)rootView.findViewById(R.id.artists_list_view);
        this.mRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh_container);
        this.mHeader = rootView.findViewById(R.id.header);
        this.mShadow = rootView.findViewById(R.id.elevation_shadow);

        if (SpotifyStreamerApplication.mIsLargeLayout) {
            this.mHeaderHeight = 0;
            this.mMinHeaderTranslation = 0;
            this.mHeader.setVisibility(View.GONE);
        }else {
            this.mHeader.setVisibility(View.VISIBLE);
            this.mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_search_artist_height);
            this.mMinHeaderTranslation = getResources().getDimensionPixelSize(R.dimen.header_search_artist_min_height);
        }

        initCommonViews(inflater);

        this.mAdapter = new ArtistAdapter(getActivity());
        this.mListView.setAdapter(mAdapter);

        // Sets the View interface to the header to get notified when the
        // artist name has changed
        if (mHeader instanceof  ViewSearchArtistHeader) {
            ((ViewSearchArtistHeader) mHeader).setmInterface(this);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.setView(this);

        if (savedInstanceState != null) {

            // Restores the artists name
            mArtistName = savedInstanceState.getString("artist_name");

            // Sets the artist name to the header view
            if (mHeader instanceof  ViewSearchArtistHeader) {
                ((ViewSearchArtistHeader) mHeader).setmArtistName(mArtistName);
            }

            // Restores the artists list data
            ArrayList<AppArtist> savedArtists = new ArrayList<>();
            for (Parcelable artist : savedInstanceState.getParcelableArrayList("artists")) {
                savedArtists.add((AppArtist)artist);
            }

            mAdapter.setmAppArtists(savedArtists);

            if (savedArtists.size() == 0) {
                showEmptyListMessage();
            }

        }else {

            // Initialize the View with the empty query message
            showEmptyQueryMessage();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

        if (mHeader instanceof ViewSearchArtistHeader) {

            int scrollY = getScrollY(view);
            int dampedScroll = (int) (scrollY * 0.5f);
            int imageOffset = mLastScroll - dampedScroll;

            ((ViewSearchArtistHeader) mHeader).offsetHeaderData(-imageOffset);

            mLastScroll = dampedScroll;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.setView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.setView(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onViewDestroy();
    }

    @Override
    public void refreshTriggered() {
        mPresenter.searchArtistByName(mArtistName);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // Stores the artist name value
        outState.putString("artist_name", mArtistName);

        // Stores the artists data list
        ArrayList<Parcelable> parcelables = new ArrayList<>();
        for (AppArtist artist : mAdapter.getAll()) {
            parcelables.add(artist);
        }

        outState.putParcelableArrayList("artists", parcelables);

    }

    @Override
    public void showArtistsList(final List<AppArtist> appArtists) {
        mAdapter.setmAppArtists(appArtists);
    }

    @Override
    public void showEmptyListMessage() {
        mEmptyView.showFace();
        mEmptyView.setmText(getString(R.string.error_empty_list_artist));
    }

    @Override
    public void showEmptyQueryMessage() {

        mEmptyView.hideFace();
        mEmptyView.setmText(getString(R.string.error_empty_query_artist));
        mAdapter.clear();

    }

    @Override
    public void showErrorMessage(String error) {
        super.showErrorMessage(error);
    }

    @Override
    public void showLoader() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoader() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        // Notifies the presenter about an artist been selected
        if (position > 0) {
            mPresenter.artistSelected((AppArtist) mAdapter.getItem(position - 1));

            if (SpotifyStreamerApplication.mIsLargeLayout) {
                mAdapter.setmSelectedItem((position - 1));
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * This sets the presenter to controll the fragment so we don't
     * have to instantiate it in here
     * @param mPresenter
     */
    public void setmPresenter(PresenterSearchArtist mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    void onScrollUp() {

    }

    @Override
    void onScrollDown() {

    }

    @Override
    public void artistNameChanged(String artistName) {

        // We only perform the search if the artist name
        // has changed
        if (!this.mArtistName.equals(artistName)) {

            this.mArtistName = artistName;
            mPresenter.searchArtistByName(artistName);
        }
    }

}
