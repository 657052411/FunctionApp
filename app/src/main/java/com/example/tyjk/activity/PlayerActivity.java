package com.example.tyjk.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tyjk.fragment.VideoFragment;

import java.lang.ref.WeakReference;

import static android.media.AudioManager.STREAM_MUSIC;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

/**
 * Created by tyjk on 2018/1/25.
 */

public class PlayerActivity extends Activity {
    private MediaPlayer mMediaPlayer;
    private AudioManager audioManager;
    private WindowManager.LayoutParams lp;
    private GestureDetector gestureDetector;
    private Surface surface;

    private RelativeLayout texture_layout;
    private FrameLayout frame_voicelight_layout;
    private LinearLayout frame_control_layout;
    private TextureView texture_view;
    private ImageView img_bg, img_play, img_play_pause, img_screen, img_back, img_control;
    private TextView tv_current_time, tv_total_time;
    private SeekBar sb_control_play, sb_control;

    private int currPosition, totalDuration;
    private int curVolum, maxVolum;
    private float startX, startY, endX, endY;
    private boolean isFullScreen;//判断是否全屏
    private static final int UPDATE = 0;
    private static final float FLIP_DISTANCE = 50;
    private static final float FLIP_VELOCITY = 0;
    private String videourl, pictureurl;

    /**
     * 通过handler来更新播放时间
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE) {
                //获取视频当前的播放时间
                currPosition = mMediaPlayer.getCurrentPosition();
                //获取视频的总时间
                totalDuration = mMediaPlayer.getDuration();
                //格式化时间
                tv_current_time.setText(getTimeStr(currPosition));
                tv_total_time.setText(getTimeStr(totalDuration));
                //将相应时间设置给SeekBar
                sb_control_play.setMax(totalDuration);
                sb_control_play.setProgress(currPosition);
                handler.sendEmptyMessageDelayed(UPDATE, 500);
            }
        }
    };

    /**
     * 格式化时间
     *
     * @param totaltime
     * @return
     */
    @SuppressLint("DefaultLocale")
    public String getTimeStr(int totaltime) {
        String time;
        int second = totaltime / 1000;//总秒数
        int hh = second / 3600;// 小时
        int mm = second % 3600 / 60;//分钟
        int ss = second % 60;//秒
        if (hh != 0) {
            time = String.format("%02d:%02d:%02d", hh, mm, ss);//格式为 00:00:00
        } else {
            time = String.format("%02d:%02d", mm, ss);//格式为 00:00
        }
        return time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //VideosAdapter通过Intent将videourl,pictureurl传递给PlayerActivity
        Intent intent = getIntent();
        videourl = intent.getStringExtra("videoUrl");
        pictureurl = intent.getStringExtra("pictureUrl");
        //实例化MediaPlayer
        mMediaPlayer = new MediaPlayer();
        //创建一个手势识别器
        gestureDetector = new GestureDetector(this, new GestureListener());
        //获取当前系统的音量和最大音量
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) throw new AssertionError();
        curVolum = audioManager.getStreamVolume(STREAM_MUSIC);
        maxVolum = audioManager.getStreamMaxVolume(STREAM_MUSIC);
        //通过获取lp当前屏幕亮度
        lp = getWindow().getAttributes();
        //初始化控件
        texture_layout = findViewById(R.id.texture_layout);
        texture_view = findViewById(R.id.texture_view);
        img_bg = findViewById(R.id.img_bg);
        img_play = findViewById(R.id.img_play);
        frame_control_layout = findViewById(R.id.frame_control_layout);
        img_back = findViewById(R.id.img_back);
        img_play_pause = findViewById(R.id.img_play_pause);
        tv_current_time = findViewById(R.id.tv_current_time);
        tv_total_time = findViewById(R.id.tv_total_time);
        sb_control_play = findViewById(R.id.sb_control_play);
        img_screen = findViewById(R.id.img_screen);
        frame_voicelight_layout = findViewById(R.id.frame_voicelight_layout);
        img_control = findViewById(R.id.img_control);
        sb_control = findViewById(R.id.sb_control);
        //给img_bg设置相应的网络背景图片
        if (pictureurl != null) {
            Glide.with(this)
                    .load((pictureurl))
                    .into(img_bg);
        } else {
            img_bg.setImageResource(R.mipmap.ic_error);
        }
        setPlayProgress();//设置播放进度监听
        setSurfaceTexture();//设置TextureView的监听事件,重写4个方法
        /**
         * 设置监听texture_view的触摸事件
         */
        texture_view.setOnTouchListener(new TouchListener());

        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer == null) {
                    Toast.makeText(PlayerActivity.this, "没有视频文件",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (mMediaPlayer.isPlaying()) {
                        img_play_pause.setImageResource(R.drawable.play_disable);
                        frame_control_layout.setVisibility(View.VISIBLE);
                        img_play.setVisibility(View.VISIBLE);
                        mMediaPlayer.pause();
                        handler.removeMessages(UPDATE);
                    } else {
                        img_play_pause.setImageResource(R.drawable.pause_disable);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                frame_control_layout.setVisibility(View.GONE);
                            }
                        }, 5000);//延迟5秒隐藏view
                        img_play.setVisibility(View.GONE);
                        img_bg.setVisibility(View.GONE);
                        mMediaPlayer.start();
                        handler.sendEmptyMessage(UPDATE);
                    }
                }
            }
        });

        img_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer == null) {
                    Toast.makeText(PlayerActivity.this, "没有视频文件",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (mMediaPlayer.isPlaying()) {
                        img_play_pause.setImageResource(R.drawable.play_disable);
                        frame_control_layout.setVisibility(View.VISIBLE);
                        img_play.setVisibility(View.VISIBLE);
                        mMediaPlayer.pause();
                        handler.removeMessages(UPDATE);
                    } else {
                        img_play_pause.setImageResource(R.drawable.pause_disable);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                frame_control_layout.setVisibility(View.GONE);
                            }
                        }, 5000);//延迟5秒隐藏view
                        img_bg.setVisibility(View.GONE);
                        img_play.setVisibility(View.GONE);
                        mMediaPlayer.start();
                        handler.sendEmptyMessage(UPDATE);
                    }
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                finish();
            }
        });

        /**
         * 监听屏幕切换事件
         */
        img_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullScreen) {//全屏
                    img_screen.setImageResource(R.drawable.enlarge);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    img_screen.setImageResource(R.drawable.shrink);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
    }

    private void setSurfaceTexture() {
        texture_view.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            //SurfaceTexture可用
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                surface = new Surface(surfaceTexture);
                new PlayerVideoThread().start();//开启一个线程去播放视频
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {//尺寸改变
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {//销毁
                //执行下面的代码会使整个程序退出(还不知道啥原因...)
//                surface = null;
//                mMediaPlayer.stop();
//                mMediaPlayer.release();
//                mMediaPlayer = null;
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {//更新
            }
        });
    }

    private class PlayerVideoThread extends Thread {
        @Override
        public void run() {
            try {
                mMediaPlayer.reset();//重置MediaPlayer
                mMediaPlayer.setDataSource(videourl);//设置播放资源(可以是应用的资源文件／url／sdcard路径)
                mMediaPlayer.setSurface(surface);//设置渲染画板
                mMediaPlayer.prepare();//异步缓冲播放资源，准备播放
                mMediaPlayer.setScreenOnWhilePlaying(true);//播放时屏幕保持屏幕唤醒
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放类型
                mMediaPlayer.setOnPreparedListener(onPreparedListener);//准备播放监听
                mMediaPlayer.setOnCompletionListener(onCompletionListener);//播放完成监听
                handler.sendEmptyMessage(UPDATE);//更新视频的总时长
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 准备播放的监听事件
     */
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

        }
    };

    /**
     * 播放完成监听事件
     */
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {//播放完成
            img_play_pause.setImageResource(R.drawable.play_disable);
            frame_control_layout.setVisibility(View.VISIBLE);
            img_bg.setVisibility(View.VISIBLE);
            img_play.setVisibility(View.VISIBLE);
            mMediaPlayer.seekTo(0);//播放完毕,将进度条设置为0
            handler.sendEmptyMessage(UPDATE);
        }
    };

    /**
     * 处理在TextureView上滑动屏幕的监听事件
     */
    private class TouchListener implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    gestureDetector.onTouchEvent(e);
                    frame_control_layout.setVisibility(View.VISIBLE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    gestureDetector.onTouchEvent(e);
                    break;
                case MotionEvent.ACTION_UP:
                    gestureDetector.onTouchEvent(e);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            frame_voicelight_layout.setVisibility(View.GONE);
                            frame_control_layout.setVisibility(View.GONE);
                        }
                    }, 5000);//延迟5秒隐藏view
                    break;
            }
            return true;
        }
    }

    /**
     * //将该activity上的触碰事件交给GestureDetector处理
     * //(该方法控制的是整个屏幕的触摸事件)
     * public boolean onTouchEvent(MotionEvent e) {
     * return gestureDetector.onTouchEvent(e);
     * }
     */

    /**
     * 重写GestureDetector.SimpleOnGestureListener类的方法
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {//触碰屏幕时
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {//双击事件
            switch (e.getAction()) {
                case MotionEvent.ACTION_UP:
                    Log.i("MYTAG", "双击抬起...");
                    if (mMediaPlayer.isPlaying()) {
                        img_play_pause.setImageResource(R.drawable.play_disable);
                        img_play.setVisibility(View.VISIBLE);
                        mMediaPlayer.pause();
                        handler.sendEmptyMessage(UPDATE);
                    } else {
                        img_play_pause.setImageResource(R.drawable.pause_disable);
                        img_play.setVisibility(View.GONE);
                        img_bg.setVisibility(View.GONE);
                        mMediaPlayer.start();
                        handler.sendEmptyMessage(UPDATE);
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    Log.i("MYTAG", "双击按下...");
                    break;
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {//滑动手势
            startX = e1.getX();
            startY = e1.getY();
            endX = e2.getX();
            endY = e2.getY();
            if (Math.pow(startX - endX, 2) >=
                    3 * Math.pow(startY - endY, 2)) {
                // 自定义(startX - endX)d的平方大于等于3*(startY - endY)的平方为横向滑动
                if (startX - endX > FLIP_DISTANCE &&  //左滑
                        Math.abs(velocityX) > FLIP_VELOCITY) {
                    Log.i("MYTAG", "向左滑...");
                    //让MediaPlayer的播放位置移动到手势拖动后的位置(*20只是为了缩小滑动比例)
                    mMediaPlayer.seekTo(currPosition - (int) (startX - endX) * 20);
                    sb_control_play.setProgress(currPosition - (int) (startX - endX) * 20);
                    handler.sendEmptyMessage(UPDATE);
                    return true;

                }
                if (endX - startX > FLIP_DISTANCE &&  //右滑
                        Math.abs(velocityX) > FLIP_VELOCITY) {
                    Log.i("MYTAG", "向右滑...");
                    mMediaPlayer.seekTo(currPosition + (int) (endX - startX) * 20);
                    sb_control_play.setProgress(currPosition + (int) (endX - startX) * 20);
                    handler.sendEmptyMessage(UPDATE);
                    return true;
                }
            } else if (Math.pow(startY - endY, 2) >=  //纵向滑动
                    3 * Math.pow(startX - endX, 2)) {
                if (startY - endY > FLIP_DISTANCE &&  //上滑
                        Math.abs(velocityY) > FLIP_VELOCITY) {
                    Log.i("MYTAG", "向上滑...");
                    Log.i("MYTAG", "startY为:" + startY);
                    Log.i("MYTAG", "endY为:" + endY);
                    if (startX < texture_view.getWidth() / 2) {  //左半屏,亮度减小设置
                        img_control.setImageResource(R.drawable.bright);
                        frame_voicelight_layout.setVisibility(View.VISIBLE);
                        setBrightness();
                        handler.sendEmptyMessage(UPDATE);
                    } else {
                        img_control.setImageResource(R.drawable.volum);
                        frame_voicelight_layout.setVisibility(View.VISIBLE);
                        setVoice();
                        handler.sendEmptyMessage(UPDATE);
                    }
                    return true;
                }
                if (endY - startY > FLIP_DISTANCE &&  //下滑
                        Math.abs(velocityY) > FLIP_VELOCITY) {
                    Log.i("MYTAG", "向下滑...");
                    Log.i("MYTAG", "startY为:" + startY);
                    Log.i("MYTAG", "endY为:" + endY);
                    if (startX > texture_view.getWidth() / 2) {//右半屏，音量减小设置
                        img_control.setImageResource(R.drawable.volum);
                        frame_voicelight_layout.setVisibility(View.VISIBLE);
                        setVoice();
                        handler.sendEmptyMessage(UPDATE);
                    } else {//左半屏,亮度减小设置
                        img_control.setImageResource(R.drawable.bright);
                        frame_voicelight_layout.setVisibility(View.VISIBLE);
                        setBrightness();
                        handler.sendEmptyMessage(UPDATE);
                    }
                    return true;
                }
            }
            return false;
        }

        /**
         * 音量设置
         */
        private void setVoice() {
            int voice;//改变后的音量
            //   音量改变=( y移动距离  /  总的高度) * 最大音量
            float delta = (startY - endY) / texture_view.getHeight() * maxVolum;
            //初始化音量
            sb_control.setMax(maxVolum);
            sb_control.setProgress(curVolum);

            if (startY - endY >= 0) {
                if (curVolum + delta >= maxVolum) {
                    voice = maxVolum;
                } else {
                    voice = curVolum + (int) delta;
                }
            } else {
                if (curVolum + delta <= 0) {
                    voice = 0;
                } else {
                    voice = curVolum + (int) delta;
                }
            }
            audioManager.setStreamVolume(STREAM_MUSIC, voice, 0);
            curVolum = voice;
            sb_control.setProgress(voice);
        }

        /**
         * 亮度设置(待完成....)
         */
        private void setBrightness() {
            //亮度的改变
            float brightness = (startY - endY) / texture_view.getHeight();
            //255为最大亮度
            sb_control.setMax(255);
//            sb_control.setProgress(curBright);
            lp.screenBrightness = lp.screenBrightness + brightness;
//            if (lp.screenBrightness >= 1) {
//                lp.screenBrightness = 1;
//            } else if (lp.screenBrightness <= 0.1) {
//                lp.screenBrightness = (float) 0.1;
//            } else if (lp.screenBrightness > 0.1 && lp.screenBrightness < 1) {
//                lp.screenBrightness = lp.screenBrightness + brightness;
//            }
            if (lp.screenBrightness == -1) {
                lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            } else if (lp.screenBrightness >= 1) {
                lp.screenBrightness = 1;
            } else if (lp.screenBrightness <= 0.1) {
                lp.screenBrightness = 0.1f;
            }
            getWindow().setAttributes(lp);
            sb_control.setProgress((int) (lp.screenBrightness) * 255);//以255为值计算
        }
    }

    /**
     * 设置进度条控制播放进度
     */
    private void setPlayProgress() {
        sb_control_play.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_current_time.setText(getTimeStr(progress));//更新当前播放进度
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//开始拖动seekBar
                handler.removeMessages(UPDATE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//停止拖动seekBar
                int progress = seekBar.getProgress();
                mMediaPlayer.seekTo(progress);
                handler.sendEmptyMessage(UPDATE);
            }
        });
    }

    /**
     * 设置缩放比例
     *
     * @param width
     * @param height
     */
    private void setVedioViewScale(int width, int height) {
//        ViewGroup.LayoutParams layoutParams = texture_view.getLayoutParams();
//        layoutParams.width = width;
//        layoutParams.height = height;
//        texture_view.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams layoutParams1 = texture_layout.getLayoutParams();
        layoutParams1.width = width;
        layoutParams1.height = height;
        texture_layout.setLayoutParams(layoutParams1);
    }

    /**
     * 控制屏幕显示的大小
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //横屏
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setVedioViewScale(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            isFullScreen = true;
            //移除半屏状态
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            //添加全屏状态
            getWindow().addFlags(FLAG_FULLSCREEN);
        } else {
            //竖屏
            setVedioViewScale(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(320));
            isFullScreen = false;
            //移除全屏状态
            getWindow().clearFlags(FLAG_FULLSCREEN);
            //添加半屏状态
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    /**
     * dp转化为px
     *
     * @param value
     * @return
     */
    public int dp2px(int value) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 按back键退出当前Activity并停止视频播放
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }
}
