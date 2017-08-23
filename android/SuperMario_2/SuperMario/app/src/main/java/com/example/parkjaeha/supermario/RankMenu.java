package com.example.parkjaeha.supermario;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by parkjaeha on 2017-08-23.
 */


public class RankMenu  extends Activity {

    RelativeLayout relativeLayout;
    boolean check = false;

    View.OnClickListener mainmove = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent finish = new Intent(RankMenu.this,MainMenu.class);
            startActivity(finish);
            //continue
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rankmenu);

        relativeLayout = (RelativeLayout) findViewById(R.id.rank_finish);


    /* adapt the image to the size of the display */

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                relativeLayout.setBackgroundResource(R.drawable.end);
            check = true;

                relativeLayout.setOnClickListener(mainmove);
            }
        }, 5000);
    }

    @Override
    public void onBackPressed() {

            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("정말 종료하시겠습니까?");
            d.setPositiveButton("예", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // process전체 종료
                    Intent i =  new Intent(RankMenu.this,MainMenu.class);
                    startActivity(i);
                    finish();
                }
            });
            d.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            d.show();



    }


}
