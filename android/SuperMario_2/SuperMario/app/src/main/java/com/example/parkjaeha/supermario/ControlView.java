package com.example.parkjaeha.supermario;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by parkjaeha on 2017-08-17.
 */

public class ControlView extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener{


    private double angle = 0.0;
    private KnobListener listner;
    float x,y;
    float mx,my;

    public interface  KnobListener{
        public void onChanged(double angle);
    }

    public void setKnobListener(KnobListener lis){
        listner = lis;
    }

    public ControlView(Context context){
        super(context);
        this.setImageResource(R.drawable.exit);
        this.setOnTouchListener(this);
    }


    public ControlView(Context context,AttributeSet attrs){
        super(context, attrs);
        this.setImageResource(R.drawable.exit);
        this.setOnTouchListener(this);
    }

    private double getAngle(float x, float y){
        mx = x - (getWidth()/2.0f);

        my = (getHeight() / 2.0f) -y;
        double degree = Math.atan2(mx,my) * 180.0 / 3.141592;
        degree = Math.round(degree);
        return  degree;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x=event.getX(0);
        y=event.getY(0);
        angle = getAngle(x,y);
        invalidate();;
        listner.onChanged(angle);

        return true;
    }

    protected  void onDraw(Canvas c){
        Paint paing = new Paint();

        c.save();
        c.rotate((float)angle,getWidth()/2,getHeight()/2);
        super.onDraw(c);
        c.restore();

    }

}

