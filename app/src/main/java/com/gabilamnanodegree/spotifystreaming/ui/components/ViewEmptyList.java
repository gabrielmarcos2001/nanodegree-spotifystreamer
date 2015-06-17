package com.gabilamnanodegree.spotifystreaming.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gabilamnanodegree.spotifystreaming.R;

/**
 * Created by gabrielmarcos on 4/30/15.
 *
 * Custom View for displaying when a list is
 * empty
 *
 */
public class ViewEmptyList extends RelativeLayout {

    private String mText;
    private TextView mTextView;
    private TextView mSubtitle;
    private ImageView mFaceImage;

    public ViewEmptyList(Context context) {
        super(context);

        inflateLayout(context);
    }

    public ViewEmptyList(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflateLayout(context);
    }

    private void inflateLayout(Context context) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.view_empty_list, this, true);

        mTextView = (TextView)findViewById(R.id.text);
        mSubtitle = (TextView)findViewById(R.id.subtitle);
        mFaceImage = (ImageView)findViewById(R.id.sad_face);

    }

    /**
     * Sets the Text
     * @param mText
     */
    public void setmText(String mText) {

        this.mText = mText;

        String[] text = mText.split(":");
        if (mTextView != null) {
            mTextView.setText(text[0]);

            if (text.length > 1) {
                mSubtitle.setText(text[1]);
            }else {
                mSubtitle.setText("");
            }
        }
    }

    /**
     * Hides the Sad Face Image
     */
    public void hideFace() {
        mFaceImage.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows the Sad Face Image
     */
    public void showFace() {
        mFaceImage.setVisibility(View.VISIBLE);
    }
}
