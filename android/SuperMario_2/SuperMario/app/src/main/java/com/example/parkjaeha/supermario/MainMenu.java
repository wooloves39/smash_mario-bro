package com.example.parkjaeha.supermario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by parkjaeha on 2017-07-28.
 */

public class MainMenu extends AppCompatActivity{
//시작화면
    TextView tx_press;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.mainmenu);

        tx_press = (TextView)findViewById(R.id.tx_press);
        //Typeface custom = Typeface.createFromAsset(getAssets(),"font.ttf");
        //tx_press.setTypeface(custom);

        tx_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this,SubMenu.class);
                startActivity(myIntent);

            }
        });

    }
}
