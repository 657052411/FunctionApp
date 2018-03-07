package com.example.tyjk.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tyjk.activity.R;
import com.example.tyjk.adapter.BaseRecyclerViewAdapter;
import com.example.tyjk.adapter.VideosAdapter;
import com.example.tyjk.bean.VideoBean;
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
public class VideoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        CustomRecyclerView.CustomLoadMoreListener {
    @Bind(R.id.re_videoview)
    CustomRecyclerView reVideoview;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private VideosAdapter adapter;
    private List<VideoBean.ItemsBean> list = new ArrayList<>();
    private RetrofitApi retrofitApi = new RetrofitApi();
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.videofragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        getVideo();

        return view;
    }

    private void initView() {

        reVideoview.init(getActivity());//设置线性布局和分隔符
        swipeRefreshLayout.setOnRefreshListener(this);//设置下拉刷新监听器
        reVideoview.setLoadMoreListener(this);//设置上拉加载监听器
        adapter = new VideosAdapter(getActivity(), list, R.layout.item_video);
        adapter.setCanLoadMore(true);//true 表示可以上拉加载，false则不能
        adapter.setCustomRecyclerView(reVideoview);
        reVideoview.setRecyclerAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }

    private void getVideo() {
        retrofitApi.getVideosRetrofit()
                .create(ApiService.class)
                .getVideos(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoBean value) {

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

    @Override
    public void loadMore() {
        page++;
        page = page + 1;
        retrofitApi.getVideosRetrofit()
                .create(ApiService.class)
                .getVideos(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoBean value) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
