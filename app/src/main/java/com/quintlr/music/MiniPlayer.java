package com.quintlr.music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Created by Akash on 6/26/2016.
 */
public class MiniPlayer extends android.support.v4.app.Fragment implements View.OnClickListener, MiniPlayerListener{
    static ImageView mini_album_art;
    static TextView mini_song_title, mini_song_artist;
    ImageButton pause_btn, prev_btn, next_btn;
    static ProgressBar mini_song_progress;
    static Handler progress_handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.mini_player_layout, container, false);
        view.setOnClickListener(this);
        mini_album_art = (ImageView) view.findViewById(R.id.mini_album_art);
        progress_handler = new Handler();
        mini_song_title = (TextView) view.findViewById(R.id.mini_song_title);
        mini_song_artist = (TextView) view.findViewById(R.id.mini_song_artist);
        prev_btn = (ImageButton) view.findViewById(R.id.mini_prev_btn);
        pause_btn = (ImageButton) view.findViewById(R.id.mini_pause_btn);
        next_btn = (ImageButton) view.findViewById(R.id.mini_next_btn);
        mini_song_progress = (ProgressBar) view.findViewById(R.id.mini_song_progess);
        mini_album_art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongControl.getSongControlInstance().prev_song();
                //setMiniPlayerValues();
            }
        });
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SongControl.getSongControlInstance().getPausedState()){
                    SongControl.getSongControlInstance().setPausedState(false);
                    pause_btn.setImageResource(R.drawable.pause_white_24dp);
                }else {
                    SongControl.getSongControlInstance().setPausedState(true);
                    pause_btn.setImageResource(R.drawable.play_arrow_white_24dp);
                }
                SongControl.getSongControlInstance().play_pause();
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongControl.getSongControlInstance().next_song();
                //setMiniPlayerValues();
            }
        });
        return view;
    }

    ////////////////////////////////////////////////////////
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /*
        if(context instanceof MainActivity){
            ((MainActivity)context).miniPlayerListener = this;
        }else if (context instanceof AlbumActivity){
            ((AlbumActivity)context).miniPlayerListener = this;
        }*/

    }

    /**
    void setMiniPlayerValues() {
        Glide.with(this)
                .load(PlayQueue.getCurrentSong().getSongAlbumArt())
                .into(mini_album_art);
        mini_song_title.setText(PlayQueue.getCurrentSong().getSongTitle());
        mini_song_artist.setText(PlayQueue.getCurrentSong().getSongArtist());
        progress_handler.postDelayed(progessbarThread,1000);
    }

    static void setMiniPlayerValues(Context context) {
        Glide.with(context)
                .load(PlayQueue.getCurrentSong().getSongAlbumArt())
                .into(mini_album_art);
        mini_song_title.setText(PlayQueue.getCurrentSong().getSongTitle());
        mini_song_artist.setText(PlayQueue.getCurrentSong().getSongArtist());
        progress_handler.postDelayed(progessbarThread,1000);
    }*/

    private static Runnable progessbarThread = new Runnable() {
        @Override
        public void run() {
            if (SongControl.getSongControlInstance().mediaPlayer.isPlaying()){
                mini_song_progress.setMax(SongControl.getSongControlInstance().mediaPlayer.getDuration());
                mini_song_progress.setProgress(SongControl.getSongControlInstance().mediaPlayer.getCurrentPosition());
                progress_handler.postDelayed(this,1000);
            }
        }
    };

    @Override
    public void onClick(View v) {
        /***
         * don't forget this...
         * */
        /*Intent intent = new Intent(getContext(),Player.class);
        intent.putExtra("selected_song_path",SongControl.getSongControlInstance().getCurrentSongPath());
        startActivity(intent);*/
    }

    @Override
    public void setMiniPlayerValues() {
        //Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
        Glide.with(this)
                .load(PlayQueue.getCurrentSong().getSongAlbumArt())
                .into(mini_album_art);
        mini_song_title.setText(PlayQueue.getCurrentSong().getSongTitle());
        mini_song_artist.setText(PlayQueue.getCurrentSong().getSongArtist());
        progress_handler.postDelayed(progessbarThread,1000);
    }

    @Override
    public void removeCallBacks() {
        progress_handler.removeCallbacks(progessbarThread);
    }
}
