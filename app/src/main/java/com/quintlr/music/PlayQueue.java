package com.quintlr.music;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by akash on 12/1/17.
 */

public class PlayQueue{
    static ArrayList<Song> songQueue = null;
    static int index = 0;
    static boolean changed = false;

    static void createQueue(ArrayList<Song> songs){
        songQueue = songs;
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
        songQueue.clear();
    }

    static int numberOfSongs(){
        return songQueue.size();
    }

    static boolean isQueueChanged(){
        return changed;
    }

    static void setQueueChanged(boolean value){
        changed = value;
    }
}
