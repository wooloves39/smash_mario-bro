package com.example.mario_project.myapplication.framework;

/**
 * Created by woolo_so5omoy on 2017-08-08.
 */

public interface Music {
    public void play();
    public void stop();
    public void pause();
    public void setLooping(boolean looping);
    public void setVolum(float volum);

    public boolean isPlaying();
    public boolean isStopping();
    public boolean isLooping();

    public void dispose();
}
