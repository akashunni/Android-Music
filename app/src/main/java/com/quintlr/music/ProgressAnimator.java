package com.quintlr.music;

import android.animation.ValueAnimator;
import android.widget.ProgressBar;
import android.widget.SeekBar;

/**
 * Created by akash on 2/2/17.
 */

public class ProgressAnimator {
    static void startProgressAnimator(final ProgressBar progressBar, int start, int end){
        ValueAnimator anim = new ValueAnimator();
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressBar.setProgress((int) animation.getAnimatedValue());
            }
        });
        anim.setIntValues(start, end);
        anim.start();
    }

    static void startProgressAnimator(final SeekBar seekBar, int start, int end){
        ValueAnimator anim = new ValueAnimator();
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                seekBar.setProgress((int) animation.getAnimatedValue());
            }
        });
        anim.setIntValues(start, end);
        anim.start();
    }
}
