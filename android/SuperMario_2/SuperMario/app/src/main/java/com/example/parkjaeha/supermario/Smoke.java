package com.example.parkjaeha.supermario;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static android.R.attr.x;

/**
 * Created by parkjaeha on 2017-08-24.
 */

public class Smoke {
    int posX = 10;
    int posY = 10;
    int r;

    public  Smoke(int x , int y){
        r=3;
        this.posX=x;
        this.posY=y;
    }

    public void update(){
        posX -=10;

    }

    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(posX-r,posY-r,r,paint);
        canvas.drawCircle(posX-r,posY-r-2,r,paint);
        canvas.drawCircle(posX-x+4,posY-r+1,r,paint);

    }


}
