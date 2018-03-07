package com.example.tyjk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tyjk.activity.R;
import com.example.tyjk.bean.Music;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by tyjk on 2017/12/29.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.PlayViewHolder> {
    /**
     * @param context
     * @param list    the datas to attach the adapter
     */
    private Context context;
    private List<Music> lists = new ArrayList<>();

//    public MusicAdapter(Context context, ArrayList<Music> list) {
//        this.context = context;
//        this.lists = list;
//    }

    @Override
    public PlayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_music, parent, false);
        PlayViewHolder playVH = new PlayViewHolder(view);
        return playVH;
    }

    @Override
    public void onBindViewHolder(PlayViewHolder holder, int position) {
        holder.tvFileName.setText(lists.get(position).getFileName());
        holder.tvMusicName.setText(lists.get(position).getTitle());
        holder.tvMusicTime.setText(lists.get(position).getDuration());
        holder.tvSingerName.setText(lists.get(position).getSinger());
        holder.tvFileSize.setText(lists.get(position).getSize());
        Log.i("TAG","文件名称:"+lists.get(position).getFileName());
        Log.i("TAG","歌曲名称:"+lists.get(position).getTitle());
        Log.i("TAG","歌曲时长:"+lists.get(position).getDuration());
        Log.i("TAG","歌手名字:"+lists.get(position).getSinger());
        Log.i("TAG","文件大小:"+lists.get(position).getSize());

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class PlayViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFileName,tvMusicName,tvMusicTime,tvSingerName,tvFileSize;
        public PlayViewHolder(View itemView) {
            super(itemView);
            tvFileName=itemView.findViewById(R.id.tvFileName);
            tvMusicName=itemView.findViewById(R.id.tvMusicName);
            tvMusicTime=itemView.findViewById(R.id.tvMusicTime);
            tvSingerName=itemView.findViewById(R.id.tvSingerName);
            tvFileSize=itemView.findViewById(R.id.tvFileSize);
        }
    }
}
