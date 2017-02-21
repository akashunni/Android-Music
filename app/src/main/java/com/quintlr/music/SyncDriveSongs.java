package com.quintlr.music;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.FileUploadPreferences;
import com.google.android.gms.drive.MetadataChangeSet;

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
        createFolder();
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
                            Log.d(TAG, "new drive contents OK.");
                            DriveContents driveContents = driveContentsResult.getDriveContents();
                            OutputStream outputStream = driveContents.getOutputStream();

                            Writer writer = new OutputStreamWriter(outputStream);
                            try {
                                writer.write("HAHAH");
                                writer.close();
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                            }

                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle("akashunni")
                                    .setMimeType("text/plain")
                                    .setStarred(true)
                                    .build();

                            // create a file in root folder
                            Drive.DriveApi.getRootFolder(googleApiClient)
                                    .createFile(googleApiClient, changeSet, driveContents)
                                    .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                        @Override
                                        public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
                                            if (driveFileResult.getStatus().isSuccess()){
                                                Log.d(TAG, "File Creation SUCCESS.");
                                            }else {
                                                Log.d(TAG, "File creation FAILED.");
                                            }
                                        }
                                    });
                        }else {
                            Log.d(TAG, "new drive contents FAILED.");
                        }
                    }
                });
    }

}
