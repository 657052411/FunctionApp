package com.example.tyjk.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tyjk.activity.R;
import com.example.tyjk.adapter.BaseRecyclerViewAdapter;
import com.example.tyjk.adapter.NewsAdapter;
import com.example.tyjk.bean.NewsBean;
import com.example.tyjk.custom.CustomRecyclerView;
import com.example.tyjk.retrofit.RetrofitApi;
import com.example.tyjk.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tyjk on 2017/10/5.
 */
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        CustomRecyclerView.CustomLoadMoreListener {

    @Bind(R.id.re_newsview)
    CustomRecyclerView reNewsview;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private NewsAdapter adapter;
    private List<NewsBean.ItemsBean> list = new ArrayList<>();
    private RetrofitApi retrofitApi = new RetrofitApi();
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newsfragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        getNews();

        return view;
    }

    private void initView() {
        reNewsview.init(getActivity());//设置线性布局和分隔符
        swipeRefreshLayout.setOnRefreshListener(this);  //设置下拉刷新监听器
        reNewsview.setLoadMoreListener(this);    //设置上拉加载监听器
        adapter = new NewsAdapter(getActivity(), list, R.layout.item_news);
        adapter.setCanLoadMore(true);  //设置可以上拉加载，如果设置为false则不能上拉加载
        adapter.setCustomRecyclerView(reNewsview);
        reNewsview.setRecyclerAdapter(adapter);
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

    private void getNews() {
        retrofitApi.getNewsRetrofit()
                .create(ApiService.class)
                .getNews(1)
                .subscribeOn(Schedulers.io())  //事件触发IO
                .observeOn(AndroidSchedulers.mainThread())  //在主线程出来返回的数据
                .subscribe(new Observer<NewsBean>() {       //订阅
                    @Override  //解除订阅
                    public void onSubscribe(Disposable d) {

                    }

                    @Override  //向下执行
                    public void onNext(NewsBean value) {
                        //将数据添加到集合中
                        list.addAll(value.getItems());
                        Log.i("---TAG---", "数据:" + value.getItems());
                    }

                    @Override  //有异常
                    public void onError(Throwable e) {

                    }

                    @Override  //完成时
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        adapter.finishLoad(BaseRecyclerViewAdapter.STATE_SUCCESS);
                    }
                });
    }

    @Override
    public void loadMore() {
        page++;
        page = page + 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                retrofitApi.getNewsRetrofit()
                        .create(ApiService.class)
                        .getNews(page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<NewsBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(NewsBean value) {
                                list.addAll(value.getItems());
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
