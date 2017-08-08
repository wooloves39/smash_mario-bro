package com.example.mario_project.myapplication;

/**
 * Created by woolo_so5omoy on 2017-08-08.
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SingleTouchTest extends Activity implements OnTouchListener{
    StringBuilder builder=new StringBuilder();
    TextView textView;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        textView=new TextView(this);
        textView.setText("Touch and drag(one finger only)!");
        textView.setOnTouchListener(this);
        setContentView(textView);
    }
    @Override
    public boolean onTouch(View v,MotionEvent event){
        builder.setLength(0);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                builder.append("down, ");
                break;
            case MotionEvent.ACTION_CANCEL:
                    builder.append("cnacel, ");
                break;
            case MotionEvent.ACTION_UP:
                    builder.append("up, ");
                break;
        }
        builder.append(event.getX());
        builder.append(", ");
        builder.append(event.getY());
        String text=builder.toString();
        Log.d("TouchTest",text);
        textView.setText(text);
        return true;
    }
}
