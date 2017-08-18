package com.example.parkjaeha.supermario.hyunwoo;
/**
 * Created by woolo_so5omoy on 2017-08-08.
 */

public interface Music {
    public void play();
    public void stop();
    public void pause();
    public void setLooping(boolean looping);
    public void setVolume(float volum);

    public boolean isPlaying();
    public boolean isStopped();
    public boolean isLooping();

    public void dispose();
}
