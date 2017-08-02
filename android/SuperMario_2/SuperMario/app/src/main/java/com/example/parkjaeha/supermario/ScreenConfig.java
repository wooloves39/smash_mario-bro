package com.example.parkjaeha.supermario;

/**
 * Created by parkjaeha on 2017-07-28.
 */

public class ScreenConfig {

    public  int mScreenWidth;
    public int mScreenHeight;
    public int mVirtualWidth;
    public int mVirtualHeight;

    public ScreenConfig(int ScreenWidth,int ScreenHeight){
        mScreenWidth = ScreenWidth;
        mScreenHeight = ScreenHeight;
    }
    public void setSize(int width,int height){
        mVirtualHeight = height;
        mVirtualWidth = width;
    }
    public int getX(int x){return  (int)(x*mScreenWidth/mVirtualWidth);}
    public int getY(int y){return  (int)(y*mScreenHeight/mVirtualHeight);}

}
