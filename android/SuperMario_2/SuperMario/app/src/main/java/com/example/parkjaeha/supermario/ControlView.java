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
    final int MAX_POINTS =2;
    float [] fx = new float[MAX_POINTS];
    float [] fy = new float[MAX_POINTS];
    boolean[] touching = new boolean[MAX_POINTS];
    static int key=0;

    public void ControlView(){
        this.key = 0;
    }


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

        /*int count = event.getPointerCount();
        Log.v("count >>", count + "");
        if (count == 2) {
            // some action
        }*/

        // multitouch
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        int action = event.getActionMasked();

     /*   switch (action){

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                fx[id] = (int) event.getX(index);
                fy[id] = (int) event.getY(index);
                touching[id] =true;

                if(MainActivity.dKey ==1 && MainActivity.mKey ==1 )
                System.out.println("ACTION_JAEHA2: " +fx[id]+"  "+fy[id] +"id:" + id);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                touching[id] = false;
                break;
        }*/

        /*if((action == MotionEvent.ACTION_DOWN) &&(action == MotionEvent.ACTION_POINTER_DOWN)){
            System.out.println("ACTION_JAEHA3: " +fx[id]+"  "+fy[id] +"id:" + id);

        }*/

        if(event.getActionMasked() == event.ACTION_UP){
            MainActivity.cKey =0;
            this.key =MainActivity.cKey;
            MainActivity.mKey = 4;
        }
        if(event.getActionMasked() == event.ACTION_DOWN){
            MainActivity.cKey =1;
            this.key =MainActivity.cKey;
        }

        invalidate();
        listner.onChanged(angle);

        return true;
    }

  /*  @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = (int) event.getX();
        y = (int) event.getY();

        int action =event.getActionMasked();

        if(action ==MotionEvent.ACTION_DOWN){
            this.str = "ACTION_DOWN";
            MainActivity.cKey = 1;
        }

        if(action ==MotionEvent.ACTION_UP){
            this.str = "ACTION_UP";
            MainActivity.cKey = 0;
        }


        return super.onTouchEvent(event);
    }*/


    protected  void onDraw(Canvas c){
        Paint paing = new Paint();

        c.save();
        c.rotate((float)angle,getWidth()/2,getHeight()/2);
        super.onDraw(c);
        c.restore();

    }

}

