package com.quintlr.music;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by akash on 13/1/17.
 */

public class SharedPrefs {
    static SharedPreferences sharedPreferences;
    static String PREF_NAME = "Music";

    static void setCurentSongIndex(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("songIndex", PlayQueue.index);
        editor.apply();
    }

    static void assignCurentSongIndex(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        PlayQueue.setIndex(sharedPreferences.getInt("songIndex", 0));
    }

}
