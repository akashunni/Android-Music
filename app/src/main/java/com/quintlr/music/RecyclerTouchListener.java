package com.quintlr.music;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Akash on 6/29/2016.
 */

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    public interface RecyclerClickListener{
        void OnClick(View view, int position);
        void OnLongClick(View view, int position);
    }

    private GestureDetector gestureDetector;
    private RecyclerClickListener recyclerClickListener;

    RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RecyclerClickListener recyclerClickListener){
        this.recyclerClickListener = recyclerClickListener;
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && recyclerClickListener != null) {
                    recyclerClickListener.OnLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && recyclerClickListener != null && gestureDetector.onTouchEvent(e)) {
            recyclerClickListener.OnClick(child, rv.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
