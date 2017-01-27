package com.quintlr.music;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

/**
 * Created by Akash on 6/22/2016.
 */
public class Song {
    private String songTitle, songArtist, songPath, albumName;
    private long songId, albumId;

    //Constructor
    Song(long songId, String songTitle, String albumName, long albumId, String songArtist, String songPath){
        this.songId = songId;
        this.songPath = songPath;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.albumName = albumName;
        this.albumId = albumId;
    }

    long getSongId(){return songId;}

    String getSongPath() {
        return songPath;
    }

    String getSongTitle() {
        return songTitle;
    }

    String getSongArtist() {
        return songArtist;
    }

    String getAlbumName() {
        return albumName;
    }

    long getAlbumId() {
        return albumId;
    }

    byte[] getSongAlbumArt(){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(getSongPath());
        byte[] temp = mmr.getEmbeddedPicture();
        if (temp==null){
            return null;
        }else {
            return temp;
            //return BitmapFactory.decodeByteArray(temp,0,temp.length);
        }
    }
}
