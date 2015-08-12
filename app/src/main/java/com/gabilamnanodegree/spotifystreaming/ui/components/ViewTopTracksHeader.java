package com.gabilamnanodegree.spotifystreaming.ui.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
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
public class ViewTopTracksHeader extends RelativeLayout {

    private ImageView mBackgroundImageView; // Background ImageView
    private ImageView mArtistImageView; // Artist Thumb ImageView
    private View mOverlay; // Alpha Overlay
    private TextView mArtistNameTextView; // Artist Name TextView
    private AppArtist mArtist; // Artist Data
    private float mAlpha = 0f; // Overlay Alpha value

    public ViewTopTracksHeader(Context context, AttributeSet attrs){
        super(context,attrs);

        inflateLayout(context);
    }

    public ViewTopTracksHeader(Context context){
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
        inflater.inflate(R.layout.header_top_tracks, this, true);

        mArtistNameTextView = (TextView)findViewById(R.id.artist_name);
        mBackgroundImageView = (ImageView)findViewById(R.id.background);
        mArtistImageView = (ImageView)findViewById(R.id.artist_image);

        mOverlay = findViewById(R.id.overlay);

        updateViewData();

    }

    /**
     * Updates the Views with the artist data
     */
    private void updateViewData() {

        // Sets the artist data if views are inflated
        if (mArtistNameTextView != null && mArtist != null) {

            mArtistNameTextView.setText(mArtist.getmName());

            if (mArtist.getmThumbUrl().isEmpty()) {

                // If we don't have any image data we set the default images
                mArtistImageView.setImageResource(R.drawable.artist_default);
                mBackgroundImageView.setImageResource(R.drawable.backround);

            }else {

                // The image loading is done with a target so
                // we can process the bitmap before setting it
                // to the imageview
                Picasso.with(getContext())
                        .load(mArtist.getmThumbUrl())
                        .into(target);
            }

        }
    }

    /**
     * Creates a Target object to get the picasso image data when downloaded
     * we want to apply some magic before adding it to the views
     */
    private Target target = new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            // Sets the artist image
            if (mArtistImageView != null) {
                mArtistImageView.setImageBitmap(bitmap);
            }

            // Sets the background image
            if (mBackgroundImageView != null) {

                // If the device supports opengl 2.0 it blurs the image
                if (UtilsOpenGl.openGl2Supported(getContext())) {

                    GPUImage gpuImage = new GPUImage(getContext());
                    gpuImage.setFilter(new GPUImageGaussianBlurFilter(5.0f));
                    gpuImage.setScaleType(GPUImage.ScaleType.CENTER_CROP);
                    gpuImage.setImage(bitmap);

                    mBackgroundImageView.setImageBitmap(gpuImage.getBitmapWithFilterApplied());

                } else {

                    // No openg-gl 2.0? then you get the default image
                    mBackgroundImageView.setImageResource(R.drawable.backround);
                }
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

            // Sets default images on error

            if (mArtistImageView != null) {
                mArtistImageView.setImageResource(R.drawable.artist_default);
            }

            if (mBackgroundImageView != null) {
                mBackgroundImageView.setImageResource(R.drawable.backround);
            }
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

            // Sets default images on prepare
            if (mArtistImageView != null) {
                mArtistImageView.setImageResource(R.drawable.artist_default);
            }

            if (mBackgroundImageView != null) {
                mBackgroundImageView.setImageResource(R.drawable.backround);
            }
        }
    };

    /**
     * Sets the artist data and updates the views if inflated
     * @param mArtist
     */
    public void setmArtist(AppArtist mArtist) {

        this.mArtist = mArtist;

        updateViewData();
    }

    /**
     * Nice Parallax effect to the header data
     * @param offset
     */
    public void offsetHeaderData(int offset) {

        // Adds the alpha effect to the overlay view
        if (mOverlay != null) {
            mAlpha += (offset*2);
            if (mAlpha < 0) mAlpha = 0;
            mOverlay.setAlpha(mAlpha / getContext().getResources().getDimensionPixelSize(R.dimen.header_top_tracks_min_height));
        }

        // Offsets the  background image to generate a parallax effect
        if (mBackgroundImageView != null) {

            // Controls the bottom limit
            if (mBackgroundImageView.getY() + offset < 0) {
                offset = (int)(0 - mBackgroundImageView.getY());
            }

            mBackgroundImageView.offsetTopAndBottom(offset);
        }

    }
}
