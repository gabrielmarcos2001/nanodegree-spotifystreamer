package com.gabilamnanodegree.spotifystreaming.ui.listItems;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;

/**
 * Created by gabrielmarcos on 6/1/15.
 *
 * Custom View to display an artist data in a list
 *
 */
public class ListItemArtist extends RelativeLayout {

    private ImageView mArtistThumb;
    private TextView mArtistName;
    private TextView mFollowers;

    private AppArtist mData;

    public ListItemArtist(Context context) {
        super(context);

        inflateLayout(context);
    }

    public ListItemArtist(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflateLayout(context);
    }

    /**
     * Inflates the view layout and gets the view references
     *
     * @param context
     */
    private void inflateLayout(Context context) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item_artist,this,true);

        this.mArtistName = (TextView)findViewById(R.id.artist_name);
        this.mArtistThumb = (ImageView)findViewById(R.id.thumb_image);
        this.mFollowers = (TextView)findViewById(R.id.followers);

        // It updates the views with the item data
        updateViews();

    }

    /**
     * Updates the Views with the Artist Data
     */
    private void updateViews() {

        // Verifies that the views are inflated and
        // we have data available
        if (mData != null && mArtistName != null) {
            mArtistName.setText(mData.getmName());

            // Loads the image using Picasso
            if (mData.getmThumbUrl().isEmpty()) {

                // Show default image
                mArtistThumb.setImageResource(R.drawable.artist_default);

            }else {

                Picasso.with(getContext())
                        .load(mData.getmThumbUrl())
                        .into(mArtistThumb);
            }

            // Sets the number of followers
            if (mData.getmFollowers() == 1) {
                mFollowers.setText(String.valueOf(NumberFormat.getInstance().format(mData.getmFollowers()) + " " +  getContext().getString(R.string.follower)));
            }else {
                mFollowers.setText(String.valueOf(NumberFormat.getInstance().format(mData.getmFollowers()) + " " + getContext().getString(R.string.followers)));
            }
        }
    }

    /**
     * Sets the Artist Data
     * @param mData
     */
    public void setmData(AppArtist mData) {
        this.mData = mData;

        updateViews();
    }
}
