package com.example.parkjaeha.supermario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by parkjaeha on 2017-07-28.
 */

public class MainMenu extends AppCompatActivity{
//시작화면


    //TextView tx_press;
    RelativeLayout relativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.mainmenu);

        //배경 아무곳 누르면 다음 화면으로 이동
        relativeLayout = (RelativeLayout)findViewById(R.id.re_mainmenu);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this,SubMenu.class);
                startActivity(myIntent);
            }
        });
/*

        tx_press = (TextView)findViewById(R.id.tx_press);

        tx_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this,SubMenu.class);
                startActivity(myIntent);

            }
        });
*/

    }
}
