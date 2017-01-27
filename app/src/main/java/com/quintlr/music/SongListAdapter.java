package com.quintlr.music;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Akash on 6/22/2016.
 */

public class SongListAdapter extends RecyclerView.Adapter{

    private  ArrayList<Song> songArrayList;

    public class SongViewHolder extends RecyclerView.ViewHolder{
        public TextView songTitle, songArtist;
        public SongViewHolder(View itemView) {
            super(itemView);
            songTitle = (TextView) itemView.findViewById(R.id.song_list_song_title);
            songArtist = (TextView) itemView.findViewById(R.id.song_list_song_artist);
        }
    }

    public SongListAdapter(ArrayList<Song> songArrayList){
        this.songArrayList = songArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_song,parent,false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SongViewHolder songholder = (SongViewHolder) holder;
        songholder.songTitle.setText(songArrayList.get(position).getSongTitle());
        songholder.songArtist.setText(songArrayList.get(position).getSongArtist());
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

}