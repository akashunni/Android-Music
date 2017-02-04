package com.quintlr.music;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Akash on 6/22/2016.
 */

public class AlbumListAdapter extends RecyclerView.Adapter{

    private ArrayList<Album> albumArrayList;
    Context context;

    public class AlbumViewHolder extends RecyclerView.ViewHolder{
        TextView AlbumName, AlbumArtist;
        ImageView AlbumArt;
        public AlbumViewHolder(View itemView) {
            super(itemView);
            AlbumName = (TextView) itemView.findViewById(R.id.album_list_album_name);
            AlbumArtist = (TextView) itemView.findViewById(R.id.album_list_artist);
            AlbumArt = (ImageView) itemView.findViewById(R.id.album_list_album_art);
        }
    }

    AlbumListAdapter(ArrayList<Album> albumArrayList, Context context){
        this.context = context;
        this.albumArrayList = albumArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_album,parent,false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AlbumViewHolder songholder = (AlbumViewHolder) holder;
        Glide.with(context)
                .load(albumArrayList.get(position).getAlbumArtPath())
                .placeholder(R.drawable.loading)
                .into(songholder.AlbumArt);
        songholder.AlbumName.setText(albumArrayList.get(position).getAlbumName());
        songholder.AlbumArtist.setText(albumArrayList.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        if (albumArrayList == null)
            return 0;
        else
            return albumArrayList.size();
    }
}
