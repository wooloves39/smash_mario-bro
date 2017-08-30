package com.example.parkjaeha.supermario;

import android.content.Context;
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
    Bitmap imgback,background;
    int count=0,check=0,key=0;

    public  static int Width, Height, cx, cy;
    long startFrameTime;

    private Canvas canvas;
    private Bitmap bitmapRunningMan;
    //AI
    private Bitmap[] bitmapMan = new Bitmap[4];
    private int[] playN= new int[3];

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
   // private RectF whereToDraw = new RectF(manXPos, manYPos, (manXPos+frameWidth), frameHeight);
    //manxpos = 왼쪽위의 캐릭터의 가로 maxypos = 왼쪽위로 캐릭터의 세로


     CPlayer Player = new CPlayer();
    CPlayer [] p = new CPlayer[4];
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
    Camera cam=new Camera();
    private RectF whereToDraw = new RectF(Player.posX, Player.posY, (Player.posX+frameWidth), frameHeight);

/*
    //연기
    private ArrayList<Smoke> smokes;
    private long smkestarttime;
*/

    //AI
    private RectF[] whereToDraw2= new RectF[4];
    private int [] a = new int[3];
    private int [] b = new int[3];
    private int [] c = new int[3];
    private int [] d = new int[3];

    private Rect[] rectangle = new Rect[4];


    public GamePanel(Context context, MainActivity game, int ScreenWidth, int ScreenHeght){
        super(context);

        //AI
        playN = new int[4];
        bitmapMan = new Bitmap[4];

        //rect 생성
        whereToDraw2 = new RectF[4];

        //캐릭터 위치 변경
        p[0] = new CPlayer();
        p[1] = new CPlayer();
        p[2] = new CPlayer();
        //p[3] = new CPlayer();

        //캐릭터 처음 위치
        p[0].posX = 500;
        p[0].posY = 10;
        p[1].posX = 1000;
        p[1].posY = 10;
        p[2].posX = 1500;
        p[2].posY = 10;
        //p[3].posX = 10;
        //p[3].posY = 10;

        //캐릭터 선정
        playerNum();
        //rect 선정
        rangeRect();

        //캐릭터 위치 객체 생성
        whereToDraw2[0]= new RectF(p[0].posX, p[0].posY, (p[0].posY+frameWidth), frameHeight);
        whereToDraw2[1]= new RectF(p[1].posX, p[1].posY, (p[1].posY+frameWidth), frameHeight);
        whereToDraw2[2]= new RectF(p[2].posX, p[2].posY, (p[2].posY+frameWidth), frameHeight);
       // whereToDraw2[3]= new RectF(p[3].posX, p[3].posY, (p[3].posY+frameWidth), frameHeight);


        //화면 사이즈 width,height 크기
        Display display = ((WindowManager)context.getSystemService(context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Width  = display.getWidth();
        Height = display.getHeight();
        cx = Width / 2;                   // 화면의 중심점
        cy = Height / 2;

        res = context.getResources();          // 리소스 읽기
        // 배경화면 읽고 배경화면의 크기를 화면의 크기로 조정

        //imgback = BitmapFactory.decodeResource(res, image.imageIDs[CharacterMenu.map_num]);
        //imgback = Bitmap.createScaledBitmap(imgback, Width, Height, true);

        background = BitmapFactory.decodeResource(res, image.img_background[CharacterMenu.map_num]);
        background = Bitmap.createScaledBitmap(background, 2*Width-70, Height, true);

        //image.imageIDs[MainActivity.ch_num]

        //캐릭터 객체 생성
        bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_move[MainActivity.ch_num]);
        bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * frameCount, frameHeight, false);

        //캐릭터 AI용
        bitmapMan[0] = BitmapFactory.decodeResource(getResources(),image.img_move[playN[0]]);
        bitmapMan[0] = Bitmap.createScaledBitmap(bitmapMan[0], frameWidth * frameCount, frameHeight, false);

        bitmapMan[1] = BitmapFactory.decodeResource(getResources(),image.img_move[playN[1]]);
        bitmapMan[1] = Bitmap.createScaledBitmap(bitmapMan[1], frameWidth * frameCount, frameHeight, false);

        bitmapMan[2] = BitmapFactory.decodeResource(getResources(),image.img_move[playN[2]]);
        bitmapMan[2] = Bitmap.createScaledBitmap(bitmapMan[2], frameWidth * frameCount, frameHeight, false);



/*
        //연기
        smokes = new ArrayList<Smoke>();
*/


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
        cam.setPos(Player.posX);

        //초기화
        Player.BeforeDirection = 1;
        Maingame.mKey = 2;
        Maingame.nKey = 4;
        Player.nextAttackFrame = -1;
        //프레임 초기화
        Player.frameCount = 8;
        Player.currentFrame = 0;
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
                //background  - 하나로 이미지 합쳐지만 이것도 하나로 합칠 예정
                //canvas.drawBitmap(background,cam.realpos.x,0,null);
                //canvas.drawBitmap(imgback,count, 0, null);
                canvas.drawBitmap(background,-cam.realpos.x-1280,0,null);

                //위치설정 및 그리기
              //  whereToDraw.set((int) manXPos, (int)manYPos, (int) manXPos+ frameWidth, (int)manYPos + frameHeight);
               // whereToDraw.set((int) Player.posX, (int) Player.posY, (int) Player.posX + frameWidth, (int) Player.posY + frameHeight);
                whereToDraw.set((int) Player.posX-cam.realpos.x, (int) Player.posY, (int) Player.posX-cam.realpos.x + frameWidth, (int) Player.posY + frameHeight);

                //ai moving
                whereToDraw2[0].set((int)p[0].posX,(int)p[0].posY,(int)p[0].posX+frameWidth,(int)p[0].posY+frameHeight);
                whereToDraw2[1].set((int)p[1].posX,(int)p[1].posY,(int)p[1].posX+frameWidth,(int)p[1].posY+frameHeight);
                whereToDraw2[2].set((int)p[2].posX,(int)p[2].posY,(int)p[2].posX+frameWidth,(int)p[2].posY+frameHeight);
                //whereToDraw2[3].set((int)p[3].posX,(int)p[3].posY,(int)p[3].posX+frameWidth,(int)p[3].posY+frameHeight);

                canvas.drawBitmap(bitmapRunningMan, frameToDraw, whereToDraw, null);
                canvas.drawBitmap(bitmapMan[0], frameToDraw, whereToDraw2[0], null);
                canvas.drawBitmap(bitmapMan[1], frameToDraw, whereToDraw2[1], null);
                canvas.drawBitmap(bitmapMan[2], frameToDraw, whereToDraw2[2], null);

               // canvas.drawRect(Player.posX+a[0],Player.posY+a[1],Player.posX+frameWidth-a[2],Player.posY+frameHeight,paint);
                canvas.drawRect(Player.posX+a[0]-cam.realpos.x,Player.posY+a[1],Player.posX+frameWidth-a[2]-cam.realpos.x,Player.posY+frameHeight,paint);
                canvas.drawRect(p[0].posX+b[0],p[0].posY+b[1],p[0].posX+frameWidth-b[2],p[0].posY+frameHeight,paint);
                canvas.drawRect(p[1].posX+c[0],p[1].posY+c[1],p[1].posX+frameWidth-c[2],p[1].posY+frameHeight,paint);
                canvas.drawRect(p[2].posX+d[0],p[2].posY+d[1],p[2].posX+frameWidth-d[2],p[2].posY+frameHeight,paint);

                //충돌 state test

                if( (Player.posX+a[0] == p[1].posX+c[0] || Player.posX+frameWidth-a[2]== p[1].posX+frameWidth-c[2]) && Player.posY+a[0] == p[1].posY+c[0] ||Player.posY+frameHeight ==p[1].posY+frameHeight){
                    Log.d("Collison : " ,"x가 부딪힘  push ");
                    Player.PUSH =1;
                    //점프로 인한 상황에서 위아래 부분이 부딪히는 충돌임
                }

                if( (( Player.posX+frameWidth-a[2]== p[1].posX+frameWidth-c[2]) || ( Player.posX+frameWidth-a[2] ==p[1].posX+c[0]) ) && Maingame.nKey ==1 && (Player.posY+a[0] == p[1].posY+c[0] ||Player.posY+frameHeight ==p[1].posY+frameHeight) ){
                    Log.d("Collison : " ,"오른쪽 어택");
                    Player.RPUNCH =1;
                    //플레이어가 오른쪽으로 어택시
                }

                if( ((Player.posX+frameWidth-a[2]== p[1].posX+frameWidth-c[2]) || (Player.posX+frameWidth-a[2] ==p[1].posX+c[0]))  && Maingame.nKey ==0 && (Player.posY+a[0] == p[1].posY+c[0] ||Player.posY+frameHeight ==p[1].posY+frameHeight) ){
                    Log.d("Collison : " ,"왼쪽 어택");
                    Player.LPUNCH =1;
                    //플레이어가 왼쪽으로 어택시
                }


                for(int i=0;i<object_num;++i){
                   // canvas.drawRect(objedect_size[i].left,objedect_size[i].top,objedect_size[i].right,objedect_size[i].bottom,paint);
                    canvas.drawRect(objedect_size[i].left-cam.realpos.x,objedect_size[i].top,objedect_size[i].right-cam.realpos.x,objedect_size[i].bottom,paint);
                }
//캐릭터 위치 설정하고 그값을 그린다.
            }
        }
        //캐릭터를 그린 시간의 마지막 시간을 비교 연산한다.
        timeThisFrame = System.currentTimeMillis() - startFrameTime;
        if (timeThisFrame >= 1) {
            fps = 1000 / timeThisFrame;
        }
/*
//연기
        for(Smoke sp : smokes){

            sp.draw(canvas);
        }
*/


    }

//캐릭터 위치변화
    void Update(float dt){

        //캐릭터가 업데이트 되는 시작시간을 저장한다.
         startFrameTime = System.currentTimeMillis();

        //포지션이동
        Log.d("Key : " ,Maingame.nKey+"");

        //캐릭터의 스프라이트를 이동하는 것처럼 보이기 위해 값을 변경시켜준다.
        if (isMoving) {

            manXPos = manXPos + runSpeedPerSecond / fps;
            //캐릭터가 width 프레임보다 커지면 다시 y를 증가시킨 x의 처음 위치로 이동

            //background moving  -  map moving _ camera
            limitCamera();

            // 캐릭터 움직임 최대 위치 제한
            if (Player.posX > Width || Player.posX<-500 || Player.posX >2300) {
                Player.posY += (int)frameHeight;
                Player.posX = 10;
                count = 0;
            }
            // 캐릭터가 height프레임을 넘어가면 처음 위치의 y자리로 이동
            if (Player.posY + frameHeight > Height) {
                Player.posY = 10;
            }

            switch (Maingame.nKey)
            {
                case 0:

                    break;
                case 1:
                    if(Player.m_bJump == false)
                    {
                        Player.frameCount = 1;
                        bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_def[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                        //왼쪽오른쪽 자동변경
                        bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                    }
                    break;
                case 2:
                    //점프
                    if(Player.m_bJump == false)
                    {
                        Player.frameCount = 5;
                        bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_jump[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                        //왼쪽오른쪽 자동변경
                        bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                        Player.m_bJump = true;
                    }
                    break;
                case 3:
                    //어택
                    Player. frameCount = 4;
                    Player.SingleFrame = true; // 프레임이 1번만 돌게 해준다.
                    //첫 공격일때
                    if(Player.map_collision == false)
                    {
                        Player.frameCount = 3;
                        Player.currentFrame = 0;
                        Player.n_AttackCount = 1; // 공격카운트를 1로 올려줌 (1번째 공격이다. )
                        bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_kick[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                        bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                        Player.nextAttackFrame = -1; // 다음 프레임은없음 그냥 BASIC으로 변경.
                    }
                    else
                    {
                        if (Player.n_AttackCount == 0) {
                            Player.currentFrame = 0;
                            Player.n_AttackCount = 1; // 공격카운트를 1로 올려줌 (1번째 공격이다. )
                            bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_Attack1[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                            bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                            Player.nextAttackFrame = -1; // 다음 프레임은없음 그냥 BASIC으로 변경.
                        }
                        //두번째 공격일때
                        else if (Player.n_AttackCount == 1) {
                            Player.nextAttackFrame = 1;
                        }
                    }

                    //그 이외
                    //else Player.n_AttackCount = 0;
                    Maingame.nKey = 4;
                    break;

                case 4: // BASIC
                    if(Maingame.mKey==2)
                    {
                        if (Player.n_AttackCount == 0)
                        {
                            if (Player.map_collision == false)
                            {
                            Player.BeforeDirection = 1;
                                Player.frameCount = 5;
                            bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_jump[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                            //왼쪽오른쪽 자동변경
                            bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                            }
                            else
                            {

                                Player.frameCount = 4;
                                 bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_basic[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                                 bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                                 Player.SetStatus(4);
                            }
                        }
                    }
                    break;
            }

                    //touch moving
                    // 좌우 움직이기 관련 버튼입니다.
                    switch (Maingame.mKey)
                    {
                        case 0:     //leftmove
                            if(Maingame.cKey ==1)
                            {
                              if (Player.map_collision == false)
                              {
                                  Player.BeforeDirection = 0;
                                  Player.frameCount = 5;
                                  bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_jump[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                                  //왼쪽오른쪽 자동변경
                                  bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                              }
                              else
                              {
                                  Player.frameCount= 8;
                                  bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_move[MainActivity.ch_num + 4]);
                                  bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                                  Player.SetStatus(0);
                              }
                                Player.Move(true, 2, 0);
                                Player.BeforeDirection = 0;
                            }
                            else{

                                Maingame.mKey = 2;
                            }
                            break;

                        case 1:
                            if(Maingame.cKey==1)
                            {
                                 if (Player.map_collision == false)
                                 {
                                     Player.BeforeDirection = 1;
                                     Player.frameCount = 5;
                                     bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_jump[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                                     //왼쪽오른쪽 자동변경
                                     bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                                 }
                                 else
                                 {
                                     Player.frameCount = 8;
                                     bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_move[MainActivity.ch_num]);
                                     bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                                     Player.SetStatus(1);
                                 }
                                Player.Move(true, 2, 1);
                                Player.BeforeDirection = 1;
                            }
                            else
                            {
                                Maingame.mKey = 2;
                            }
                            break;
                        case 2:
                            if(Maingame.nKey == 4)
                            {
                                if (Player.n_AttackCount == 0)
                                {
                                    if (Player.map_collision == false)
                                    {
                                        Player.frameCount = 5;
                                        bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_jump[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                                        //왼쪽오른쪽 자동변경
                                        bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                                    }
                                    else {
                                        Player.frameCount = 4;
                                        bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_basic[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                                        bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                                        Player.SetStatus(4);
                                    }
                                }
                            }
                            break;
                        case 3:
                            //Player.move(0, -1);
                            break;
                        default:
                            Log.d("hello",Maingame.nKey+"  "+ Maingame.cKey);
                            break;
                    }



                    //캐릭터의 스프라이트를 이동하는 것처럼 보이기 위해 값을 변경시켜준다.
            boolean offJump = Player.JumpTimer();
            if(offJump)
            {
                Maingame.nKey  = 4;
            }



            Gravity();
            map_collision();
            cam.realsetPos(Player.posX);
            cam.add();
            if(live=false&&life==false){
                Die.play(explosionId,1,1,0,0,1);
                life=true;
            }

            manageCurrentFrame();

/*
//연기
            long elapsed =(System.nanoTime() - smkestarttime)/1000000;
            if(elapsed>120) {
                smokes.add(new Smoke(Player.posX, Player.posY + 10));
                smkestarttime = System.nanoTime();
            }

            for(int i =0;i<smokes.size();i++){
                smokes.get(i).update();
                if(smokes.get(1).posX < -10){
                    smokes.remove(i);
                }
            }
*/
        }
    }

//charager frame
    public void manageCurrentFrame() {
        long time = System.currentTimeMillis();

        if (isMoving) {
            if (time > lastFrameChangeTime + frameLengthInMillisecond) {
                lastFrameChangeTime = time;
                //

                Player.currentFrame++;


                if (Player.currentFrame >= Player.frameCount) {
                    if (Player.SingleFrame) // True면 한번만 돌도록.
                    {
                        if (Player.nextAttackFrame == 1) //다음 공격씬이 있을경우
                        {
                            //다음 공격씬
                            Player.currentFrame = 0;
                            bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_Attack2[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                            bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                            Player.nextAttackFrame = -1;

                        }
                        else if (Player.nextAttackFrame == -1) //다음 공격 없는경우
                        {
                            Player.frameCount = 5;
                            Player.currentFrame = 0;
                            if(Player.map_collision == false) Player.frameCount = 4;
                            else Player.frameCount = 5;
                            bitmapRunningMan = BitmapFactory.decodeResource(getResources(), image.img_basic[MainActivity.ch_num + (4 * Player.BeforeDirection)]);
                            bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * Player.frameCount, frameHeight, false);
                            Player.SingleFrame = false;
                            Player.n_AttackCount =0;
                        }
                    }
                    else
                        Player.currentFrame = 0;

                }
            }


            //
        }
        frameToDraw.left = Player.currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;
    }

    //AI rect 변경
    public void rangeRect(){
        if(MainActivity.ch_num == 0){
            //빨초노보 rect
            a[0]=75;a[1]=80;a[2]=50;
            b[0]=55;b[1]=65;b[2]=50;
            c[0]=55;c[1]=55;c[2]=40;
            d[0]=55;d[1]=30;d[2]=40;

        }else if(MainActivity.ch_num ==1){
            //초빨보노
            a[0]=55;a[1]=65;a[2]=50;
            b[0]=75;b[1]=80;b[2]=50;
            c[0]=55;c[1]=30;c[2]=40;
            d[0]=55;d[1]=55;d[2]=40;

        }else if(MainActivity.ch_num ==2){
            //노빨초보
            a[0]=55;a[1]=55;a[2]=40;
            b[0]=75;b[1]=80;b[2]=50;
            c[0]=55;c[1]=65;c[2]=50;
            d[0]=55;d[1]=30;d[2]=40;

        }else if(MainActivity.ch_num ==3){
            //보빨초노
            a[0]=55;a[1]=30;a[2]=40;
            b[0]=75;b[1]=80;b[2]=50;
            c[0]=55;c[1]=65;c[2]=50;
            d[0]=55;d[1]=55;d[2]=40;

        }
    }

    public void limitCamera(){
        if(Maingame.nKey ==1 && Player.posX <1000 && Player.posX>500 ){
            count=count-10;
        }else if(Maingame.nKey ==0 && Player.posX<500 &&Player.posX>-300){
            count = count+10;
        }
    }

    public void playerNum(){
      if(MainActivity.ch_num == 0){
          playN[0] =1;//초
          playN[1] =2;//노
          playN[2] =3;//보

      }else if(MainActivity.ch_num ==1){
          playN[0] =0;//빨
          playN[1] =3;//보
          playN[2] =2;//노


      }else if(MainActivity.ch_num ==2){
          playN[0] =0;//빨
          playN[1] =1;//초
          playN[2] =3;//보
      }else if(MainActivity.ch_num ==3){
          playN[0] =0;//빨
          playN[1] =1;//초
          playN[2] =2;//노
      }
    }

    //현우 코드
    public void Gravity() {
        //Player
        if (Player.map_collision == false) {
            if(Player.m_Velocity.y<30)
            Player.m_Velocity.y += 3;
        } else {
            Player.m_Velocity.y = 0;
        }
        Player.posY += Player.m_Velocity.y;
        if (Player.posY > 1600) live = false;


    }


    public void map_collision(){
        POINT cha_point=new POINT();
        cha_point.x=(2*Player.posX+frameWidth)/2;
        cha_point.y=(Player.posY+frameHeight);
        Player.map_collision=false;
        for(int i=0;i<object_num;++i){
            if( cha_point.y<=objedect_size[i].top)continue;
            if( cha_point.y>=objedect_size[i].bottom)continue;
            if( cha_point.x<=objedect_size[i].left)continue;
            if( cha_point.x>=objedect_size[i].right)continue;
            Player.map_collision=true;
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
                object_pos[0].x=(1180*Width)/2560;
                object_pos[0].y=(1050*Height)/1440;
                size.x=820;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=(740*Width)/2560;
                object_pos[1].y=(340*Height)/1440;
                size.x=260;
                size.y=50;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=(1600*Width)/2560;
                object_pos[2].y=(340*Height)/1440;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=(1180*Width)/2560;
                object_pos[3].y=(700*Height)/1440;
                map_size_setting(3,object_pos[3],size);
                object_pos[4].x=(2030*Width)/2560;
                object_pos[4].y=(700*Height)/1440;
                map_size_setting(4,object_pos[4],size);
                object_pos[5].x=(330*Width)/2560;
                object_pos[5].y=(700*Height)/1440;
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
                object_pos[0].x=(1200*Width)/2560;
                object_pos[0].y=(1200*Height)/1440;
                size.x=950;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=(830*Width)/2560;
                object_pos[1].y=(540*Height)/1440;

                size.x=260;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=(1580*Width)/2560;
                object_pos[2].y=(540*Height)/1440;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=(1200*Width)/2560;
                object_pos[3].y=(640*Height)/1440;
                size.x=280;
                size.y=40;
                map_size_setting(3,object_pos[3],size);
                object_pos[4].x=(1780*Width)/2560;
                object_pos[4].y=(840*Height)/1440;
                size.x=190;
                size.y=40;
                map_size_setting(4,object_pos[4],size);
                object_pos[5].x=(640*Width)/2560;
                object_pos[5].y=(840*Height)/1440;
                map_size_setting(5,object_pos[5],size);
                object_pos[6].x=(1580*Width)/2560;
                object_pos[6].y=(1040*Height)/1440;
                map_size_setting(6,object_pos[6],size);
                object_pos[7].x=(840*Width)/2560;
                object_pos[7].y=(1040*Height)/1440;
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
                object_pos[0].x=(1150*Width)/2560;
                object_pos[0].y=(1300*Height)/1440;
                size.x=700;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=(1200*Width)/2560;
                object_pos[1].y=(460*Height)/1440;
                size.x=920;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=(1920*Width)/2560;
                object_pos[2].y=(980*Height)/1440;
                size.x=180;
                size.y=40;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=(420*Width)/2560;
                object_pos[3].y=(980*Height)/1440;
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
                object_pos[0].x=(1200*Width)/2560;
                object_pos[0].y=(1340*Height)/1440;
                size.x=1100;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=(1200*Width)/2560;
                object_pos[1].y=(1040*Height)/1440;
                size.x=530;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=(1200*Width)/2560;
                object_pos[2].y=(940*Height)/1440;
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
                object_pos[0].x=(1200*Width)/2560;
                object_pos[0].y=(940*Height)/1440;
                size.x=840;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=(720*Width)/2560;
                object_pos[1].y=(770*Height)/1440;
                size.x=180;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=(1650*Width)/2560;
                object_pos[2].y=(770*Height)/1440;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=(1000*Width)/2560;
                object_pos[3].y=(390*Height)/1440;
                map_size_setting(3,object_pos[3],size);
                object_pos[4].x=(1400*Width)/2560;
                object_pos[4].y=(390*Height)/1440;
                map_size_setting(4,object_pos[4],size);
                object_pos[5].x=(440*Width)/2560;
                object_pos[5].y=(570*Height)/1440;
                map_size_setting(5,object_pos[5],size);
                object_pos[6].x=(1950*Width)/2560;
                object_pos[6].y=(570*Height)/1440;
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
                object_pos[0].x=(1200*Width)/2560;
                object_pos[0].y=(1080*Height)/1440;
                size.x=1000;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=(500*Width)/2560;
                object_pos[1].y=(720*Height)/1440;
                size.x=380;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=(1880*Width)/2560;
                object_pos[2].y=(720*Height)/1440;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=(1200*Width)/2560;
                object_pos[3].y=(400*Height)/1440;
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
                object_pos[0].x=(1200*Width)/2560;
                object_pos[0].y=(1240*Height)/1440;
                size.x=1150;
                size.y=40;
                //ing
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=(50*Width)/2560;
                object_pos[1].y=(500*Height)/1440;
                size.x=280;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=(600*Width)/2560;
                object_pos[2].y=(90*Height)/1440;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=(1580*Width)/2560;
                object_pos[3].y=(180*Height)/1440;
                map_size_setting(3,object_pos[3],size);
                object_pos[4].x=(2320*Width)/2560;
                object_pos[4].y=(810*Height)/1440;
                map_size_setting(4,object_pos[4],size);
                object_pos[5].x=(920*Width)/2560;
                object_pos[5].y=(350*Height)/1440;
                size.x=290;
                size.y=40;
                map_size_setting(5,object_pos[5],size);
                object_pos[6].x=(700*Width)/2560;
                object_pos[6].y=(860*Height)/1440;
                map_size_setting(6,object_pos[6],size);
                object_pos[7].x=(1780*Width)/2560;
                object_pos[7].y=(660*Height)/1440;
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
                object_pos[0].x=(910*Width)/2560;
                object_pos[0].y=(1430*Height)/1440;
                size.x=400;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=(1430*Width)/2560;
                object_pos[1].y=(1430*Height)/1440;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=(280*Width)/2560;
                object_pos[2].y=(1000*Height)/1440;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=(2030*Width)/2560;
                object_pos[3].y=(1000*Height)/1440;
                map_size_setting(3,object_pos[3],size);
                object_pos[4].x=(580*Width)/2560;
                object_pos[4].y=(600*Height)/1440;
                map_size_setting(4,object_pos[4],size);
                object_pos[5].x=(1780*Width)/2560;
                object_pos[5].y=(600*Height)/1440;
                map_size_setting(5,object_pos[5],size);
                object_pos[6].x=(1200*Width)/2560;
                object_pos[6].y=(170*Height)/1440;
                map_size_setting(6,object_pos[6],size);
                object_pos[7].x=(80*Width)/2560;
                object_pos[7].y=(170*Height)/1440;
                map_size_setting(7,object_pos[7],size);
                object_pos[8].x=(2280*Width)/2560;
                object_pos[8].y=(170*Height)/1440;
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
                object_pos[0].x=(1200*Width)/2560;
                object_pos[0].y=(1040*Height)/1440;
                size.x=1200;
                size.y=40;
                map_size_setting(0,object_pos[0],size);
                object_pos[1].x=(550*Width)/2560;
                object_pos[1].y=(640*Height)/1440;
                size.x=280;
                size.y=40;
                map_size_setting(1,object_pos[1],size);
                object_pos[2].x=(1880*Width)/2560;
                object_pos[2].y=(640*Height)/1440;
                map_size_setting(2,object_pos[2],size);
                object_pos[3].x=(1200*Width)/2560;
                object_pos[3].y=(340*Height)/1440;
                map_size_setting(3,object_pos[3],size);
                break;

        }
    }

//surface
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

/*
        smkestarttime = System.nanoTime();
*/

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
