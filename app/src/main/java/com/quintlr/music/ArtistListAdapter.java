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
 * Created by akash on 20/1/17.
 */

public class ArtistListAdapter extends RecyclerView.Adapter {

    private ArrayList<Artist> artistArrayList;
    Context context;

    ArtistListAdapter(ArrayList<Artist> artistArrayList, Context context){
        this.artistArrayList = artistArrayList;
        this.context = context;
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder{
        ImageView artistLogo;
        TextView artistName, noOfSongs;
        public ArtistViewHolder(View itemView) {
            super(itemView);
            artistLogo = (ImageView) itemView.findViewById(R.id.artist_list_artist_logo);
            artistName = (TextView) itemView.findViewById(R.id.artist_list_artist_name);
            noOfSongs = (TextView) itemView.findViewById(R.id.artist_list_no_of_songs);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArtistViewHolder artistViewHolder = (ArtistViewHolder) holder;
        Glide.with(context)
                .load(R.drawable.loading)
                .into(artistViewHolder.artistLogo);
        artistViewHolder.artistName.setText(artistArrayList.get(position).getArtistName());
        artistViewHolder.noOfSongs.setText(artistArrayList.get(position).getNumberOfSongs()+"");
    }

    @Override
    public int getItemCount() {
        if (artistArrayList == null)
            return 0;
        else
            return artistArrayList.size();
    }
}
