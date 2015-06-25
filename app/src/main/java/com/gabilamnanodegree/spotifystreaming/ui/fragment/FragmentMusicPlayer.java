package com.gabilamnanodegree.spotifystreaming.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.ui.components.ViewMusicPlayerHeader;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.player.PresenterPlayer;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.player.PresenterPlayerImp;
import com.gabilamnanodegree.spotifystreaming.ui.view.ViewPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielmarcos on 6/18/15.
 *
 * Dialog Fragment used to display the Music Player ui
 *
 */
public class FragmentMusicPlayer extends DialogFragment implements ViewPlayer, View.OnClickListener, ViewMusicPlayerHeader.HeaderActionsInterface{

    private static PresenterPlayer mPresenter;

    private AppArtist mSelectedArtist;
    private List<AppTrack> mTracks;
    private int mSelectedTrackIndex;

    private ViewMusicPlayerHeader mHeader;
    private boolean mMusicPlaying = false;
    private boolean mIsLargeLayout = false;

    public static FragmentMusicPlayer newInstance() {
        return new FragmentMusicPlayer();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!mIsLargeLayout) {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_SpotifyStreamer_NoActionBar_Dialog);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_music_player, container, false);

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        mHeader = (ViewMusicPlayerHeader)rootView.findViewById(R.id.header);
        mHeader.setmInterface(this);

        FloatingActionButton pausePlayButton = (FloatingActionButton)rootView.findViewById(R.id.play_pause_button);
        ImageButton prevButton = (ImageButton)rootView.findViewById(R.id.previous_track);
        ImageButton nextButton = (ImageButton)rootView.findViewById(R.id.next_track);

        rootView.findViewById(R.id.close_button).setOnClickListener(this);
        rootView.findViewById(R.id.share_button).setOnClickListener(this);

        pausePlayButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        if (mPresenter == null) {
            mPresenter = new PresenterPlayerImp(getActivity());
            mPresenter.setData(mTracks,mSelectedTrackIndex, mSelectedArtist);
        }else {
            mPresenter.setContext(getActivity());
        }

        if (savedInstanceState != null) {

            // Restores the artists data
            mSelectedArtist = savedInstanceState.getParcelable("artist");

            // Restores the selected track index
            mSelectedTrackIndex = savedInstanceState.getInt("trackIndex");

            // Restores the tracks list data
            ArrayList<AppTrack> savedTracks = new ArrayList<>();
            for (Parcelable track : savedInstanceState.getParcelableArrayList("tracks")) {
                savedTracks.add((AppTrack)track);
            }

            mTracks = savedTracks;
            mPresenter.setData(mTracks,mSelectedTrackIndex, mSelectedArtist);
        }


        mPresenter.setView(this);

        updateViewData();

        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_SpotifyStreamer_NoActionBar_Dialog);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // Stores the artist data
        outState.putParcelable("artist", mSelectedArtist);

        // Stores the tracks data list
        ArrayList<Parcelable> parcelables = new ArrayList<>();
        for (AppTrack track : mTracks) {
            parcelables.add(track);
        }

        outState.putParcelableArrayList("tracks", parcelables);

        // Stores the selected track index
        outState.putInt("trackIndex", mSelectedTrackIndex);

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
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        mPresenter.onViewDestroy();
        mPresenter = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.play_pause_button:

                if (mMusicPlaying) {
                    mPresenter.pauseClicked();
                }else {
                    mPresenter.playClicked();
                }
                break;

            case R.id.next_track:
                mPresenter.nextClicked();
                break;

            case R.id.previous_track:
                mPresenter.prevClicked();
                break;

            case R.id.close_button:
                dismiss();
                break;

            case R.id.share_button:
                mPresenter.shareClicked();
                break;
        }
    }

    @Override
    public void progressChanged(int progress) {
        mPresenter.seekTo(progress);
    }

    /**
     * Sets the Artist data to display in the view
     * @param artist
     */
    public void setArtist(AppArtist artist) {
        this.mSelectedArtist = artist;
    }

    /**
     * Sets the tracks data to display in the view
     * @param mTracks
     */
    public void setmTracks(List<AppTrack> mTracks) {
        this.mTracks = mTracks;
    }

    public void setmInitialTrackIndex(int mInitialTrackIndex) {
        this.mSelectedTrackIndex = mInitialTrackIndex;
    }

    @Override
    public void selectTrackByIndex(int index) {
        mSelectedTrackIndex = index;
        updateViewData();
    }

    @Override
    public void updateProgress(int progress, String totalDuration, String currentDuration) {

        if (mHeader != null) {
            mHeader.updateProgress(progress, totalDuration, currentDuration);
        }
    }

    /**
     * Updates the Views with the track / artist data
     */
    private void updateViewData() {

        // Checks that we have valid information and the views are indlated
        if (//this.mSelectedTrack != null
                this.mSelectedArtist != null
                && mHeader != null) {

            mHeader.setmArtist(mSelectedArtist);
            mHeader.setmTrack(mTracks.get(mSelectedTrackIndex));
        }
    }
}
