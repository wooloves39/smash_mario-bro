package com.example.mario_project.myapplication.framework.impl;

/**
 * Created by woolo_so5omoy on 2017-08-10.
 */
import java.util.List;
import android.view.View.OnTouchListener;
import com.example.mario_project.myapplication.framework.Input.TouchEvent;
public interface TouchHandler extends OnTouchListener{
    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);
    public int getTouchY(int pointer);

    public List<TouchEvent> getTouchEvents();
}
//    public static class TouchEvent{
//        public static final int TOUCH_DOWN=0;
//        public static final int TOUCH_UP=1;
//        public static final int TOUCH_DRAGGED=2;
//
//        public int type;
//        public int x,y;
//        public int pointer;
//    }
//}
