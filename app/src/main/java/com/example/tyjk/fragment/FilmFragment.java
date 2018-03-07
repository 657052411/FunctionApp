package com.example.tyjk.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.example.tyjk.activity.R;
import com.example.tyjk.adapter.BaseRecyclerViewAdapter;
import com.example.tyjk.adapter.FilmAdapter;
import com.example.tyjk.bean.FilmBean;
import com.example.tyjk.bean.HeaderBean;
import com.example.tyjk.custom.CustomRecyclerView;
import com.example.tyjk.retrofit.RetrofitApi;
import com.example.tyjk.service.ApiService;
import com.example.tyjk.viewholder.TopViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tyjk on 2017/10/11.
 */
public class FilmFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        CustomRecyclerView.CustomLoadMoreListener {
    @Bind(R.id.Banner)
    ConvenientBanner Banner;
    @Bind(R.id.re_filmView)
    CustomRecyclerView reFilmView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private FilmAdapter adapter;
    private List<FilmBean.SubjectsBean> list = new ArrayList<>();
    private List<HeaderBean.TopStoriesBean> lists = new ArrayList<>();
    private RetrofitApi retrofitApi = new RetrofitApi();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filmfragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        getHeaderdata();
        getFilm();

        return view;
    }

    private void initView() {
        reFilmView.init(getActivity());//设置线性布局和分隔符
        swipeRefreshLayout.setOnRefreshListener(this);  //设置下拉刷新监听器
        reFilmView.setLoadMoreListener(this);    //设置上拉加载监听器
        adapter = new FilmAdapter(getActivity(), list, R.layout.item_film);
        adapter.setCanLoadMore(true);  //设置可以上拉加载，如果设置为false则不能上拉加载
        adapter.setCustomRecyclerView(reFilmView);//绑定适配器
        reFilmView.setRecyclerAdapter(adapter);//设置适配器
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //处理下拉刷新
                swipeRefreshLayout.setRefreshing(false);  //下拉刷新结束
            }
        }, 2000);
    }

    /**
     * 加载图片轮播数据
     */
    private void getHeaderdata() {
        retrofitApi.getHeaderRetrofit()
                .create(ApiService.class)
                .getHeader()
                .subscribeOn(Schedulers.io())  //事件触发IO
                .observeOn(AndroidSchedulers.mainThread())  //在主线程出来返回的数据
                .subscribe(new Observer<HeaderBean>() {       //订阅
                    @Override  //解除订阅
                    public void onSubscribe(Disposable d) {

                    }

                    @Override  //向下执行
                    public void onNext(HeaderBean value) {
                        //将数据添加到集合中
                        lists.addAll(value.getTop_stories());
                        //list_1=value.getTop_stories();用这种方法也一样
                    }

                    @Override  //有异常
                    public void onError(Throwable e) {

                    }

                    @Override  //完成时
                    public void onComplete() {

                        Banner.setPages(new CBViewHolderCreator<TopViewHolder>() { //设置页数
                            @Override
                            public TopViewHolder createHolder() {
                                return new TopViewHolder();
                            }
                        }, lists)
                                //添加指示器
                                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                                //设置指示器的方向
                                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
                    }
                });
    }

    /**
     * 加载电影内容数据
     */
    private void getFilm() {
        retrofitApi.getFilmRetrofit()
                .create(ApiService.class)
                .getFilm(0, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FilmBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FilmBean value) {
                        list.addAll(value.getSubjects());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        adapter.finishLoad(BaseRecyclerViewAdapter.STATE_SUCCESS);
                    }
                });
    }

    /**
     * 加载更多数据
     */
    @Override
    public void loadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                retrofitApi.getFilmRetrofit()
                        .create(ApiService.class)
                        .getFilm(list.size() + 1, 10)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<FilmBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(FilmBean value) {
                                list.addAll(value.getSubjects());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                swipeRefreshLayout.setRefreshing(false);
                                adapter.notifyItemRemoved(adapter.getItemCount());
                                adapter.finishLoad(BaseRecyclerViewAdapter.STATE_SUCCESS);
                            }
                        });
            }
        }, 2000);
    }

    @Override
    public void onResume() {
        super.onResume();
        Banner.startTurning(3000);//设置轮播间隔时间
    }

    @Override
    public void onPause() {
        super.onPause();
        Banner.stopTurning();//停止轮播
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}