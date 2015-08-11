package com.gabilamnanodegree.spotifystreaming.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gabilamnanodegree.spotifystreaming.model.entities.AppArtist;
import com.gabilamnanodegree.spotifystreaming.model.entities.AppTrack;
import com.gabilamnanodegree.spotifystreaming.ui.listItems.ListItemTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielmarcos on 6/1/15.
 *
 * Adapter for displaying a list of tracks
 * into a listview
 *
 */
public class TracksAdapter extends BaseAdapter {

    private List<AppTrack> mAppTracks;
    private Context mContext;

    public TracksAdapter(Context context) {
        this.mAppTracks = new ArrayList<>();
        this.mContext = context;
    }

    public void setmAppTracks(List<AppTrack> mAppTracks) {
        this.mAppTracks = mAppTracks;
        notifyDataSetChanged();
    }

    public List<AppTrack> getAll() {
        return mAppTracks;
    }

    @Override
    public int getCount() {
        return mAppTracks.size();
    }

    @Override
    public Object getItem(int i) {
        try {
            return mAppTracks.get(i);
        }catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        ListItemTrack v = (ListItemTrack)convertView;

        if (v == null) {

            holder = new ViewHolder();

            v = new ListItemTrack(mContext);
            holder.mListItem = v;
            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        if( holder != null ){
            holder.mListItem.setmData(mAppTracks.get(position));
        }

        return v;
    }

    /**
     * ViewHolder pattern
     */
    private class ViewHolder{

        ListItemTrack mListItem;

    }

    /**
     * Clears the tracks
     */
    public void clearTracks() {

        if (mAppTracks != null) {
            mAppTracks.clear();
        }

        notifyDataSetChanged();
    }
}
