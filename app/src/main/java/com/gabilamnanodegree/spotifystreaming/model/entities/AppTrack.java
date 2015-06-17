package com.gabilamnanodegree.spotifystreaming.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by gabrielmarcos on 6/1/15.
 *
 * Track Data Model
 *
 */
public class AppTrack implements Parcelable {

    private String mThumbUrl; // Album thumb url
    private String mTrackName; // Track name
    private String mAlbumName; // Album name
    private String mPreviewUrl; // track preview url

    /**
     * Constructor
     */
    public AppTrack() {
        this.mThumbUrl = "";
        this.mTrackName = "";
        this.mAlbumName = "";
        this.mPreviewUrl = "";
    }

    /**
     * Constructor
     * @param track spotify track object
     */
    public AppTrack(Track track) {

        this.mTrackName = track.name;
        this.mAlbumName = track.album.name;
        this.mPreviewUrl = track.preview_url;

        List<Image> images = track.album.images;

        // We get the first available image for
        // this example
        if (images != null) {
            try {
                this.mThumbUrl = images.get(0).url;
            }catch (IndexOutOfBoundsException e) {
                this.mThumbUrl = "";
            }
        }
    }

    /**
     * Parcel Constructor
     * @param in
     */
    public AppTrack(Parcel in){
        readFromParcel(in);
    }

    public String getmThumbUrl() {
        return mThumbUrl;
    }

    public void setmThumbUrl(String mThumbUrl) {
        this.mThumbUrl = mThumbUrl;
    }

    public String getmTrackName() {
        return mTrackName;
    }

    public void setmTrackName(String mTrackName) {
        this.mTrackName = mTrackName;
    }

    public String getmAlbumName() {
        return mAlbumName;
    }

    public void setmAlbumName(String mAlbumName) {
        this.mAlbumName = mAlbumName;
    }

    public String getmPreviewUrl() {
        return mPreviewUrl;
    }

    public void setmPreviewUrl(String mPreviewUrl) {
        this.mPreviewUrl = mPreviewUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Reads the data from a parcel
     * @param parcel
     */
    private void readFromParcel(Parcel parcel) {
        this.mThumbUrl = parcel.readString();
        this.mTrackName = parcel.readString();
        this.mAlbumName = parcel.readString();
        this.mPreviewUrl = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.mThumbUrl);
        parcel.writeString(this.mTrackName);
        parcel.writeString(this.mAlbumName);
        parcel.writeString(this.mPreviewUrl);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AppTrack createFromParcel(Parcel in) {
            return new AppTrack(in);
        }

        public AppTrack[] newArray(int size) {
            return new AppTrack[size];
        }
    };
}
