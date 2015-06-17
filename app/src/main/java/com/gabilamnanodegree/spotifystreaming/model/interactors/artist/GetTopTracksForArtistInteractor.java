package com.gabilamnanodegree.spotifystreaming.model.interactors.artist;

import android.os.AsyncTask;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.model.interactors.BaseInteractor;
import com.gabilamnanodegree.spotifystreaming.model.repository.RepositoryTracks;

import java.util.List;

/**
 * Created by gabrielmarcos on 6/15/15.
 */
public class GetTopTracksForArtistInteractor extends BaseInteractor implements GetTopTracksForArtist {

    private RepositoryTracks mRepository;
    private GetTopTracksForArtist.Callback mCallback;
    private AsyncLoader mLoader;

    /**
     * Constructor
     * @param mRepository
     */
    public GetTopTracksForArtistInteractor(RepositoryTracks mRepository) {
        this.mRepository = mRepository;
    }

    /**
     * AsyncTask for getting the data in a background trhead
     */
    class AsyncLoader extends AsyncTask<String, Integer, List<AppTrack>> {

        @Override
        protected List<AppTrack> doInBackground(String... params) {
            return mRepository.get(params[0]);
        }

        @Override
        protected void onPostExecute(List<AppTrack> artists) {

            if (isCancelled() || artists == null) {
                return;
            }

            mCallback.onTopTracksFetched(artists);

        }

    }

    @Override
    public void execute(String artistId, Callback callback) {
        if (mLoader != null) mLoader.cancel(true);

        this.mLoader = new AsyncLoader();
        mCallback = callback;
        mLoader.execute(artistId);
    }

    @Override
    public void cancel() {
        if (mLoader != null)mLoader.cancel(true);
    }
}
