package com.example.mario_project.myapplication.framework;

/**
 * Created by woolo_so5omoy on 2017-08-08.
 */
import com.example.mario_project.myapplication.framework.Graphics.PixmapFormat;
public interface Pixmap {
    public int getWidth();
    public int getHeight();

    public PixmapFormat getFormat();
    public void dispose();
}
