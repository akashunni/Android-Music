package com.quintlr.music;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

/**
 * Created by akash on 19/2/17.
 */

public class SyncDriveSongs {

    private static String TAG = "akash";

    static void createFolder(GoogleApiClient googleApiClient, String DRIVE_FOLDER_NAME){
        if (googleApiClient.isConnected()){
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(DRIVE_FOLDER_NAME)
                    .build();
            Drive.DriveApi.getRootFolder(googleApiClient)
                    .createFolder(googleApiClient, changeSet)
                    .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                        @Override
                        public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {
                            Log.d(TAG, "onResult: "+driveFolderResult.getStatus().isSuccess());
                        }
                    });
        }else {
            googleApiClient.connect();
            Log.d(TAG, "NOT CONNECTED "+googleApiClient.isConnecting());
        }
    }

}
