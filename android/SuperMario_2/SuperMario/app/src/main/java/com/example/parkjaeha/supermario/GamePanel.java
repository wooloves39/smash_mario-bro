package com.example.parkjaeha.supermario;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * Created by user1 on 2017-08-09.
 */

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.parkjaeha.supermario.RECT;
import com.example.parkjaeha.supermario.POINT;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{

    public GameThread thread;
    public boolean Pause_game;
    Info image = new Info();
    Resources res;
    Bitmap imgback,charaters;

    public  static int Width, Height, cx, cy;
    private int sw, sh;
   // private  Test test;
    private Mario mario;
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
        map_setting();
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
       Die=new SoundPool(2,AudioManager.STREAM_MUSIC,0);
        explosionId=Die.load(this.getContext(),R.raw.die,1);
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
                canvas.drawBitmap(imgback, 0, 0, null);

                //canvas.drawBitmap(charaters, cx- sw, cy- sh, null);
                //위치설정 및 그리기
                whereToDraw.set((int) manXPos, (int) manYPos, (int) manXPos + frameWidth, (int) manYPos + frameHeight);
                manageCurrentFrame();
                canvas.drawBitmap(bitmapRunningMan, frameToDraw, whereToDraw, null);
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

        //캐릭터의 스프라이트를 이동하는 것처럼 보이기 위해 값을 변경시켜준다.
        Gravity();
        map_collision();
        if(live=false&&life==false){
            Die.play(explosionId,1,1,0,0,1);
            life=true;
        }
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
        }
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
       if(mapobject_collision==false){
           manYPos+=5;
       }
    }
   public void map_collision(){
        mapobject_collision=false;
        for(int i=0;i<object_num;++i){
            if( manYPos<=object_RECT_top[i])continue;
            if( manYPos>=object_RECT_bottom[i])continue;
            if( manXPos<=object_RECT_left[i])continue;
            if( manXPos<=object_RECT_right[i])continue;
            mapobject_collision=true;
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

                object_posX=new int[6];
              //  object_pos=new POINT[6];
                object_posY=new int[6];
                //objedect_size=new RECT[6];
                object_RECT_bottom=new int[6];
                object_RECT_left=new int[6];
                object_RECT_right=new int[6];
                object_RECT_top=new int[6];
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
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;

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
