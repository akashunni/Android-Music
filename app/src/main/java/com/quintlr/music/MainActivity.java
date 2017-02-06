package com.quintlr.music;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.security.Permission;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    static Context context;
    ViewPager viewPager;
    static final int STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPrefs.assignCurrentSongIndex(this);
        context = this;
        // toolbar/actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // all about navigation drawer toggle on toolbar
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // nav view options selector listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // check for permissions..
        // if permission is GRANTED then loadComponents() function is called.
        /** TO BE FIXED : when app is in background and permission is revoked **/
        checkAndGetPermissions();

        // Where tabs appears
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

        if (!PlayQueue.isQueueNULL()){
            MiniPlayer.setMiniPlayerValues(this, SharedPrefs.getCurrentSongElapsedDuration(this), SharedPrefs.getCurrentSongTotalDuration(this));
        }else {
            Toast.makeText(context, "No Songs Available :(", Toast.LENGTH_SHORT).show();
        }
        /** this statement is causing the song to reload when resumed after pressing the back button **/
    }

    // components which require the storage permissions.
    void loadComponents(){
        MyLibTabFragmentAdapter myLibTabFragmentAdapter = new MyLibTabFragmentAdapter(getSupportFragmentManager(), getApplicationContext());
        if (viewPager != null) {
            viewPager.setAdapter(myLibTabFragmentAdapter);
        }

        // create the playQueue
        PlayQueue.createQueue(Fetcher.getRealSongArrayList(this));

        // fab button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayQueue.reCreateListAndShuffle(Fetcher.getRealSongArrayList(getApplicationContext()));
                SongControl.getSongControlInstance().loadSong();
                Toast.makeText(getApplicationContext(), "Shuffling all Songs", Toast.LENGTH_SHORT).show();
            }
        });

        // load mini player with the song.
        SongControl.getSongControlInstance().loadSong();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

        }

        return super.onOptionsItemSelected(item);
    }

    // method implemented from nav view on item select
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            viewPager.setCurrentItem(4);
        } else if (id == R.id.nav_songs) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_albums) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_artists) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_playlists) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_settings) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        //saves the current song's index.
        SharedPrefs.setCurrentSongIndex(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        SongControl.releaseMediaPlayer();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("akash", "onActivityResult: " + requestCode + " " + resultCode);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            viewPager.setCurrentItem(data.getIntExtra("TAB_ITEM", 0));
        }
    }

    public void checkAndGetPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            /* The permission is NOT already granted.
             Check if the user has been asked about this permission already and denied
             it. If so, we want to give more explanation about why the permission is needed.*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    /* Show our own UI next time when the user denied for the permission*/
                    // calls when dialog shows after the first denial.
                    //Log.d("akash", "checkAndGetPermissions: IF->RATIONALE");
                }
                /* Fire off an async request to actually get the permission
                 This will show the standard permission request dialog UI*/
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            }
        }else {
            loadComponents();
        }
    }

    // callback
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION){
            if (grantResults.length==1 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                // if permission is not granted.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        // onDenyClick

                    }else {
                        // don't show again checked and deny clicked.
                        // called always on start if that option was checked.
                        permissionUI();
                    }
                }
            }else {
                // allow clicked.
                // called always on start if that option was clicked.
                loadComponents();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void permissionUI(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Permission to access the songs from your device was denied. You cannot see the content or play any music unless the permission is GRANTED. Would you like to do it now?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        Toast.makeText(MainActivity.this, "Click on PERMISSIONS & grant access to Storage & RESTART THE APP.", Toast.LENGTH_LONG).show();
                        //open app settings.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
                        Snackbar.make(coordinatorLayout, "Cannot retrieve songs.", Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // on clicking Retry
                                        permissionUI();
                                    }
                                })
                                .show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
