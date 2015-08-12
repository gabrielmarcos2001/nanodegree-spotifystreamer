package com.gabilamnanodegree.spotifystreaming.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gabilamnanodegree.spotifystreaming.R;
import com.gabilamnanodegree.spotifystreaming.ui.components.FixedListView;
import com.gabilamnanodegree.spotifystreaming.ui.components.ViewEmptyList;
import com.gabilamnanodegree.spotifystreaming.utils.UtilsDpi;

/**
 * Created by gabrielmarcos on 9/8/14.
 */
public abstract class FragmentBaseListWithHeader extends BaseFragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener{

    private final int REFRESH_LAYOUT_OFFSET = 50;

    /**
     * Header View - displayed on top of the ViewPager
     */
    protected View mHeader;
    protected View mShadow;
    protected FixedListView mListView;
    protected ViewEmptyList mEmptyView;
    protected SwipeRefreshLayout mRefreshLayout;
    protected int mLastScroll;

    /**
     * Variables to store the height of the header
     */
    protected int mHeaderHeight;
    protected int mMinHeaderTranslation = 0;

    private int mLastScrollY;
    private int mPreviousFirstVisibleItem;
    protected int mScrollThreshold;

    abstract void onScrollUp();

    abstract void onScrollDown();

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        int scrollY = getScrollY(view);

        mHeader.setTranslationY(Math.max(-scrollY, -mMinHeaderTranslation));

        if (mShadow != null) {
            mShadow.setTranslationY(Math.max(-scrollY, -mMinHeaderTranslation));
        }

        if (isSameRow(firstVisibleItem)) {
            int newScrollY = getTopItemScrollY();
            boolean isSignificantDelta = Math.abs(mLastScrollY - newScrollY) > mScrollThreshold;
            if (isSignificantDelta) {
                if (mLastScrollY > newScrollY) {
                    onScrollUp();
                } else {
                    onScrollDown();
                }
            }
            mLastScrollY = newScrollY;
        } else {
            if (firstVisibleItem > mPreviousFirstVisibleItem) {
                onScrollUp();
            } else {
                onScrollDown();
            }

            mLastScrollY = getTopItemScrollY();
            mPreviousFirstVisibleItem = firstVisibleItem;
        }

    }

    public int getScrollY(AbsListView view) {

        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private boolean isSameRow(int firstVisibleItem) {
        return firstVisibleItem == mPreviousFirstVisibleItem;
    }

    private int getTopItemScrollY() {
        if (mListView == null || mListView.getChildAt(0) == null) return 0;
        View topChild = mListView.getChildAt(0);
        return topChild.getTop();
    }

    /**
     * This method initialize Common views, should be called
     * before returning the inflated view in the onCreateView
     * of the fragment
     */
    public void initCommonViews(LayoutInflater inflater) {

        if (mListView != null) {

            this.mListView.setOnItemClickListener(this);
            this.mListView.setOnScrollListener(this);

            if (mEmptyView != null) {
                this.mListView.setEmptyView(mEmptyView);
            }

            // We add a placeholder invisible view as a header to the listview
            View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
            placeHolderView.setPadding(0, mHeaderHeight, 0, 0);
            placeHolderView.setVisibility(View.GONE);
            if (mListView.getHeaderViewsCount() ==0 ) {
                mListView.addHeaderView(placeHolderView);
            }
        }

        if (mRefreshLayout != null) {

            // Sets a refresh
            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshTriggered();
                }
            });

            mRefreshLayout.setProgressViewOffset(false, mHeaderHeight - (int) UtilsDpi.convertDpToPixel(REFRESH_LAYOUT_OFFSET, getActivity()), mHeaderHeight + (int) UtilsDpi.convertDpToPixel(REFRESH_LAYOUT_OFFSET, getActivity()));

        }

    }

    public abstract void refreshTriggered();

}
