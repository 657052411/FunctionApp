package com.example.tyjk.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tyjk.activity.R;
import com.example.tyjk.activity.WebNewsActivity;
import com.example.tyjk.bean.NewsBean;
import com.example.tyjk.viewholder.FooterViewHolder;
import com.example.tyjk.viewholder.ItemViewHolder;
import com.example.tyjk.viewholder.MyRVViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyjk on 2017/10/17.
 */
public class NewsAdapter extends BaseRecyclerViewAdapter<NewsBean.ItemsBean, MyRVViewHolder> {
    /**
     * @param context
     * @param list    the datas to attach the adapter
     * @param viewId
     */
    private Context context;
    private List<NewsBean.ItemsBean> lists = new ArrayList<>();
    private int viewId;

    public NewsAdapter(Context context, List<NewsBean.ItemsBean> list, int viewId) {
        super(context, list, viewId);
        this.context = context;
        this.lists = list;
        this.viewId = viewId;
    }

    @Override
    protected void bindDataToItemView(final MyRVViewHolder myRVViewHolder, final int position) {

        if (myRVViewHolder instanceof ItemViewHolder) {
            ItemViewHolder holder = (ItemViewHolder) myRVViewHolder;
            View itemView = ((ItemViewHolder) myRVViewHolder).itemView;
            final ImageView imageView = holder.getView(R.id.iv_news_detail_pic);
            final NewsBean.ItemsBean index=lists.get(position);
            //设置头像和用户名
            final NewsBean.ItemsBean.UserBean user = index.getUser();
            if (user != null) {
                String url = user.getThumb();
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(("http:" + url)).into(imageView);//图片加载
                Log.i("---TAG---", "头像地址:" + user.getThumb());
                holder.setText(R.id.tv_user, index.getUser().getLogin());
                Log.i("---TAG---", "用户名:" + user.getLogin());
            } else {
                imageView.setVisibility(View.VISIBLE);
                holder.setImageResource(R.id.iv_news_detail_pic, R.mipmap.ic_error);
                holder.setText(R.id.tv_user, "匿名用户");
            }

            holder.setText(R.id.tv_news_detail_title, index.getContent());
            holder.setText(R.id.tv_funny, index.getVotes().getUp() + "");
            holder.setText(R.id.tv_comment, index.getComments_count() + "");
            holder.setText(R.id.tv_share, index.getShare_count() + "");
            Log.i("---TAG---", "内容数据:" + index.getContent());
            Log.i("---TAG---", "好笑数据:" + index.getVotes().getUp() + "");
            Log.i("---TAG---", "评论数据:" + index.getComments_count() + "");
            Log.i("---TAG---", "分享数据:" + index.getShare_count() + "");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.ll_news_detail:
                            Intent intent = new Intent();//第一种传递方法
                            intent.setClass(context, WebNewsActivity.class);
                            if (user != null) {
                                intent.putExtra("user", user.getLogin());
                                intent.putExtra("icon", user.getThumb());
                            } else {
                                intent.putExtra("user", "匿名用户");
                                intent.putExtra("icon", R.mipmap.ic_error);
                            }
                            intent.putExtra("content", index.getContent());
                            intent.putExtra("funny", index.getVotes().getUp() + "");
                            intent.putExtra("comment", index.getComments_count() + "");
                            intent.putExtra("share", index.getShare_count() + "");
                            context.startActivity(intent);
                            break;
                    }
                }
            });

        } else if (myRVViewHolder instanceof FooterViewHolder) {

        }
//        else if (myRVViewHolder instanceof HeaderViewHolder){
//
//        }

    }
}
