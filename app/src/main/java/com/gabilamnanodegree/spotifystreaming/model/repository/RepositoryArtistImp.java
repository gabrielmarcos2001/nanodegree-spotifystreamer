package com.gabilamnanodegree.spotifystreaming.model.repository;

import android.content.Context;
import android.os.Handler;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetArtistsByName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by gabrielmarcos on 6/1/15.
 *
 * Implementation of the artist repository
 * Used for Artist related requests
 *
 */
public class RepositoryArtistImp extends RepositoryBase implements RepositoryArtist {

    private SpotifyService mSpotify;

    /**
     * Constructor
     */
    public RepositoryArtistImp(Context context) {

        super(context);

        SpotifyApi api = new SpotifyApi();
        mSpotify = api.getService();

    }

    @Override
    public List<AppArtist> get(String name) {

        try {
            ArtistsPager artistsPager = mSpotify.searchArtists(name);

            ArrayList<AppArtist> result = new ArrayList<>();

            List<Artist> artistsFetched =  artistsPager.artists.items;
            for (Artist artist : artistsFetched) {
                result.add(new AppArtist(artist));
            }

            return result;
        }catch (Exception e) {
            return  null;
        }
    }

    @Override
    public void getAsync(String name, final GetArtistsByName.Callback callback) {

        mSpotify.searchArtists(name, new retrofit.Callback<ArtistsPager>() {

            @Override
            public void success(ArtistsPager artistsPager, Response response) {

                final ArrayList<AppArtist> result = new ArrayList<>();

                // Gets the list of artists returned by the API
                List<Artist> artistsFetched =  artistsPager.artists.items;
                for (Artist artist : artistsFetched) {
                    result.add(new AppArtist(artist));
                }

                Handler mainHandler = new Handler(mContext.getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {

                        // We want to return the result in the main thread
                        callback.onArtistsFetched(result);
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
