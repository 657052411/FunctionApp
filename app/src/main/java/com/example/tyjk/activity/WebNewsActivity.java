package com.example.tyjk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by tyjk on 2017/10/23.
 */
public class WebNewsActivity extends Activity {

    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.tv_user)
    TextView tvUser;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_funny)
    TextView tvFunny;
    @Bind(R.id.tv_comment)
    TextView tvComment;
    @Bind(R.id.tv_share)
    TextView tvShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_news);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String icon = intent.getStringExtra("icon");
        String user = intent.getStringExtra("user");
        String content = intent.getStringExtra("content");
        String funny = intent.getStringExtra("funny");
        String comment = intent.getStringExtra("comment");
        String share = intent.getStringExtra("share");

        Log.i("---TAG---", "Item头像地址:" + icon);
        Log.i("---TAG---", "Item用户数据:" + user);
        Log.i("---TAG---", "Item内容数据:" + content);
        Log.i("---TAG---", "Item好笑数据:" + funny);
        Log.i("---TAG---", "Item评论数据:" + comment);
        Log.i("---TAG---", "Item分享数据:" + share);

        if (icon !=null) {
            Glide.with(this)
                    .load(("http:" + icon))
                    .into(ivIcon);
            tvUser.setText(user);
        } else {
            ivIcon.setImageResource(R.mipmap.ic_error);
            tvUser.setText("匿名用户");
        }
        tvContent.setText(content);
        tvFunny.setText(funny);
        tvComment.setText(comment);
        tvShare.setText(share);

    }

}
