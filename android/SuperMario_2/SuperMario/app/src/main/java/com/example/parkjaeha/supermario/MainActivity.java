package com.example.parkjaeha.supermario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity {
// 플레이 화면 불러오기
   MainView mainView;
    static int ch_num=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
            ch_num = i.getExtras().getInt("character");

            mainView = new MainView(this);
            setContentView(mainView);

        }



} // 프로그램 끝




/*
setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//가로 모드

  //------------------------------------
    //         새로 만든 View
    //------------------------------------
    class GameView extends View {
        int width, height;                          // 화면의 폭과 높이
        float x, y;                                   // 캐릭터의 현재 좌표
        float dx, dy;                                // 캐릭터가 이동할 방향과 거리
        int cw, ch;                                   // 캐릭터의 폭과 높이
        Bitmap charater[] = new Bitmap[2];
        int counter = 0;
        float x1, y1, x2, y2;
        boolean canRun = true;

        //------------------------------------
        //       게임 초기화
        //------------------------------------
        public GameView(Context context) {
            super(context);
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();

            width = display.getWidth();          // 화면의 가로폭
            height = display.getHeight();        // 화면의 세로폭
            x = 100;                                 // 캐릭터의 현재 x위치
            y = 100;                                 // 캐릭터의 현재 y위치
            dx = 4;                                   // 캐릭터가 x축으로 이동할 거리
            dy = 6;                                   // 캐릭터가 y축으로 이동할 거리

            // 캐릭터 비트맵 읽기
            charater[0] = BitmapFactory.decodeResource(getResources(), R.drawable.mario_ui);
            charater[1] = BitmapFactory.decodeResource(getResources(), R.drawable.mario_ui);
            cw = charater[0].getWidth() / 2;           // 캐릭터의 폭/2
            ch = charater[1].getHeight() / 2;           // 캐릭터의 높이/2

            setFocusable(true);
            mHandler.sendEmptyMessageDelayed(0, 10);
        }

        //------------------------------------
        //       실제 그림을 그려주는 부분
        //------------------------------------
        public void onDraw(Canvas canvas) {
            x += dx;                                  // 가로로 이동
            y += dy;                                  // 세로로 이동
            if (x < cw) {                           // 왼쪽 벽
                x = cw;
                dx = -dx;
            } else if (x > width - cw) {         // 오른쪽 벽
                x = width - cw;
                dx = -dx;
            } else if (y < ch) {                     // 천정
                y = ch;
                dy = -dy;
            } else if (y > height - ch) {        // 바닥
                y = height - ch;
                dy = -dy;
            }
            counter++;
            int n = counter % 20 / 10;
            canvas.drawBitmap(charater[n], x - cw, y - ch, null);
        } // onDraw 끝

        //------------------------------------
        //      Timer Handler
        //------------------------------------
        Handler mHandler = new Handler() {                          // 타이머로 사용할 Handler
            public void handleMessage(Message msg) {
                if (canRun == true) {                                          // 반복 조건이 참이면 실행
                    invalidate();                                                 // onDraw() 다시 실행
                    Log.v("변수 값", "x=" + x + "  y=" + y);
                    mHandler.sendEmptyMessageDelayed(0, 10);     // 10/1000초마다 반복
                }  else {                                                           // 반복할 필요가 없으면
                    finish();                                                    // Activity 종료
                }
            }
        }; // Handler

        //------------------------------------
        //      onTouchEvent
        //------------------------------------
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                x1 = event.getX();                                   // 버튼을 누른 위치
                y1 = event.getY();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                x2 = event.getX();                                   // 버튼을 이동한 후 손을 뗀 위치
                y2 = event.getY();
                dx = (x2 - x1) / 10;                                // 버튼의 거리
                dy = (y2 - y1) / 10;
                x = x1;                                             // 캐릭터의 현재 위치를 버튼을 누른 곳으로 설정
                y = y1;
            }
            return true;
        } // TouchEvent

        //------------------------------------
        //      키보드 이벤트
        //------------------------------------
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                canRun = false;
            }
            return true;
        } // onKeyDown

    } // GameView 끝
 *  */