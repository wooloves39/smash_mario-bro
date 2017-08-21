package com.example.parkjaeha.supermario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import static com.example.parkjaeha.supermario.R.id.img_map;

/**
 * Created by parkjaeha on 2017-07-28.
 */

import android.media.AudioManager;
import android.media.SoundPool;
public class SubMenu extends ActionBarActivity {
//맵나오는 화면
    MediaPlayer mediaPlayer;
    SoundPool choice;
    int explosionId=-1;

    int mapNumber=0;
    ImageView imageView;
    Info image = new Info();
    Gallery gallery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //사운드 배경음악
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer=MediaPlayer.create(this,R.raw.choice_state);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        choice=new SoundPool(2,AudioManager.STREAM_MUSIC,0);
        explosionId=choice.load(this,R.raw.choice,1);
        //풀스크린
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //submenu_layout
        setContentView(R.layout.submenu);

        imageView = (ImageView) findViewById(img_map);

        //이미지 기본세팅
        imageView.setImageResource(image.imageIDs[mapNumber]);
       // 갤러리뷰 셋어뎁터
        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));

        //이미지 클릭시
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
choice.play(explosionId,1,1,0,0,1);
                if(mapNumber==9){
                    mapNumber = (int)(Math.random()*9);
                }
                Intent mainact = new Intent(SubMenu.this,CharacterMenu.class);
                mainact.putExtra("map",mapNumber);//해답은 여기있을듯 사운드 자료 같이 받아오는법
                startActivity(mainact);
            }
        });
        //갤러리 클릭시
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                choice.play(explosionId,1,1,0,0,1);
                imageView.setImageResource(image.imageIDs[position]);
                System.out.println(position);
                mapNumber = position;

            }
        });


    }
    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

        //이미지 어뎁터
        public class ImageAdapter extends BaseAdapter{
            private Context context;
            //private int itemBackground;
            public ImageAdapter(Context c){
                context = c;
            }
            public int getCount(){
                return image.imageIDs.length;
            }

            public Object getItem(int postion){
            return postion;
            }

            public long getItemId(int postion){
                return postion;
            }
            public View getView(int postion, View convertView, ViewGroup parent) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(image.imageIDs[postion]);
                imageView.setLayoutParams(new Gallery.LayoutParams(400, 400));
                imageView.setBackgroundColor(Color.GRAY);
                return imageView;
            }

         }

}
