package com.quintlr.music;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by akash on 13/1/17.
 */

public class Fetcher {

    static ArrayList<Song> getRealSongArrayList(Context context) {
        ArrayList<Song> songArrayList = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
        };
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            //add songs to array list
            do {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String albumName = cursor.getString(2);
                long albumId = cursor.getLong(3);
                String artist = cursor.getString(4);
                String path = cursor.getString(5);

                songArrayList.add(new Song(id, title, albumName, albumId, artist, path));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return songArrayList;
    }

    static ArrayList<Album> getAlbumList(Context context) {
        ArrayList<Album> albumList = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART
        };
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Audio.Albums.ALBUM + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            //add album art path to array list
            do {
                albumList.add(new Album(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return albumList;
    }

    static String getAlbumArtFromAlbumID(Context context, long albumID) {
        String[] projection = new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_ART
        };
        String selection = "_id = " + albumID;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                selection,/*selection*/
                null,/*selection - args*/
                null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(1);
        }
        cursor.close();
        return null;
    }

    static ArrayList<Song> getSongsFromAlbumID(Context context, long albumID) {
        ArrayList<Song> songArrayList = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
        };
        String selection = "album_id = " + albumID;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,/*selection*/
                null,/*selection - args*/
                MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            //add songs to array list
            do {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String albumName = cursor.getString(2);
                long albumId = cursor.getLong(3);
                String artist = cursor.getString(4);
                String path = cursor.getString(5);
                songArrayList.add(new Song(id, title, albumName, albumId, artist, path));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songArrayList;
    }


    static ArrayList<Artist> getArtistList(Context context) {
        ArrayList<Artist> artistList = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST
                //MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                //MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
        };
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Audio.Artists.ARTIST + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                artistList.add(new Artist(
                        cursor.getInt(0),
                        cursor.getString(1),
                        1, 2
                        //cursor.getInt(2),
                        //cursor.getInt(3)
                ));
                //Log.d("akash", "getSongsFromAlbumID: "+ cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return artistList;
    }

}
