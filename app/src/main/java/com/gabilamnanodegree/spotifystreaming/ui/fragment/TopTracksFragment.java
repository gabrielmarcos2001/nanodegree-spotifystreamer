package com.gabilamnanodegree.spotifystreaming.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.cache.SPCacheImp;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.ui.SpotifyStreamerApplication;
import com.gabilamnanodegree.spotifystreaming.ui.activity.BaseActivity;
import com.gabilamnanodegree.spotifystreaming.ui.adapter.TracksAdapter;
import com.gabilamnanodegree.spotifystreaming.ui.components.FixedListView;
import com.gabilamnanodegree.spotifystreaming.ui.components.ViewEmptyList;
import com.gabilamnanodegree.spotifystreaming.ui.components.ViewTopTracksHeader;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.topTracks.PresenterTopTracks;
import com.gabilamnanodegree.spotifystreaming.ui.view.ViewTopTracks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public class TopTracksFragment extends FragmentBaseListWithHeader implements ViewTopTracks {

    private Toolbar mToolbar;

    /**
     * Presenter for controlling the fragment logic
     */
    private PresenterTopTracks mPresenter;

    /**
     * Adapter for displaying the tracks items
     */
    private TracksAdapter mAdapter;

    private AppArtist mArtist; // Artist Data

    private TextView mTitleTextView; // Title TextView

    public static Fragment newInstance() {
        return new TopTracksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        if (mToolbar != null) {
            ((BaseActivity)getActivity()).setSupportActionBar(mToolbar);
            ((BaseActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((BaseActivity)getActivity()).getSupportActionBar().setTitle("");
        }

        this.mEmptyView = (ViewEmptyList)rootView.findViewById(R.id.empty_view);
        this.mListView = (FixedListView)rootView.findViewById(R.id.top_tracks_list_view);
        this.mRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh_container);
        this.mTitleTextView = (TextView)rootView.findViewById(R.id.title);
        this.mHeader = rootView.findViewById(R.id.header);
        this.mShadow = rootView.findViewById(R.id.elevation_shadow);

        // Initialize the views depending on if we are using a tablet or a phone
        if (SpotifyStreamerApplication.mIsLargeLayout && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.mHeaderHeight = 0;
            this.mMinHeaderTranslation = 0;
            this.mHeader.setVisibility(View.GONE);
            this.mTitleTextView.setVisibility(View.GONE);
            this.mEmptyView.hideFace();
            this.mToolbar.setVisibility(View.GONE);
        }else {
            this.mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_top_tracks_height);
            this.mMinHeaderTranslation = getResources().getDimensionPixelSize(R.dimen.header_top_tracks_min_height);
            this.mHeader.setVisibility(View.VISIBLE);
            this.mTitleTextView.setVisibility(View.VISIBLE);
            this.mToolbar.setVisibility(View.VISIBLE);
        }

        // Initialize the Common views once all the views are inflated and binded
        initCommonViews(inflater);

        this.mAdapter = new TracksAdapter(getActivity());
        this.mListView.setAdapter(mAdapter);

        if (mHeader instanceof ViewTopTracksHeader) {
            ((ViewTopTracksHeader)mHeader).setmArtist(mArtist);
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mPresenter == null) return;

        mPresenter.setView(this);

        if (savedInstanceState != null) {

            // Restores the artists data
            mArtist = savedInstanceState.getParcelable("artist");

            // Sets the artist data to the header view
            if (mHeader instanceof ViewTopTracksHeader) {
                ((ViewTopTracksHeader)mHeader).setmArtist(mArtist);
            }

            // Restores the tracks list data
            ArrayList<AppTrack> savedTracks = new ArrayList<>();
            for (Parcelable track : savedInstanceState.getParcelableArrayList("tracks")) {
                savedTracks.add((AppTrack)track);
            }

            mAdapter.setmAppTracks(savedTracks);

            if (savedTracks.size() == 0) {
                showEmptyListMessage();
            }

        }else {

            // Tryes to get the selected artist from the cache system
            mArtist = new SPCacheImp().getSelectedArtist();

            // If we have an artist data we search for its track
            // Only if the saved instance state is null - so we don't do
            // the search again on screen orientation changes
            if (mArtist != null) {
                mPresenter.searchTopTracksByArtist(mArtist.getmId());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // Stores the artist data
        outState.putParcelable("artist", mArtist);

        // Stores the tracks data list
        ArrayList<Parcelable> parcelables = new ArrayList<>();
        for (AppTrack track : mAdapter.getAll()) {
            parcelables.add(track);
        }

        outState.putParcelableArrayList("tracks", parcelables);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.setView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.setView(null);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.setView(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onViewDestroy();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

        if (mHeader instanceof ViewTopTracksHeader) {

            int scrollY = getScrollY(view);
            int dampedScroll = (int) (scrollY * 0.5f);
            int imageOffset = mLastScroll - dampedScroll;

            ((ViewTopTracksHeader) mHeader).offsetHeaderData(-imageOffset);

            mLastScroll = dampedScroll;
        }

        int scrollY = getScrollY(view);

        if (mTitleTextView != null) {
            mTitleTextView.setTranslationY(Math.max(-scrollY, -mMinHeaderTranslation));
        }
    }

    @Override
    public void refreshTriggered() {
        if (mArtist != null) {
            mPresenter.searchTopTracksByArtist(mArtist.getmId());
        }else {
            hideLoader();
        }
    }

    @Override
    public void showTracksList(List<AppTrack> appTracks) {
        mAdapter.setmAppTracks(appTracks);
    }

    @Override
    public void showEmptyListMessage() {
        mEmptyView.showFace();
        mEmptyView.setmText(getString(R.string.error_empty_list_track));
    }

    @Override
    public void showErrorMessage(String error) {
        super.showErrorMessage(error);
    }

    @Override
    public void showLoader() {
        mRefreshLayout.setRefreshing(true);
        mEmptyView.hideFace();
        mEmptyView.setmText(getString(R.string.loading));
    }

    @Override
    public void hideLoader() {
        mRefreshLayout.setRefreshing(false);
        mEmptyView.setmText("");
    }

    /**
     * Sets the artist and searches for its tracks
     * @param mArtist
     */
    public void setmArtist(AppArtist mArtist) {

        this.mArtist = mArtist;

        if (mPresenter != null && mArtist != null) {
            mPresenter.searchTopTracksByArtist(mArtist.getmId());
        }

        // The artist has been refreshed
        if (mArtist == null && mAdapter != null) {
            mAdapter.clearTracks();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        // We do a search for position gt 0 because we have a header view on the list
        if (position > 0) {
            mPresenter.trackSelected(position-1, mArtist);
        }
    }

    @Override
    public void openMusicPlayer(DialogFragment fragment) {

        // The device is using a large layout, so show the fragment as a dialog
        fragment.show(getChildFragmentManager(), "music_player");

    }

    @Override
    void onScrollUp() {

    }

    @Override
    void onScrollDown() {

    }

    public void setmPresenter(PresenterTopTracks mPresenter) {
        this.mPresenter = mPresenter;
    }
}
