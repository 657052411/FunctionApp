package com.example.tyjk.custom;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.example.tyjk.adapter.BaseRecyclerViewAdapter;

/**
 * Created by tyjk on 2017/10/16.
 *    自定义RecyclerView
 */
public class CustomRecyclerView extends RecyclerView {
    private LinearLayoutManager layoutManager;
    private BaseRecyclerViewAdapter adapter;
    private CustomLoadMoreListener loadListener;
    private int lastVisibleItem;

    public CustomRecyclerView(Context context) {
        super(context);
        initView();
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface CustomLoadMoreListener {
        //加载更多
        void loadMore();
    }

    public void setLoadMoreListener(CustomLoadMoreListener loadListener) {
        this.loadListener = loadListener;
    }

    public void startLoadMore() {
        loadListener.loadMore();
    }

    public void setRecyclerAdapter(BaseRecyclerViewAdapter adapter) {
        this.adapter = adapter;
        setAdapter(adapter);
    }

    //设置线性布局和分隔符
    public void init(Context context) {
        this.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        this.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    private void initView() {

        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == layoutManager.getItemCount()) {
                    if (loadListener != null && adapter.getCanLoadMore()
                            && adapter.getLoadState() == BaseRecyclerViewAdapter.STATE_DEFAULT) {
                        loadListener.loadMore();
                        adapter.setLoadState(BaseRecyclerViewAdapter.STATE_LOADING);
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                layoutManager = (LinearLayoutManager) getLayoutManager();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }
}
