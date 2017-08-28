package com.example.parkjaeha.supermario;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.parkjaeha.supermario.R.raw.opening;

/**
 * Created by parkjaeha on 2017-07-28.
 */

public class MainMenu extends AppCompatActivity{
//시작화면

    RelativeLayout relativeLayout;
    MediaPlayer mediaPlayer;
    int explosionId=-1;
    int legnth = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.mainmenu);


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

    //사운드 opening
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer=MediaPlayer.create(this, opening);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //배경 아무곳 누르면 다음 화면으로 이동
        relativeLayout = (RelativeLayout)findViewById(R.id.re_mainmenu);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mainmenu로 넘어갈때 뮤직 스탑
                mediaPlayer.stop();
                Intent myIntent = new Intent(MainMenu.this,SubMenu.class);
                startActivity(myIntent);

            }
        });
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
