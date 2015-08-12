package com.gabilamnanodegree.spotifystreaming.model.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.ui.SpotifyStreamerApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielmarcos on 8/12/15.
 *
 * Implementation of the cache interface using the shared preferences
 *
 */
public class SPCacheImp implements Cache {

    private final String KEY_SEARCH_TERM = ".searchTerm";
    private final String KEY_SELECTED_ARTIST = ".selectedArtist";
    private final String KEY_ARTISTS_FETCHED = ".artistsFetched";
    private final String KEY_TRACKS = ".tracks";

    /**
     * The information stored in the shared preferences is encrypted
     */
    private SharedPreferences mSecurePrefs;

    private Context mContext;

    /**
     * Constructor
     */
    public SPCacheImp() {

        this.mContext = SpotifyStreamerApplication.getInstance().getApplicationContext();
        mSecurePrefs = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);

    }


    @Override
    public void setSelectedArtist(AppArtist artist) {

        // Stores the object data as a json
        try {

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(artist);

            SharedPreferences.Editor editor = mSecurePrefs.edit();
            editor.putString(KEY_SELECTED_ARTIST, json);
            editor.apply();

        }catch (JsonProcessingException e) {
        }
    }

    @Override
    public AppArtist getSelectedArtist() {

        String jsonObject = mSecurePrefs.getString(KEY_SELECTED_ARTIST, null);

        if (jsonObject != null) {

            try {

                // Returns the artist from the cached data
                ObjectMapper mapper = new ObjectMapper();
                AppArtist artist = mapper.readValue(jsonObject, AppArtist.class);
                return artist;

            }catch (JsonProcessingException e) {
            }catch (IOException e) {
            }

        }

        return null;
    }

    @Override
    public void setSearchTerms(String searchTerms) {

        SharedPreferences.Editor editor = mSecurePrefs.edit();
        editor.putString(KEY_SEARCH_TERM, searchTerms);
        editor.apply();
    }

    @Override
    public String getSearchTerms() {
        return mSecurePrefs.getString(KEY_SEARCH_TERM, "");
    }

    @Override
    public void setTracks(List<AppTrack> tracks) {

        // Stores the object data as a json
        try {

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(tracks);

            SharedPreferences.Editor editor = mSecurePrefs.edit();
            editor.putString(KEY_TRACKS, json);
            editor.apply();

        }catch (JsonProcessingException e) {

        }
    }

    @Override
    public List<AppTrack> getTracks() {

        String jsonObject = mSecurePrefs.getString(KEY_TRACKS, null);

        if (jsonObject != null) {

            try {

                // Returns the tracks from the cached data
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<AppTrack> tracks = mapper.readValue(jsonObject, new TypeReference<List<AppTrack>>(){});
                return tracks;

            }catch (JsonProcessingException e) {

            }catch (IOException e) {

            }

        }

        return new ArrayList<>();
    }

    @Override
    public void setArtistsFetched(List<AppArtist> artists) {

        // Stores the object data as a json
        try {

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(artists);

            SharedPreferences.Editor editor = mSecurePrefs.edit();
            editor.putString(KEY_ARTISTS_FETCHED, json);
            editor.apply();

        }catch (JsonProcessingException e) {

        }
    }

    @Override
    public List<AppArtist> getArtistsFetched() {

        String jsonObject = mSecurePrefs.getString(KEY_ARTISTS_FETCHED, null);

        if (jsonObject != null) {

            try {

                // Returns the artist from the cached data
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<AppArtist> artists = mapper.readValue(jsonObject, new TypeReference<List<AppArtist>>(){});
                return artists;

            }catch (JsonProcessingException e) {

            }catch (IOException e) {

            }

        }

        return new ArrayList<>();
    }

    @Override
    public void clearSelectedArtist() {
        SharedPreferences.Editor editor = mSecurePrefs.edit();
        editor.putString(KEY_SELECTED_ARTIST, null);
        editor.putString(KEY_TRACKS,null);
        editor.apply();
    }

    @Override
    public void clearFetchedArtists() {
        SharedPreferences.Editor editor = mSecurePrefs.edit();
        editor.putString(KEY_ARTISTS_FETCHED, null);
        editor.apply();
    }
}
