package com.example.mario_project.myapplication;

/**
 * Created by woolo_so5omoy on 2017-08-09.
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
public class FullScreenTest extends SingleTouchTest{
    @Override
    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }
}
