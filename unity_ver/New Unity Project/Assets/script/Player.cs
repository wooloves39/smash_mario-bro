using System.Collections;
using System.Collections.Generic;
using UnityEngine;
public struct player_options
{
    public float speed;
    public float velocityX_max;
    public int JumpCount;
    public int smash_count;
    public float collsion_Length;
    public void setting(int character)
    {
        switch (character)
        {
            case 0:
                speed = 5.0f;
                velocityX_max = 10;
                JumpCount = 2;
                smash_count = 3;
                collsion_Length = 80;
                break;
            case 1:
                speed = 3.0f;
                velocityX_max = 5;
                JumpCount = 2;
                smash_count = 4;
                collsion_Length = 80;
                break;
            case 2:
                speed = 3.0f;
                velocityX_max = 5;
                JumpCount = 3;
                smash_count = 3;
                collsion_Length = 80;
                break;
            case 3:
                speed = 3.0f;
                velocityX_max = 5;
                JumpCount = 2;
                smash_count = 3;
                collsion_Length = 110;
                break;
            default:
                break;
        }
    }
};
public class Player : MonoBehaviour {
    public POINT m_Position;//플레이어의 좌표
    public POINT m_Velocity;//가속도
    public POINT m_dirRight;//플레이어의 방향
    public player_options Player_option;
    public int nTexture;//스프라이트의 개수
    public int smash_point = 2;    //강한 공격 게이지 및 수치
    public int gage = 0;  //강한공격은 막기나 일반공격에 의해 게이지가 채워지고 게이지가 10이 넘으면 스매시 포인터가 1개 생긴다. 최대 개수 3개
    public bool impact = false;//맞은 상태인지 아닌지
    public bool impact_de = false;//플레이어가 맞은 상태동안 해당 공격에 더 이상 충돌체크를 하지 않는 변수
    public int DIR;    //키입력에 따른 방향
    public int n_AttackCount;//일반 공격 어택은 1과2로 구성, 조작에 따른 스프라이트 변경 변수
    public bool FrameEnd;//프레임이 끝나고 시작됨을 알리는 변수
    public bool m_bJump = false;//점프중인지 아닌지 판별하는 요소
    public int JumpCount = 0;//점프는 최대 2번 가능
    public bool fly = false;//강한공격을 맞아 날아가는 상태인지에 대한 변수
    public char[] damage;//화면에 출력되기 위한 변수
    public int PlayTime_num;//플레이 시간
    public char[] PlayTime;//플레이 시간을 출력하기 위한 변수
    public int ranking_num;//랭킹
    public char[] ranking;//랭킹을 출력하기 위한 변수
    public int total_score_num;// 플레이 시간과 초기 공격력을 계산한 전체 점수
    public char[] total_score;   //받은 데이터와 전체 점수, 랭킹->text로 그려야 하기때문에 각각 인덱스를 가짐
    public int low_power = 12;//약한 공격의 데미지
    public int High_power = 30;  //약한 때림과 강한 때림에 따른 power
    public int attacker_num;//때린 플레이어의 넘버
    public bool mapobject_collsion = false;//맵과 충돌중인지 아닌지 판별
    public bool sma = false;//스메시 공격을 맞은 상태인지 아닌지 판별
    public bool hit = false;//공격을 맞아서 처리하는 동안 돌아가는 변수
    public bool down = false;//아래 키를 누르면 내려가게 만드는 것 아직 미완성
    public bool live = true;//화면에서 일정 거리 밖으로 나가면 죽는 요소
    public int damage_num = 100;//초기 공격력
    // Use this for initialization
    void Start () {
        
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
