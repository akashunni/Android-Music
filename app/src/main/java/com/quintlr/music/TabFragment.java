package com.quintlr.music;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Akash on 6/27/2016.
 */

public class TabFragment extends android.support.v4.app.Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    static ArrayList<Song> songList;
    static ArrayList<Album> albumList;
    static ArrayList<Artist> artistList;

    public static TabFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /****DANGEROUS*******/
        songList = Fetcher.getRealSongArrayList(getContext());
        albumList = Fetcher.getAlbumList(getContext());
        artistList = Fetcher.getArtistList(getContext());
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        final RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(getContext(), DividerItemDecorator.VERTICAL_LIST);
        switch (mPage) {
            // recycler view for songs list
            case 1:
                recyclerView = (RecyclerView) view.findViewById(R.id.recycler_list_view);
                final SongListAdapter songListAdapter = new SongListAdapter(songList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(itemDecoration);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(songListAdapter);
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.RecyclerClickListener() {
                    @Override
                    public void OnClick(View view, int position) {
                        if (PlayQueue.isQueueChanged()) {
                            PlayQueue.deletePlayQueue();
                            /** I really don't know why songList has no data here after deleting the queue... So Fetching from scratch.. :( **/
                            PlayQueue.createQueue(Fetcher.getRealSongArrayList(getContext()));
                            PlayQueue.setQueueChanged(false);
                            SongControl.getSongControlInstance().setShuffledState(false);
                        }
                        SongControl.paused = false;
                        SongControl.getSongControlInstance(position).loadSong();
                    }

                    @Override
                    public void OnLongClick(View view, int position) {

                    }
                }));
                break;
            // recycler view for albums list
            case 2:
                recyclerView = (RecyclerView) view.findViewById(R.id.recycler_list_view);
                AlbumListAdapter albumListAdapter = new AlbumListAdapter(albumList, getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(itemDecoration);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(albumListAdapter);
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.RecyclerClickListener() {
                    @Override
                    public void OnClick(View view, int position) {
                        Fragment fragment = new AlbumFragment();
                        Bundle bundle = new Bundle();
                        bundle.putLong("selected_album_id", albumList.get(position).getId());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Transition explodeTransform = TransitionInflater.from(getContext()).
                                    inflateTransition(android.R.transition.explode);

                            setExitTransition(explodeTransform);
                            fragment.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_bottom));

                            fragment.setArguments(bundle);
                            getFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.drawer_layout, fragment, "album")
                                    .addToBackStack("BACKSTACK_album")
                                    .commit();
                        }
                    }

                    @Override
                    public void OnLongClick(View view, int position) {

                    }
                }));
                break;
            case 3:
                recyclerView = (RecyclerView) view.findViewById(R.id.recycler_list_view);
                ArtistListAdapter artistListAdapter = new ArtistListAdapter(artistList, getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(itemDecoration);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(artistListAdapter);
                break;
        }
        return view;
    }
}