package com.example.parkjaeha.supermario;

/**
 * Created by parkjaeha on 2017-08-04.
 */
public abstract class GameObject {

    protected int x;
    protected  int y;
    protected  int dy;
    protected  int dx;
    protected  int width;
    protected  int height;

/*
    public GameObject(Bitmap image, int rowCount, int colCount, int x, int y)  {

        this.image = image;
        this.rowCount= rowCount;
        this.colCount= colCount;

        this.x= x;
        this.y= y;

        this.WIDTH = image.getWidth();
        this.HEIGHT = image.getHeight();

        this.width = this.WIDTH/ colCount;
        this.height= this.HEIGHT/ rowCount;
    }


    protected Bitmap createSubImageAt(int row, int col)  {
        // createBitmap(bitmap, x, y, width, height).
        Bitmap subImage = Bitmap.createBitmap(image, col* width, row* height ,width,height);
        return subImage;
    }

    publ*/

}
