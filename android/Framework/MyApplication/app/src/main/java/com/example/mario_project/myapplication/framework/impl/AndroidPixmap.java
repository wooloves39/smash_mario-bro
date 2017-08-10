package com.example.mario_project.myapplication.framework.impl;

/**
 * Created by woolo_so5omoy on 2017-08-10.
 */
import android.graphics.Bitmap;

import com.example.mario_project.myapplication.framework.Graphics.PixmapFormat;
import com.example.mario_project.myapplication.framework.Pixmap;
public class AndroidPixmap implements Pixmap {
    Bitmap bitmap;
    PixmapFormat format;

    public AndroidPixmap(Bitmap itmap,PixmapFormat format){
        this.bitmap=bitmap;
        this.format=format;
    }
    @Override
    public int getWidth(){
        return bitmap.getWidth();
    }
    @Override
    public int getHeight(){
        return bitmap.getHeight();
    }
    @Override
    public PixmapFormat getFormat(){
        return format;
    }
    @Override
    public void dispose(){
        bitmap.recycle();
    }
}
