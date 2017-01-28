package com.quintlr.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import static com.quintlr.music.MainActivity.context;

/**
 * Created by Akash on 6/25/2016.
 * Deals with the main music playback.
 */

class SongControl implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    static SongControl songControl = null;
    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean paused = true, shuffled = false, shuffledWhilePlaying = false;

    // Constructor #1
    static SongControl getSongControlInstance(int songId){
        if(songControl==null) {
            songControl = new SongControl();
        }
        PlayQueue.setIndex(songId);
        return songControl;
    }

    // Constructor #2
    static SongControl getSongControlInstance(){
        if(songControl==null) {
            songControl = new SongControl();
        }
        return songControl;
    }

    String getCurrentSongPath(){
        Log.d("akash", "getCurrentSongPath: "+PlayQueue.index);
        MiniPlayer.setMiniPlayerValues(context);
        return PlayQueue.getCurrentSongPath();
    }

    void play_song(){
        if(mediaPlayer!=null){
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getCurrentSongPath());
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            play_pause();
        }
    }

    void play_pause(){
        //Log.d("akash", "BEFORE : isPlay = "+mediaPlayer.isPlaying()+" :: paused = "+paused);
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            paused = true;
        }else if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            paused = false;
        }
        //Log.d("akash", "AFTER : isPlay = "+mediaPlayer.isPlaying()+" :: paused = "+paused);
    }

    void next_song(){
        //Log.d("akash", "next_song: ");
        PlayQueue.nextSong();
        play_song();
    }

    void prev_song(){
        PlayQueue.prevSong();
        play_song();
    }


    void seekTo(int progress){
        mediaPlayer.seekTo(progress);
    }

    void setLooping(boolean isSet){
        if(isSet){
            mediaPlayer.setLooping(true);
        }else{
            mediaPlayer.setLooping(false);
        }
    }

    boolean isLooping(){
        return mediaPlayer.isLooping();
    }

    String getTotalDuration(){
        int mil_sec = mediaPlayer.getDuration();
        int min = mil_sec / 60000;
        float real_min = (float) mil_sec / 60000;
        int sec = (int) ((real_min - min) * 60);
        String time;
        if(min<10)
            time = "0"+min;
        else
            time = min+"";
        if(sec<10)
            time = time+":0"+sec;
        else
            time = time+":"+sec;
        return time;
    }

    void setShuffledState(boolean shuffled) {
        this.shuffled = shuffled;
        if(mediaPlayer.isPlaying()){
            shuffledWhilePlaying = true;
        }
    }

    boolean getShuffledState(){
        return shuffled;
    }

    boolean isPlaying(){
        return mediaPlayer.isPlaying() && !paused;
    }

    void setPausedState(boolean paused){
        this.paused = paused;
    }

    boolean getPausedState(){
        return paused;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next_song();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
