package com.gabilamnanodegree.spotifystreaming.ui.presenter.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

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

    private AppArtist mArtist;
    private ViewPlayer mView;

    private int mCurrentTrack = 0;
    private List<AppTrack> mTracks;

    //Connects to the music player service
    private ServiceConnection mMusicPlayerConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicPlayerService.MusicPlayerBinder binder = (MusicPlayerService.MusicPlayerBinder)service;

            // Gets the service reference
            mPlayerService = binder.getService();

            // Sets the current song
            mPlayerService.setmCurrentSong(mTracks.get(mCurrentTrack));
            mPlayerService.setmInterface(PresenterPlayerImp.this);
            mServiceBound = true;
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
    public void nextClicked() {

        // Increments the current track index taking care
        // of the limits
        mCurrentTrack ++;
        if (mCurrentTrack >= mTracks.size()) {
            mCurrentTrack = 0;
        }

        // Plays the next song
        mPlayerService.setmCurrentSong(mTracks.get(mCurrentTrack));
        mPlayerService.playSong();

        if (mView != null) {
            mView.selectTrackByIndex(mCurrentTrack);
            //this.mView.showInfoForTrack(mTracks.get(mCurrentTrack));
        }

    }

    @Override
    public void prevClicked() {

        // Decrements the current track index taking care
        // of the limits
        mCurrentTrack --;
        if (mCurrentTrack < 0) {
            mCurrentTrack = mTracks.size()-1;
        }

        // Plays the previous song
        mPlayerService.setmCurrentSong(mTracks.get(mCurrentTrack));
        mPlayerService.playSong();

        if (mView != null) {
            mView.selectTrackByIndex(mCurrentTrack);
            //this.mView.showInfoForTrack(mTracks.get(mCurrentTrack));
        }
    }

    @Override
    public void playClicked() {
        mPlayerService.playSong();
    }

    @Override
    public void pauseClicked() {
        mPlayerService.stopPlayer();
    }

    @Override
    public void seekTo(int progress) {
        mPlayerService.seekTo(progress);
    }

    @Override
    public void setData(List<AppTrack> tracks, int selectedIndex, AppArtist artist) {
        this.mArtist = artist;
        this.mTracks = tracks;
        this.mCurrentTrack = selectedIndex;

        if (mView != null) {
            mView.selectTrackByIndex(mCurrentTrack);
            //this.mView.showInfoForTrack(mTracks.get(mCurrentTrack));
        }
    }

    @Override
    public void shareClicked() {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this song");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, mTracks.get(mCurrentTrack).getmPreviewUrl());

        Intent chooserIntent = Intent.createChooser(shareIntent, "Share With");
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
            mView.updateProgress(progress, totalDuration, currentDuration);
        }
    }

    @Override
    public void setView(ViewPlayer view) {
        this.mView = view;

        if (mView != null) {
            mView.selectTrackByIndex(mCurrentTrack);
        }
    }


}
