package com.example.parkjaeha.supermario;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by parkjaeha on 2017-07-28.
 */

public class MainView extends SurfaceView implements SurfaceHolder.Callback {
//플레이화면

    Info image = new Info();

    public  static int RUN = 1;
    public  static int PAUSE = 2;
    public  int mMode = RUN;

    private SurfaceHolder mHolder;
    private MyThread   mThread;
    private Context mContext;

    public  static int Width, Height, cx, cy;     // 화면의 전체 폭과 중심점
    private int x1, y1, x2, y2;                     // Viewport 좌표
    private int sx1, sy1, sx2, sy2;               // Viewport가 1회에 이동할 거리
    private Bitmap imgBack1, imgBack2;       // 배경 이미지
    private int w, h;                                   // 우주선의 폭과 높이
    private long counter = 0;                       // 루프의 전체 반복 횟수
    private  boolean canRun = true;              // 스레드 실행용 플래그

    // 추가된 부분  ---------------------------------
    private Bitmap[]   Spaceship = new Bitmap[8];       // 우주선 8방향
    private Bitmap[]   Arrow = new Bitmap[8];             // 화살표 8방향
    private Rect[]     ArrowRect = new Rect[8];          // 화살표 영역 8방향
    private Integer[]  ArrowX = new Integer[8];            // 화살표 중심 좌표 8개
    private Integer[]  ArrowY = new Integer[8];            // 화살표 중심 좌표 8개

    private int sw, sh;                          // 우주선의 폭과 높이
    private int aw, ah;                          // 화살표의 폭과 높이
    private static int RAD = 80;              // 중심에서 화살표까지의 거리(반지름
    private int Dir = 2;                          // 시작시  스크롤하는 방향
    // 스크롤 방향에 따른 벡터
    public  static int Vec[][] = {{1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}};

    private ArrayList<Missle> MissleList;


    // ---------------------------------
    //    ReadSprite
    // ---------------------------------
    public void ReadSprite(Context context) {
        // 화면 해상도 구하기
        Display display = ((WindowManager)context.getSystemService(context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Width  = display.getWidth();
        Height = display.getHeight();
        cx = Width / 2;                   // 화면의 중심점
        cy = Height / 2;

        Resources res = context.getResources();          // 리소스 읽기
        // 배경화면 읽고 배경화면의 크기를 화면의 크기로 조정
        System.out.println("MV:"+CharacterMenu.map_num);
        imgBack1 = BitmapFactory.decodeResource(res, image.imageIDs[CharacterMenu.map_num]);
        imgBack1 = Bitmap.createScaledBitmap(imgBack1, Width, Height, true);


        // 전경
        // imgBack2 = BitmapFactory.decodeResource(res, R.drawable.background);
        // imgBack2 = Bitmap.createScaledBitmap(imgBack2, Width, Height, true);

        x1 = cx;          // Viewport의 시작 위치는 이미지의 한가운데
        y1 = cy;
        //-----------추가된 내용 ----------------------
        sx1 = 1;         // 원경을 1회에 이동시킬 거리
        sy1 = 1;
        sx2 = 2;         // 근경을 1회에 이동시킬 거리
        sy2 = 2;

        // 우주선과 화살표 이미지 읽기
        System.out.println("MV:"+MainActivity.ch_num);
        Spaceship[0] = BitmapFactory.decodeResource(res, image.img_Scharacter[MainActivity.ch_num]);
       // Arrow[0] = BitmapFactory.decodeResource(res, R.drawable.waluizy_ui);

        // 우주선의 폭과 높이
        sw = Spaceship[0].getWidth() / 2;
        sh = Spaceship[0].getHeight() / 2;

        // 화살표의 폭과 높이
 //       aw = Arrow[0].getWidth() / 2;
 //       ah = Arrow[0].getHeight() / 2;

        // 45도 간격으로 8방향 회전한 이미지 만들기
        Matrix matrix = new Matrix();
        for (int i = 1; i < 8; i++) {
            matrix.preRotate(0);
            Spaceship[i] = Bitmap.createBitmap(Spaceship[0], 0, 0, sw * 2, sh * 2, matrix, true);
          //  Arrow[i] = Bitmap.createBitmap(Arrow[0], 0, 0, aw * 2, ah * 2, matrix, true);
        }

       // CalcArrowPos();        // 화살표의 좌표 계산
    } // ReadSprite
    public void CalcArrowPos() {
        for (int i = 0; i <= 7; i++) {
            int x = (int) (cx + Math.cos(i * 45 * Math.PI / 180) * RAD);
            int y = (int) (cy - Math.sin(i * 45 * Math.PI / 180) * RAD);
            // 화살표의 중심 좌표
            ArrowX[i] = x;
            ArrowY[i] = y;

            // 화살표 영역을 Rect()에 저장
            ArrowRect[i] = new Rect(x - aw, y - ah, x + aw, y + ah);
        }
    }



    // ---------------------------------
    //   MyGame View
    // ---------------------------------
    public MainView(Context context) {
        super(context);
        SurfaceHolder mHolder = getHolder();
        mHolder.addCallback(this);

        ReadSprite(context);          // 비트맵 읽기
        mThread = new MyThread(mHolder, context);
        setFocusable(true);
        MissleList = new ArrayList<Missle>();
    }

    // ---------------------------------
    //
    // ---------------------------------
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread.start();
    }

    // ---------------------------------
    //
    // ---------------------------------
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }

    // ---------------------------------
    //
    // ---------------------------------
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
                //
            }
        }
    }

    // ---------------  여기서부터 스레드 영역이야. 넘보지 마!  -----------------------
    public class MyThread extends Thread {
        // ---------------------------------
        //    MyThread
        // ---------------------------------
        public MyThread(SurfaceHolder Holder, Context context) {
            mHolder = Holder;
            mContext = context;
        }

        // ---------------------------------
        //      Run
        // ---------------------------------
        public void run() {
            Rect src = new Rect();                     // Viewport의 좌표
            Rect dst = new Rect();                     // View(화면)의 좌표
            dst.set(0, 0, Width, Height);              // View는 화면 전체 크기
            Rect src1 = new Rect();

            while (canRun) {
                Canvas canvas = null;
                try {
                    canvas = mHolder.lockCanvas();
                    synchronized (mHolder) {
                      //  ScrollImage();                                      // Viewport 이동
                        src.set(x1, y1, (x1 + cx), (y1 + cy));            // 이동한 Viewport 좌표
                       // src1.set(x2, y2, x2 + cx, y2 + cy);
                        canvas.drawBitmap(imgBack1, 0, 0, null);    // 원경
                        // canvas.drawBitmap(imgBack1, src, dst, null);    // 원경
                      //  canvas.drawBitmap(imgBack2, src1, dst, null);   // 근경

                        //MoveAndDrawMissle(canvas);                        // 미사일
                        canvas.drawBitmap(Spaceship[Dir], cx- sw, cy- sh, null);  // 우주선
                        /*for (int i = 0; i <= 7; i++) {                           // 화살표
                            canvas.drawBitmap(Arrow[i], ArrowX[i] - aw, ArrowY[i] - ah, null);
                        }*/
                    }
                } finally {
                    if (canvas != null) mHolder.unlockCanvasAndPost(canvas);
                }
            } // while
        } // run

        // ---------------------------------
        //    배경 스크롤
        // ---------------------------------
        public void ScrollImage() {
            if (mMode == PAUSE) return;
            counter++;
            x2 += sx2 * Vec[Dir][0];
            y2 += sy2 * Vec[Dir][1];
            if (x2 < 0) x2 = cx;                        // Viewpoint가 이미지를 벗어났나 조사
            else if (x2 > cx) x2 = 0;
            else if (y2 < 0) y2 = cy;
            else if (y2 > cy) y2 = 0;
            if (counter % 2 == 0) {
                x1 += sx1 * Vec[Dir][0];           // Viewport 이동
                y1 += sy1 * Vec[Dir][1];
                if (x1 < 0) x1 = cx;                  // Viewpoint가 이미지를 벗어났나 조사
                else if (x1 > cx) x1 = 0;
                else if (y1 < 0) y1 = cy;
                else if (y1 > cy) y1 = 0;
            }
        } // ScrollBackground

        // ---------------------------------
        //      미사일 이동하고 그리기
        // ---------------------------------
        private void MoveAndDrawMissle(Canvas canvas) {
            if (mMode == PAUSE || MissleList.size() == 0) return;
            for (int i = MissleList.size() - 1; i >= 0; i--) {      // ArrayList를 역순으로 검사
                if (MissleList.get(i).Move() == true)              // 미사일 이동 후 화면을 벗어났는지 판단
                    MissleList.remove(i);                            // 미사일 삭제
            }

            for (Missle tMissle : MissleList) {                      // 모든 미사일을 canvas에 그린다
                canvas.drawBitmap(Arrow[tMissle.dir], tMissle.x - aw, tMissle.y - ah, null);
            }
        } // MoveAndDrawMissle

    } // Thread

    // ---------------------------------
    //        onTouchEvent
    // ---------------------------------
    public boolean onTouchEvent(MotionEvent event) {
        if (mMode == PAUSE) return false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Rect temp = new Rect();
            int x = (int) event.getX();           // 클릭한 위치 조사
            int y = (int) event.getY();


            // 8개의 화살표 영역에 대해 겹침 상태 조사
       /*     for (int i = 0; i <= 7; i++) {
                if (ArrowRect[i].contains(x, y) == true) {
                    Dir = i;
                    break;
                }
            } // for*/

        } // if
        return false;
    } // touch

    // ---------------------------------
    //        onKeyDown
    // ---------------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mMode == RUN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            synchronized (mHolder) {
                MissleList.add(new Missle(cx, cy, Dir)); // ArrayList에 오브젝트 저장
                // 발사음은 여기에서 처리한다
                // SoundPool을 미리 만들어 두었으면 myPool.play(...) 으로 삐용~
            }
        }
        return false;
    }

    ///////////////////////////////////

    public class Missle {
        public  int x, y;
        public  int dir;
        private int dx, dy;

        // ---------------------------------
        //    생성자
        // ---------------------------------
        public Missle(int _x, int _y, int _dir) {
            x = _x;   // 미사일의 발사 위치
            y = _y;
            dir = _dir;  // 미사일의 방향
            dx = 10;   // 1회에 이동할 거리
            dy = 10;
        }

        // ---------------------------------
        //    미사일 이동
        // ---------------------------------
        public boolean Move() {
            x += dx * MainView.Vec[dir][0];  // 미사일 이동
            y += dy * MainView.Vec[dir][1];

            if (x < 0 || x > MainView.Width || y < 0 || y > MainView.Height)
                return true; // 화면을 벗어나면 true
            else
                return false;
        }

    } // Missle 끝


} // end SurfaceView
