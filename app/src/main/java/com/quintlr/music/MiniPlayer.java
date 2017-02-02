package com.quintlr.music;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.content.ContentValues.TAG;

/**
 * Created by Akash on 6/26/2016.
 */
public class MiniPlayer extends android.support.v4.app.Fragment
        implements View.OnTouchListener{
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
        view.setOnTouchListener(this);
        mini_album_art = (ImageView) view.findViewById(R.id.mini_album_art);
        progress_handler = new Handler();
        mini_song_title = (TextView) view.findViewById(R.id.mini_song_title);
        mini_song_artist = (TextView) view.findViewById(R.id.mini_song_artist);
        prev_btn = (ImageButton) view.findViewById(R.id.mini_prev_btn);
        pause_btn = (ImageButton) view.findViewById(R.id.mini_pause_btn);
        next_btn = (ImageButton) view.findViewById(R.id.mini_next_btn);
        mini_song_progress = (ProgressBar) view.findViewById(R.id.mini_song_progess);

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressAnimator.startProgressAnimator(mini_song_progress, mini_song_progress.getProgress(), 0);
                SongControl.getSongControlInstance().prevSong();
            }
        });
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongControl.getSongControlInstance().playOrPause();
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressAnimator.startProgressAnimator(mini_song_progress, mini_song_progress.getProgress(), 0);
                SongControl.getSongControlInstance().nextSong();
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
        mini_song_progress.setProgress(0);
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
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Intent intent = new Intent(getContext(), PlayerActivity.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(
                                getActivity(),
                                mini_album_art,
                                mini_album_art.getTransitionName());
                startActivity(intent, options.toBundle());
            }else {
                startActivity(intent);
            }

        }
        return true;
    }
}
