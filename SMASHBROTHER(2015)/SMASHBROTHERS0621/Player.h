#pragma once
#include "stdafx.h"
#include"Camera.h"

typedef struct Image
{
	CImage Texture;
	UINT	nSpriteCount;		// 스프라이트 전체 인덱스
	UINT	nSpriteCurrent;		// 현재 스프라이트 인덱스
	UINT	g_nSpriteX;			// 스프라이트 가로
	UINT	g_nSpriteY;			// 스프라이트 세로
	vector<pair<POINT, UINT>> Pos_and_Count;
};
typedef struct player_options {
	float speed;
	float velocityX_max;
	int JumpCount;
	int smash_count;
	float collsion_Length;
	void setting(int character) 
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
class CPlayer
{
public:
	POINT m_Position;//플레이어의 좌표
	POINT m_Velocity;//가속도

	POINT m_dirRight;//플레이어의 방향
	player_options Player_option;
	int nTexture;//스프라이트의 개수
				 //강한 공격 게이지 및 수치
	int smash_point = 2;
	int gage = 0;
	//강한공격은 막기나 일반공격에 의해 게이지가 채워지고 게이지가 10이 넘으면 스매시 포인터가 1개 생긴다. 최대 개수 3개
public:

	Image	*m_ppTexture;//스프라이트
	UINT attack_SpriteCount;//공격한 플레이어의 전체 인덱스 넘버
	UINT attack_SpriteCurrent;//공격한 플레이어의 그 시점 인덱스 넘버
	bool impact = false;//맞은 상태인지 아닌지
	bool impact_de = false;//플레이어가 맞은 상태동안 해당 공격에 더 이상 충돌체크를 하지 않는 변수
	CImage rank_state;//랭킹 UI
	CImage UI;//플레이시 보이는 UI
	CImage GageUI;
	CImage GageBackUI;
	int Point_sprite_index = 0;
	DWORD	m_State;			//현재상태 
	DWORD	m_BeforeState;		//과거의상태 
	int		DIR;	//키입력에 따른 방향
	int		n_AttackCount;//일반 공격 어택은 1과2로 구성, 조작에 따른 스프라이트 변경 변수

	bool	FrameEnd;//프레임이 끝나고 시작됨을 알리는 변수
	bool	m_bJump = false;//점프중인지 아닌지 판별하는 요소

	System*	charSystem;
	Channel* pChannel;
	Sound* charSound[6];//fmod에 의거한 사운드와 채널 강한공격, 약한 공격, 점프, 강한공격을 맞을시, 약한공격을 맞을시, 죽을 시
	int JumpCount = 0;//점프는 최대 2번 가능
	bool fly = false;//강한공격을 맞아 날아가는 상태인지에 대한 변수
	Image Smash_Point;
	Image fly_paticle;
	Image attack_paticle;
	bool hidden = false;
	void fling_paticle() {
		if (m_State == FLY_LEFT || m_State == FLY_RIGHT) {
			pair<POINT, UINT>pos = { m_Position, 0 };
			fly_paticle.Pos_and_Count.push_back(pos);
		}
	}
	void SetSprite(LPCTSTR pCImage, Image&type, int nSpriteCount)
	{
		type.Texture.Load(pCImage);
		type.g_nSpriteX = nSpriteCount;
		type.nSpriteCount = nSpriteCount;
		type.g_nSpriteY = 1;
		type.nSpriteCurrent = 0;
	}
	void DrawParticle(HDC hDC, CCamera cam, Image&type, int size)
	{
		UINT nSpriteWidth = type.Texture.GetWidth() / fly_paticle.nSpriteCount;
		UINT nSpriteHeight = type.Texture.GetHeight() / 1;
		// 0605. 스프라이트 올려주는 타이머를 Player 클래스안에 넣을순없을까? 
		for (int i = 0; i < type.Pos_and_Count.size(); ++i) {
			UINT xCoord = type.Pos_and_Count[i].second%type.g_nSpriteX;
			UINT yCoord = type.Pos_and_Count[i].second / type.g_nSpriteX;
			type.Texture.Draw(hDC
				, type.Pos_and_Count[i].first.x - nSpriteWidth / 2 - cam.getPos().x - size, type.Pos_and_Count[i].first.y - nSpriteHeight / 2 - cam.getPos().y*(1280 / 940) * 3 - size, nSpriteWidth + size, nSpriteHeight + size
				, xCoord * nSpriteWidth, yCoord * nSpriteHeight
				, nSpriteWidth, nSpriteHeight);
		}
	}
	void Draw_Impact(HDC hDC, CCamera cam) {
		if (fly == true)DrawParticle(hDC, cam, fly_paticle, -15);
		if (attack_paticle.Pos_and_Count.size())DrawParticle(hDC, cam, attack_paticle, 20);
	}
	void DrawSmashPoint(HDC hDC, POINT char_pos) {
		UINT nSpriteWidth = Smash_Point.Texture.GetWidth() / Smash_Point.nSpriteCount;
		UINT nSpriteHeight = Smash_Point.Texture.GetHeight() / 1;
		for (int i = 0; i < smash_point; ++i)
			Smash_Point.Texture.Draw(hDC, char_pos.x + i * 30 - 25, char_pos.y, 25, 25, Point_sprite_index * 50, 0, nSpriteWidth, nSpriteHeight);
	}

	void setting(CPlayer* target, UINT target_name) {
		rank_state = target->rank_state;
		UI = target->UI;
		Player_option.setting(target_name);
		fly_paticle = target->fly_paticle;
		Smash_Point = target->Smash_Point;
		attack_paticle = target->attack_paticle;
		GageUI = target->GageUI;
		GageBackUI = target->GageBackUI;
	}
	void DrawUI(HDC hDC, int Player_index) {
		static float gageBar = 0;
		POINT Smash_Pos = { 80 + Player_index * 300,630 };
		DrawSmashPoint(hDC, Smash_Pos);
		UI.TransparentBlt(hDC, 80 + Player_index * 300, 660, 50, 50, 0, 0, 30, 30, RGB(255, 255, 255));
		GageBackUI.Draw(hDC, 220 + Player_index * 300, 620, 100, 30);
		if (gage > 0) {
			if (gage>gageBar)
				gageBar += 0.1;
			GageUI.Draw(hDC, 220 + Player_index * 300, 620, gageBar * 10, 30);
		}
	}
	void Hidden() {
		hidden = !hidden;
		//damage_num = 500;
	}
public:
	CPlayer(int nStatus);//생성 및 초기화
	~CPlayer();
	UINT Get_SPcount() { return m_ppTexture->nSpriteCount; }//현재 상태 인덱스 넘버
	UINT Get_SPcurrent() { return m_ppTexture->nSpriteCurrent; }//전체 상태 인덱스 너버
	void SetStatus(int state);//각각의 상황에 맞는 스프라이트를 셋팅한다.
	DWORD GetStatus(void) { return m_State; };//스프라이트의 값을 출력한다.
	void SetPosition(float x, float y) {//초기 포지션 값을 설정
		m_Position.x = x; m_Position.y = y;
	};
	void SetTexture(int nIndex, LPCTSTR pCImage, int nSpriteCount);//텍스처들을 셋팅한다.
	POINT GetPosition() { return m_Position; };//포지션 출력
	void Move(DWORD dwDirection, float fDistance);//해당방향에 해당 거리만큼 이동한다.
	void MoveX(const POINT& d3dxvShift);//해당 포인터의 x값 만큼 x가속도를 증감한다.
	void MoveY(const POINT& d3dxvShift);//해당 포인터의 y값 만큼 y가속도를 증감한다.
	void MoveY(const float Yshift);//해당 변수만큼 y가속도를 증감한다.
	void StateChangeX(void) { m_Velocity.x = 0.0; }//x가속도를 0으로 초기화
	void StateChangeY() {
		m_Velocity.y = 0.0;//y가속도를 0으로 초기화
	}
	void DrawSprite(HDC hDC, int g_nSpriteCurrent, CCamera cam);//play state에서 그려지는것에 대한 함수
	void DrawSprite(HDC hDC, int g_nSpriteCurrent, int x, int y);//ranking state에서 그려지는 것들에 대한 함수
	void JumpTimer(void);//점프 시 가속도와 위치의 변화
	void SetBasic(int State);//해당 방향으로 평시 스프라이트로 전환
	void Render(HDC hDC);//해당 스프라이트에 해당 인덱스를 그려냄
						 //-------------- 현우추가 여기서부터 ----------//
	void SetImage(Image* Image) {//이미지를 초기화
		m_ppTexture = Image;

	};
	Image* GetImage(void) { return m_ppTexture; };//해당이미지를 출력
	int damage_num = 100;//초기 공격력
public:
	char damage[3];//화면에 출력되기 위한 변수
	int PlayTime_num;//플레이 시간
	char PlayTime[3];//플레이 시간을 출력하기 위한 변수
	int ranking_num;//랭킹
	char ranking[2];//랭킹을 출력하기 위한 변수
	int total_score_num;// 플레이 시간과 초기 공격력을 계산한 전체 점수
	char total_score[10];   //받은 데이터와 전체 점수, 랭킹->text로 그려야 하기때문에 각각 인덱스를 가짐

	int low_power = 12;//약한 공격의 데미지
	int High_power = 30;  //약한 때림과 강한 때림에 따른 power
	int attacker_num;//때린 플레이어의 넘버
	bool mapobject_collsion = false;//맵과 충돌중인지 아닌지 판별
	bool sma = false;//스메시 공격을 맞은 상태인지 아닌지 판별
	bool hit = false;//공격을 맞아서 처리하는 동안 돌아가는 변수
	bool down = false;//아래 키를 누르면 내려가게 만드는 것 아직 미완성
	bool live = true;//화면에서 일정 거리 밖으로 나가면 죽는 요소
					 //----------- FUNCTION	DEFINE -------------//

public:
	virtual void KeyState(CCamera& cam, int state, int mode = 1, int player = 1);//키입력을 위한 함수 /*따로 주석 처리*/
	int getVelocity() { return m_Velocity.x; }//x축의 가속도를 출력
	int getDamege_num() { return damage_num; }//데미지 숫자 출력
	void printdamege() { wsprintf(damage, TEXT("%d"), damage_num); }//데미지 갱신 함수 데미지는 플레이 도중 갱신된다.
	char* getDamege() { return damage; }//데미지 문자 출력
	double Vx(double P) { return sin(45 * RAD)*P; }
	double Vy(double P) { return cos(45 * RAD)*P; }//맞은 데미지를 45각도로 나눈다.
	void smashing(int damage, int power, bool smash);//스메시 공격을 맞아 처리하는 함수 프레임마다 중력과 공기저항, 스프라이트에 영향을 받는다.
	void gravity(void);//매프레임 맵과충돌하지 않을 시 발생하는 중력을 처리

	int getAttacker() { return attacker_num; }//공격자를 알게하는 함수
	void text_rank() {//ranking 화면이 되었을때 활성화되는 함수
		wsprintf(PlayTime, TEXT("%d"), PlayTime_num);
		wsprintf(ranking, TEXT("%d"), ranking_num);
		wsprintf(total_score, TEXT("%d"), total_score_num);
	}
	void attack(CPlayer **other, int player_num);//공격자가 방어자를 판단하고, 해당 게이지를 얻는 등의 처리를 하는 함수
	void defance(CPlayer **other, int player_num);//일부 수정함 충돌체크 1번만 하게 구현 방어자가 공격자를 판단하고 해당 공격의 성격, 처리를 하는 함수
												  //bool Lowhit_num(); 약하게 맞는 연타횟수를 만들고 싶음
	int getSmashpoint() { return smash_point; }//플레이어가 가지고 있는 스매시 포인터를 출력
	void smashsub() { if (smash_point > 0)--smash_point; }//강한 공격으로 스매시 포인터 1개씩감소
	void smashadd() {
		if (gage >= 10) {
			if (smash_point < Player_option.smash_count)
				++smash_point;
			gage = 0;
		}
	}//게이지가 10이상일시 스매시 포인터를 1개씩 증가
	int getgage() { return gage; }//게이지 출력
	virtual void Playercollision(CPlayer **other, int player_num);//플레이어 끼리 충돌 시 옆으로 밀어내는 함수
	void release() {//리플레이 시 선택하도록 변수들 초기화
		delete[] m_ppTexture;
		rank_state.Destroy();
		UI.Destroy();

	}
	virtual bool AI() {//AI인지 플레이어인지 판별하는 함수
		return false;
	}
	virtual void distance(CPlayer **other, int player_num) {}//AI사용함수
	void Timer(void);//스프라이트 인덱스 처리함수

};
class CAIPlayer
	:public CPlayer
{
public:

	bool road_algo = false;//상대가 나보다 아래에 있을때에 대한 길찾기 알고리즘 변수
	CPlayer* target;//타겟팅이 된 플레이어
	bool targeting = false;//타겟팅이 되었는지 아닌지 판별

public:

	CAIPlayer(int nStatus);
	virtual void KeyState(CCamera& cam, int state, int mode = 1, int player = 1);//타겟팅에 맞춰 조작
																				 //데미지까지 더하기

	virtual void distance(CPlayer **other, int player_num);//상대의 거리 파악, 타겟팅
	virtual bool AI() {//AI인지 아닌지 판별
		return true;
	}
};

