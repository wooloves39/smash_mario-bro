package com.example.parkjaeha.supermario;

/**
 * Created by woolo_so5omoy on 2017-08-27.
 */

public class Camera {
    POINT pos=new POINT();
    POINT realpos=new POINT();
    POINT getPos(){
        return pos;
    }
    void setPos(int x){
        if(x<-1400||x>=1400);
            else{
            pos.x=x*(2560/1880)-1280;
            pos.y=0;
            realpos.x=x*(2560/1880)-1280;
            realpos.y=0;
        }
    }
    void realsetPos(int x){
        if(x<=-1400||x>=1400);
        else{
            realpos.x=x*(2560/1880)-1280;
            realpos.y=0;
        }
    }
    void add(){
        if(realpos.x!=pos.x)
            pos.x+=(realpos.x-pos.x)/20;
    }
}
