package com.quintlr.music;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by akash on 29/1/17.
 */

public class PlayerFragment extends Fragment {

    public static Bitmap blur_album_art = null;
    static boolean set_zero_seekbar = true;
    ImageView blur_back, album_art;
    ImageButton play_pause_btn, next_btn, prev_btn, rep_btn, shfl_btn;
    SeekBar seekBar;
    TextView songTitle, songAlbum, songArtist, totalTime;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        seekBar = (SeekBar) view.findViewById(R.id.player_seekbar);
        blur_back = (ImageView) view.findViewById(R.id.player_blur_album_art);
        album_art = (ImageView) view.findViewById(R.id.player_album_art);
        songTitle = (TextView) view.findViewById(R.id.player_song_title);
        songAlbum = (TextView) view.findViewById(R.id.player_album_title);
        songArtist = (TextView) view.findViewById(R.id.player_song_artist);
        totalTime = (TextView) view.findViewById(R.id.player_time_total);
        play_pause_btn = (ImageButton) view.findViewById(R.id.player_play_button);
        next_btn = (ImageButton) view.findViewById(R.id.player_next_button);
        prev_btn = (ImageButton) view.findViewById(R.id.player_prev_button);
        rep_btn = (ImageButton) view.findViewById(R.id.player_repeat_button);
        shfl_btn = (ImageButton) view.findViewById(R.id.player_shuffle_button);
        seekBar.setPadding(0,0,0,0);
        setPlayerValues();
        return view;
    }

    void setPlayerValues(){
        totalTime.setText(SongControl.getSongControlInstance().getTotalDuration());
        songTitle.setText(PlayQueue.getCurrentSong().getSongTitle());
        songAlbum.setText(PlayQueue.getCurrentSong().getAlbumName());
        songArtist.setText(PlayQueue.getCurrentSong().getSongArtist());
        Glide.with(this)
                .load(PlayQueue.getCurrentSong().getSongAlbumArt())
                .into(album_art);
        blur_album_art = BitmapFactory.decodeByteArray(PlayQueue.getCurrentSong().getSongAlbumArt(),0,PlayQueue.getCurrentSong().getSongAlbumArt().length);
        if(blur_album_art!=null){
            blur_album_art = Bitmap.createScaledBitmap(blur_album_art,50,50,true);
            blur_album_art = changeBitmapContrastBrightness(blur_album_art,1,-60);
            blur_album_art = fastblur(getContext(), blur_album_art, 15);
            blur_back.setImageBitmap(blur_album_art);
        }
        seekBar.postDelayed(seekbarThread,1000);
        if (SongControl.getSongControlInstance().getPausedState()){
            play_pause_btn.setImageResource(R.drawable.play_arrow_white_24dp);
        }else{
            play_pause_btn.setImageResource(R.drawable.pause_white_24dp);
        }
    }

    private Runnable seekbarThread = new Runnable() {
        @Override
        public void run() {
            /** temporary solution */
            if(set_zero_seekbar){
                setPlayerValues();
                set_zero_seekbar = false;
            }
            /** upto here */
            if (SongControl.getSongControlInstance().mediaPlayer.isPlaying()){
                seekBar.setMax(SongControl.getSongControlInstance().mediaPlayer.getDuration());
                seekBar.setProgress(SongControl.getSongControlInstance().mediaPlayer.getCurrentPosition());
                seekBar.postDelayed(this, 1000);
            }
        }
    };

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
}
