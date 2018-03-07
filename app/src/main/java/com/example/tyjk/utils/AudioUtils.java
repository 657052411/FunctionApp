package com.example.tyjk.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.tyjk.bean.Music;

import java.util.ArrayList;

/**
 * Created by tyjk on 2017/12/29.
 */

//public class AudioUtils {

    /**
     * 获取sd卡所有的音乐文件
     *
     * @return
     * @throws Exception
     */
//    public static ArrayList<Music> getAllMusics(Context context) {
//
//        ArrayList<Music> musics = null;
//
//        Cursor cursor = context.getContentResolver().query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                new String[] { MediaStore.Audio.Media._ID,
//                        MediaStore.Audio.Media.DISPLAY_NAME,
//                        MediaStore.Audio.Media.TITLE,
//                        MediaStore.Audio.Media.DURATION,
//                        MediaStore.Audio.Media.ARTIST,
//                        MediaStore.Audio.Media.ALBUM,
//                        MediaStore.Audio.Media.YEAR,
//                        MediaStore.Audio.Media.MIME_TYPE,
//                        MediaStore.Audio.Media.SIZE,
//                        MediaStore.Audio.Media.DATA },
//                MediaStore.Audio.Media.MIME_TYPE + "=? or "
//                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
//                new String[] { "audio/mpeg", "audio/x-ms-wma" }, null);
//
//        musics = new ArrayList<Music>();
//
//        if (cursor.moveToFirst()) {
//
//            Music music = null;
//
//            do {
//                music = new Music();
//                // 文件名
//                music.setFileName(cursor.getString(1));
//                // 歌曲名
//                music.setTitle(cursor.getString(2));
//                // 时长
//                music.setDuration(cursor.getInt(3));
//                // 歌手名
//                music.setSinger(cursor.getString(4));
//                // 专辑名
//                music.setAlbum(cursor.getString(5));
//                // 年代
//                if (cursor.getString(6) != null) {
//                    music.setYear(cursor.getString(6));
//                } else {
//                    music.setYear("未知");
//                }
//                // 歌曲格式
//                if ("audio/mpeg".equals(cursor.getString(7).trim())) {
//                    music.setType("mp3");
//                } else if ("audio/x-ms-wma".equals(cursor.getString(7).trim())) {
//                    music.setType("wma");
//                }
//                // 文件大小
//                if (cursor.getString(8) != null) {
//                    float size = cursor.getInt(8) / 1024f / 1024f;
//                    music.setSize((size + "").substring(0, 4) + "M");
//                } else {
//                    music.setSize("未知");
//                }
//                // 文件路径
//                if (cursor.getString(9) != null) {
//                    music.setFileUrl(cursor.getString(9));
//                }
//                musics.add(music);
//            } while (cursor.moveToNext());
//
//            cursor.close();
//
//        }
//        return musics;
//    }
//
//}
