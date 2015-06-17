package com.gabilamnanodegree.spotifystreaming.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by gabrielmarcos on 6/15/15.
 */
public class UtilsDpi {

    /**
     *
     * Converts DP units to pixels
     *
     * @param dp
     * @param context
     * @return
     */
    public static float convertDpToPixel(float dp, Context context){

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     *
     * Converts Pixels to DP units
     *
     * @param px
     * @param context
     * @return
     */
    public static float convertPixelsToDp(float px, Context context){

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }
}
