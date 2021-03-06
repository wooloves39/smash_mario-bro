package com.example.parkjaeha.supermario;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by parkjaeha on 2017-07-30.
 */

public class CharacterMenu extends AppCompatActivity implements View.OnClickListener {
//캐릭터 선택 메뉴
    ImageView img_ch1,img_ch2,img_ch3,img_ch4;
    int ch_num = 0 ;
    static  int map_num=0;

    //MediaPlayer mediaPlayer;
    SoundPool choice;
    int explosionId=-1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.charactermenu);

        choice=new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        explosionId=choice.load(this,R.raw.choice,1);


        img_ch1 = (ImageView)findViewById(R.id.img_ch1);
        img_ch2 = (ImageView)findViewById(R.id.img_ch2);
        img_ch3 = (ImageView)findViewById(R.id.img_ch3);
        img_ch4 = (ImageView)findViewById(R.id.img_ch4);

        img_ch1.setOnClickListener(this);
        img_ch2.setOnClickListener(this);
        img_ch3.setOnClickListener(this);
        img_ch4.setOnClickListener(this);

        Intent i = getIntent();
        map_num = i.getExtras().getInt("map");


    }
    //캐릭터 고르면 미디어 스탑
//캐릭터 선택 시
    @Override
    public void onClick(View v) {

        if(v == img_ch1){
            choice.play(explosionId,1,1,0,0,1);
            SubMenu.mediaPlayer.stop();
            ch_num = 0;
            Intent chintent = new Intent(CharacterMenu.this,MainActivity.class);
            chintent.putExtra("character",ch_num);
            startActivity(chintent);
            finish();

        }else if( v == img_ch2){
            choice.play(explosionId,1,1,0,0,1);
            SubMenu.mediaPlayer.stop();
            ch_num = 1;
            Intent chintent = new Intent(CharacterMenu.this,MainActivity.class);
            chintent.putExtra("character",ch_num);
            startActivity(chintent);
            finish();

        }else if( v == img_ch3){
            choice.play(explosionId,1,1,0,0,1);
            SubMenu.mediaPlayer.stop();
            ch_num = 2;
            Intent chintent = new Intent(CharacterMenu.this,MainActivity.class);
            chintent.putExtra("character",ch_num);
            startActivity(chintent);
            finish();

        }else if( v == img_ch4){
            choice.play(explosionId,1,1,0,0,1);
            SubMenu.mediaPlayer.stop();
            ch_num = 3;
            Intent chintent = new Intent(CharacterMenu.this,MainActivity.class);
            chintent.putExtra("character",ch_num);
            startActivity(chintent);
            finish();

        }

    }
}
