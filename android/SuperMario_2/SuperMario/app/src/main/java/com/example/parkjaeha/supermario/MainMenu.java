package com.example.parkjaeha.supermario;
import java.io.IOException;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
/**
 * Created by parkjaeha on 2017-07-28.
 */

public class MainMenu extends AppCompatActivity{
//시작화면
    MediaPlayer mediaPlayer;
    int explosionId=-1;

    //TextView tx_press;
    RelativeLayout relativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.mainmenu);
        //사운드 배경음악
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer=MediaPlayer.create(this,R.raw.opening);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        //배경 아무곳 누르면 다음 화면으로 이동
        relativeLayout = (RelativeLayout)findViewById(R.id.re_mainmenu);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                Intent myIntent = new Intent(MainMenu.this,SubMenu.class);
                startActivity(myIntent);
            }
        });
/*

        tx_press = (TextView)findViewById(R.id.tx_press);

        tx_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this,SubMenu.class);
                startActivity(myIntent);

            }
        });
*/

    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
