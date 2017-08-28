package com.example.parkjaeha.supermario;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.parkjaeha.supermario.R.id.img_map;

/**
 * Created by parkjaeha on 2017-07-28.
 */

public class SubMenu extends ActionBarActivity {
//맵나오는 화면

    static  MediaPlayer mediaPlayer;
    SoundPool choice;
    int explosionId=-1;

    int mapNumber=0;
    ImageView imageView;
    Info image = new Info();
    Gallery gallery;
    int legnth=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //사운드 배경음악
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer=MediaPlayer.create(this,R.raw.choice_state);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //  startService(new Intent("com.example.parkjaeha.supermario"));
        choice=new SoundPool(2,AudioManager.STREAM_MUSIC,0);
        explosionId=choice.load(this,R.raw.choice,1);

        //run 을 이용해 패키지의 액티비티가 top에 있는지 확인해서 없으면 미디어 pause 있으면 start

        Timer t = new Timer();

        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    String str = null;
                    @Override
                    public void run() {

                        ActivityManager am ;
                        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                        Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
                        ComponentName componentInfo = taskInfo.get(0).topActivity;
                        str=    componentInfo.getPackageName();
                        if(!str.equals("com.example.parkjaeha.supermario")){
                            mediaPlayer.pause();
                        }else {
                           mediaPlayer.start();
                        }
                    }
                });
            }

        }, 0, 1000);

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
                mainact.putExtra("map",mapNumber);
                startActivity(mainact);
                finish();

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

    //다시시작시,멈출시
    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mediaPlayer.start();
        mediaPlayer.seekTo(legnth);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        legnth = mediaPlayer.getCurrentPosition();
    }
}
