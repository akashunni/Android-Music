package com.quintlr.music;

import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DrivePreferencesApi;
import com.google.android.gms.drive.FileUploadPreferences;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.events.DriveEventService;
import com.google.android.gms.drive.metadata.internal.MetadataBundle;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by akash on 19/2/17.
 */

public class SyncDriveSongs {

    private String TAG = "akash", thu = "";
    private String DRIVE_FOLDER_NAME;
    private ArrayList<Song> songArrayList;
    private GoogleApiClient googleApiClient;
    private static SyncDriveSongs syncDriveSongs = null;

    static SyncDriveSongs getInstance(){
        if (syncDriveSongs == null){
            syncDriveSongs = new SyncDriveSongs();
        }
        return syncDriveSongs;
    }

    void sync(GoogleApiClient googleApiClient, String DRIVE_FOLDER_NAME, ArrayList<Song> songArrayList){
        this.DRIVE_FOLDER_NAME = DRIVE_FOLDER_NAME;
        this.songArrayList = songArrayList;
        this.googleApiClient = googleApiClient;
        uploadSongs();
        //createFolder();
    }

    private void createFolder(){
        if (googleApiClient.isConnected()){
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(DRIVE_FOLDER_NAME)
                    .setStarred(true)
                    .build();
            Drive.DriveApi.getRootFolder(googleApiClient)
                    .createFolder(googleApiClient, changeSet)
                    .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                        @Override
                        public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {
                            Log.d(TAG, "Folder Created : "+driveFolderResult.getStatus().isSuccess());
                            uploadSongs();
                        }
                    });
        }else {
            Log.d(TAG, "NOT CONNECTED "+googleApiClient.isConnecting());
        }
    }

    void uploadSongs(){

        Drive.DriveApi.newDriveContents(googleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                        if (driveContentsResult.getStatus().isSuccess()){

                        }else {
                            Log.d(TAG, "new drive contents FAILED.");
                        }
                    }
                });




        /*Drive.DriveApi.newDriveContents(googleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                        if (driveContentsResult.getStatus().isSuccess()){
                            Log.d(TAG, "new drive contents OK.");
                            final DriveContents driveContents = driveContentsResult.getDriveContents();
                            OutputStream outputStream = driveContents.getOutputStream();
                            File file = new File(songArrayList.get(0).getSongPath());
                            byte[] byteArray = new byte[1024000];
                            int off = 0;
                            try {
                                FileInputStream fileInputStream = new FileInputStream(file);
                                while (fileInputStream.available() > 0){
                                    if (fileInputStream.read(byteArray) != -1) {
                                        if (fileInputStream.available() > 1024000){
                                            outputStream.write(byteArray, off, 1024000);
                                        }else {
                                            outputStream.write(byteArray);
                                        }
                                    }
                                    Log.d(TAG, "onResult: "+fileInputStream.available());
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle("akashunni")
                                    .setMimeType("audio/mpeg")
                                    .setStarred(true)
                                    .build();

                            Drive.DriveApi.getRootFolder(googleApiClient)
                                    .createFile(googleApiClient, changeSet, driveContents)
                                    .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                        @Override
                                        public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
                                            if (driveFileResult.getStatus().isSuccess()){
                                                Log.d(TAG, "OKAY");
                                            }else {
                                                Log.d(TAG, "FAILED");
                                            }
                                        }
                                    });
                        }else {
                            Log.d(TAG, "new drive contents FAILED.");
                        }
                    }
                });*/
    }

}
