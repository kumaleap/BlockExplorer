package com.tdp.blockexplorer.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
/**
 * Created by ninghui on 2016/4/11.
 */
public class LoadMoreRecycleView extends RecyclerView{

    /**
     * 最后一个可见条目的position
     */
    private int lastVisibleItem;
    /**
     * 是否正在执行加载更多标志，防止重复加载
     */
    private boolean mIsLoadingMore = false;
    private LinearLayoutManager mLayoutManager;
    private Adapter mAdapter;

    public LoadMoreRecycleView(Context context) {
        super(context);
    }

    public LoadMoreRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (!mIsLoadingMore && mOnLoadMoreListener != null && state == RecyclerView.SCROLL_STATE_IDLE && mAdapter != null && lastVisibleItem + 1 == mAdapter.getItemCount()) {
            mIsLoadingMore = true;
            mOnLoadMoreListener.loadMore();
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if(mLayoutManager!=null){
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public interface OnLoadMoreListener {
        void loadMore();
    }

    public void setOnLoadMoreListener(LinearLayoutManager mLayoutManager, Adapter mAdapter, OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
        this.mLayoutManager = mLayoutManager;
        this.mAdapter = mAdapter;
    }

    public void setIsLoadingMore(boolean isLoadingMore) {
        this.mIsLoadingMore = isLoadingMore;
    }
}
