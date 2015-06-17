package com.gabilamnanodegree.spotifystreaming.ui.presenter.searchArtist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.interactors.artist.GetArtistsByName;
import com.gabilamnanodegree.spotifystreaming.ui.activity.TopTracksActivity;
import com.gabilamnanodegree.spotifystreaming.ui.view.ViewSearchByArtist;
import com.gabilamnanodegree.spotifystreaming.ui.presenter.PresenterBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 *
 * Presenter implementation for controlling the logic
 * of any Search By Artist View interface
 *
 */
public class PresenterSearchArtistImp extends PresenterBase implements PresenterSearchArtist, GetArtistsByName.Callback {

    private ViewSearchByArtist mView;
    private GetArtistsByName mGetArtistsByNameInteractor;
    private List<AppArtist> mResult;

    public PresenterSearchArtistImp(Context mContext, GetArtistsByName interactor) {
        super(mContext);

        this.mGetArtistsByNameInteractor = interactor;
    }

    @Override
    public void searchArtistByName(String name) {

        if (mView == null) return;

        if (name.equals("")) {

            mView.showEmptyQueryMessage();

        }else {

            // Tells the view to display a loader because this
            // is going to take some time
            mView.showLoader();

            // Gets the list of artists from the repository
            mGetArtistsByNameInteractor.execute(name, this);
        }
    }

    @Override
    public void artistSelected(AppArtist artist) {

        Intent i = new Intent(mContext, TopTracksActivity.class);
        i.putExtra("artist", artist);
        mContext.startActivity(i);

    }

    @Override
    public void initialize() {

    }

    @Override
    public void onViewDestroy() {
        this.mView = null;
    }

    @Override
    public void setView(ViewSearchByArtist view) {

        this.mView = view;

        if (mView != null && mResult != null) {

            // Once the view is set - if we had results stored
            // from a previous query we set these results
            // to the view
            this.mView.showArtistsList(this.mResult);

            if (this.mResult.size() == 0) {
                mView.showEmptyListMessage();
            }
        }
    }

    @Override
    public void onArtistsFetched(List<AppArtist> appArtists) {

        // Stores the results locally
        this.mResult = appArtists;

        if (mView == null) return;

        this.mView.hideLoader();
        this.mView.showArtistsList(appArtists);

        if (appArtists.size() == 0) {
            mView.showEmptyListMessage();
        }
    }

    @Override
    public void onError(String error) {

        this.mResult = null;

        if (mView == null) return;

        this.mView.hideLoader();
        this.mView.showErrorMessage(error);
    }
}
