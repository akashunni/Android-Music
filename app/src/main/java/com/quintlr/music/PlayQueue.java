package com.quintlr.music;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by akash on 12/1/17.
 */

public class PlayQueue{
    static ArrayList<Song> songQueue = null;
    static int index = 0;
    /** to check that the list is changed from other classes ***/
    static boolean changed = false;

    static void createQueue(ArrayList<Song> songs){
        songQueue = songs;
        for (int i =0; i< songQueue.size(); i++)
            Log.d("akash", "NEW: "+i+" : "+songQueue.get(i).getSongTitle());
    }

    static ArrayList<Song> getSongQueue(){
        return songQueue;
    }

    static Song getSongAtIndex(int i){
        return songQueue.get(i);
    }

    static String getSongPathAtIndex(int i){
        return songQueue.get(i).getSongPath();
    }

    static void prevSong(){
        if (index == 0){
            index = songQueue.size() - 1;
        }else {
            index--;
        }
    }

    static void nextSong(){
        if(index == songQueue.size()-1){
            index = 0;
        }else {
            index++;
        }
    }

    static Song getCurrentSong(){
        return songQueue.get(index);
    }

    static String getCurrentSongPath(){
        return songQueue.get(index).getSongPath();
    }

    public static void setIndex(int index) {
        PlayQueue.index = index;
    }

    static void deletePlayQueue(){
        changed = true;
        index = 0;
        songQueue.clear();
    }

    static int numberOfSongs(){
        if (songQueue != null)
            return songQueue.size();
        return 0;
    }

    static boolean isQueueChanged(){
        return changed;
    }

    static void setQueueChanged(boolean value){
        changed = value;
    }

    static void shuffleQueue(){
        /**
         * MAJOR BUG : The currently playing song's index is not changed.
         * */
        if(songQueue != null) {
            Song currentSong = getCurrentSong();
            Collections.shuffle(songQueue);
            songQueue.remove(currentSong);
            index = 0;
            songQueue.add(index, currentSong);
            changed = true;
            for (int i =0; i< songQueue.size(); i++)
                Log.d("akash", "shuffleQueue: "+i+" : "+songQueue.get(i).getSongTitle());
        }
    }

    static void appendSongsToQueue(ArrayList<Song> extra){
        if (songQueue != null && extra != null){
            songQueue.removeAll(extra);
            songQueue.addAll(extra);
        }
    }
}
