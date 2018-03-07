package com.example.tyjk.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.example.tyjk.activity.R;
import com.example.tyjk.bean.HeaderBean;

/**
 * Created by tyjk on 2017/10/17.
 * <p>
 * 展示图片轮播数据
 */
public class TopViewHolder implements Holder<HeaderBean.TopStoriesBean> {
    private ImageView image;
    private TextView tv;

    @Override
    public View createView(Context context) {
        //实例化一个布局
        View view = LayoutInflater.from(context).inflate(R.layout.header, null);
        image = view.findViewById(R.id.image);
        tv = view.findViewById(R.id.tv);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, HeaderBean.TopStoriesBean data) {
        //加载图片
        Glide.with(context).load(data.getImage()).into(image);//设置给ImageView控件
        tv.setText(data.getTitle());
        Log.i("---TAG---", data.getTitle());
    }

}
