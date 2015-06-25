package com.gabilamnanodegree.spotifystreaming.utils;

/**
 * Created by gabrielmarcos on 6/24/15.
 */
public class UtilsTimers {

    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }


    public static int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage;

        long currentSeconds = (int) (currentDuration);
        long totalSeconds = (int) (totalDuration);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)* 1000;

        // return percentage
        return percentage.intValue();
    }


    public static int progressToTimer(int progress, int totalDuration) {

        int currentDuration;

        totalDuration = (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 1000) * totalDuration);

        return currentDuration * 1000;
    }
}
