package com.example.parkjaeha.supermario;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.parkjaeha.supermario.R.id.volume;


public class MainActivity extends ActionBarActivity {
    //extends activtiy
// 플레이 화면 불러오기
    GamePanel gamePanel;
    //ch_num test를 위해 1로 설정 -1로 나중에 변경 요망
    static int ch_num= -1;
    public static int nKey = -1;
    public static int mKey = -1;
    public static int cKey = -1;

    View pauseButton;
    View pauseMenu;
    View a_Button;
    View b_Button;
    View c_Button;
    View d_Button;
    View control_Button;
    View timeout;
    public static Toast mToast;
    RelativeLayout relativeLayout ;
    long myBaseTime;
    public int seconds = 99;
    Boolean timecheck = true;
    boolean doubleBackToExitPressedOnce = false;

    final int MAX_POINTS =2;


//continue 클릭시 플레이 계속
    View.OnTouchListener Continue_list =  new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            pauseMenu.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
            gamePanel.Pause_game = false;
            timecheck= true;
            return true;
        }
    };

    //mainmenu 클릭시 메인메뉴 화면으로 이동
    View.OnTouchListener Main_menu_list = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v,MotionEvent event) {
            gamePanel.thread.setRunning(false);
            Intent i = new Intent(MainActivity.this,MainMenu.class);
            startActivity(i);
            finish();

            return true;
        }
    };

    // 스탑버튼 클릭시 continue,main 선택용 보기가 나오고 게임 일시 정지
    View.OnTouchListener pause_click = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v,MotionEvent event) {
            pauseButton.setVisibility(View.GONE);
            pauseMenu.setVisibility(View.VISIBLE);
            gamePanel.Pause_game = true;
            timecheck = false;


            return true;
        }
    };

    //A btn
    View.OnTouchListener a_btn_click =  new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v,MotionEvent event) {
            Log.d("A_CLICK" , ": A");
            nKey = 0;
            return true;
        }
    };

    //B btn
    View.OnTouchListener b_btn_click =  new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v,MotionEvent event) {
            Log.d("B_CLICK" , ": B");
            nKey = 1;
            return true;
        }
    };

    //C btn
    View.OnTouchListener c_btn_click =  new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v,MotionEvent event) {
           Log.d("C_CLICK" , ": C");
            nKey = 2;
            return true;

        }
    };
    //D btn
    View.OnTouchListener d_btn_click =  new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v,MotionEvent event) {
            Log.d("D_CLICK" , ": D");
            nKey = 3;
            return true;
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //캐릭터 정보 가져오기

        //테스트를 위해 주석
        Intent i = getIntent();
        ch_num = i.getExtras().getInt("character");

        setContentView(R.layout.game);
        //화면 사이즈
        DisplayMetrics ds = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(ds);

        final int heights = ds.heightPixels;
        final int widths = ds.widthPixels;

        // 화면사이즈에 맞게 맵&extra 생성
        relativeLayout = (RelativeLayout) findViewById(R.id.re_main_game);
        gamePanel = new GamePanel(getApplicationContext(), this, widths, heights);
        relativeLayout.addView(gamePanel);

        ////////////////////////////////////////////////////////////

        //layout button 추가
        LayoutInflater myInflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        pauseButton = myInflater.inflate(R.layout.pause, null, false);
        pauseButton.setX(widths - 150);
        pauseButton.setY(0);
        relativeLayout.addView(pauseButton);

        //스탑 버튼 oncliklistener, size지정
        pauseButton.setOnTouchListener(pause_click);
        pauseButton.getLayoutParams().height = 150;
        pauseButton.getLayoutParams().width = 150;

        //////////////////////////////////////////////////////////////////////////////
        //선택 메뉴 화면 layout
        pauseMenu = myInflater.inflate(R.layout.pause_menu, null, false);
        relativeLayout.addView(pauseMenu);
        pauseMenu.setVisibility(View.GONE);

        TextView cont = (TextView) pauseMenu.findViewById(R.id.textView);
        TextView menu = (TextView) pauseMenu.findViewById(R.id.textView2);

        //보기 버튼 클릭시
        cont.setOnTouchListener(Continue_list);
        menu.setOnTouchListener(Main_menu_list);

        ///////////////////////////////////////////////////////////////////

        //a_btn  A
        LayoutInflater abtnInflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        a_Button = abtnInflater.inflate(R.layout.a_button, null, false);
        a_Button.setX(widths - 600);
        a_Button.setY(heights - 400);
        a_Button.setRotation(315.0f);
        relativeLayout.addView(a_Button);

        a_Button.setOnTouchListener(a_btn_click);
        a_Button.getLayoutParams().height = 200;
        a_Button.getLayoutParams().width = 200;

        //b_btn B
        LayoutInflater bbtnInflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        b_Button = bbtnInflater.inflate(R.layout.a_button, null, false);
        b_Button.setX(widths - 300);
        b_Button.setY(heights - 400);
        b_Button.setRotation(135.0f);
        relativeLayout.addView(b_Button);

        b_Button.setOnTouchListener(b_btn_click);
        b_Button.getLayoutParams().height = 200;
        b_Button.getLayoutParams().width = 200;

        //b_btn C
        LayoutInflater cbtnInflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        c_Button = cbtnInflater.inflate(R.layout.a_button, null, false);
        c_Button.setX(widths - 450);
        c_Button.setY(heights - 550);
        c_Button.setRotation(135.0f);
        relativeLayout.addView(c_Button);

        c_Button.setOnTouchListener(c_btn_click);
        c_Button.getLayoutParams().height = 200;
        c_Button.getLayoutParams().width = 200;

        //b_btn D
        LayoutInflater dbtnInflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        d_Button = dbtnInflater.inflate(R.layout.a_button, null, false);
        d_Button.setX(widths - 450);
        d_Button.setY(heights - 250);
        d_Button.setRotation(135.0f);
        relativeLayout.addView(d_Button);

        d_Button.setOnTouchListener(d_btn_click);
        d_Button.getLayoutParams().height = 200;
        d_Button.getLayoutParams().width = 200;

        /////////////////////////////////////////////////////////////////////////////////
        //control Button

        LayoutInflater controlbtnInflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        control_Button = controlbtnInflater.inflate(R.layout.b_button, null, false);
        control_Button.setX(widths - 2000);
        control_Button.setY(heights - 400);
        relativeLayout.addView(control_Button);


        control_Button.getLayoutParams().height = 200;
        control_Button.getLayoutParams().width = 200;
        mToast = Toast.makeText(getApplicationContext(),"NULL",Toast.LENGTH_SHORT);

        ControlView control_Button = (ControlView) findViewById(volume);

        control_Button.setKnobListener(new ControlView.KnobListener() {
            @Override
            public void onChanged(double angle) {
                int volume  = (int)angle;
                if (angle > 0){
                    volume  = volume*50/180;
                   // mToast.setText(" "+ volume);
                } // 오른쪽으로 회전
                else{
                    volume = 50+(50-(-volume)*50/180);
                   // mToast.setText(" " +volume);
                } ; // 왼쪽으로 회전
                //mToast.show();

                if(15>volume || volume>85){
                   // mToast.setText("up"+volume);
                }else if(volume>15 && volume<40){
                   // mToast.setText("right"+volume);
                    Log.d("right","mkey=1");
                    mKey = 1;
                }else if(volume>40 &&volume<65){
               // mToast.setText("down"+volume);
                }else if(volume>65 &&volume<85){
                   // mToast.setText("left"+volume);
                    Log.d("left","mkey=0");
                    mKey = 0;

                }
                //mToast.show();

            }
        });

        //countdown timer
        LayoutInflater timeInflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        timeout = timeInflater.inflate(R.layout.timeout, null, false);
        timeout.setX(widths - 1500);
        timeout.setY(0);

        relativeLayout.addView(timeout);

        timeout.getLayoutParams().height = 220;
        timeout.getLayoutParams().width = 300;

        //시간용
        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        TextView tv = (TextView) findViewById(R.id.contentView);
                        if(timecheck) {
                            tv.setText(String.valueOf(seconds));
                            seconds -= 1;
                            if (seconds == 0) {
                                //gamePanel.thread.setRunning(false);
                                tv.setText(String.valueOf(seconds));
                              /*  Intent rankmove = new Intent(MainActivity.this,RankMenu.class);
                                startActivity(rankmove);
                           */
                            seconds = 99;
                            }

                        }
//멀티 터치시
                        // mkey -> 1이면 오른쪽 0이면 왼쪽
                        // nkey -> a,b,c,d버튼 위- C(2) 왼쪽- A(0) 아래- D(3) 오른쪽- B(1)
                        // ckey -> ACTION_DOWN (1) ACTION_UP(0)
                        if(mKey==1 && nKey ==0 && cKey ==1) {
                            Log.d("U&C:: " , "R멀티A");

                        }else if(mKey==0 && nKey==0 && cKey ==1){
                            Log.d("U&C:: " , "L멀티A");
                        }

                        if(mKey==1 && nKey ==1 && cKey ==1) {
                            System.out.println("U&C:: " + "R멀티B");

                        }else if(mKey==0 && nKey==1 && cKey ==1){
                            Log.d("U&C:: " , "L멀티B");
                        }

                        if(mKey==1 &&nKey ==2 && cKey ==1) {
                            System.out.println("U&C:: " + "R멀티C");

                        }else if(mKey==0 && nKey==2 && cKey ==1){
                            Log.d("U&C:: " , "L멀티C");
                        }

                        if(mKey==1 && nKey ==3 && cKey ==1) {
                            System.out.println("U&C:: " + "R멀티D");

                        }else if(mKey==0 && nKey==3 && cKey ==1){
                            Log.d("U&C:: " , "L멀티D");
                        }

                        nKey=-1;

                    }

                });

            }

        }, 0, 1000);

    }       //oncreate

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("정말 종료하시겠습니까?");
            d.setPositiveButton("예", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // process전체 종료
                    Intent i =  new Intent(MainActivity.this,MainMenu.class);
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

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

} // 프로그램 끝



