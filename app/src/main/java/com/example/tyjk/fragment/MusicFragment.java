package com.example.tyjk.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tyjk.activity.R;
import com.example.tyjk.adapter.MusicAdapter;
import com.example.tyjk.bean.Music;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tyjk on 2017/10/5.
 */
public class MusicFragment extends Fragment {
    @Bind(R.id.btnPlaybackMode)
    Button tvPlaybackMode;
    @Bind(R.id.btnPrevious)
    Button tvPrevious;
    @Bind(R.id.btnStartOrStop)
    Button tvStartOrStop;
    @Bind(R.id.btnNext)
    Button tvNext;
    @Bind(R.id.btnPlayList)
    Button tvPlayList;
    @Bind(R.id.tvCurrentTime)
    TextView tvCurrentTime;
    @Bind(R.id.tvTotalTime)
    TextView tvTotalTime;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.rePlayList)
    RecyclerView rePlayList;

    private File file;
    private String sdCard;
    private String music="Music/"+"tonghuazhen.mp3";
    private MediaPlayer mediaPlayer;

    private ArrayList<Music> list; //音乐列表
    private MusicAdapter adapter;

    private int count = 0;//记录暂停与播放按钮的次数
    private int duration = 0; //获取时长(ms)
    private int current = 0; //当前音乐播放位置
    private boolean isPause = false;//是否暂停状态
    private boolean isOver = false; //是否播放完毕
    String fileName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.musicfragment, container, false);
        ButterKnife.bind(this, view);
        boolOpenMedia();
        file = new File(Environment.getExternalStorageDirectory(), music);
        Log.i("TAG","根目录:"+Environment.getExternalStorageDirectory());
        sdCard = file.getAbsolutePath();
        Log.i("TAG","sdCard目录:"+file.getAbsolutePath());
        mediaPlayer = new MediaPlayer();
        return view;
    }
    private void boolOpenMedia(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.MEDIA_CONTENT_CONTROL)  //打开相机权限
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)   //可读
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)  //可写
                        != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.MEDIA_CONTENT_CONTROL,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);}
    }

    @OnClick({R.id.btnPlaybackMode, R.id.btnPrevious, R.id.btnStartOrStop, R.id.btnNext, R.id.btnPlayList})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnPlaybackMode://播放模式
                Toast.makeText(getActivity(), "还没实现播放模式切换功能",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnPrevious://上一首
                Toast.makeText(getActivity(), "还没实现播放上一首功能",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnStartOrStop://暂停与播放
                if (count == 0) {//第一次进入时,从头开始播放
                    play(0);
                    tvStartOrStop.setBackgroundResource(R.mipmap.a2);
                    count++;
                } else { //不是第一次进入时
                    if (mediaPlayer.isPlaying() && !isPause) {//如果播放状态,就暂停
                        mediaPlayer.pause();//暂停
                        isPause = true;
                        tvStartOrStop.setBackgroundResource(R.mipmap.a3);
                    } else { //如果暂停状态,就播放
                        mediaPlayer.start();//播放
                        isPause = false;
                        tvStartOrStop.setBackgroundResource(R.mipmap.a2);
                    }
                    count++;
                }
                break;
            case R.id.btnNext://下一首
                Toast.makeText(getActivity(), "还没实现播放下一首功能",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnPlayList://播放列表
                Toast.makeText(getActivity(), "还没实现展示播放列表功能",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void play(int position) {
        try {
            mediaPlayer.reset();//把各项参数恢复到初始状态
            mediaPlayer.setDataSource(sdCard);
            mediaPlayer.prepare();//进行缓冲,准备播放
            mediaPlayer.setOnPreparedListener(new PreparedListener(position));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class PreparedListener implements MediaPlayer.OnPreparedListener {
        private final int position;

        private PreparedListener(int position) {
            this.position = position;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            if (position == 0) {
                mediaPlayer.start();// 播放音乐
            } else {
                mediaPlayer.seekTo(position);//从当前位置开始播放
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }

    }
}
