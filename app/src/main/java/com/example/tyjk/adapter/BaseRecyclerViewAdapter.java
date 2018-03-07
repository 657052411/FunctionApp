package com.example.tyjk.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;


import com.example.tyjk.activity.R;
import com.example.tyjk.custom.CustomRecyclerView;
import com.example.tyjk.viewholder.FooterViewHolder;
//import com.example.tyjk.viewholder.HeaderViewHolder;
import com.example.tyjk.viewholder.ItemViewHolder;
import com.example.tyjk.viewholder.MyRVViewHolder;

import java.util.List;


/**
 * this is a BaseAdapter for RecyclerView
 * author
 * version 1.0
 * date 2017/6/3.
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends MyRVViewHolder> extends RecyclerView.Adapter<VH> {

    /**
     * click listener
     */
    protected OnRVItemClickListener<T> mOnItemClickListener;
    /**
     * long click listener
     */
    protected OnRVItemLongClickListener<T> mOnItemLongClickListener;
    /**
     * data
     */
    protected List<T> mList;
    protected Context mContext;
    protected int mViewId;
    private FooterViewHolder holder;
    private int mLoadState = 0;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_HEADER = 2;
    public static final int STATE_DEFAULT = 0;   //默认情况
    public static final int STATE_LOADING = 1;     //正在加载
    public static final int STATE_FAILURE = 2;     //加载失败
    public static final int STATE_SUCCESS = 3;     //加载失败
    public static final int STATE_FINISH = 4;      //没有更多数据了

    /**
     * 得到加载的状态
     *
     * @return
     */
    public int getLoadState() {
        return mLoadState;
    }

    public void setLoadState(int state) {
        mLoadState = state;
    }

    public void finishLoad(int state) {
        switch (state) {
            case BaseRecyclerViewAdapter.STATE_SUCCESS:  //加载成功
                mLoadState = STATE_DEFAULT;
                break;
            case BaseRecyclerViewAdapter.STATE_FAILURE:  //加载失败
                mLoadState = STATE_FAILURE;
                setLoadFailView();
                break;
            case BaseRecyclerViewAdapter.STATE_FINISH:   //没有更多数据了
                mLoadState = STATE_FINISH;
                setLoadFinishView();
                break;
        }
    }

    private void setLoadFinishView() {
        holder.setProgressBarVisible(View.GONE);
        holder.setMessage("没有更多数据了o(╯□╰)o");
    }

    private void setLoadFailView() {
        holder.setProgressBarVisible(View.GONE);
        holder.setMessage("加载失败，点击重试");
    }

    /**
     * @param list the datas to attach the adapter
     */
    public BaseRecyclerViewAdapter(Context context, List<T> list, int viewId) {
        mList = list;
        mContext = context;
        mViewId = viewId;
    }

    /**
     * get a item by index
     *
     * @param position
     * @return
     */
    protected T getItem(int position) {
        if (position == mList.size() && mCanLoadMore)
            return null;
        else
            return mList.get(position);
    }

    private boolean mCanLoadMore = false;   //说明是否可以上拉加载

    public void setCanLoadMore(boolean loadMore) {
        mCanLoadMore = loadMore;
    }

    public boolean getCanLoadMore() {
        return mCanLoadMore;
    }

    @Override
    public int getItemCount() {
        if (mCanLoadMore)
            return mList.size() + 1;
        else
            return mList.size() ;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && mCanLoadMore) {
            return TYPE_FOOTER;
        }
//        else if (position == 0) {
//            return TYPE_HEADER;
//        }
        else {
            return TYPE_ITEM;
        }
    }

    /**
     * set a long click listener
     *
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnRVItemLongClickListener<T> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }


    public void setOnItemClickListener(OnRVItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    protected View inflateItemView(ViewGroup viewGroup, int layoutId) {
        return inflateItemView(viewGroup, layoutId, false);
    }

    protected View inflateItemView(ViewGroup viewGroup, int layoutId, boolean attach) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, attach);
    }


    @Override
    public final void onBindViewHolder(VH vh, int position) {
        bindDataToItemView(vh, position);
        bindItemViewClickListener(vh, position);
    }


    protected abstract void bindDataToItemView(VH vh, int position);

    protected final void bindItemViewClickListener(final VH vh, final int position) {
        if (mOnItemClickListener != null) {
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(v, vh.getLayoutPosition());
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onLongClick(v, vh.getLayoutPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mCanLoadMore && viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_foot, parent, false);
            FooterViewHolder holder = new FooterViewHolder(v);
            holder.bindAdapter(this);
            return (VH) holder;
        }
//        else if (viewType==TYPE_HEADER){
//            View v = LayoutInflater.from(mContext).inflate(R.layout.header,parent,false);
//            HeaderViewHolder holder=new HeaderViewHolder(v);
//            return (VH) holder;
//        }
        else {
            View v = LayoutInflater.from(mContext).inflate(mViewId, parent, false);
            ItemViewHolder holder = new ItemViewHolder(v);
            return (VH) holder;
        }
    }


    public void reload() {
        mRecyclerView.startLoadMore();

    }

    private CustomRecyclerView mRecyclerView;

    public void setCustomRecyclerView(CustomRecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public interface OnRVItemClickListener<T> {
        void onClick(View view, int position);
    }

    public interface OnRVItemLongClickListener<T> {
        void onLongClick(View view, int position);
    }
}

