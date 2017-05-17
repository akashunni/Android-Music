package com.quintlr.music;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SyncActivity extends AppCompatActivity {

    private TextView syncScore, profileEmail, driveSpace;
    private ProgressBar syncProgress;
    private Button syncButton;
    private ImageView profilePic;
    String DRIVE_FOLDER_NAME = "Music - Synced Songs";
    String TAG = "akash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        syncScore = (TextView) findViewById(R.id.activity_sync_syncScore);
        profileEmail = (TextView) findViewById(R.id.activity_sync_profileEMail);
        driveSpace = (TextView) findViewById(R.id.activity_sync_driveSpace);
        profilePic = (ImageView) findViewById(R.id.activity_sync_profilePic);
        syncProgress = (ProgressBar) findViewById(R.id.activity_sync_progressBar);
        syncButton = (Button) findViewById(R.id.activity_sync_syncBtn);

        final ArrayList<Song> songArrayList = Fetcher.getRealSongArrayList(this);

        profileEmail.setText(MainActivity.Email);
        Glide.with(getApplicationContext())
                .load(MainActivity.ImageURI)
                .into(profilePic);

        syncScore.setText("50/"+songArrayList.size());
        syncProgress.setMax(songArrayList.size());
        syncProgress.setProgress(50);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GoogleAPI.getInstance().isConnected()){
                    Log.d(TAG, "CONNECTED..!");
                    SyncDriveSongs.getInstance()
                            .sync(GoogleAPI.getInstance().getGoogleApiClient(),
                                    DRIVE_FOLDER_NAME,
                                    songArrayList);

                }else {
                    Log.d(TAG, "DISCONNECTED..!");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAPI.getInstance().connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GoogleAPI.getInstance().disconnect();
    }
}
