package com.example.parkjaeha.supermario;

/**
 * Created by parkjaeha on 2017-08-21.
 */


public class CPlayer
{
    int posX = 10;
    int posY = 10;
    //캐릭터 상태저장
    static int nAnimation = 0; // 애니메이션 상태
    static int nStatus = 0; // 상태 define을 쓰고싶다..........
    static int BeforeDirection = 1; //직전의 방향을 저장해둔다. 0번이면 오른쪽, 1번이면 왼쪽

    int nKey = -1;
    void SetKey(int key)
    {this.nKey = key;}
    int beforeKey = -1;
    boolean changeDir = false;

    int JumpHeight = 0;

    int PUSH=0,RPUNCH=0,LPUNCH=0;

    int GetKey(){return this.nKey;}

    //void move(int x, int y)
    //{ this.posX += x; this.posY += y;}

    void SetAnimation(int nAni){nAnimation = nAni;}

    //현우 코드
    boolean map_collision=false;
    public MainActivity Maingame = new MainActivity();
    //---------------------0825 Player 변수 일괄추가 -------------//
    public POINT m_Position = new POINT();//플레이어의 좌표
    public POINT m_Velocity = new POINT();//가속도

    public POINT m_dirRight = new POINT();//플레이어의 방향
    public Player_options Player_option= new Player_options();
    int nTexture;//스프라이트의 개수
    //강한 공격 게이지 및 수치
    int smash_point = 2;
    int gage = 0;

    int attack_SpriteCount;//공격한 플레이어의 전체 인덱스 넘버
    int attack_SpriteCurrent;//공격한 플레이어의 그 시점 인덱스 넘버
    boolean impact = false;//맞은 상태인지 아닌지
    boolean impact_de = false;//플레이어가 맞은 상태동안 해당 공격에 더 이상 충돌체크를 하지 않는 변수

    int	m_State;			//현재상태
    int	m_BeforeState;		//과거의상태
    int		DIR;	//키입력에 따른 방향
    int		n_AttackCount;//일반 공격 어택은 1과2로 구성, 조작에 따른 스프라이트 변경 변수
    static int     nextAttackFrame = -1;
    static int     now_frameCount = 8;

    //프레임이 1번만 돌아가는 상태일 경우 사용.
    static boolean SingleFrame = false;

    boolean	FrameEnd;//프레임이 끝나고 시작됨을 알리는 변수
    boolean	m_bJump = false;//점프중인지 아닌지 판별하는 요소
    int JumpCount = 0;//점프는 최대 2번 가능
    boolean fly = false;//강한공격을 맞아 날아가는 상태인지에 대한 변수
    POINT d3dxvShift = new POINT();

    //-------------------0830 프레임을 안에다 넣기-----------------------//
    static int frameCount = 8;
    static int currentFrame = 0;

    //---------------------0825 Player 함수 일괄추가 -------------//

    //디폴트 생성자 추가
    public CPlayer()
    {
        BeforeDirection = 1;
        m_dirRight.x = 1;
        m_dirRight.y = 0;

        Player_option.speed = 3.0f;
        Player_option.velocityX_max = 25;
        JumpCount = 2;
        Player_option.smash_count = 4;
        Player_option.collsion_Length = 80;
    }

    void SetStatus(int Status){nStatus = Status;}//각각의 상황에 맞는 스프라이트를 셋팅한다.
    int GetStatus() { return m_State; }//스프라이트의 값을 출력한다.
    void SetPosition(int x, int y) {//초기 포지션 값을 설정
        this.posX = x; this.posY = y;
    }

    POINT GetPosition() { return m_Position; }//포지션 출력
    void Move(boolean dwDirection, float fDistance, int Key){
        if (dwDirection)
        {
            d3dxvShift.x = 0;
            d3dxvShift.y = 0;
//
            if (dwDirection )
            {
                if(Key == 0) // 왼쪽
                {
                    d3dxvShift.x -= m_dirRight.x * 2 ;
                    if(beforeKey == 1)
                    {
                        changeDir = true;
                        m_Velocity.x = 0;
                    }
                    else changeDir = false;
                    beforeKey = 0;
                }
                else
                {
                    d3dxvShift.x += m_dirRight.x * 2;
                    if(beforeKey == 0)
                    {
                        changeDir = true;
                        m_Velocity.x = 0;
                    }
                    else changeDir = false;
                    beforeKey = 1;
                }
            }
//
            if (d3dxvShift.x != 0.0f)
                MoveX(d3dxvShift);
            if (d3dxvShift.y != 0.0f)
                MoveY(d3dxvShift);
        }
    }//해당방향에 해당 거리만큼 이동한다.

    boolean  JumpTimer()//벡터로 바꾸기
    {
        if (m_bJump == true)
        {
            this.posY -= (10 + (3*(10-JumpHeight)));
            map_collision = true;

            this.JumpHeight += 1;
            if(this.JumpHeight >= 10)
            {
                m_bJump = false;
                map_collision = false;
                this.JumpHeight = 0;
                return true;
            }
            else
                return false;
        }
        return false;
    }


    void MoveX(POINT d3dxvShift){
        if (m_Velocity.x <= Player_option.velocityX_max && m_Velocity.x >= -Player_option.velocityX_max)
            m_Velocity.x += d3dxvShift.x;
        this.posX += m_Velocity.x;
    }//해당 포인터의 x값 만큼 x가속도를 증감한다.

    void MoveY(POINT d3dxvShift){
        m_Velocity.y += d3dxvShift.y;
        this.posY += m_Velocity.y;
    }//해당 포인터의 y값 만큼 y가속도를 증감한다.
    void MoveY(float Yshift){
        m_Velocity.y += Yshift;
        this.posY += m_Velocity.y;
    }//해당 변수만큼 y가속도를 증감한다.
    void StateChangeX() { this.m_Velocity.x = 0; }//x가속도를 0으로 초기화
    void StateChangeY() {
        this.m_Velocity.y = 0;//y가속도를 0으로 초기화
    }
    void SetBasic(int State)
    {
        SetStatus(0);
    }
}
