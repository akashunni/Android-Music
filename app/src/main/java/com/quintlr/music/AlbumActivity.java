package com.quintlr.music;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

public class AlbumActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    SongListAdapter songListAdapter;
    long albumId = 0;
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

        songListAdapter = new SongListAdapter(Fetcher.getSongsFromAlbumID(this, albumId));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(songListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.RecyclerClickListener() {
            @Override
            public void OnClick(View view, int position) {
                /**
                 * implement play song in album view...
                 * */
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.album_layout_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.album_layout_collapsing_toolbar);

        toolbar.showOverflowMenu();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
