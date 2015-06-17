package com.gabilamnanodegree.spotifystreaming.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by gabrielmarcos on 6/1/15.
 *
 * Artist Data Model
 *
 */
public class AppArtist implements Parcelable {

    private String mId; // Spotify artist id
    private String mThumbUrl; // Thumb Image url
    private String mName; // Name
    private int mFollowers; // Number of followers

    /**
     * Constructor
     */
    public AppArtist() {
        this.mId = "";
        this.mThumbUrl = "";
        this.mName = "";
        this.mFollowers = 0;
    }

    /**
     * Parcel Constructor
     * @param in
     */
    public AppArtist(Parcel in){
        readFromParcel(in);
    }

    /**
     * Constructor
     * @param artist Spotify Artist
     */
    public AppArtist(Artist artist) {
        this.mId = artist.id;

        List<Image> images = artist.images;

        // We get the first available image for this example
        if (images != null) {
            try {
                this.mThumbUrl = images.get(0).url;
            }catch (IndexOutOfBoundsException e) {
                this.mThumbUrl = "";
            }
        }

        this.mFollowers = artist.followers.total;
        this.mName = artist.name;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmThumbUrl() {
        return mThumbUrl;
    }

    public void setmThumbUrl(String mThumbUrl) {
        this.mThumbUrl = mThumbUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmFollowers() {
        return mFollowers;
    }

    public void setmFollowers(int mFollowers) {
        this.mFollowers = mFollowers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.mId);
        parcel.writeString(this.mName);
        parcel.writeString(this.mThumbUrl);
        parcel.writeInt(this.mFollowers);

    }

    /**
     * Reads the artist data from a parcel object
     * @param in
     */
    private void readFromParcel(Parcel in) {

        this.mId = in.readString();
        this.mName = in.readString();
        this.mThumbUrl = in.readString();
        this.mFollowers = in.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AppArtist createFromParcel(Parcel in) {
            return new AppArtist(in);
        }

        public AppArtist[] newArray(int size) {
            return new AppArtist[size];
        }
    };
}
