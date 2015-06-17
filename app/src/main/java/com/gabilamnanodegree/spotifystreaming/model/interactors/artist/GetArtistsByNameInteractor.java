package com.gabilamnanodegree.spotifystreaming.model.interactors.artist;

import android.os.AsyncTask;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.interactors.BaseInteractor;
import com.gabilamnanodegree.spotifystreaming.model.repository.RepositoryArtist;

import java.util.List;


/**
 * Created by gabrielmarcos on 6/4/15.
 *
 * Interactor implementation used for getting a list of artists
 * by its name using an async task
 *
 */
public class GetArtistsByNameInteractor extends BaseInteractor implements GetArtistsByName {

    private RepositoryArtist mRepository;
    private GetArtistsByName.Callback mCallback;
    private AsyncLoader mLoader;

    /**
     * Constructor
     * @param mRepository
     */
    public GetArtistsByNameInteractor(RepositoryArtist mRepository) {
        this.mRepository = mRepository;
    }

    /**
     * AsyncTask for getting the data in a background trhead
     */
    class AsyncLoader extends AsyncTask<String, Integer, List<AppArtist>> {

        @Override
        protected List<AppArtist> doInBackground(String... params) {
            return mRepository.get(params[0]);
        }

        @Override
        protected void onPostExecute(List<AppArtist> artists) {

            if (isCancelled() || artists == null) {
                return;
            }

            mCallback.onArtistsFetched(artists);

        }

    }

    /**
     * Executes the Async Loader
     * @param callback
     */
    public void execute(String artistName, GetArtistsByName.Callback callback) {

        if (mLoader != null) mLoader.cancel(true);

        this.mLoader = new AsyncLoader();
        mCallback = callback;
        mLoader.execute(artistName);
    }

    @Override
    public void cancel() {
        if (mLoader != null)mLoader.cancel(true);
    }
}
