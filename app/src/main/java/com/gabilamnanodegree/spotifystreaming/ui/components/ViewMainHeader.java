package com.gabilamnanodegree.spotifystreaming.ui.components;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.cache.SPCacheImp;
import com.gabilamnanodegree.spotifystreaming.ui.SpotifyStreamerApplication;

/**
 * Created by gabrielmarcos on 6/12/15.
 *
 * Header View to be displayed on the Main Activity for large devices on landscape
 *
 */
public class ViewMainHeader extends RelativeLayout implements TextWatcher {

    /**
     * Interface used for sending actions to the container
     * of this view
     */
    public interface MainHeaderActions {
        void artistNameChanged(String artistName);
    }

    private EditText mArtistNameEditText; // Artist Name EditText
    private String mArtistName; // Artist Name value
    private MainHeaderActions mInterface; // Interface for sending actions

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

    public ViewMainHeader(Context context, AttributeSet attrs){
        super(context,attrs);

        inflateLayout(context);
    }

    public ViewMainHeader(Context context){
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

                if (SpotifyStreamerApplication.mIsLargeLayout && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mArtistNameEditText.requestFocus();
                }
            }
        });

        mArtistNameEditText.addTextChangedListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (SpotifyStreamerApplication.mIsLargeLayout && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mArtistName = editable.toString();
            mHandler.removeCallbacks(mFilterTask);
            mHandler.postDelayed(mFilterTask, 1000);
        }
    }

    public void setmInterface(MainHeaderActions mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // Removes the text changed listener
        mArtistNameEditText.removeTextChangedListener(this);
    }
}
