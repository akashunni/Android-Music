package com.quintlr.music;

import android.media.MediaPlayer;

/**
 * Created by akash on 1/2/17.
 */

public interface SongControlInterface extends MediaPlayer.OnCompletionListener {

    String getCurrentSongPath();
    void loadSong();
    void playOrPause();
    void nextSong();
    void prevSong();
    void seekTo(int progress);
    void setLooping(boolean isSet);
    boolean isLooping();
    String getTotalDuration();
    String getElapsedTime();
    String getTimeFromMilliSec(int milliSec);
    void setShuffledState(boolean shuffled);
    boolean getShuffledState();
    boolean isPlaying();
    void setPausedState(boolean pausedState);
    boolean getPausedState();

    @Override
    void onCompletion(MediaPlayer mp);
}
