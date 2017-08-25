package com.example.parkjaeha.supermario;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by user1 on 2017-08-09.
 */

public class GameThread extends Thread {

    private  SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    long dt;

    //생성자에서 sufaceholder 호출
    public GameThread(SurfaceHolder holder, GamePanel gamePanel){

        this.surfaceHolder = holder;
        this.gamePanel = gamePanel;
        dt=0;
    }
    //setrunning
    void setRunning(boolean running){
        this.running = running;
    }

    @Override
    public void run() {
        Canvas canvas;
        long  fps;
        while (running){

            if(!gamePanel.Pause_game){

                long StartDraw = System.currentTimeMillis();

                canvas = null;
                try{
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder){
                        //update,draw로 변화를 계속 확인하고 그려주기

                        gamePanel.Update(dt);
                        gamePanel.Draw(canvas);


                    }
                }catch (Exception e){

                }
                finally {
                    if(canvas!=null){
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }


                long EndDraw = System.currentTimeMillis();
                dt= (EndDraw - StartDraw)/1000000;

                if(dt < 10)  {
                    dt= 10; // Millisecond.
                }
                System.out.print(" Wait Time="+ dt);

                try {
                    // Sleep.
                    this.sleep(dt);
                } catch(InterruptedException e)  {

                }

                StartDraw = System.currentTimeMillis();
                System.out.print(".");
            }




        }

    }
}
