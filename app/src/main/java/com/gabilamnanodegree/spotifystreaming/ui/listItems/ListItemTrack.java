package com.gabilamnanodegree.spotifystreaming.ui.listItems;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.squareup.picasso.Picasso;

/**
 * Created by gabrielmarcos on 6/1/15.
 */
public class ListItemTrack extends RelativeLayout {

    private ImageView mAlbumThumb;
    private TextView mTrackName;
    private TextView mAlbumName;

    private AppTrack mData;

    public ListItemTrack(Context context) {
        super(context);

        inflateLayout(context);
    }

    public ListItemTrack(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflateLayout(context);
    }

    /**
     * Inflates the View layout and initialize the
     * Views references
     *
     * @param context
     */
    private void inflateLayout(Context context) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item_track,this,true);

        mAlbumName = (TextView)findViewById(R.id.album_name);
        mTrackName = (TextView)findViewById(R.id.track_name);
        mAlbumThumb = (ImageView)findViewById(R.id.thumb_image);

        // It updates the views with the item data
        updateViews();

    }

    /**
     * Updates the Views with the Track Data
     */
    private void updateViews() {

        // Verifies that the views are inflated and
        // we have data available
        if (mData != null && mAlbumName != null) {

            mAlbumName.setText(mData.getmAlbumName());
            mTrackName.setText(mData.getmTrackName());

            if (mData.getmThumbUrl().equals("")) {
                mAlbumThumb.setImageResource(R.drawable.album_default);
            }else {
                // Loads the image using Picasso
                Picasso.with(getContext())
                        .load(mData.getmThumbUrl())
                        .into(mAlbumThumb);

            }

        }
    }

    /**
     * Sets the Track Data
     * @param mData
     */
    public void setmData(AppTrack mData) {
        this.mData = mData;

        updateViews();
    }
}
