package com.gabilamnanodegree.spotifystreaming.ui.presenter.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.model.service.MusicPlayerService;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.PresenterBase;
import com.gabilamnanodegree.spotifystreaming.ui.view.ViewPlayer;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/23/15.
 */
public class PresenterPlayerImp extends PresenterBase implements PresenterPlayer, MusicPlayerService.MusicProgressInterface {

    private MusicPlayerService mPlayerService;
    private boolean mServiceBound = false;
    private Intent mPlaybackServiceIntent;

    private ViewPlayer mView;

    //Connects to the music player service
    private ServiceConnection mMusicPlayerConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicPlayerService.MusicPlayerBinder binder = (MusicPlayerService.MusicPlayerBinder)service;

            // Gets the service reference
            mPlayerService = binder.getService();

            // Sets the current song
            mPlayerService.setmInterface(PresenterPlayerImp.this);
            mServiceBound = true;

            // Once we get connected to the service we update the view state
            if (mView != null) updateViewState();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    /**
     * Constructor
     * @param mContext
     */
    public PresenterPlayerImp(Context mContext) {
        super(mContext);

        if (mPlaybackServiceIntent==null) {

            mPlaybackServiceIntent = new Intent(mContext, MusicPlayerService.class);
            mContext.startService(mPlaybackServiceIntent);
            mContext.bindService(mPlaybackServiceIntent, mMusicPlayerConnection, Context.BIND_AUTO_CREATE);

        }
    }

    @Override
    public void playTrack(AppTrack track) {
        mPlayerService.setmCurrentSong(track);
        mPlayerService.playSong();
        mView.initInPlayingMode();
    }

    @Override
    public void pauseCurrentTrack() {
        mPlayerService.pause();
        if (mView != null) mView.initInPauseMode(mPlayerService.getmCurrentProgressPosition(), mPlayerService.getmTotalDuration(), mPlayerService.getmCurrentDuration());
    }

    @Override
    public void resumeCurrentTrack() {
        mPlayerService.resume();
        if (mView != null) mView.initInPlayingMode();
    }

    //@Override
    /*
    public AppTrack getCurrentPlayingTrack() {

        if (mPlayerService != null) {

            if (mPlayerService.isPlaying()) {
                return mPlayerService.getmCurrentSong();
            }else {
                return null;
            }
        }else {
            return null;
        }
    }*/

    //@Override
    /*
    public void stopCurrentTrack() {
        if (mPlayerService != null) {
            mPlayerService.stopPlayer();
        }
    }*/


    @Override
    public void seekTo(int progress) {

        if (mPlayerService.getmCurrentSong() == null) {

            // Store the current position
            mPlayerService.setmInitialOffset(progress);

        }else {

            if (mPlayerService.getmCurrentSong().getmId().equals(mView.getSelectedTrack().getmId())) {
                mPlayerService.seekTo(progress);
            }else {
                mPlayerService.setmInitialOffset(progress);
            }
        }

    }


    @Override
    public void shareTrack(AppTrack track) {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mContext.getString(R.string.share_song_title));
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, track.getmPreviewUrl());

        Intent chooserIntent = Intent.createChooser(shareIntent, mContext.getString(R.string.share_song_intent));
        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(chooserIntent);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onViewDestroy() {
        this.mView = null;
        if (mServiceBound) {
            mContext.unbindService(mMusicPlayerConnection);
            mServiceBound = false;
        }
    }

    @Override
    public void updateProgress(int progress, String totalDuration, String currentDuration) {

        if (mView != null) {

            // We verify that the song playing is the same in the presenter for updating the progress
            if (mPlayerService.getmCurrentSong().getmId().equals(mView.getSelectedTrack().getmId())) {
                mView.updateProgress(progress, totalDuration, currentDuration);
            }
        }
    }

    @Override
    public void setView(ViewPlayer view) {
        this.mView = view;

        if (mView != null) {
            updateViewState();
        }
    }

    /**
     *
     * Updates the view state depending on the
     * player state
     *
     */
    private void updateViewState() {

        if (mPlayerService != null) {

            if (mPlayerService.getmCurrentSong() != null) {

                // The View will start in different states depending on the service current playing song
                if (mPlayerService.getmCurrentSong().getmId().equals(mView.getSelectedTrack().getmId())) {
                    if (mPlayerService.isPlaying()) {
                        // The song is currently playing
                        mView.initInPlayingMode();
                    } else {
                        // The song is paused
                        mView.initInPauseMode(mPlayerService.getmCurrentProgressPosition(), mPlayerService.getmTotalDuration(), mPlayerService.getmCurrentDuration());
                    }

                } else {

                    // The song in the service is not the same as in the view
                    mView.initInStopMode();
                }

            }else {

                // There is no song on the player
                mView.initInStopMode();
            }

        }else {

            // We dont have a player service yet
            mView.initInStopMode();
        }
    }


}
