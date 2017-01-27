package com.quintlr.music;

/**
 * Created by akash on 20/1/17.
 */

public class Artist {
    private String artistName;
    private int id, numberOfSongs, numberOfAlbums;

    Artist(int id, String artistName, int numberOfSongs, int numberOfAlbums){
        this.id = id;
        this.artistName = artistName;
        this.numberOfSongs = numberOfSongs;
        this.numberOfAlbums = numberOfAlbums;
    }

    public int getId() {
        return id;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }
}
