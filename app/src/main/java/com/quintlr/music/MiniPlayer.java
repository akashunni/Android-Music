package com.quintlr.music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.transition.TransitionInflater;
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
public class MiniPlayer extends android.support.v4.app.Fragment implements View.OnClickListener{
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
            }
        });
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongControl.getSongControlInstance().play_pause();
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongControl.getSongControlInstance().next_song();
            }
        });
        return view;
    }

    ////////////////////////////////////////////////////////


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    static void setMiniPlayerValues(Context context) {
        Glide.with(context)
                .load(PlayQueue.getCurrentSong().getSongAlbumArt())
                .into(mini_album_art);
        mini_song_title.setText(PlayQueue.getCurrentSong().getSongTitle());
        mini_song_artist.setText(PlayQueue.getCurrentSong().getSongArtist());
        progress_handler.postDelayed(progessbarThread,1000);
    }

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
        Fragment fragment = new PlayerFragment();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition changeTransform = TransitionInflater.from(getContext()).
                    inflateTransition(R.transition.change_image_transform);
            Transition explodeTransform = TransitionInflater.from(getContext()).
                    inflateTransition(android.R.transition.slide_bottom);

            setSharedElementReturnTransition(changeTransform);
            setExitTransition(explodeTransform);

            fragment.setSharedElementEnterTransition(changeTransform);
            fragment.setEnterTransition(explodeTransform);
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction
                .add(R.id.drawer_layout , fragment, "player")
                .addSharedElement(mini_album_art, "Transition")
                .addToBackStack("BACKSTACK_player")
                .commit();
    }
}
