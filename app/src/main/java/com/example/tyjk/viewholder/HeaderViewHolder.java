package com.example.tyjk.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.example.tyjk.activity.R;
import com.example.tyjk.bean.HeaderBean;

/**
 * Created by tyjk on 2017/11/2.
 */

//public class HeaderViewHolder extends MyRVViewHolder implements Holder<HeaderBean.TopStoriesBean>{
//    private ImageView image;
//    private TextView tv;
//    public HeaderViewHolder(View itemView) {
//        super(itemView);
//        image=itemView.findViewById(R.id.image);
//        tv=itemView.findViewById(R.id.tv);
//    }
//
//    @Override
//    public View createView(Context context) {
//        return null;
//    }
//
//    @Override
//    public void UpdateUI(Context context, int position, HeaderBean.TopStoriesBean data) {
//
//        Glide.with(context)
//                .load(data.getImage())
//                .into(image);//设置给ImageView控件
//        tv.setText(data.getTitle());
//        Log.i("---TAG---", data.getTitle());
//    }
//
//    public int getHolderType(){
//        return 2;
//    }
//}
