package com.gabilamnanodegree.spotifystreaming.ui.components;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by gabrielmarcos on 8/12/15.
 */
public class FixedListView extends ListView {

    public FixedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedListView(Context context) {
        super(context);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
            // Completely random error when returning from the top tracks fragment
        }
    }
}
