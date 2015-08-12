package com.gabilamnanodegree.spotifystreaming.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gabilamnanodegree.spotifystreaming.model.cache.SPCacheImp;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.ui.listItems.ListItemArtist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 *
 * Adapter for displaying a list
 * of Artists into a listview
 *
 */
public class ArtistAdapter extends BaseAdapter {

    private List<AppArtist> mAppArtists;
    private Context mContext;

    private int mSelectedItem = -1; // Stores the selected position of the item in the list

    public ArtistAdapter(Context context) {
        this.mAppArtists = new ArrayList<>();
        this.mContext = context;
    }

    public void setmAppArtists(List<AppArtist> mAppArtists) {
        this.mAppArtists = mAppArtists;

        // Checks if the user has a selected artist
        AppArtist selectedArtist = new SPCacheImp().getSelectedArtist();

        if (selectedArtist != null) {

            mSelectedItem = -1;

            // Updates the selected item index bases on the user selection
            for (int i=0; i< mAppArtists.size(); i++) {
                if (mAppArtists.get(i).getmId().equals(selectedArtist.getmId())) {
                    mSelectedItem = i;
                }
            }
        }else {
            mSelectedItem = -1;
        }

        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return mAppArtists.size();
    }

    @Override
    public Object getItem(int i) {
        try {
            return mAppArtists.get(i);
        }catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Clears the adapter
     */
    public void clear() {
        mSelectedItem = -1;
        mAppArtists.clear();
        notifyDataSetChanged();
    }

    /**
     * Returns all the artists currently
     * in the adapter
     * @return
     */
    public List<AppArtist> getAll() {
        return mAppArtists;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        ListItemArtist v = (ListItemArtist)convertView;

        if (v == null) {

            holder = new ViewHolder();

            v = new ListItemArtist(mContext);
            holder.mListItem = v;
            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        if( holder != null ){

            boolean selected = false;
            if (position == mSelectedItem) {
                selected = true;
            }
            holder.mListItem.setmData(mAppArtists.get(position),selected);
        }

        return v;
    }

    /**
     * ViewHolder pattern
     */
    private class ViewHolder{

        ListItemArtist mListItem;

    }

    public void setmSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
    }
}
