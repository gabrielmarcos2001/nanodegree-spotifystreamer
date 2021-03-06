package com.gabilamnanodegree.spotifystreaming.ui.components;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.cache.SPCacheImp;
import com.gabilamnanodegree.spotifystreaming.ui.SpotifyStreamerApplication;

/**
 * Created by gabrielmarcos on 6/12/15.
 *
 * Header View to be displayed on the Search Artist fragment
 *
 */
public class ViewSearchArtistHeader extends RelativeLayout implements TextWatcher {

    /**
     * Interface used for sending actions to the container
     * of this view
     */
    public interface SearchArtistHeaderActions {
        void artistNameChanged(String artistName);
    }

    private ImageView mImageView; // Background ImageView
    private View mOverlay; // Alpha Overlay
    private EditText mArtistNameEditText; // Artist Name EditText
    private String mArtistName; // Artist Name value
    private SearchArtistHeaderActions mInterface; // Interface for sending actions
    private float mAlpha = 0f; // Overlay alpha value

    /**
     * A handler is used to wait some time before sending the
     * artist name on text changed
     */
    private Handler mHandler = new Handler();

    Runnable mFilterTask = new Runnable() {

        @Override
        public void run() {

            new SPCacheImp().setSearchTerms(mArtistName);

            if (mInterface != null) {
                mInterface.artistNameChanged(mArtistName);
            }
        }
    };

    public ViewSearchArtistHeader(Context context, AttributeSet attrs){
        super(context,attrs);

        inflateLayout(context);
    }

    public ViewSearchArtistHeader(Context context){
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
        inflater.inflate(R.layout.header_search_artist, this, true);

        mArtistNameEditText = (EditText)findViewById(R.id.artist_name);

        mImageView = (ImageView)findViewById(R.id.background);
        mOverlay = findViewById(R.id.overlay);

        // Gets the artist name from the persistence manager
        mArtistName = new SPCacheImp().getSearchTerms();

        if (mArtistName != null) {
            mArtistNameEditText.setText(mArtistName);
        }

        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);

                if (!(SpotifyStreamerApplication.mIsLargeLayout && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                    mArtistNameEditText.requestFocus();
                }
            }
        });

        mArtistNameEditText.addTextChangedListener(this);

    }

    /**
     * Updates the artist name data
     * @param artistName
     */
    public void setmArtistName(String artistName) {

        if (!this.mArtistName.equals(artistName)) {
            mArtistName = artistName;

            if (mArtistNameEditText != null) {
                mArtistNameEditText.setText(mArtistName);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (!(SpotifyStreamerApplication.mIsLargeLayout && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            mArtistName = editable.toString();
            mHandler.removeCallbacks(mFilterTask);
            mHandler.postDelayed(mFilterTask, 1000);
        }
    }

    public void setmInterface(SearchArtistHeaderActions mInterface) {
        this.mInterface = mInterface;
    }

    /**
     * Adds a nice parallax effect to the header data
     * @param offset
     */
    public void offsetHeaderData(int offset) {

        // Adds the alpha effect to the overlay view
        if (mOverlay != null) {

            mAlpha += (offset*2);
            if (mAlpha < 0) mAlpha = 0;
            mOverlay.setAlpha(mAlpha/getContext().getResources().getDimensionPixelSize(R.dimen.header_search_artist_min_height));
        }

        // Offsets the  background image to generate a parallax effect
        if (mImageView != null) {

            // Controls the bottom limit
            if (mImageView.getY() + offset < 0) {
                offset = (int)(0 - mImageView.getY());
            }

            mImageView.offsetTopAndBottom(offset);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // Removes the text changed listener
        mArtistNameEditText.removeTextChangedListener(this);
    }
}
