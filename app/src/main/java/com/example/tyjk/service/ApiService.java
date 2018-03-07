package com.example.tyjk.service;

import com.example.tyjk.bean.FilmBean;
import com.example.tyjk.bean.HeaderBean;
import com.example.tyjk.bean.NewsBean;
import com.example.tyjk.bean.VideoBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tyjk on 2017/10/11.
 */
public interface ApiService {

    // http://news-at.zhihu.com/api/4/news/latest(轮播图)
    // http://api.douban.com/v2/movie/top250?start=0&count=10(豆瓣影视)
    // http://v.juhe.cn/toutiao/index?key=d78b502268f7456b79fbe7228cecdd46(新闻数据)
    // http://m2.qiushibaike.com/article/list/text?page=1(趣闻数据)
    // http://m2.qiushibaike.com/article/list/video?page=1(视频数据)

    @GET("latest")
    Observable<HeaderBean> getHeader();

    @GET("top250?")
    Observable<FilmBean> getFilm(@Query("start") int start, @Query("count") int count);

//    @GET("toutiao/index?key=d78b502268f7456b79fbe7228cecdd46")
//    Observable<NewsBean> getNews(@Query("type") String type);

    @GET("text?page=1")
    Observable<NewsBean> getNews(@Query("page") int page);

    @GET("video?page=1")
    Observable<VideoBean>getVideos(@Query("page")int page);

}
