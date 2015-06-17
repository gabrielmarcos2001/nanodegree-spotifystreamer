package com.gabilamnanodegree.spotifystreaming.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;

/**
 * Created by gabrielmarcos on 6/15/15.
 */
public class UtilsOpenGl {

    /**
     * Checks if OpenGL 2.0 is supported in the device
     * @param context
     * @return
     */
    public static boolean openGl2Supported(Context context) {

        final ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x20000;

    }
}
