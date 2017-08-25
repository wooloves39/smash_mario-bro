package com.example.parkjaeha.supermario;

/**
 * Created by parkjaeha on 2017-08-21.
 */


public class CPlayer
{
    int posX = 10;
    int posY = 10;
    float m_Velocity_Y=0;
    float m_Velocity_X=0;
    boolean live=true;
    //캐릭터 상태저장
    static int nAnimation = 0; // 애니메이션 상태
    static int nStatus = 0; // 상태 define을 쓰고싶다..........
    static int BeforeDirection = 0; //직전의 방향을 저장해둔다. 0번이면 오른쪽, 1번이면 왼쪽

    int nKey = -1;
    void SetKey(int key)
    {this.nKey = key;}
    int GetKey(){return this.nKey;}

    void move(int x, int y)
    { this.posX += x; this.posY += y;}

    void SetAnimation(int nAni){nAnimation = nAni;}
    void SetStatus(int Status){nStatus = Status;}
    //현우 코드
    boolean map_collision=false;
}
