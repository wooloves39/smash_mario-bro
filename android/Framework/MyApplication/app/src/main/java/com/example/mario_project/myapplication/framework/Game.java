package com.example.mario_project.myapplication.framework;

/**
 * Created by woolo_so5omoy on 2017-08-08.
 */

public interface Game {
    public Input getInput();
    public FileIO getFileIO();
    public Graphics getGraphics();
    public Audio getAudio();
    public void setScreen(Screen screen);
    public Screen getCurrentScreen();
    public Screen getStartScreen();
}
