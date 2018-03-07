package com.example.tyjk.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.tyjk.activity.PlayerActivity;
import com.example.tyjk.activity.R;
import com.example.tyjk.bean.VideoBean;
import com.example.tyjk.viewholder.FooterViewHolder;
import com.example.tyjk.viewholder.ItemViewHolder;
import com.example.tyjk.viewholder.MyRVViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyjk on 2017/10/24.
 */
public class VideosAdapter extends BaseRecyclerViewAdapter<VideoBean.ItemsBean, MyRVViewHolder> {
    /**
     * @param context
     * @param list    the datas to attach the adapter
     * @param viewId
     */
    private Context mContext;
    private List<VideoBean.ItemsBean> list = new ArrayList<>();
    private int viewId;

    public VideosAdapter(Context context, List<VideoBean.ItemsBean> list, int viewId) {
        super(context, list, viewId);
        this.mContext = context;
        this.list = list;
        this.viewId = viewId;
    }

    @Override
    protected void bindDataToItemView(final MyRVViewHolder myRVViewHolder, final int position) {
        if (myRVViewHolder instanceof ItemViewHolder) {
            ItemViewHolder holder = (ItemViewHolder) myRVViewHolder;
            LinearLayout ll_videos_detail = holder.getView(R.id.ll_videos_detail);
            ImageView img_video = holder.getView(R.id.img_video);
            ImageView img_play = holder.getView(R.id.img_play);

            final VideoBean.ItemsBean index = list.get(position);
            holder.setText(R.id.tv_video, index.getContent());
            Glide.with(mContext).load(index.getPic_url()).into(img_video);
            img_play.setImageResource(R.mipmap.xadsdk_ad_play);
            Log.i("---TAG---", "图片url:" + index.getPic_url());
            ll_videos_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, PlayerActivity.class);
                    intent.putExtra("videoUrl", index.getHigh_url());
                    intent.putExtra("pictureUrl", index.getPic_url());
                    mContext.startActivity(intent);
                }
            });

            img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, PlayerActivity.class);
                    intent.putExtra("videoUrl", index.getHigh_url());
                    intent.putExtra("pictureUrl", index.getPic_url());
                    mContext.startActivity(intent);
                }
            });
        } else if (myRVViewHolder instanceof FooterViewHolder) {

        }
    }
}
