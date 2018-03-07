package com.example.tyjk.viewholder;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.tyjk.activity.R;
import com.example.tyjk.custom.CustomVideoView;

/**
 * Created by tyjk on 2017/10/16.
 */
public class ItemViewHolder extends MyRVViewHolder {

    private SparseArray<View> views;

    public ItemViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<View>();
    }

    public <T extends View> T getView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    public int getHolderType() {
        return 0;
    }

    public ItemViewHolder setText(int viewId, String value) {
        TextView view = getView(viewId);
        view.setText(value);
        return ItemViewHolder.this;
    }

    public ItemViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return ItemViewHolder.this;
    }

    public ItemViewHolder setImageResource(int viewId, int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return ItemViewHolder.this;
    }

    public ItemViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return ItemViewHolder.this;
    }

    public ItemViewHolder setBackgroundResource(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return ItemViewHolder.this;
    }

    public ItemViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return ItemViewHolder.this;
    }

    public ItemViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return ItemViewHolder.this;
    }

    public ItemViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return ItemViewHolder.this;
    }

    public ItemViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return ItemViewHolder.this;
    }

    public ItemViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return ItemViewHolder.this;
    }

}
