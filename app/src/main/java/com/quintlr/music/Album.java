package com.quintlr.music;

/**
 * Created by Akash on 7/2/2016.
 */

/*This class is a blue-print for an Album*/
public class Album {

    private String albumName, artist, albumArtPath;
    private long id;

    Album(long id, String albumName, String artist, String albumArtPath){
        this.id = id;
        this.albumName = albumName;
        this.artist = artist;
        this.albumArtPath = albumArtPath;
    }

    public long getId() {
        return id;
    }

    public String getAlbumArtPath() {
        return albumArtPath;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtist() {
        return artist;
    }
}
