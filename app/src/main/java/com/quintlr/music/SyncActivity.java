package com.quintlr.music;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class SyncActivity extends AppCompatActivity {

    private TextView syncScore;
    private ProgressBar syncProgress;
    private Button syncButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        syncScore = (TextView) findViewById(R.id.activity_sync_syncScore);
        syncProgress = (ProgressBar) findViewById(R.id.activity_sync_progressBar);
        syncButton = (Button) findViewById(R.id.activity_sync_syncBtn);

        ArrayList<Song> songArrayList = Fetcher.getRealSongArrayList(this);

        syncScore.setText("50/"+songArrayList.size());
        syncProgress.setMax(songArrayList.size());
        syncProgress.setProgress(50);

    }
}
