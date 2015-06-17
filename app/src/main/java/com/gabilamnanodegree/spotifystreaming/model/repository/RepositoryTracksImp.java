package com.gabilamnanodegree.spotifystreaming.model.repository;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetTopTracksForArtist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by gabrielmarcos on 6/1/15.
 *
 * Implementation of the Tracks Repository
 * Used for Tracks related requests
 *
 */
public class RepositoryTracksImp extends RepositoryBase implements RepositoryTracks {

    private SpotifyService mSpotify;

    /**
     * Constructor
     */
    public RepositoryTracksImp(Context context) {

        super(context);

        SpotifyApi api = new SpotifyApi();
        mSpotify = api.getService();
    }

    @Override
    public List<AppTrack> get(String artistId) {

        Map<String,Object> parameters = new HashMap<>();

        String country = Locale.getDefault().getCountry();

        if (country.equals("")) {
            country = "US";
        }

        parameters.put("country", country);

        Tracks tracks = mSpotify.getArtistTopTrack(artistId, parameters);

        ArrayList<AppTrack> result = new ArrayList<>();

        List<Track> tracksFetched =  tracks.tracks;
        for (Track track : tracksFetched) {
            result.add(new AppTrack(track));
        }

        return result;
    }

    @Override
    public void getAsync(String artistId, final GetTopTracksForArtist.Callback callback) {

        // Initialize the parameters object - Country is mandatory
        // in this case.
        Map<String,Object> parameters = new HashMap<>();
        String country = Locale.getDefault().getCountry();

        if (country.equals("")) {
            country = "US";
        }

        parameters.put("country", country);

        mSpotify.getArtistTopTrack(artistId, parameters, new retrofit.Callback<Tracks>() {

            @Override
            public void success(Tracks tracks, Response response) {

                final ArrayList<AppTrack> result = new ArrayList<>();

                // Gets the list of tracks returned by the API
                List<Track> tracksFetched =  tracks.tracks;
                for (Track track : tracksFetched) {
                    result.add(new AppTrack(track));
                }

                Handler mainHandler = new Handler(mContext.getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {

                        // We want to return the result in the main thread
                        callback.onTopTracksFetched(result);
                    }
                };

                mainHandler.post(myRunnable);
            }

            @Override
            public void failure(final RetrofitError error) {

                Handler mainHandler = new Handler(mContext.getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {

                        // We want to return the result in the main thread
                        callback.onError(error.getMessage());
                    }
                };

                mainHandler.post(myRunnable);

            }
        });

    }

    @Override
    public void clean() {
        mSpotify = null;
    }
}
