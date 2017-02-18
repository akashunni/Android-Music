package com.quintlr.music;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by akash on 13/1/17.
 */

public class SharedPrefs {
    static SharedPreferences sharedPreferences;
    static String PREF_NAME = "CurrentMusic";

    static void setCurrentSongIndex(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("songIndex", PlayQueue.index);
        editor.putInt("elapsedTime", SongControl.getSongControlInstance().mediaPlayer.getCurrentPosition());
        editor.putInt("totalTime", SongControl.getSongControlInstance().mediaPlayer.getDuration());
        editor.apply();
    }

    static void setSignInStatus(Context context, boolean status){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("signedIn", status);
        editor.apply();
    }

    static boolean getSignInStatus(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("signedIn", false);
    }

    static void assignCurrentSongIndex(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        PlayQueue.setIndex(sharedPreferences.getInt("songIndex", 0));
    }

    static int getCurrentSongTotalDuration(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("totalTime", 0);
    }

    static int getCurrentSongElapsedDuration(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("elapsedTime", 0);
    }

}
