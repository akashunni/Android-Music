package com.quintlr.music;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
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

/**
 * Created by Akash on 6/26/2016.
 */
public class MiniPlayer extends android.support.v4.app.Fragment
        implements View.OnTouchListener{
    static ImageView mini_album_art;
    static TextView mini_song_title, mini_song_artist;
    ImageButton prev_btn, next_btn;
    static ImageButton pause_btn;
    static ProgressBar mini_song_progress;
    static Handler progress_handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
                if (SongControl.getSongControlInstance().isPlaying()){
                    SongControl.paused = true;
                    pause_btn.setImageResource(R.drawable.play_arrow_white_24dp);
                }else {
                    SongControl.paused = false;
                    pause_btn.setImageResource(R.drawable.pause_white_24dp);
                }
                SongControl.getSongControlInstance().playOrPause();
                if (SongControl.getSongControlInstance().isPlaying()){
                    progress_handler.removeCallbacks(progessbarThread);
                    progessbarThread.run();
                }
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressAnimator.startProgressAnimator(mini_song_progress, mini_song_progress.getProgress(), 0);
                SongControl.getSongControlInstance().nextSong();
            }
        });

        if (savedInstanceState != null) {
            setMiniPlayerValues(getContext());
            /** TEMP. SOLUTION FOR PROGRESSBAR LOADING
             * -> total & elapsed time values are same when loading at first.
             * -> So, this calls the onCompletion method as it thinks that song is over.
             * -> so, by applying jugaad ;) values are set when only rotation takes place.
             * **/
            mini_song_progress.setMax(SongControl.getSongControlInstance().getTotalDurationInMillis());
            mini_song_progress.setProgress(SongControl.getSongControlInstance().getElapsedDurationInMillis());
        }

        return view;
    }

    static void setMiniPlayerValues(Context context) {
        if (!PlayQueue.isQueueNULL()){
            Glide.with(context)
                    .load(PlayQueue.getCurrentSong().getSongAlbumArt())
                    .into(mini_album_art);
            //checking paused value since, isplaying() is false when a new song is loaded.
            if (SongControl.paused){
                pause_btn.setImageResource(R.drawable.play_arrow_white_24dp);
            }else {
                pause_btn.setImageResource(R.drawable.pause_white_24dp);
            }
            mini_song_title.setText(PlayQueue.getCurrentSong().getSongTitle());
            mini_song_artist.setText(PlayQueue.getCurrentSong().getSongArtist());
            progress_handler.removeCallbacks(progessbarThread);
            progress_handler.post(progessbarThread);
        }else {
            Toast.makeText(context, "No Songs Available :(", Toast.LENGTH_SHORT).show();
        }
    }

    // has load_song & seekTo calls
    static void setMiniPlayerValues(Context context, int elapsedTime, int totalTime) {
        if (!PlayQueue.isQueueNULL()){
            mini_song_progress.setMax(totalTime);
            mini_song_progress.setProgress(elapsedTime);
            ///////////////////////////////////////////////
            SongControl.getSongControlInstance().loadSong();
            ///////////////////////////////////////////////
            Glide.with(context)
                    .load(PlayQueue.getCurrentSong().getSongAlbumArt())
                    .into(mini_album_art);
            mini_song_title.setText(PlayQueue.getCurrentSong().getSongTitle());
            mini_song_artist.setText(PlayQueue.getCurrentSong().getSongArtist());
            SongControl.getSongControlInstance().seekTo(elapsedTime);
            progress_handler.removeCallbacks(progessbarThread);
            progress_handler.post(progessbarThread);
        }else {
            Toast.makeText(context, "No Songs Available :(", Toast.LENGTH_SHORT).show();
        }
    }

    private static Runnable progessbarThread = new Runnable() {
        @Override
        public void run() {
            mini_song_progress.setProgress(SongControl.getSongControlInstance().getElapsedDurationInMillis());
            //think about it....
            mini_song_progress.setMax(SongControl.getSongControlInstance().getTotalDurationInMillis());
            if (SongControl.getSongControlInstance().isPlaying()){
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

    @Override
    public void onStop() {
        progress_handler.removeCallbacks(progessbarThread);
        super.onStop();
    }

    @Override
    public void onStart() {
        progress_handler.removeCallbacks(progessbarThread);
        progress_handler.post(progessbarThread);
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SharedPrefs.setCurrentSongIndex(getContext());
        super.onSaveInstanceState(outState);
    }
}
