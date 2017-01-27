package com.quintlr.music;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Akash on 7/2/2016.
 *
 */

public class MyLibTabFragmentAdapter extends FragmentPagerAdapter{

    private String tabTitles[] = new String[] { "Songs", "Albums", "Artists" , "Playlists", "****"};

    public MyLibTabFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return TabFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
