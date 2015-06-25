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

import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
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

    public static final String ACTION_PLAY = "com.gabilamnanodegree.spotifystreaming.PLAY";
    private static final int NOTIFICATION_ID = 999;

    private MediaPlayer mMediaPlayer = null;
    private WifiManager.WifiLock mWifiLock;
    private Notification mNotification;

    private AppTrack mCurrentSong;
    private int mInitialOffset;

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

        /*
        if (intent.getAction().equals(ACTION_PLAY)) {

            String songName = "song";

            // assign the song name to songName
            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                    new Intent(getApplicationContext(), BaseActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            mNotification = new Notification();
            mNotification.tickerText = "";
            mNotification.icon = R.drawable.artist_default;
            mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
            mNotification.setLatestEventInfo(getApplicationContext(), "MusicPlayerSample",
                    "Playing: " + songName, pi);

            startForeground(NOTIFICATION_ID, mNotification);

        }*/

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

                    String workedTotalDuration = UtilsTimers.milliSecondsToTimer(totalDuration);
                    String workedCurrentDuration = UtilsTimers.milliSecondsToTimer(currentPosition);

                    // Updating progress bar
                    int progress = (UtilsTimers.getProgressPercentage(currentPosition, totalDuration));

                    if (mInterface != null) {
                        mInterface.updateProgress(progress, workedTotalDuration, workedCurrentDuration);
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


    private void initMusicPlayer() {

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


    public void stopPlayer() {

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();

        mWifiLock.release();

        stopForeground(true);
    }
}
