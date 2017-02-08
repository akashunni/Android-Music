package com.quintlr.music;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class PlayerActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    public static Bitmap blur_album_art = null;
    static boolean set_zero_seekbar = true;
    ImageView blur_back, album_art;
    ImageButton play_pause_btn, next_btn, prev_btn, rep_btn, shfl_btn;
    SeekBar seekBar;
    static Handler seekbar_handler;
    TextView songTitle, songAlbum, songArtist, totalTime, elapsedTime;
    GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        seekBar = (SeekBar) findViewById(R.id.player_seekbar);
        blur_back = (ImageView) findViewById(R.id.player_blur_album_art);
        album_art = (ImageView) findViewById(R.id.player_album_art);
        songTitle = (TextView) findViewById(R.id.player_song_title);
        songAlbum = (TextView) findViewById(R.id.player_album_title);
        songArtist = (TextView) findViewById(R.id.player_song_artist);
        elapsedTime = (TextView) findViewById(R.id.player_time_elapsed);
        totalTime = (TextView) findViewById(R.id.player_time_total);
        play_pause_btn = (ImageButton) findViewById(R.id.player_play_button);
        next_btn = (ImageButton) findViewById(R.id.player_next_button);
        prev_btn = (ImageButton) findViewById(R.id.player_prev_button);
        rep_btn = (ImageButton) findViewById(R.id.player_repeat_button);
        shfl_btn = (ImageButton) findViewById(R.id.player_shuffle_button);
        seekBar.setPadding(0,0,0,0);

        seekbar_handler = new Handler();

        setPlayerValues();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean from_user) {
                if(from_user){
                    SongControl.getSongControlInstance().seekTo(progress);
                    elapsedTime.setText(SongControl.getSongControlInstance().getTimeFromMilliSec(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }


        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressAnimator.startProgressAnimator(seekBar, seekBar.getProgress(), 0);
                SongControl.getSongControlInstance().nextSong();
                setPlayerValuesExceptSeekBar();
            }
        });

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressAnimator.startProgressAnimator(seekBar, seekBar.getProgress(), 0);
                SongControl.getSongControlInstance().prevSong();
                setPlayerValuesExceptSeekBar();
            }
        });

        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongControl.getSongControlInstance().playOrPause();
                if (SongControl.getSongControlInstance().isPlaying()){
                    seekBar.removeCallbacks(seekbarThread);
                    seekbarThread.run();
                }
            }
        });

        shfl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SongControl.getSongControlInstance().isShuffled()){
                    PlayQueue.shuffleQueue();
                    SongControl.getSongControlInstance().setShuffledState(true);
                    Toast.makeText(PlayerActivity.this, "Shuffling Songs", Toast.LENGTH_SHORT).show();
                }
                /**  **/
            }
        });

        gestureDetectorCompat = new GestureDetectorCompat(getApplicationContext(), this);
    }


    void setPlayerValues(){
        if (!PlayQueue.isQueueNULL()){
            seekBar.setMax(SongControl.getSongControlInstance().getTotalDurationInMillis());
            seekBar.setProgress(SongControl.getSongControlInstance().getElapsedDurationInMillis());
            totalTime.setText(SongControl.getSongControlInstance().getTotalDuration());
            elapsedTime.setText(SongControl.getSongControlInstance().getElapsedTime());
            songTitle.setText(PlayQueue.getCurrentSong().getSongTitle());
            songAlbum.setText(PlayQueue.getCurrentSong().getAlbumName());
            songArtist.setText(PlayQueue.getCurrentSong().getSongArtist());

            Glide.with(this)
                    .load(PlayQueue.getCurrentSong().getSongAlbumArt())
                    .into(album_art);

            blur_album_art = PlayQueue.getCurrentSong().getSongAlbumArtAsBitmap();
            if(blur_album_art!=null){
                blur_album_art = Bitmap.createScaledBitmap(blur_album_art,50,50,true);
                blur_album_art = changeBitmapContrastBrightness(blur_album_art,1,-70);
                blur_album_art = fastblur(this, blur_album_art, 15);
                blur_back.setImageBitmap(blur_album_art);
            }

            seekBar.removeCallbacks(seekbarThread);
            seekBar.post(seekbarThread);

            if (SongControl.getSongControlInstance().getPausedState()){
                play_pause_btn.setImageResource(R.drawable.play_arrow_white_24dp);
            }else{
                play_pause_btn.setImageResource(R.drawable.pause_white_24dp);
            }
        }else {
            Toast.makeText(this, "No Songs Available :(", Toast.LENGTH_SHORT).show();
        }
    }


    void setPlayerValuesExceptSeekBar(){
        if (!PlayQueue.isQueueNULL()){
            seekBar.setMax(SongControl.getSongControlInstance().getTotalDurationInMillis());
            seekBar.setProgress(SongControl.getSongControlInstance().getElapsedDurationInMillis());
            totalTime.setText(SongControl.getSongControlInstance().getTotalDuration());
            elapsedTime.setText(SongControl.getSongControlInstance().getElapsedTime());
            songTitle.setText(PlayQueue.getCurrentSong().getSongTitle());
            songAlbum.setText(PlayQueue.getCurrentSong().getAlbumName());
            songArtist.setText(PlayQueue.getCurrentSong().getSongArtist());

            Glide.with(this)
                    .load(PlayQueue.getCurrentSong().getSongAlbumArt())
                    .into(album_art);

            blur_album_art = PlayQueue.getCurrentSong().getSongAlbumArtAsBitmap();
            if(blur_album_art!=null){
                blur_album_art = Bitmap.createScaledBitmap(blur_album_art,50,50,true);
                blur_album_art = changeBitmapContrastBrightness(blur_album_art,1,-70);
                blur_album_art = fastblur(this, blur_album_art, 15);
                blur_back.setImageBitmap(blur_album_art);
            }

            if (SongControl.getSongControlInstance().getPausedState()){
                play_pause_btn.setImageResource(R.drawable.play_arrow_white_24dp);
            }else{
                play_pause_btn.setImageResource(R.drawable.pause_white_24dp);
            }
        }else {
            Toast.makeText(this, "No Songs Available :(", Toast.LENGTH_SHORT).show();
        }
    }

    private Runnable seekbarThread = new Runnable() {
        @Override
        public void run() {
            if (set_zero_seekbar) {
                setPlayerValuesExceptSeekBar();
                set_zero_seekbar = false;
            }
            elapsedTime.setText(SongControl.getSongControlInstance().getElapsedTime());
            seekBar.setMax(SongControl.getSongControlInstance().getTotalDurationInMillis());
            seekBar.setProgress(SongControl.getSongControlInstance().getElapsedDurationInMillis());
            if (SongControl.getSongControlInstance().isPlaying()){
                seekBar.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /** This is not my code. Although I modified it :) */
    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness)
    {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {
        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }
        return  null;
    }
    /** upto here */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // 125 is the threshold value set by me
        if ((e2.getY()-e1.getY()) > 125){
            onBackPressed();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}