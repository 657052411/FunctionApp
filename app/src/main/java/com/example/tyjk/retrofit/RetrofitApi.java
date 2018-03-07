package com.example.tyjk.retrofit;

import com.example.tyjk.constants.Constant;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tyjk on 2017/10/17.
 */
public class RetrofitApi {
    public Retrofit retrofit;

    /**
     * 轮播图的Retrofit
     *
     * @return
     */
    public Retrofit getHeaderRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.HEADER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 电影数据的Retrofit
     *
     * @return
     */
    public Retrofit getFilmRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.FILM_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 趣闻数据的Retrofit
     *
     * @return
     */
    public Retrofit getNewsRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 视频数据的Retrofit
     *
     * @return
     */
    public Retrofit getVideosRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

}
