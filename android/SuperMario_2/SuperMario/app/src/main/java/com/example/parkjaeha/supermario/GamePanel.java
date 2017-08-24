package com.example.parkjaeha.supermario;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * Created by user1 on 2017-08-09.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{

    public GameThread thread;
    public boolean Pause_game;
    Info image = new Info();
    Resources res;
    Bitmap imgback,charaters,background;
    int count=0,check=0;

    SharedPreferences pref ;
    public  static int Width, Height, cx, cy;
    private int sw, sh;
   // private  Test test;
    //private Player player;
    long startFrameTime;

    private volatile boolean playing;
    private Canvas canvas;
    private Bitmap bitmapRunningMan;
    private boolean isMoving;
    private float runSpeedPerSecond = 500;
    private float manXPos = 10, manYPos = 10;
    private int frameWidth = 230, frameHeight = 274;
    private int frameCount = 8;
    private int currentFrame = 0;
    private long fps;
    private long timeThisFrame;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMillisecond = 50;

    private Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
    //framewitdh = 캐릭터의 가로  frameheight = 캐릭터의 세로
    private RectF whereToDraw = new RectF(manXPos, manYPos, (manXPos+frameWidth), frameHeight);
    //manxpos = 왼쪽위의 캐릭터의 가로 maxypos = 왼쪽위로 캐릭터의 세로
     CPlayer Player = new CPlayer();
    private MainActivity Maingame = new MainActivity();

    //현우 코드  08-21
    MediaPlayer mediaPlayer;
    SoundPool Die;
    int explosionId=-1;
    static boolean life=false;
    private boolean live=true;
    private boolean mapobject_collision=false;
    private float m_Velocity_Y=0;
    private float m_Velocity_X=0;
    private int object_num;
    private int[] object_posX;
    private int[] object_posY;
    private int[] object_RECT_bottom;
    private int[] object_RECT_top;
    private int[] object_RECT_left;
    private int[] object_RECT_right;
    private POINT[] object_pos;
    private RECT[] objedect_size;
    Paint paint=new Paint();


    public GamePanel(Context context, MainActivity game, int ScreenWidth, int ScreenHeght){
        super(context);


        //화면 사이즈 width,height 크기
        Display display = ((WindowManager)context.getSystemService(context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Width  = display.getWidth();
        Height = display.getHeight();
        cx = Width / 2;                   // 화면의 중심점
        cy = Height / 2;

        res = context.getResources();          // 리소스 읽기
        // 배경화면 읽고 배경화면의 크기를 화면의 크기로 조정

        imgback = BitmapFactory.decodeResource(res, image.imageIDs[CharacterMenu.map_num]);
        imgback = Bitmap.createScaledBitmap(imgback, Width, Height, true);

        //background = BitmapFactory.decodeResource(res, R.drawable.background);
        background = BitmapFactory.decodeResource(res, image.img_background[CharacterMenu.map_num]);
        background = Bitmap.createScaledBitmap(background, 2*Width, Height, true);

        //image.imageIDs[MainActivity.ch_num]
        //캐릭터 객체 생성
        bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_move[MainActivity.ch_num]);
        bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * frameCount, frameHeight, false);

            //getholder 호출
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(),this);
        setFocusable(true);

        //현우 코드
        Die=new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        explosionId=Die.load(this.getContext(),R.raw.die,1);
        map_setting();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 터치 모션
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN :
                isMoving = !isMoving;
                break;
        }

        return true;
    }
    int counter= 99;
//draw
    void Draw(Canvas canvas){
        if(!Pause_game){
            if(canvas!=null){
                //background  - 하나로 이미지 합쳐지만 이것도 하나로 합칠 예정
                canvas.drawBitmap(background,count-1280,0,null);
                canvas.drawBitmap(imgback,count, 0, null);

                //위치설정 및 그리기
              //  whereToDraw.set((int) manXPos, (int)manYPos, (int) manXPos+ frameWidth, (int)manYPos + frameHeight);
                whereToDraw.set((int) Player.posX, (int) Player.posY, (int) Player.posX + frameWidth, (int) Player.posY + frameHeight);

              canvas.drawBitmap(bitmapRunningMan, frameToDraw, whereToDraw, null);

                canvas.drawRect(Player.posX,Player.posY,Player.posX+frameWidth,Player.posY+frameHeight,paint);

                for(int i=0;i<object_num;++i){
                    canvas.drawRect(objedect_size[i].left,objedect_size[i].top,objedect_size[i].right,objedect_size[i].bottom,paint);
                }
//캐릭터 위치 설정하고 그값을 그린다.
            }

        }
        //캐릭터를 그린 시간의 마지막 시간을 비교 연산한다.
        timeThisFrame = System.currentTimeMillis() - startFrameTime;
        if (timeThisFrame >= 1) {
            fps = 1000 / timeThisFrame;
        }
    }

//캐릭터 위치변화
    void Update(float dt){

        //캐릭터가 업데이트 되는 시작시간을 저장한다.
         startFrameTime = System.currentTimeMillis();

        //포지션이동
        Log.d("Key : " ,Maingame.nKey+"");
        //캐릭터의 스프라이트를 이동하는 것처럼 보이기 위해 값을 변경시켜준다.



        //캐릭터의 스프라이트를 이동하는 것처럼 보이기 위해 값을 변경시켜준다.
        if (isMoving)
        {
            manXPos = manXPos + runSpeedPerSecond / fps;
            //캐릭터가 width 프레임보다 커지면 다시 y를 증가시킨 x의 처음 위치로 이동
            //background moving  -  map moving _ camera
            if(Maingame.nKey ==1 && Player.posX <1000 && Player.posX>500 ){
                count=count-10;
            }
            else if(Maingame.nKey ==0 && Player.posX<500 &&Player.posX>-300){
                count = count+10;
            }
            // 캐릭터 움직임 최대 위치 제한
            if (Player.posX > Width || Player.posX<-500 || Player.posX >2300)
            {
                Player.posY += (int)frameHeight;
                Player.posX = 10;
                count = 0;
            }
            // 캐릭터가 height프레임을 넘어가면 처음 위치의 y자리로 이동
            if (Player.posY + frameHeight > Height)
            {
            Player.posY = 10;
            }



            switch (Maingame.nKey)
            {
                case 0: // leftmove
                    bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_move[MainActivity.ch_num+4]);
                    bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * frameCount, frameHeight, false);
                    Player.move(-10, 0);
                    Player.BeforeDirection = 1;
                    break;

                case 1: // Rightmove
                    bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_move[MainActivity.ch_num]);
                    bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * frameCount, frameHeight, false);
                    Player.move(10, 0);
                    Player.BeforeDirection = 0;
                    break;
                case 2:
                    bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_jump[MainActivity.ch_num + (4*Player.BeforeDirection)]); //왼쪽 오른쪽 자동 변경
                    bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * frameCount, frameHeight, false);
                    Player.move(0, 10);
                    break;
                case 3:
                    Player.move(0, -1);
                    break;
            }

            Gravity();
            map_collision();
            if(live=false&&life==false)
            {
                Die.play(explosionId,1,1,0,0,1);
                life=true;
            }

            manageCurrentFrame();
        }
    }


    //charager frame
    public void manageCurrentFrame() {
        long time = System.currentTimeMillis();

        if (isMoving) {
            if (time > lastFrameChangeTime + frameLengthInMillisecond) {
                lastFrameChangeTime = time;
                currentFrame++;

                if (currentFrame >= frameCount) {
                    currentFrame = 0;
                }
            }
        }

        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;
    }

    //현우 코드
    public void Gravity(){
        //  if(mapobject_collision==false){
        //     m_Velocity_Y+=1;
        // }
        // else {
        //     m_Velocity_Y=0;
        // }
        // manYPos+=m_Velocity_Y;
        // if(manYPos>1600)live=false;
        if(Player.map_collision==false){
            Player.posY +=5;
        }
    }
    public void map_collision(){
        POINT cha_point=new POINT();
        cha_point.x=(2*Player.posX+frameWidth)/2;
        cha_point.y=(Player.posY+frameHeight);
        Player.map_collision=false;
        for(int i=0;i<object_num;++i){
            if( cha_point.y<=objedect_size[i].top)continue;
            if( cha_point.x<=objedect_size[i].left)continue;
            if( cha_point.x>=objedect_size[i].right)continue;
            Player.map_collision=true;
            return;
        }
    }
    //-ing
    public void map_size_setting(int num,POINT pos,POINT size){
        objedect_size[num].top=pos.y-size.y;
        objedect_size[num].bottom=pos.y+size.y;
        objedect_size[num].left=pos.x-size.x;
        objedect_size[num].right=pos.x+size.x;

    }
    public void map_setting(){
        POINT size=new POINT();
        switch (CharacterMenu.map_num){
            case 0:
                object_num=6;
                object_pos=new POINT[6];
                objedect_size=new RECT[6];
                for(int i=0;i<object_num;++i){
                    object_pos[i]=new POINT();
                    objedect_size[i]=new RECT();
                }
                object_pos[0].x=1180;
                object_pos[0].y=1090;
                size.x=760;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=800;
                object_pos[1].y=440;
                size.x=260;
                size.y=50;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=1550;
                object_pos[2].y=440;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=1180;
                object_pos[3].y=840;
                map_size_setting(3,object_pos[3],size);
                object_pos[4].x=2030;
                object_pos[4].y=840;
                map_size_setting(4,object_pos[4],size);
                object_pos[5].x=330;
                object_pos[5].y=840;
                map_size_setting(5,object_pos[5],size);
                break;
            case 1:
                object_num=8;

                object_pos=new POINT[8];
                objedect_size=new RECT[8];
                for(int i=0;i<object_num;++i){
                    object_pos[i]=new POINT();
                    objedect_size[i]=new RECT();
                }
                object_pos[0].x=1200;
                object_pos[0].y=1200;
                size.x=950;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=830;
                object_pos[1].y=540;

                size.x=260;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=1580;
                object_pos[2].y=540;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=1200;
                object_pos[3].y=640;
                size.x=280;
                size.y=40;
                map_size_setting(3,object_pos[3],size);
                object_pos[4].x=1780;
                object_pos[4].y=840;
                size.x=190;
                size.y=40;
                map_size_setting(4,object_pos[4],size);
                object_pos[5].x=640;
                object_pos[5].y=840;
                map_size_setting(5,object_pos[5],size);
                object_pos[6].x=1580;
                object_pos[6].y=1040;
                map_size_setting(6,object_pos[6],size);
                object_pos[7].x=840;
                object_pos[7].y=1040;
                map_size_setting(7,object_pos[7],size);
                break;
            case 2:
                object_num=4;
                object_pos=new POINT[4];
                objedect_size=new RECT[4];
                for(int i=0;i<object_num;++i){
                    object_pos[i]=new POINT();
                    objedect_size[i]=new RECT();
                }
                object_pos[0].x=1150;
                object_pos[0].y=1300;
                size.x=700;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=1200;
                object_pos[1].y=460;
                size.x=920;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=1920;
                object_pos[2].y=980;
                size.x=180;
                size.y=40;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=420;
                object_pos[3].y=980;
                map_size_setting(3,object_pos[3],size);

                break;
            case 3:
                object_num=3;
                object_pos=new POINT[3];
                objedect_size=new RECT[3];
                for(int i=0;i<object_num;++i){
                    object_pos[i]=new POINT();
                    objedect_size[i]=new RECT();
                }
                object_pos[0].x=1200;
                object_pos[0].y=1340;
                size.x=1100;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=1200;
                object_pos[1].y=1040;
                size.x=530;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=1200;
                object_pos[2].y=940;
                size.x=290;
                size.y=40;
                map_size_setting(2,object_pos[2],size);
                break;
            case 4:
                object_num=7;
                object_pos=new POINT[7];
                objedect_size=new RECT[7];
                for(int i=0;i<object_num;++i){
                    object_pos[i]=new POINT();
                    objedect_size[i]=new RECT();
                }
                object_pos[0].x=1200;
                object_pos[0].y=940;
                size.x=840;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=720;
                object_pos[1].y=770;
                size.x=180;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=1650;
                object_pos[2].y=770;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=1000;
                object_pos[3].y=390;
                map_size_setting(3,object_pos[3],size);
                object_pos[4].x=1400;
                object_pos[4].y=390;
                map_size_setting(4,object_pos[4],size);
                object_pos[5].x=440;
                object_pos[5].y=570;
                map_size_setting(5,object_pos[5],size);
                object_pos[6].x=1950;
                object_pos[6].y=570;
                map_size_setting(6,object_pos[6],size);
                break;
            case 5:
                object_num=4;
                object_pos=new POINT[4];
                objedect_size=new RECT[4];
                for(int i=0;i<object_num;++i){
                    object_pos[i]=new POINT();
                    objedect_size[i]=new RECT();
                }
                object_pos[0].x=1200;
                object_pos[0].y=1080;
                size.x=1000;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=500;
                object_pos[1].y=720;
                size.x=380;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=1880;
                object_pos[2].y=720;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=1200;
                object_pos[3].y=400;
                map_size_setting(3,object_pos[3],size);
                break;
            case 6:
                object_num=8;
                object_pos=new POINT[8];
                objedect_size=new RECT[8];
                for(int i=0;i<object_num;++i){
                    object_pos[i]=new POINT();
                    objedect_size[i]=new RECT();
                }
                object_pos[0].x=1200;
                object_pos[0].y=1240;
                size.x=1150;
                size.y=40;
                //ing
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=50;
                object_pos[1].y=500;
                size.x=280;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=600;
                object_pos[2].y=90;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=1580;
                object_pos[3].y=180;
                map_size_setting(3,object_pos[3],size);
                object_pos[4].x=2320;
                object_pos[4].y=810;
                map_size_setting(4,object_pos[4],size);
                object_pos[5].x=920;
                object_pos[5].y=350;
                size.x=290;
                size.y=40;
                map_size_setting(5,object_pos[5],size);
                object_pos[6].x=700;
                object_pos[6].y=860;
                map_size_setting(6,object_pos[6],size);
                object_pos[7].x=1780;
                object_pos[7].y=660;
                map_size_setting(7,object_pos[7],size);

                break;
            case 7:
                object_num=9;
                object_pos=new POINT[9];
                objedect_size=new RECT[9];
                for(int i=0;i<object_num;++i){
                    object_pos[i]=new POINT();
                    objedect_size[i]=new RECT();
                }
                object_pos[0].x=910;
                object_pos[0].y=1430;
                size.x=400;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=1430;
                object_pos[1].y=1430;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=280;
                object_pos[2].y=1000;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=2030;
                object_pos[3].y=1000;
                map_size_setting(3,object_pos[3],size);
                object_pos[4].x=580;
                object_pos[4].y=600;
                map_size_setting(4,object_pos[4],size);
                object_pos[5].x=1780;
                object_pos[5].y=600;
                map_size_setting(5,object_pos[5],size);
                object_pos[6].x=1200;
                object_pos[6].y=170;
                map_size_setting(6,object_pos[6],size);
                object_pos[7].x=80;
                object_pos[7].y=170;
                map_size_setting(7,object_pos[7],size);
                object_pos[8].x=2280;
                object_pos[8].y=170;
                map_size_setting(8,object_pos[8],size);

                break;
            case 8:
                object_num=4;
                object_pos=new POINT[4];
                objedect_size=new RECT[4];
                for(int i=0;i<object_num;++i){
                    object_pos[i]=new POINT();
                    objedect_size[i]=new RECT();
                }
                object_pos[0].x=1200;
                object_pos[0].y=1040;
                size.x=1200;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=550;
                object_pos[1].y=640;
                size.x=280;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=1880;
                object_pos[2].y=640;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=1200;
                object_pos[3].y=340;
                map_size_setting(3,object_pos[3],size);
                break;

        }
    }

//surface
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        while(retry){

            try{
                thread.join();
                retry=false;

            }catch (InterruptedException e){

            }

        }

    }
}
