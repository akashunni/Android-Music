package com.quintlr.music;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    static Context context;
    ViewPager viewPager;

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
        MyLibTabFragmentAdapter myLibTabFragmentAdapter = new MyLibTabFragmentAdapter(getSupportFragmentManager(), getApplicationContext());
        if (viewPager != null) {
            viewPager.setAdapter(myLibTabFragmentAdapter);
        }

        // Where tabs appears
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

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

        if (!PlayQueue.isQueueNULL()){
            MiniPlayer.setMiniPlayerValues(this, SharedPrefs.getCurrentSongElapsedDuration(this), SharedPrefs.getCurrentSongTotalDuration(this));
        }else {
            Toast.makeText(context, "No Songs Available :(", Toast.LENGTH_SHORT).show();
        }
        /** this statement is causing the song to reload when resumed after pressing the back button **/
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
        SharedPrefs.setCurrentSongIndex(this);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("akash", "onActivityResult: " + requestCode + " " + resultCode);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            viewPager.setCurrentItem(data.getIntExtra("TAB_ITEM", 0));
        }
    }
}
