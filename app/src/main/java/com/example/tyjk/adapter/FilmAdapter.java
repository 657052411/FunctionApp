package com.example.tyjk.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tyjk.activity.R;
import com.example.tyjk.activity.WebFilmActivity;
import com.example.tyjk.activity.WebNewsActivity;
import com.example.tyjk.bean.FilmBean;
import com.example.tyjk.viewholder.FooterViewHolder;
//import com.example.tyjk.viewholder.HeaderViewHolder;
import com.example.tyjk.viewholder.ItemViewHolder;
import com.example.tyjk.viewholder.MyRVViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyjk on 2017/10/17.
 */
public class FilmAdapter extends BaseRecyclerViewAdapter<FilmBean.SubjectsBean, MyRVViewHolder> {
    /**
     * @param context
     * @param list    the datas to attach the adapter
     * @param viewId
     */
    private Context context;
    private List<FilmBean.SubjectsBean> list = new ArrayList<>();
    private int viewId;

    public FilmAdapter(Context context, List<FilmBean.SubjectsBean> list, int viewId) {
        super(context, list, viewId);
        this.context = context;
        this.list = list;
        this.viewId = viewId;
    }

    @Override
    protected void bindDataToItemView(MyRVViewHolder myRVViewHolder, final int position) {

        if (myRVViewHolder instanceof ItemViewHolder) {
            ItemViewHolder holder = (ItemViewHolder) myRVViewHolder;
            View itemView = ((ItemViewHolder) myRVViewHolder).itemView;
            ImageView imageView = holder.getView(R.id.iv_item);
            FilmBean.SubjectsBean index=list.get(position);
            Glide.with(context).load(index.getImages().getLarge()).into(imageView);
            holder.setText(R.id.tv_item1, index.getTitle());
            holder.setText(R.id.tv_item2, index.getDirectors().get(0).getName());
            holder.setText(R.id.tv_item3, index.getCasts().get(0).getName());
            holder.setText(R.id.tv_item4, index.getGenres().get(0));
            holder.setText(R.id.tv_item5, index.getYear());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.ll_film_detail:
                            Intent intent = new Intent();//第一种传递方法
                            intent.setClass(context, WebFilmActivity.class);
                            intent.putExtra("url", list.get(position).getAlt());
//                            Log.i("---TAG---","要传的值:"+list.get(position).getAlt());
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
