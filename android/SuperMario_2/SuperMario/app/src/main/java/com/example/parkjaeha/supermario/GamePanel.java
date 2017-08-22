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
    private float m_Velocity_Y=0;
    private float m_Velocity_X=0;
    private int object_num;
    private int[] object_posX;
    private int[] object_posY;
    private float[] object_RECT_bottom;
    private float[] object_RECT_top;
    private float[] object_RECT_left;
    private float[] object_RECT_right;
     POINT[] object_pos=new POINT[6];
     RECT[] objedect_size;
    Paint paint=new Paint();
    public GamePanel(Context context, MainActivity game, int ScreenWidth, int ScreenHeght){
        super(context);
        //Mario ma = new Mario();

 //       ma.getMoveBitmaps();

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

        background = BitmapFactory.decodeResource(res, R.drawable.background);
        background = Bitmap.createScaledBitmap(background, Width+1000, Height, true);
        //  test =  new Test(BitmapFactory.decodeResource(res,R.drawable.mario_rightbasic),100,0,ScreenWidth,ScreenHeght);
        //charaters = BitmapFactory.decodeResource(res, image.img_Scharacter[MainActivity.ch_num]);
        //charaters = BitmapFactory.decodeResource(res,R.drawable.mario_rightbasic);
        //sw = charaters.getWidth() / 2;
        //sh = charaters.getHeight() / 2;

        //image.imageIDs[MainActivity.ch_num]
        //캐릭터 객체 생성
        bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_rightmove[MainActivity.ch_num]);
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
//draw
    void Draw(Canvas canvas){
        if(!Pause_game){
            if(canvas!=null){
                //background
                canvas.drawBitmap(background,0,0,null);
               // Log.d("Background : ")
                canvas.drawBitmap(imgback,0, 0, null);

                //canvas.drawBitmap(charaters, cx- sw, cy- sh, null);
                //위치설정 및 그리기
              //  whereToDraw.set((int) manXPos, (int)manYPos, (int) manXPos+ frameWidth, (int)manYPos + frameHeight);

                whereToDraw.set((int) Player.posX, (int) Player.posY, (int) Player.posX + frameWidth, (int) Player.posY + frameHeight);


              canvas.drawBitmap(bitmapRunningMan, frameToDraw, whereToDraw, null);
                canvas.drawRect(Player.posX,Player.posY,Player.posX+frameWidth,Player.posY+frameHeight,paint);

                for(int i=0;i<object_num;++i){
                canvas.drawRect(object_RECT_left[i],object_RECT_top[i],object_RECT_right[i],object_RECT_bottom[i],paint);
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
        Gravity();
        map_collision();
        if(live=false&&life==false){
            Die.play(explosionId,1,1,0,0,1);
            life=true;
        }

        //캐릭터의 스프라이트를 이동하는 것처럼 보이기 위해 값을 변경시켜준다.
        if (isMoving) {
            manXPos = manXPos + runSpeedPerSecond / fps;
            //캐릭터가 width 프레임보다 커지면 다시 y를 증가시킨 x의 처음 위치로 이동
            if (manXPos > Width) {
                manYPos += (int)frameHeight;
                manXPos = 10;
            }
            // 캐릭터가 height프레임을 넘어가면 처음 위치의 y자리로 이동
            if (manYPos + frameHeight > Height) {
                manYPos = 10;
            }

            switch (Maingame.nKey)
            {
                case 0:
                    bitmapRunningMan = BitmapFactory.decodeResource(getResources(), R.drawable.wario_leftmove);
                    bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * frameCount, frameHeight, false);

                    Player.move(-10, 0);
                    break;
                case 1:
                    bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_rightmove[MainActivity.ch_num]);
                    bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * frameCount, frameHeight, false);


                    Player.move(10, 0);
                    break;
                case 2:
                    Player.move(0, 1);
                    break;
                case 3:
                    Player.move(0, -1);
                    break;
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
         Player.posY+=5;
        }
    }
    public void map_collision(){
        Player.map_collision=false;
        for(int i=0;i<object_num;++i){
            if( Player.posY<=object_RECT_top[i])continue;
            if( Player.posY>=object_RECT_bottom[i])continue;
            if( Player.posX<=object_RECT_left[i])continue;
            if( Player.posX<=object_RECT_right[i])continue;
            Player.map_collision=true;
        }
    }
    //-ing
    public void map_size_setting(int num,int xpos,int ypos,POINT size){
        object_RECT_top[num]=ypos-size.y;
        object_RECT_bottom[num]=ypos+size.y;
        object_RECT_left[num]=xpos-size.x;
        object_RECT_right[num]=xpos+size.x;

    }
    public void map_setting(){
        POINT size=new POINT();
        switch (CharacterMenu.map_num){
            case 0:
                object_num=6;
                for(int i=0;i<object_num;++i){
                    object_pos[i]=new POINT();
                }
                object_posX=new int[6];
                 // object_pos=new POINT[6];
                object_posY=new int[6];
                //objedect_size=new RECT[6];
                object_RECT_bottom=new float[6];
                object_RECT_left=new float[6];
                object_RECT_right=new float[6];
                object_RECT_top=new float[6];
                 object_pos[0].x=1280;
               // object_pos[0].x=1280;
                object_posX[0]=1280;
                object_posY[0]=1140;
                size.x=780;
                size.y=40;
                map_size_setting(0,object_posX[0],object_posY[0],size);
                object_posX[1]=880;
                object_posY[1]=440;
                size.x=320;
                size.y=80;
                map_size_setting(1,object_posX[1],object_posY[1],size);
                object_posX[2]=1680;
                object_posY[2]=440;
                map_size_setting(2,object_posX[2],object_posY[2],size);
                object_posX[3]=1280;
                object_posY[3]=840;
                map_size_setting(3,object_posX[3],object_posY[3],size);
                object_posX[4]=2180;
                object_posY[4]=840;
                map_size_setting(4,object_posX[4],object_posY[4],size);
                object_posX[5]=380;
                object_posY[5]=840;
                map_size_setting(5,object_posX[5],object_posY[5],size);
                break;
            case 1:
                object_num=8;

                object_posX=new int[8];
                //  object_pos=new POINT[6];
                object_posY=new int[8];
                //objedect_size=new RECT[6];
                object_RECT_bottom=new float[8];
                object_RECT_left=new float[8];
                object_RECT_right=new float[8];
                object_RECT_top=new float[8];
                // object_pos[0].x=1280;

                object_posX[0]=1280;
                object_posY[0]=1140;
                size.x=980;
                size.y=40;
                map_size_setting(0,object_posX[0],object_posY[0],size);
                object_posX[1]=880;
                object_posY[1]=440;
                size.x=280;
                size.y=40;
                map_size_setting(1,object_posX[1],object_posY[1],size);
                object_posX[2]=1680;
                object_posY[2]=440;
                map_size_setting(2,object_posX[2],object_posY[2],size);
                object_posX[3]=1280;
                object_posY[3]=640;
                size.x=380;
                size.y=40;
                map_size_setting(3,object_posX[3],object_posY[3],size);
                object_posX[4]=1880;
                object_posY[4]=840;
                size.x=190;
                size.y=40;
                map_size_setting(4,object_posX[4],object_posY[4],size);
                object_posX[5]=640;
                object_posY[5]=840;
                map_size_setting(5,object_posX[5],object_posY[5],size);
                object_posX[6]=1680;
                object_posY[6]=1040;
                map_size_setting(6,object_posX[6],object_posY[6],size);
                object_posX[7]=840;
                object_posY[7]=1040;
                map_size_setting(7,object_posX[7],object_posY[7],size);
                break;
            case 2:
                object_num=4;

                object_posX=new int[4];
                //  object_pos=new POINT[6];
                object_posY=new int[4];
                //objedect_size=new RECT[6];
                object_RECT_bottom=new float[4];
                object_RECT_left=new float[4];
                object_RECT_right=new float[4];
                object_RECT_top=new float[4];
                // object_pos[0].x=1280;

                object_posX[0]=1280;
                object_posY[0]=840;
                size.x=780;
                size.y=40;
                map_size_setting(0,object_posX[0],object_posY[0],size);
                object_posX[1]=1280;
                object_posY[1]=440;
                size.x=920;
                size.y=40;
                map_size_setting(1,object_posX[1],object_posY[1],size);
                object_posX[2]=2080;
                object_posY[2]=1040;
                size.x=180;
                size.y=40;
                map_size_setting(2,object_posX[2],object_posY[2],size);
                object_posX[3]=480;
                object_posY[3]=1040;
                map_size_setting(3,object_posX[3],object_posY[3],size);

                break;
            case 3:
                object_num=3;

                object_posX=new int[3];
                //  object_pos=new POINT[6];
                object_posY=new int[3];
                //objedect_size=new RECT[6];
                object_RECT_bottom=new float[3];
                object_RECT_left=new float[3];
                object_RECT_right=new float[3];
                object_RECT_top=new float[3];
                // object_pos[0].x=1280;

                object_posX[0]=1280;
                object_posY[0]=940;
                size.x=1180;
                size.y=40;
                map_size_setting(0,object_posX[0],object_posY[0],size);
                object_posX[1]=1280;
                object_posY[1]=1040;
                size.x=580;
                size.y=40;
                map_size_setting(1,object_posX[1],object_posY[1],size);
                object_posX[2]=1280;
                object_posY[2]=940;
                size.x=290;
                size.y=40;
                map_size_setting(2,object_posX[2],object_posY[2],size);
                break;
            case 4:
                object_num=7;

                object_posX=new int[7];
                //  object_pos=new POINT[6];
                object_posY=new int[7];
                //objedect_size=new RECT[6];
                object_RECT_bottom=new float[7];
                object_RECT_left=new float[7];
                object_RECT_right=new float[7];
                object_RECT_top=new float[7];
                // object_pos[0].x=1280;

                object_posX[0]=1280;
                object_posY[0]=1340;
                size.x=860;
                size.y=40;
                map_size_setting(0,object_posX[0],object_posY[0],size);
                object_posX[1]=780;
                object_posY[1]=840;
                size.x=180;
                size.y=40;
                map_size_setting(1,object_posX[1],object_posY[1],size);
                object_posX[2]=1780;
                object_posY[2]=840;
                map_size_setting(2,object_posX[2],object_posY[2],size);
                object_posX[3]=1080;
                object_posY[3]=440;
                map_size_setting(3,object_posX[3],object_posY[3],size);
                object_posX[4]=1480;
                object_posY[4]=440;
                map_size_setting(4,object_posX[4],object_posY[4],size);
                object_posX[5]=480;
                object_posY[5]=640;
                map_size_setting(5,object_posX[5],object_posY[5],size);
                object_posX[6]=1080;
                object_posY[6]=640;
                map_size_setting(6,object_posX[6],object_posY[6],size);
                 break;
            case 5:
                object_num=4;

                object_posX=new int[4];
                //  object_pos=new POINT[6];
                object_posY=new int[4];
                //objedect_size=new RECT[6];
                object_RECT_bottom=new float[4];
                object_RECT_left=new float[4];
                object_RECT_right=new float[4];
                object_RECT_top=new float[4];
                // object_pos[0].x=1280;

                object_posX[0]=1280;
                object_posY[0]=1340;
                size.x=1060;
                size.y=40;
                map_size_setting(0,object_posX[0],object_posY[0],size);
                object_posX[1]=580;
                object_posY[1]=740;
                size.x=380;
                size.y=40;
                map_size_setting(1,object_posX[1],object_posY[1],size);
                object_posX[2]=1980;
                object_posY[2]=740;
                map_size_setting(2,object_posX[2],object_posY[2],size);
                object_posX[3]=1280;
                object_posY[3]=740;
                map_size_setting(3,object_posX[3],object_posY[3],size);
                     break;
            case 6:
                object_num=8;

                object_posX=new int[8];
                //  object_pos=new POINT[6];
                object_posY=new int[8];
                //objedect_size=new RECT[6];
                object_RECT_bottom=new float[8];
                object_RECT_left=new float[8];
                object_RECT_right=new float[8];
                object_RECT_top=new float[8];
                // object_pos[0].x=1280;

                object_posX[0]=1280;
                object_posY[0]=940;
                size.x=1180;
                size.y=40;
                map_size_setting(0,object_posX[0],object_posY[0],size);
                object_posX[1]=80;
                object_posY[1]=540;
                size.x=280;
                size.y=40;
                map_size_setting(1,object_posX[1],object_posY[1],size);
                object_posX[2]=680;
                object_posY[2]=140;
                map_size_setting(2,object_posX[2],object_posY[2],size);
                object_posX[3]=1680;
                object_posY[3]=240;
                map_size_setting(3,object_posX[3],object_posY[3],size);
                object_posX[4]=2480;
                object_posY[4]=840;
                map_size_setting(4,object_posX[4],object_posY[4],size);
                object_posX[5]=880;
                object_posY[5]=340;
                size.x=290;
                size.y=40;
                map_size_setting(5,object_posX[5],object_posY[5],size);
                object_posX[6]=780;
                object_posY[6]=840;
                map_size_setting(6,object_posX[6],object_posY[6],size);
                object_posX[7]=1880;
                object_posY[7]=640;
                map_size_setting(7,object_posX[7],object_posY[7],size);

                break;
            case 7:
                object_num=9;

                object_posX=new int[9];
                //  object_pos=new POINT[6];
                object_posY=new int[9];
                //objedect_size=new RECT[6];
                object_RECT_bottom=new float[9];
                object_RECT_left=new float[9];
                object_RECT_right=new float[9];
                object_RECT_top=new float[9];
                // object_pos[0].x=1280;

                object_posX[0]=1000;
                object_posY[0]=1340;
                size.x=400;
                size.y=40;
                map_size_setting(0,object_posX[0],object_posY[0],size);
                object_posX[1]=1560;
                object_posY[1]=1340;
                map_size_setting(1,object_posX[1],object_posY[1],size);
                object_posX[2]=380;
                object_posY[2]=1040;
                map_size_setting(2,object_posX[2],object_posY[2],size);
                object_posX[3]=2180;
                object_posY[3]=1040;
                map_size_setting(3,object_posX[3],object_posY[3],size);
                object_posX[4]=680;
                object_posY[4]=640;
                map_size_setting(4,object_posX[4],object_posY[4],size);
                object_posX[5]=1880;
                object_posY[5]=640;
                map_size_setting(5,object_posX[5],object_posY[5],size);
                object_posX[6]=1280;
                object_posY[6]=240;
                map_size_setting(6,object_posX[6],object_posY[6],size);
                object_posX[7]=180;
                object_posY[7]=240;
                map_size_setting(7,object_posX[7],object_posY[7],size);
                object_posX[8]=2380;
                object_posY[8]=240;
                map_size_setting(8,object_posX[8],object_posY[8],size);

                break;
            case 8:
                object_num=4;

                object_posX=new int[4];
                //  object_pos=new POINT[6];
                object_posY=new int[4];
                //objedect_size=new RECT[6];
                object_RECT_bottom=new float[4];
                object_RECT_left=new float[4];
                object_RECT_right=new float[4];
                object_RECT_top=new float[4];
                // object_pos[0].x=1280;

                object_posX[0]=1280;
                object_posY[0]=1340;
                size.x=1480;
                size.y=40;
                map_size_setting(0,object_posX[0],object_posY[0],size);
                object_posX[1]=740;
                object_posY[1]=640;
                size.x=280;
                size.y=40;
                map_size_setting(1,object_posX[1],object_posY[1],size);
                object_posX[2]=1980;
                object_posY[2]=640;
                map_size_setting(2,object_posX[2],object_posY[2],size);
                object_posX[3]=1280;
                object_posY[3]=340;
                map_size_setting(3,object_posX[3],object_posY[3],size);
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
