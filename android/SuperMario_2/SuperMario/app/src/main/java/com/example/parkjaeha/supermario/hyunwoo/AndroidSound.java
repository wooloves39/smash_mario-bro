package com.example.parkjaeha.supermario.hyunwoo;
/**
 * Created by woolo_so5omoy on 2017-08-10.
 */
import android.media.SoundPool;

public class AndroidSound implements Sound {
    int soundId;
    SoundPool soundPool;
    public AndroidSound(SoundPool soundPool,int soundId){
        this.soundId=soundId;
        this.soundPool=soundPool;
    }
    @Override
    public void play(float volume){
        soundPool.play(soundId,volume,volume,0,0,1);
    }
    @Override
    public void dispose(){
        soundPool.unload(soundId);
    }
}
