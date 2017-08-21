package com.example.parkjaeha.supermario;

/**
 * Created by parkjaeha on 2017-08-21.
 */

public class CPlayer
{
    int posX = 10;
    int posY = 10;
    int nAnimation = 0;

    int nKey = -1;
    void SetKey(int key)
    {this.nKey = key;}
    int GetKey(){return this.nKey;}

    void move(int x, int y)
    { this.posX += x; this.posY += y;}



    void SetAnimation(int nAni){nAnimation = nAni;}
}
