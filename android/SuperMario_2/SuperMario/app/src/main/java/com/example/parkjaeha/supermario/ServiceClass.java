package com.example.parkjaeha.supermario;

/**
 * Created by woolo_so5omoy on 2017-08-25.
 */
         import android.app.Service;
         import android.content.Intent;
         import android.media.MediaPlayer;
         import android.media.MediaRecorder;
         import android.os.IBinder;
         import android.util.Log;
public class ServiceClass extends Service{
    private MediaPlayer mPlayer=null;
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onStart(Intent intent,int startId){
        super.onStart(intent,startId);
        mPlayer= MediaPlayer.create(this,R.raw.choice_state);
        mPlayer.start();
    }
    @Override
    public void onDestroy(){
        mPlayer.stop();
        super.onDestroy();
    }
}
