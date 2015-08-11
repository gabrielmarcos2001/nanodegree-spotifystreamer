package com.gabilamnanodegree.spotifystreaming.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private enum PlayerState {
        STOPEED,
        PLAYING,
        PAUSED
    }

    private PlayerState mState; // Stores the current state of the view

    private static PresenterPlayer mPresenter;

    private AppArtist mSelectedArtist; // Current selected artist
    private List<AppTrack> mTracks; // List of the artist top tracks
    private int mSelectedTrackIndex; // Current selected track index

    private ViewMusicPlayerHeader mHeader;
    private boolean mMusicPlaying = false;
    private boolean mIsLargeLayout = false;
    private boolean mPaused = false;

    private FloatingActionButton mPlayPauseButton;

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

        mPlayPauseButton = (FloatingActionButton)rootView.findViewById(R.id.play_pause_button);
        ImageButton prevButton = (ImageButton)rootView.findViewById(R.id.previous_track);
        ImageButton nextButton = (ImageButton)rootView.findViewById(R.id.next_track);

        rootView.findViewById(R.id.close_button).setOnClickListener(this);
        rootView.findViewById(R.id.share_button).setOnClickListener(this);

        mPlayPauseButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        if (mPresenter == null) {
            mPresenter = new PresenterPlayerImp(getActivity());
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

        }

        mPresenter.setView(this);

        // Checks if there is a song a song playing at moment of opening the player
        if (mPresenter.getCurrentPlayingTrack() != null) {

            // If the playing song is the same than the selected song by the user then we init the view
            // in Playing Mode
            if (mPresenter.getCurrentPlayingTrack().getmTrackName().equals(mTracks.get(mSelectedTrackIndex))) {
                initInPlayingMode();
            }else {
                initInStopMode();
            }
        }else {
            initInStopMode();
        }

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

            if (mState == PlayerState.PAUSED) {
                // Stop the song
            }
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

                // If the music is playing the we pause the music
                if (mState == PlayerState.PLAYING) {

                    mPresenter.pauseCurrentTrack();

                    // We update the current state to Paused
                    mState = PlayerState.PAUSED;

                }else {

                    // If the music is not playing we check if we are coming
                    // from a paused state or we are playing a current song
                    if (mState == PlayerState.PAUSED) {
                        mPresenter.resumeCurrentTrack();
                    }else {
                        mPresenter.playTrack(mTracks.get(mSelectedTrackIndex));
                    }

                    // We update the current state to Playing
                    mState = PlayerState.PLAYING;

                }
                break;

            case R.id.next_track:
                processNextClicked();
                break;

            case R.id.previous_track:
                processPrevClicked();
                break;

            case R.id.close_button:
                dismiss();
                break;

            case R.id.share_button:
                mPresenter.shareTrack(mTracks.get(mSelectedTrackIndex));
                break;
        }
    }

    /**
     * Process the Next button clicked
     */
    private void processNextClicked() {

        mSelectedTrackIndex ++;

        if (mSelectedTrackIndex >= mTracks.size()) {
            mSelectedTrackIndex = 0;
        }

        updateViewData();

        mPresenter.playTrack(mTracks.get(mSelectedTrackIndex));
    }

    /**
     * Process the previous button clicked
     */
    private void processPrevClicked() {

        mSelectedTrackIndex --;
        if (mSelectedTrackIndex < 0) {
            mSelectedTrackIndex = mTracks.size()-1;
        }

        updateViewData();

        mPresenter.playTrack(mTracks.get(mSelectedTrackIndex));
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
    public void updateProgress(int progress, String totalDuration, String currentDuration) {

        if (mHeader != null) {
            mHeader.updateProgress(progress, totalDuration, currentDuration);
        }
    }

    @Override
    public AppTrack getSelectedTrack() {
        return mTracks.get(mSelectedTrackIndex);
    }

    /**
     * Updates the Views with the track / artist data
     */
    private void updateViewData() {

        // Checks that we have valid information and the views are inflated
        if (this.mSelectedArtist != null
                && mHeader != null) {

            mHeader.setmArtist(mSelectedArtist);
            mHeader.setmTrack(mTracks.get(mSelectedTrackIndex));
        }
    }

    @Override
    public void initInPlayingMode() {
        mState = PlayerState.PLAYING;
        mPlayPauseButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_media_pause));
    }

    @Override
    public void initInStopMode() {
        mState = PlayerState.STOPEED;
        mPlayPauseButton.setImageBitmap(BitmapFactory.decodeResource(getResources(),android.R.drawable.ic_media_play));
    }
}
