package com.example.parkjaeha.supermario;

import android.view.SurfaceHolder;

/**
 * Created by parkjaeha on 2017-07-28.
 */

public class MainThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private com.example.parkjaeha.supermario.MainView mainView;
    private boolean running = false;


    public MainThread(SurfaceHolder surfaceHolder, MainView mainView){
        surfaceHolder = surfaceHolder;
        this.mainView = mainView;
    }


}
