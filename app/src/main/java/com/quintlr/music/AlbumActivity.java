package com.quintlr.music;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    SongListAdapter songListAdapter;
    long albumId = 0;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            albumId = extras.getLong("selected_album_id", 0L);
            Log.d("akash", "onCreate: "+albumId);
        }else {
            albumId = (long) savedInstanceState.getSerializable("selected_album_id");
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(this, DividerItemDecorator.VERTICAL_LIST);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_view);
        appBarLayout = (AppBarLayout) findViewById(R.id.album_layout_app_bar_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        songListAdapter = new SongListAdapter(Fetcher.getSongsFromAlbumID(this, albumId));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(songListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.RecyclerClickListener() {
            @Override
            public void OnClick(View view, int position) {
                PlayQueue.deletePlayQueue();
                PlayQueue.createQueue(Fetcher.getSongsFromAlbumID(getApplicationContext(), albumId));
                SongControl.getSongControlInstance(position).play_song();
            }

            @Override
            public void OnLongClick(View view, int position) {

            }
        }));

        ImageView album_art = (ImageView) findViewById(R.id.album_layout_album_art);
        FloatingActionButton fab_album_shuffle = (FloatingActionButton) findViewById(R.id.album_layout_fab);

        Glide.with(this)
                .load(Fetcher.getAlbumArtFromAlbumID(this, albumId))
                .placeholder(R.drawable.loading)
                .into(album_art);

        toolbar = (Toolbar) findViewById(R.id.album_layout_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.album_layout_collapsing_toolbar);

        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        actionBarDrawerToggle.syncState();

        Palette palette = new Palette.Builder(BitmapFactory.decodeFile(Fetcher.getAlbumArtFromAlbumID(this, albumId))).generate();
        int col = palette.getDarkMutedColor(ContextCompat.getColor(this, R.color.colorPrimary));
        collapsingToolbarLayout.setBackgroundColor(col);
        collapsingToolbarLayout.setStatusBarScrimColor(col);
        collapsingToolbarLayout.setContentScrimColor(col);
        fab_album_shuffle.setBackgroundTintList(ColorStateList.valueOf(col));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Toast.makeText(this, "dnvlsknv", Toast.LENGTH_SHORT).show();
            /*
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            appBarLayout.setExpanded(false);*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
