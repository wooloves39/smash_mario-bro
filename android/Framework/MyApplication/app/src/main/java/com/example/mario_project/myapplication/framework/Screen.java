package com.example.mario_project.myapplication.framework;

/**
 * Created by woolo_so5omoy on 2017-08-08.
 */

public abstract class Screen {
    protected final Game game;
    public Screen(Game game){
        this.game=game;
    }
    public abstract void update(float deltaTime);
    public abstract void present(float deltaTime);
    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();
}
