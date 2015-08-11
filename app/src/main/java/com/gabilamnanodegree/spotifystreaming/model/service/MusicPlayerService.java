package com.gabilamnanodegree.spotifystreaming.model.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.ui.activity.BaseActivity;
import com.gabilamnanodegree.spotifystreaming.utils.UtilsTimers;

import java.io.IOException;

/**
 * Created by gabrielmarcos on 6/23/15.
 *
 * Service used to play the music on the background
 *
 */
public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer = null;
    private WifiManager.WifiLock mWifiLock;

    private AppTrack mCurrentSong;
    private int mInitialOffset;
    private int mPausedPosition;

    private int mCurrentProgress;
    private String mCurrentDuration;
    private String mTotalDuration;

    private final IBinder mMusicBinder = new MusicPlayerBinder();

    public interface MusicProgressInterface {
        void updateProgress(int progress, String totalDuration, String currentDuration);
    }

    private MusicProgressInterface mInterface;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaPlayer = new MediaPlayer(); // initialize it here
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        initMusicPlayer();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * Sets the current song
     * @param mCurrentSong
     */
    public void setmCurrentSong(AppTrack mCurrentSong) {
        this.mCurrentSong = mCurrentSong;
    }

    /**
     * Plays the current song
     */
    public void playSong() {

        mPausedPosition = 0;
        mMediaPlayer.reset();

        try {

            mMediaPlayer.setDataSource(mCurrentSong.getmPreviewUrl());
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
            mHandler.post(mUpdateTimeTask);

        }catch (IOException e) {

            // Error setting the url
        }
    }

    /**
     * This runnable is used for sending messages with information
     * about the current progress of the song
     *
     */
    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {

            try {

                if (mMediaPlayer.isPlaying()) {

                    long totalDuration = mMediaPlayer.getDuration();
                    long currentPosition = mMediaPlayer.getCurrentPosition();

                    mTotalDuration = UtilsTimers.milliSecondsToTimer(totalDuration);
                    mCurrentDuration = UtilsTimers.milliSecondsToTimer(currentPosition);

                    // Updating progress bar
                    mCurrentProgress = (UtilsTimers.getProgressPercentage(currentPosition, totalDuration));

                    if (mInterface != null) {
                        mInterface.updateProgress(mCurrentProgress, mTotalDuration, mCurrentDuration);
                    }
                }

                mHandler.postDelayed(this, 100);

            }catch (IllegalStateException e) {

            }

        }
    };

    /**
     * Interface used for interacting with the service
     * @param mInterface
     */
    public void setmInterface(MusicProgressInterface mInterface) {
        this.mInterface = mInterface;
    }

    /**
     * Seeks to ...
     *
     * @param progress
     */
    public void seekTo(int progress) {

        if (mMediaPlayer.isPlaying()) {

            // If the media player is playing we update the current position
            long totalDuration = mMediaPlayer.getDuration();
            mMediaPlayer.seekTo(UtilsTimers.progressToTimer(progress, (int) totalDuration));

        }else {

            // If the media player was not playing we store the initial offset
            // value to apply once the player starts
            mInitialOffset = progress;
        }
    }

    public void setmInitialOffset(int mInitialOffset) {
        this.mInitialOffset = mInitialOffset;
    }

    /**
     * Returns the current track linked to the service
     * @return
     */
    public AppTrack getmCurrentSong() {
        return mCurrentSong;
    }

    public int getmCurrentProgressPosition() {
        return mCurrentProgress;
    }

    public String getmTotalDuration() {
        return mTotalDuration;
    }

    public String getmCurrentDuration() {
        return mCurrentDuration;
    }

    private void initMusicPlayer() {

        mInitialOffset = 0;
        mCurrentProgress = 0;

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);

        // Sets the wake locks so the music continues playing after the device goes into sleep mode
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mWifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "playerLock");

        mWifiLock.acquire();

    }

    /**
     * Binder for communicating the service with the activity
     */
    public class MusicPlayerBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        if (mInitialOffset != 0) {
            mediaPlayer.seekTo(UtilsTimers.progressToTimer(mInitialOffset, mMediaPlayer.getDuration()));
        }

        mediaPlayer.start();
        mInitialOffset = 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onDestroy() {
        stopPlayer();
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    /**
     * Pauses the media player
     */
    public void pause() {

        mInitialOffset = 0;

        mMediaPlayer.pause();
        mPausedPosition = mMediaPlayer.getCurrentPosition();
    }

    /**
     * Resumes the media player
     */
    public void resume() {

        // The user could have changed the seekbar while the music was paused
        if (mInitialOffset != 0) {
            mMediaPlayer.seekTo(UtilsTimers.progressToTimer(mInitialOffset, mMediaPlayer.getDuration()));
        }else {
            mMediaPlayer.seekTo(mPausedPosition);
        }

        mMediaPlayer.start();

        mPausedPosition = 0;
        mInitialOffset = 0;
    }

    /**
     * Stops the media player
     */
    public void stopPlayer() {

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }

        mMediaPlayer.release();
        mCurrentProgress = 0;
        mPausedPosition = 0;
        mInitialOffset = 0;
        mWifiLock.release();

        stopForeground(true);
    }
}
