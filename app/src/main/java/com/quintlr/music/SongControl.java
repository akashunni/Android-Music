package com.quintlr.music;

import android.media.MediaPlayer;
import android.os.PowerManager;

import java.io.IOException;

/**
 * Created by akash on 1/2/17.
 */

public class SongControl implements SongControlInterface, MediaPlayer.OnErrorListener {

    static SongControl songControl = null;
    static MediaPlayer mediaPlayer = new MediaPlayer();
    static boolean paused = true, shuffle = false;

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

    @Override
    public String getCurrentSongPath() {
        MiniPlayer.setMiniPlayerValues(MainActivity.context);
        return PlayQueue.getCurrentSongPath();
    }

    @Override
    public void loadSong() {
        if (!PlayQueue.isQueueNULL()) {
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(getCurrentSongPath());
                    mediaPlayer.prepare();
                    mediaPlayer.setWakeMode(MainActivity.context, PowerManager.PARTIAL_WAKE_LOCK);
                    mediaPlayer.setOnCompletionListener(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                playOrPause();
            }
        }
    }

    @Override
    public void playOrPause() {
        // only miniplayer & player activity can change paused value.
        if(mediaPlayer.isPlaying()){
            if (paused){
                mediaPlayer.pause();
            }
        }else if (!mediaPlayer.isPlaying()){
            if (!paused){
                mediaPlayer.start();
            }
        }
    }

    @Override
    public void nextSong() {
        PlayQueue.nextSong();
        loadSong();
    }

    @Override
    public void prevSong() {
        PlayQueue.prevSong();
        loadSong();
    }

    @Override
    public void seekTo(int progress) {
        if (mediaPlayer != null){
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void setLooping(boolean Set) {
        mediaPlayer.setLooping(Set);
    }

    @Override
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    @Override
    public String getTimeFromMilliSec(int milliSec) {
        int min = milliSec / 60000;
        float real_min = (float) milliSec / 60000;
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

    @Override
    public String getTotalDuration() {
        return getTimeFromMilliSec(mediaPlayer.getDuration());
    }

    @Override
    public String getElapsedTime() {
        return getTimeFromMilliSec(mediaPlayer.getCurrentPosition());
    }

    @Override
    public int getTotalDurationInMillis() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getElapsedDurationInMillis() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void setShuffledState(boolean shuffled) {
        shuffle = shuffled;
    }

    @Override
    public boolean isShuffled() {
        return shuffle;
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void setPausedState(boolean pausedState) {
        paused = pausedState;
    }

    @Override
    public boolean getPausedState() {
        return paused;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        PlayerActivity.set_zero_seekbar = true;
        nextSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    static void releaseMediaPlayer(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
