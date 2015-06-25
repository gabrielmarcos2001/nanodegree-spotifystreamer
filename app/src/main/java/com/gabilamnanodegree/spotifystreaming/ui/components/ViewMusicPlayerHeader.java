package com.gabilamnanodegree.spotifystreaming.ui.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.utils.UtilsOpenGl;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;

/**
 * Created by gabrielmarcos on 6/12/15.
 *
 * Header View to be displayed on the Top Tracks fragment
 *
 */
public class ViewMusicPlayerHeader extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {

    /**
     * Every action performed in this view just sends a message we don't handle
     * any logic in here
     */
    public interface HeaderActionsInterface {
        void progressChanged(int progress);
    }

    private ImageView mAlbumImageView; // Background ImageView

    private TextView mArtistNameTextView; // Artist Name TextView
    private TextView mAlbumNameTextView; // Album Name TextView
    private TextView mTrackNameTextView; // Track Name TextView
    private SeekBar mProgressSeekbar;

    private AppTrack mTrack; // Selected Track
    private AppArtist mArtist; // Selected Artist

    private HeaderActionsInterface mInterface;

    public ViewMusicPlayerHeader(Context context, AttributeSet attrs){
        super(context,attrs);

        inflateLayout(context);
    }

    public ViewMusicPlayerHeader(Context context){
        super(context);

        inflateLayout(context);
    }

    /**
     * Layout inflater
     * @param context
     */
    private void inflateLayout(Context context) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_music_player, this, true);

        mArtistNameTextView = (TextView)findViewById(R.id.artist_name);
        mAlbumNameTextView = (TextView)findViewById(R.id.album_name);
        mTrackNameTextView = (TextView)findViewById(R.id.track_name);

        mAlbumImageView = (ImageView)findViewById(R.id.album_image);
        mProgressSeekbar = (SeekBar)findViewById(R.id.progress_seekbar);

        mProgressSeekbar.setOnSeekBarChangeListener(this);

        updateViewData();

    }

    /**
     * Updates the Views with the artist data
     */
    private void updateViewData() {

        // Sets the artist data if views are inflated
        if (mArtistNameTextView != null && mTrack != null) {

            mArtistNameTextView.setText(mArtist.getmName());
            mAlbumNameTextView.setText(mTrack.getmAlbumName());
            mTrackNameTextView.setText(mTrack.getmTrackName());

            if (mTrack.getmThumbUrl().isEmpty()) {

                // If we don't have any image data we set the default images
                mAlbumImageView.setImageResource(R.drawable.album_default);

            }else {

                Picasso.with(getContext())
                        .load(mTrack.getmThumbUrl())
                        .into(mAlbumImageView);
            }

        }
    }


    /**
     * Sets the artist data and updates the views if inflated
     * @param mArtist
     */
    public void setmArtist(AppArtist mArtist) {
        this.mArtist = mArtist;

        updateViewData();
    }

    /**
     * Sets the track data and updates the views if inflated
     * @param mTrack
     */
    public void setmTrack(AppTrack mTrack) {
        this.mTrack = mTrack;

        updateViewData();
    }

    /**
     * Sets the actions interface
     * @param mInterface
     */
    public void setmInterface(HeaderActionsInterface mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mInterface != null) {
            mInterface.progressChanged(seekBar.getProgress());
        }
    }

    /**
     * Resets the seekbar progress
     */
    public void resetProgressSeekbar() {

        if (mProgressSeekbar != null) {
            mProgressSeekbar.setProgress(0);
            mProgressSeekbar.setMax(1000);
        }
    }

    public void updateProgress(int progress, String totalDuration, String currentDuration) {
        if (mProgressSeekbar != null) {
            mProgressSeekbar.setProgress(progress);
        }
    }
}
