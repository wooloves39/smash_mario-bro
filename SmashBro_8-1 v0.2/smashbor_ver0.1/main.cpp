#include "stdafx.h"
#include "Camera.h"
#include "Player.h"
#include"map.h"
int state = 0;
enum state { title, cho_map, cho_cha, play, ranking, ending };//스테이트 순서 변경
enum DIRECTION_P
{
	DIRECTION_LEFT = 1,
	DIRECTION_RIGHT
};
CImage Title;
CImage Background;
CImage Choice_map[6];
CImage Choice_cha[4];
CImage Ending;
CImage Rankstate;
CImage mapEX[10];
POINT sel;
POINT sel2;
System*	pSystem;
Sound* stateSound[4];
Sound* choiceSound;
Sound* changeSound;
float stateVolume = 0.2f;
Channel*		pChannel[2];
bool	GameReady = false;
static UCHAR pKeyBuffer[256];
int mode;//1피냐 2피냐
int PlayTime = 99;//플레이 타임
char Playtime_t[3];

int		  nowPlayer[2] = { 0, 3 };	//현재 내가 움직이고있는 플레이어
int		  nPlayer;		//현재 플레이하는 모든 플레이어의 수
bool Player1 = false;
bool Player2 = false;
//-----Player추가 

CPlayer** m_Player;

LRESULT CALLBACK WndProc(HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam);
map m;
CCamera cam;
int map_stage;
CImage demage_UI;

//SET PLAYER를위해 캐릭터를 전역변수로선언
CPlayer *Mario;
CPlayer *Wario;
CPlayer *Luizy;
CPlayer *Waluizy;


void setranking() {
	//pChannel[0]->stop();
	pSystem->playSound(FMOD_CHANNEL_REUSE, stateSound[ranking - 2], false, &pChannel[0]);
	state = ranking;
	for (int i = 0; i < nPlayer; i++) {
		if (m_Player[i]->live == true)m_Player[i]->PlayTime_num = 99;
		m_Player[i]->total_score_num = (m_Player[i]->PlayTime_num * 50) + m_Player[i]->damage_num;
	}
	for (int i = 0; i < nPlayer; ++i) {

		int rank = 1;
		for (int j = 0; j < nPlayer; j++) {
			if (m_Player[i]->total_score_num < m_Player[j]->total_score_num)rank++;
		}
		m_Player[i]->ranking_num = rank;
		if (m_Player[i]->ranking_num == 1)m_Player[i]->SetStatus(WIN);
		else
			m_Player[i]->SetStatus(LOSE);
		m_Player[i]->text_rank();
	}

}//전체 랭킹을 정리하고 셋팅한다.

void createSound() {
	System_Create(&pSystem);
	pSystem->init(6, FMOD_INIT_NORMAL, NULL);

	pSystem->createSound("sound\\opening.mp3", FMOD_HARDWARE | FMOD_LOOP_NORMAL, NULL, &stateSound[title]);
	pSystem->createSound("sound\\choice.mp3", FMOD_HARDWARE | FMOD_LOOP_NORMAL, NULL, &stateSound[cho_map]);

	pSystem->createSound("sound\\ranking.mp3", FMOD_HARDWARE | FMOD_LOOP_NORMAL, NULL, &stateSound[ranking - 2]);
	pSystem->createSound("sound\\ending.mp3", FMOD_HARDWARE | FMOD_LOOP_NORMAL, NULL, &stateSound[ending - 2]);
	pSystem->createSound("sound\\imfact\\choice.wav", FMOD_HARDWARE | FMOD_LOOP_OFF, NULL, &choiceSound);
	pSystem->createSound("sound\\imfact\\change.wav", FMOD_HARDWARE | FMOD_LOOP_OFF, NULL, &changeSound);
}//플레이전 설정에서 사용되는 사운드들

void reset() {//리플레이시 값들을 초기화
	pChannel[0]->stop();
	pChannel[1]->stop();
	m.release();
	sel.x = 140;
	sel.y = 550;
	map_stage = 1;
	for (int i = 0; i < nPlayer; i++) {
		m_Player[i]->release();
	}
	sel2.y = 175 + 120 + 25;
	sel2.x = 300 * 3 + 50 + 125;
	PlayTime = 99;

};
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpszCmdParam, int nCmdShow)
{
	HWND hWnd;
	MSG Message;
	HINSTANCE g_hInst;//
	WNDCLASS WndClass;
	g_hInst = hInstance;

	WndClass.cbClsExtra = 0;
	WndClass.cbWndExtra = 0;
	WndClass.hbrBackground = (HBRUSH)GetStockObject(BLACK_BRUSH);
	WndClass.hCursor = LoadCursor(NULL, IDC_ARROW);
	WndClass.hIcon = LoadIcon(NULL, IDI_APPLICATION);
	WndClass.hInstance = hInstance;
	WndClass.lpfnWndProc = (WNDPROC)WndProc;
	WndClass.lpszClassName = TEXT("Smash Mario Brothers!");
	WndClass.lpszMenuName = NULL;
	WndClass.style = CS_HREDRAW | CS_VREDRAW;
	RegisterClass(&WndClass);

	RECT rcWindow = { 0, 0, 1280, 720 };
	AdjustWindowRect(&rcWindow, WS_OVERLAPPEDWINDOW, false);
	hWnd = CreateWindow(TEXT("Smash Mario Brothers!"), TEXT("Smash Mario Brothers!"), WS_OVERLAPPED | WS_SYSMENU | WS_CAPTION, 0, 0, rcWindow.right - rcWindow.left, rcWindow.bottom - rcWindow.top, NULL, (HMENU)NULL, hInstance, NULL);



	ShowWindow(hWnd, nCmdShow);
	UpdateWindow(hWnd);

	while (GetMessage(&Message, 0, 0, 0)) {
		TranslateMessage(&Message);
		DispatchMessage(&Message);
		if (m_Player != nullptr) {
			if (mode == 2) {
				m_Player[0]->KeyState(cam, state, mode, 1);
				m_Player[1]->KeyState(cam, state, mode, 2);
				m_Player[2]->KeyState(cam, state, 1);
				m_Player[3]->KeyState(cam, state, 1);
			}
			else {
				m_Player[0]->KeyState(cam, state, mode);
				m_Player[1]->KeyState(cam, state, 1);
				m_Player[2]->KeyState(cam, state, 1);
				m_Player[3]->KeyState(cam, state, 1);
			}
		}
	}
	return Message.wParam;
}

//-----------------------------------------------------------//

void BuildPlayer()
{
	
	Mario = new CPlayer(28);
	//BASIC 
	Mario->SetTexture(BASIC_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHTBASIC.png"), 4);
	Mario->SetTexture(BASIC_LEFT,
		_T("character\\MARIO\\MARIO_LEFTBASIC.png"), 4);
	//RUN
	Mario->SetTexture(MOVE_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHTMOVE.png"), 8);
	Mario->SetTexture(MOVE_LEFT,
		_T("character\\MARIO\\MARIO_LEFTMOVE.png"), 8);
	//JUMP
	Mario->SetTexture(JUMP_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHTJUMP.png"), 5);
	Mario->SetTexture(JUMP_LEFT,
		_T("character\\MARIO\\MARIO_LEFTJUMP.png"), 5);
	//ATTACK - STRONG
	Mario->SetTexture(HATTACK_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHTSTRONGATTACK.png"), 6);
	Mario->SetTexture(HATTACK_LEFT,
		_T("character\\MARIO\\MARIO_LEFTSTRONGATTACK.png"), 6);
	//ATTACK 1 
	//1-1
	Mario->SetTexture(ATTACK1_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHTATTACK1-1.png"), 4);
	Mario->SetTexture(ATTACK1_LEFT,
		_T("character\\MARIO\\MARIO_LEFTATTACK1-1.png"), 4);
	//1-2
	Mario->SetTexture(ATTACK2_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHTATTACK1.png"), 4);
	Mario->SetTexture(ATTACK2_LEFT,
		_T("character\\MARIO\\MARIO_LEFTATTACK1.png"), 4);
	//1-3 점프중 공격키누르면 나오는것 
	Mario->SetTexture(KICK_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHTKICK.png"), 3);
	Mario->SetTexture(KICK_LEFT,
		_T("character\\MARIO\\MARIO_LEFTKICK.png"), 3);
	//DEFENSE
	Mario->SetTexture(DEFENSE_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHT_DEF.png"), 2);
	Mario->SetTexture(DEFENSE_LEFT,
		_T("character\\MARIO\\MARIO_LEFT_DEF.png"), 2);
	//SIT
	Mario->SetTexture(SIT_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHTSIT.png"), 3);
	Mario->SetTexture(SIT_LEFT,
		_T("character\\MARIO\\MARIO_LEFTSIT.png"), 3);
	//FLY (날아가는 모션)
	Mario->SetTexture(FLY_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHT_FLY.png"), 4);
	Mario->SetTexture(FLY_LEFT,
		_T("character\\MARIO\\MARIO_LEFT_FLY.png"), 4);
	//UP(일어나는 모션)
	Mario->SetTexture(UP_LEFT,
		_T("character\\MARIO\\MARIO_LEFTUP.png"), 2);
	Mario->SetTexture(UP_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHTUP.png"), 2);
	//DYE
	Mario->SetTexture(DYE_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHTDYE.png"), 3);
	Mario->SetTexture(DYE_LEFT,
		_T("character\\MARIO\\MARIO_LEFTDYE.png"), 3);

	//LOSE
	Mario->SetTexture(LOSE,
		_T("character\\MARIO\\MARIO_LOSE.png"), 2);
	//WIN
	Mario->SetTexture(WIN,
		_T("character\\MARIO\\MARIO_VICTORY.png"), 4);
	Mario->rank_state.Load("character\\MARIO\\marioUI.png");
	Mario->UI.Load("character\\MARIO\\mario_UI.png");
	//ADD CHANGE_EX
	Mario->SetTexture(CHANGE_EX_RIGHT,
		_T("character\\MARIO\\MARIO_RIGHT_CEX.png"), 2);
	Mario->SetTexture(CHANGE_EX_LEFT,
		_T("character\\MARIO\\MARIO_LEFT_CEX.png"), 2);
	Mario->SetSprite(_T("character\\fly_impact.png"), Mario->fly_paticle, 4);
	Mario->SetSprite(_T("character\\MARIO\\export_damagedEffect.png"), Mario->attack_paticle, 4);
	Mario->SetSprite(_T("character\\rotateStar.png"),Mario->Smash_Point,12 );
	//2. Wario
	Wario = new CPlayer(28);
	//BASIC
	Wario->SetTexture(BASIC_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHTBASIC.png"), 4);
	Wario->SetTexture(BASIC_LEFT,
		_T("character\\WARIO\\WARIO_LEFTBASIC.png"), 4);
	//RUN
	Wario->SetTexture(MOVE_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHTMOVE.png"), 8);
	Wario->SetTexture(MOVE_LEFT,
		_T("character\\WARIO\\WARIO_LEFTMOVE.png"), 8);
	//JUMP
	Wario->SetTexture(JUMP_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHTJUMP.png"), 5);
	Wario->SetTexture(JUMP_LEFT,
		_T("character\\WARIO\\WARIO_LEFTJUMP.png"), 5);
	//ATTACK - STRONG
	Wario->SetTexture(HATTACK_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHTSTRONGATTACK.png"), 6);
	Wario->SetTexture(HATTACK_LEFT,
		_T("character\\WARIO\\WARIO_LEFTSTRONGATTACK.png"), 6);
	//ATTACK 1 
	//1-1
	Wario->SetTexture(ATTACK1_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHTATTACK1-1.png"), 4);
	Wario->SetTexture(ATTACK1_LEFT,
		_T("character\\WARIO\\WARIO_LEFTATTACK1-1.png"), 4);
	//1-2
	Wario->SetTexture(ATTACK2_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHTATTACK1.png"), 4);
	Wario->SetTexture(ATTACK2_LEFT,
		_T("character\\WARIO\\WARIO_LEFTATTACK1.png"), 4);
	//1-3 점프중 공격키누르면 나오는것 
	Wario->SetTexture(KICK_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHTKICK.png"), 3);
	Wario->SetTexture(KICK_LEFT,
		_T("character\\WARIO\\WARIO_LEFTKICK.png"), 3);
	//DEFENSE
	Wario->SetTexture(DEFENSE_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHT_DEF.png"), 2);
	Wario->SetTexture(DEFENSE_LEFT,
		_T("character\\WARIO\\WARIO_LEFT_DEF.png"), 2);
	//SIT
	Wario->SetTexture(SIT_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHTSIT.png"), 3);
	Wario->SetTexture(SIT_LEFT,
		_T("character\\WARIO\\WARIO_LEFTSIT.png"), 3);
	//FLY (날아가는 모션)
	Wario->SetTexture(FLY_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHT_FLY.png"), 4);
	Wario->SetTexture(FLY_LEFT,
		_T("character\\WARIO\\WARIO_LEFT_FLY.png"), 4);
	//UP(일어나는 모션)
	Wario->SetTexture(UP_LEFT,
		_T("character\\WARIO\\WARIO_LEFTUP.png"), 2);
	Wario->SetTexture(UP_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHTUP.png"), 2);
	//DYE
	Wario->SetTexture(DYE_LEFT,
		_T("character\\WARIO\\WARIO_LEFTDIE.png"), 3);
	Wario->SetTexture(DYE_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHTDIE.png"), 3);
	//LOSE
	Wario->SetTexture(LOSE,
		_T("character\\WARIO\\WARIO_LOSE.png"), 2);
	//WIN
	Wario->SetTexture(WIN,
		_T("character\\WARIO\\WARIO_VICTORY.png"), 4);
	//ADD CHANGE_EX
	Wario->SetTexture(CHANGE_EX_RIGHT,
		_T("character\\WARIO\\WARIO_RIGHT_CEX.png"), 2);
	Wario->SetTexture(CHANGE_EX_LEFT,
		_T("character\\WARIO\\WARIO_LEFT_CEX.png"), 2);


	Wario->rank_state.Load("character\\WARIO\\warioUI.png");
	Wario->UI.Load("character\\WARIO\\wario_UI.png");
	Wario->SetSprite(_T("character\\fly_impact.png"), Wario->fly_paticle,4);
	Wario->SetSprite(_T("character\\WARIO\\export_damagedEffect.png"),Wario->attack_paticle, 4);
	Wario->SetSprite(_T("character\\rotateStar.png"),Wario->Smash_Point, 12);
	//3. LUIZY
	Luizy = new CPlayer(28);
	//BASIC
	Luizy->SetTexture(BASIC_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHTBASIC.png"), 4);
	Luizy->SetTexture(BASIC_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFTBASIC.png"), 4);
	//RUN
	Luizy->SetTexture(MOVE_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHTMOVE.png"), 8);
	Luizy->SetTexture(MOVE_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFTMOVE.png"), 8);
	//JUMP
	Luizy->SetTexture(JUMP_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHTJUMP.png"), 5);
	Luizy->SetTexture(JUMP_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFTJUMP.png"), 5);
	//ATTACK - STRONG
	Luizy->SetTexture(HATTACK_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHTSTRONGATTACK.png"), 6);
	Luizy->SetTexture(HATTACK_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFTSTRONGATTACK.png"), 6);
	//ATTACK 1 
	//1-1
	Luizy->SetTexture(ATTACK1_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHTATTACK1-1.png"), 4);
	Luizy->SetTexture(ATTACK1_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFTATTACK1-1.png"), 4);
	//1-2
	Luizy->SetTexture(ATTACK2_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHTATTACK1.png"), 4);
	Luizy->SetTexture(ATTACK2_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFTATTACK1.png"), 4);
	//1-3 점프중 공격키누르면 나오는것 
	Luizy->SetTexture(KICK_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHTKICK.png"), 3);
	Luizy->SetTexture(KICK_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFTKICK.png"), 3);
	//DEFENSE
	Luizy->SetTexture(DEFENSE_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHT_DEF.png"), 2);
	Luizy->SetTexture(DEFENSE_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFT_DEF.png"), 2);
	//SIT
	Luizy->SetTexture(SIT_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHTSIT.png"), 3);
	Luizy->SetTexture(SIT_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFTSIT.png"), 3);
	//FLY (날아가는 모션)
	Luizy->SetTexture(FLY_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHT_FLY.png"), 4);
	Luizy->SetTexture(FLY_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFT_FLY.png"), 4);
	//UP(일어나는 모션)
	Luizy->SetTexture(UP_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFTUP.png"), 2);
	Luizy->SetTexture(UP_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHTUP.png"), 2);
	//DYE
	Luizy->SetTexture(DYE_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFTDIE.png"), 3);
	Luizy->SetTexture(DYE_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHTDIE.png"), 3);
	//LOSE
	Luizy->SetTexture(LOSE,
		_T("character\\LUIZY\\LUIZY_LOSE.png"), 2);
	//WIN
	Luizy->SetTexture(WIN,
		_T("character\\LUIZY\\LUIZY_VICTORY.png"), 4);
	Luizy->rank_state.Load("character\\LUIZY\\luizyUI.png");
	Luizy->UI.Load("character\\LUIZY\\luizy_UI.png");
	//ADD CHANGE_EX
	Luizy->SetTexture(CHANGE_EX_RIGHT,
		_T("character\\LUIZY\\LUIZY_RIGHT_CEX.png"), 2);
	Luizy->SetTexture(CHANGE_EX_LEFT,
		_T("character\\LUIZY\\LUIZY_LEFT_CEX.png"), 2);
	Luizy->SetSprite(_T("character\\fly_impact.png"),Luizy->fly_paticle, 4);
	Luizy->SetSprite(_T("character\\LUIZY\\export_damagedEffect.png"), Luizy->attack_paticle,4);
	Luizy->SetSprite(_T("character\\rotateStar.png"), Luizy->Smash_Point,12);
	Waluizy = new CPlayer(28);
	//BASIC
	Waluizy->SetTexture(BASIC_RIGHT,
		_T("character\\WALUIZY\\WALUIZY_RIGHTBASIC.png"), 4);
	Waluizy->SetTexture(BASIC_LEFT,
		_T("character\\WALUIZY\\WALUIZY_LEFTBASIC.png"), 4);
	//RUN
	Waluizy->SetTexture(MOVE_RIGHT,
		_T("character\\WALUIZY\\waluizy_RIGHTMOVE.png"), 8);
	Waluizy->SetTexture(MOVE_LEFT,
		_T("character\\WALUIZY\\waluizy_LEFTMOVE.png"), 8);
	//JUMP
	Waluizy->SetTexture(JUMP_RIGHT,
		_T("character\\WALUIZY\\waluizy_RIGHTJUMP.png"), 5);
	Waluizy->SetTexture(JUMP_LEFT,
		_T("character\\WALUIZY\\waluizy_LEFTJUMP.png"), 5);
	//ATTACK - STRONG
	Waluizy->SetTexture(HATTACK_RIGHT,
		_T("character\\WALUIZY\\waluizy_RIGHTSTRONGATTACK.png"), 6);
	Waluizy->SetTexture(HATTACK_LEFT,
		_T("character\\WALUIZY\\waluizy_LEFTSTRONGATTACK.png"), 6);
	//ATTACK 1 
	//1-1
	Waluizy->SetTexture(ATTACK1_RIGHT,
		_T("character\\WALUIZY\\waluizy_RIGHTATTACK1-1.png"), 4);
	Waluizy->SetTexture(ATTACK1_LEFT,
		_T("character\\WALUIZY\\waluizy_LEFTATTACK1-1.png"), 4);
	//1-2
	Waluizy->SetTexture(ATTACK2_RIGHT,
		_T("character\\WALUIZY\\waluizy_RIGHTATTACK1.png"), 4);
	Waluizy->SetTexture(ATTACK2_LEFT,
		_T("character\\WALUIZY\\waluizy_LEFTATTACK1.png"), 4);
	//1-3 점프중 공격키누르면 나오는것 
	Waluizy->SetTexture(KICK_RIGHT,
		_T("character\\WALUIZY\\waluizy_RIGHTKICK.png"), 3);
	Waluizy->SetTexture(KICK_LEFT,
		_T("character\\WALUIZY\\waluizy_LEFTKICK.png"), 3);
	//DEFENSE
	Waluizy->SetTexture(DEFENSE_RIGHT,
		_T("character\\WALUIZY\\waluizy_RIGHT_DEF.png"), 2);
	Waluizy->SetTexture(DEFENSE_LEFT,
		_T("character\\WALUIZY\\waluizy_LEFT_DEF.png"), 2);
	//SIT
	Waluizy->SetTexture(SIT_RIGHT,
		_T("character\\WALUIZY\\waluizy_RIGHTSIT.png"), 3);
	Waluizy->SetTexture(SIT_LEFT,
		_T("character\\WALUIZY\\waluizy_LEFTSIT.png"), 3);
	//FLY (날아가는 모션)
	Waluizy->SetTexture(FLY_RIGHT,
		_T("character\\WALUIZY\\waluizy_RIGHT_FLY.png"), 4);
	Waluizy->SetTexture(FLY_LEFT,
		_T("character\\WALUIZY\\waluizy_LEFT_FLY.png"), 4);
	//UP(일어나는 모션)
	Waluizy->SetTexture(UP_LEFT,
		_T("character\\WALUIZY\\waluizy_LEFTUP.png"), 2);
	Waluizy->SetTexture(UP_RIGHT,
		_T("character\\WALUIZY\\waluizy_RIGHTUP.png"), 2);
	//DYE
	Waluizy->SetTexture(DYE_LEFT,
		_T("character\\WALUIZY\\waluizy_LEFTDIE.png"), 3);
	Waluizy->SetTexture(DYE_RIGHT,
		_T("character\\WALUIZY\\waluizy_RIGHTDIE.png"), 3);
	//LOSE
	Waluizy->SetTexture(LOSE,
		_T("character\\WALUIZY\\waluizy_LOSE.png"), 2);
	//WIN
	Waluizy->SetTexture(WIN,
		_T("character\\WALUIZY\\waluizy_VICTORY.png"), 4);
	Waluizy->rank_state.Load("character\\WALUIZY\\waluizyUI.png");
	Waluizy->UI.Load("character\\WALUIZY\\waluizy_UI.png");
	//ADD CHANGE_EX
	Waluizy->SetTexture(CHANGE_EX_RIGHT,
		_T("character\\WALUIZY\\WALUIZY_RIGHT_CEX.png"), 2);
	Waluizy->SetTexture(CHANGE_EX_LEFT,
		_T("character\\WALUIZY\\WALUIZY_LEFT_CEX.png"), 2);
	Waluizy->SetSprite(_T("character\\fly_impact.png"),Waluizy->fly_paticle, 4);
	Waluizy->SetSprite(_T("character\\WALUIZY\\export_damagedEffect.png"),Waluizy->attack_paticle, 4);
	Waluizy->SetSprite(_T("character\\rotateStar.png"),Waluizy->Smash_Point, 12);
}// 캐릭터 스프라이트를 전부 읽어 들인후 해당 캐릭터로 배정
void SetPlayerChar(int Player1, int Player2)
{
	m_Player = new CPlayer*[4];

	// -1이면 1p mode 
	if (Player2 == ONE_PLAYER) mode = 1;

	//1. Mario
	if (mode == 1)
	{
		m_Player[0] = new CPlayer(26);
		m_Player[0]->SetPosition(-200, 300);
		m_Player[0]->SetStatus(BASIC_RIGHT);	//현재상태 셋팅 

		m_Player[1] = new CAIPlayer(26);
		m_Player[1]->SetPosition(200, 300);
		m_Player[1]->SetStatus(BASIC_LEFT);	//현재상태 셋팅 


		m_Player[2] = new CAIPlayer(26);
		m_Player[2]->SetPosition(0, 300);
		m_Player[2]->SetStatus(BASIC_LEFT);	//현재상태 셋팅 

		m_Player[3] = new CAIPlayer(26);
		m_Player[3]->SetPosition(100, 300);
		m_Player[3]->SetStatus(BASIC_LEFT);	//현재상태 셋팅 

		switch (Player1)
		{
		case 0:
			m_Player[0]->SetImage(Mario->GetImage());
			m_Player[0]->setting(Mario, 0);
			m_Player[1]->SetImage(Wario->GetImage());
			m_Player[1]->setting(Wario, 1);
			m_Player[2]->SetImage(Luizy->GetImage());
			m_Player[2]->setting(Luizy, 2);
			m_Player[3]->SetImage(Waluizy->GetImage());
			m_Player[3]->setting(Waluizy, 3);
			break;
		case 1:
			m_Player[1]->SetImage(Mario->GetImage());
			m_Player[1]->setting(Mario, 0);
			m_Player[2]->SetImage(Wario->GetImage());
			m_Player[2]->setting(Wario, 1);
			m_Player[0]->SetImage(Luizy->GetImage());
			m_Player[0]->setting(Luizy, 2);
			m_Player[3]->SetImage(Waluizy->GetImage());
			m_Player[3]->setting(Waluizy, 3);
			break;
		case 2:
			m_Player[1]->SetImage(Mario->GetImage());
			m_Player[1]->setting(Mario, 0);
			m_Player[0]->SetImage(Wario->GetImage());
			m_Player[0]->setting(Wario, 1);
			m_Player[2]->SetImage(Luizy->GetImage());
			m_Player[2]->setting(Luizy, 2);
			m_Player[3]->SetImage(Waluizy->GetImage());
			m_Player[3]->setting(Waluizy, 3);
			break;
		case 3:
			m_Player[0]->SetImage(Waluizy->GetImage());
			m_Player[0]->setting(Waluizy, 3);

			m_Player[1]->SetImage(Mario->GetImage());
			m_Player[1]->setting(Mario, 0);

			m_Player[3]->SetImage(Wario->GetImage());
			m_Player[3]->setting(Wario, 1);

			m_Player[2]->SetImage(Luizy->GetImage());
			m_Player[2]->setting(Luizy, 2);
			break;
		default:
			break;
		}

	}
	if (mode == 2)
	{
		m_Player[0] = new CPlayer(26);
		m_Player[0]->SetPosition(-200, 300);
		m_Player[0]->SetStatus(BASIC_RIGHT);	//현재상태 셋팅 

		m_Player[1] = new CPlayer(26);
		m_Player[1]->SetPosition(200, 300);
		m_Player[1]->SetStatus(BASIC_LEFT);	//현재상태 셋팅 


		m_Player[2] = new CAIPlayer(26);
		m_Player[2]->SetPosition(0, 300);
		m_Player[2]->SetStatus(BASIC_LEFT);	//현재상태 셋팅 

		m_Player[3] = new CAIPlayer(26);
		m_Player[3]->SetPosition(100, 300);
		m_Player[3]->SetStatus(BASIC_LEFT);	//현재상태 셋팅 

		bool bMario = false;
		bool bWario = false;
		bool bLuizy = false;
		bool bWaluizy = false;

		//--------2P셋팅
		for (int i = 0; i < 2; ++i)
		{

			switch (nowPlayer[i])
			{
			case 0:
				m_Player[i]->SetImage(Mario->GetImage());
				m_Player[i]->setting(Mario, 0);
				bMario = true;
				break;
			case 1:
				m_Player[i]->SetImage(Luizy->GetImage());
				m_Player[i]->setting(Luizy, 2);
				bLuizy = true;
				break;
			case 2:
				m_Player[i]->SetImage(Wario->GetImage());
				m_Player[i]->setting(Wario, 1);
				bWario = true;
				break;
			case 3:
				m_Player[i]->SetImage(Waluizy->GetImage());
				m_Player[i]->setting(Waluizy, 3);
				bWaluizy = true;
				break;
			default:
				break;
			}
		}

		//AI
		for (int i = 2; i < 4; ++i)
		{
			if (!bMario)
			{
				m_Player[i]->SetImage(Mario->GetImage());
				m_Player[i]->setting(Mario, 0);
				bMario = true;
			}

			else if (!bWario)
			{
				m_Player[i]->SetImage(Wario->GetImage());
				m_Player[i]->setting(Wario, 1);
				bWario = true;
			}
			else if (!bLuizy)
			{
				m_Player[i]->SetImage(Luizy->GetImage());
				m_Player[i]->setting(Luizy, 2);
				bLuizy = true;
			}

			else
			{
				m_Player[i]->SetImage(Waluizy->GetImage());
				m_Player[i]->setting(Waluizy, 3);
				bWaluizy = true;
			}
		}
	}
}
LRESULT CALLBACK WndProc(HWND hWnd, UINT iMsg, WPARAM wParam, LPARAM lParam)
{
	static RECT rect;
	static POINT point;
	HDC hdc;
	PAINTSTRUCT ps;
	static HFONT TimeFont = CreateFont(70, 0, 0, 0, FW_NORMAL, 0, 0, 0, ANSI_CHARSET, 0, 0, 0, 0, TEXT("HY헤드라인M"));
	static HFONT UIFont = CreateFont(15, 0, 0, 0, FW_NORMAL, 0, 0, 0, ANSI_CHARSET, 0, 0, 0, 0, TEXT("HY헤드라인M"));//문자체
	static HPEN hPen = CreatePen(PS_SOLID, 3, RGB(255, 0, 0));
	static HBRUSH hBrush = CreateSolidBrush(RGB(255, 0, 0)), hBrush2 = CreateSolidBrush(RGB(0, 255, 0));
	static RECT rectView;
	static int stage_view = 0;
	DWORD dwDirection = 0;
	static int down = 0;
	switch (iMsg) {
	case WM_CREATE:
		GetClientRect(hWnd, &rectView);
		Title.Load(TEXT("sub_image\\titleUI.png"));

		Choice_cha[0].Load(TEXT("sub_image\\choice1.png"));
		Choice_cha[1].Load(TEXT("sub_image\\choice2.png"));
		Choice_cha[2].Load(TEXT("sub_image\\choice3.png"));
		Choice_cha[3].Load(TEXT("sub_image\\choice4.png"));
		Ending.Load(TEXT("sub_image\\end.bmp"));
		Background.Load(TEXT("sub_image\\background.bmp"));
		mapEX[0].Load(TEXT("map\\map1\\map1.bmp"));
		mapEX[1].Load(TEXT("map\\map2\\map2.bmp"));
		mapEX[2].Load(TEXT("map\\map3\\map3.bmp"));
		mapEX[3].Load(TEXT("map\\map4\\map4.bmp"));
		mapEX[4].Load(TEXT("map\\map5\\map5.bmp"));
		mapEX[5].Load(TEXT("map\\map6\\map6.bmp"));
		mapEX[6].Load(TEXT("map\\map7\\map7.bmp"));
		mapEX[7].Load(TEXT("map\\map8\\map8.bmp"));
		mapEX[8].Load(TEXT("map\\map9\\map9.bmp"));
		mapEX[9].Load(TEXT("sub_image\\random_map.bmp"));
		demage_UI.Load(TEXT("sub_image\\demage_UI.png"));
		createSound();
		Rankstate.Load(TEXT("sub_image\\ranking.bmp"));//랭킹 배경 구현
		pChannel[0]->setVolume(stateVolume);
		pSystem->playSound(FMOD_CHANNEL_REUSE, stateSound[title], false, &pChannel[0]);

		pChannel[1]->setVolume(0.5);
		sel.x = 625;
		sel.y = 530;

		wsprintf(Playtime_t, TEXT("%d"), PlayTime);

		break;
	case WM_KEYDOWN:
		switch (state)
		{
		case title:
		{
			switch (wParam)
			{
			case VK_DOWN:
				sel.y += 80;
				if (sel.y > 620)
					sel.y = 530;
				break;
			case VK_UP:
				sel.y -= 80;
				if (sel.y < 530)
					sel.y = 610;
				break;
			case 'A':
			case 'S':
				if (sel.y < 550)mode = 1;
				else mode = 2;
				state = cho_map;
				pSystem->playSound(FMOD_CHANNEL_REUSE, stateSound[state], false, &pChannel[0]);
				sel.x = 140;
				sel.y = 550;
				map_stage = 1;
				pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
				break;
			default:
				break;
			}
		}
		break;
		case cho_map:
			if (mode == 1) {
				switch (wParam)
				{
				case VK_LEFT:
					pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
					sel.x -= 200;
					if (sel.x < 0)
						sel.x = 140 + 200 * 4;
					map_stage = ((sel.x - 140) / 100) + 1;
					if (sel.y == 650)
						map_stage += 1;
					if (map_stage == 10)
						SetTimer(hWnd, 1, 150, NULL);
					else
						KillTimer(hWnd, 1);
					break;

				case VK_RIGHT:
					pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
					sel.x += 200;
					if (sel.x > 140 + 200 * 4)
						sel.x = 140;
					map_stage = ((sel.x - 140) / 100) + 1;
					if (sel.y == 650)
						map_stage += 1;

					if (map_stage == 10)
						SetTimer(hWnd, 1, 150, NULL);
					else
						KillTimer(hWnd, 1);
					break;


				case VK_UP:
					pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
					sel.y -= 100;
					if (sel.y < 500)
						sel.y = 650;
					map_stage = ((sel.x - 140) / 100) + 1;
					if (sel.y == 650)
						map_stage += 1;
					if (map_stage == 10)
						SetTimer(hWnd, 1, 150, NULL);
					else
						KillTimer(hWnd, 1);
					break;
				case VK_DOWN:
					pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
					sel.y += 100;
					if (sel.y > 700)
						sel.y = 550;
					map_stage = ((sel.x - 140) / 100) + 1;
					if (sel.y == 650)
						map_stage += 1;
					if (map_stage == 10)
						SetTimer(hWnd, 1, 150, NULL);
					else
						KillTimer(hWnd, 1);
					break;

				case 'A':
				{
					state = cho_cha;
					map_stage = ((sel.x - 140) / 100) + 1;
					if (sel.y == 650)
						map_stage += 1;
					if (map_stage == 10)
						map_stage = rand() % 9 + 1;
					m.load(map_stage, rectView);
					sel.x = 50 + 125;
					sel.y = 175 + 120 + 25;

					nowPlayer[0] = MARIO;
					pSystem->playSound(FMOD_CHANNEL_REUSE, choiceSound, false, &pChannel[1]);

					KillTimer(hWnd, 1);

					break;
				}
				}
			}
			else {
				switch (wParam)
				{
				case 'A':
					pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
					sel.x -= 200;
					if (sel.x < 0)
						sel.x = 140 + 200 * 4;
					map_stage = ((sel.x - 140) / 100) + 1;
					if (sel.y == 650)
						map_stage += 1;
					if (map_stage == 10)
						SetTimer(hWnd, 1, 150, NULL);
					else
						KillTimer(hWnd, 1);
					break;
				case 'D':
					pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
					sel.x += 200;
					if (sel.x > 140 + 200 * 4)
						sel.x = 140;
					map_stage = ((sel.x - 140) / 100) + 1;
					if (sel.y == 650)
						map_stage += 1;

					if (map_stage == 10)
						SetTimer(hWnd, 1, 150, NULL);
					else
						KillTimer(hWnd, 1);
					break;
				case 'W':
					pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
					sel.y -= 100;
					if (sel.y < 500)
						sel.y = 650;
					map_stage = ((sel.x - 140) / 100) + 1;
					if (sel.y == 650)
						map_stage += 1;
					if (map_stage == 10)
						SetTimer(hWnd, 1, 150, NULL);
					else
						KillTimer(hWnd, 1);
					break;
				case 'S':
					pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
					sel.y += 100;
					if (sel.y > 700)
						sel.y = 550;
					map_stage = ((sel.x - 140) / 100) + 1;
					if (sel.y == 650)
						map_stage += 1;
					if (map_stage == 10)
						SetTimer(hWnd, 1, 150, NULL);
					else
						KillTimer(hWnd, 1);
					break;
				case 'F':
					state = cho_cha;
					map_stage = ((sel.x - 140) / 100) + 1;
					if (sel.y == 650)
						map_stage += 1;
					if (map_stage == 10)
						map_stage = rand() % 9 + 1;
					m.load(map_stage, rectView);
					sel.x = 50 + 125;
					sel.y = 175 + 120 + 25;
					sel2.x = 50 + 125 + 900;
					sel2.y = 175 + 120 + 25;
					nowPlayer[0] = MARIO;
					pSystem->playSound(FMOD_CHANNEL_REUSE, choiceSound, false, &pChannel[1]);

					KillTimer(hWnd, 1);

					break;
				}
			}

			break;
		case cho_cha:
			if (mode == 1) {
				switch (wParam)
				{
				case VK_LEFT:
					pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
					sel.x -= 250 + 50;
					--nowPlayer[0];
					if (sel.x < 0) {
						sel.x = 300 * 3 + 50 + 125;
						nowPlayer[0] = 3;
					}
					break;

				case VK_RIGHT:
					pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
					sel.x += 300;
					++nowPlayer[0];
					if (sel.x > 1200) {
						sel.x = 175;
						nowPlayer[0] = 0;
					}
					break;
				case 'A':
					pSystem->playSound(FMOD_CHANNEL_REUSE, choiceSound, false, &pChannel[1]);
					state = play;
					sel.x = 100 + 125 + 50;
					sel.y = 175 + 120 + 50;
					SetTimer(hWnd, 5, 1000, NULL);
					SetTimer(hWnd, 6, 200, NULL);
					pChannel[0]->stop();
					m.mapSystem->playSound(FMOD_CHANNEL_REUSE, m.mapSound, false, &pChannel[0]);
					//--------PLAYER SET--------//

					nPlayer = 4; // 현재 플레이하는 플레이어는 1명. 
					
					BuildPlayer();
					SetPlayerChar(nowPlayer[0], ONE_PLAYER);
					cam.setPos(m_Player[0]->GetPosition().x);

					wsprintf(Playtime_t, TEXT("%d"), PlayTime);
					SetTimer(hWnd, 0, 100, NULL);

				}
				break;

			}
			else {
				switch (wParam)
				{
				case 'A':
					if (Player1 == false) {
						pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
						sel.x -= 250 + 50;
						--nowPlayer[0];
						if (sel.x < 0) {
							sel.x = 300 * 3 + 50 + 125;
							nowPlayer[0] = 3;
						}
					}
					break;

				case 'D':
					if (Player1 == false) {
						pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
						sel.x += 300;
						++nowPlayer[0];
						if (sel.x > 1200) {
							sel.x = 175;
							nowPlayer[0] = 0;
						}
					}
					break;
				case 'F':
					pSystem->playSound(FMOD_CHANNEL_REUSE, choiceSound, false, &pChannel[1]);
					Player1 = true;
					break;
				case VK_LEFT:
					if (Player2 == false) {
						pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
						sel2.x -= 250 + 50;
						--nowPlayer[1];
						if (sel2.x < 0) {
							sel2.x = 300 * 3 + 50 + 125;
							nowPlayer[1] = 3;
						}
					}
					break;
				case VK_RIGHT:
					if (Player2 == false) {
						pSystem->playSound(FMOD_CHANNEL_REUSE, changeSound, false, &pChannel[1]);
						sel2.x += 300;
						++nowPlayer[1];
						if (sel2.x > 1200) {
							sel2.x = 175;
							nowPlayer[1] = 0;
						}
					}
					break;
				case VK_NUMPAD4:
					pSystem->playSound(FMOD_CHANNEL_REUSE, choiceSound, false, &pChannel[1]);
					Player2 = true;
					break;
				}
				if (Player1 == true && Player2 == true)
				{
					pChannel[0]->stop();
					m.mapSystem->playSound(FMOD_CHANNEL_REUSE, m.mapSound, false, &pChannel[0]);

					//--------PLAYER SET--------//
					state = play;
					SetTimer(hWnd, 5, 1000, NULL);
					SetTimer(hWnd, 6, 200, NULL);
					nPlayer = 4; // 현재 플레이하는 플레이어는 1명.
					BuildPlayer();
					SetPlayerChar(nowPlayer[0], nowPlayer[1]);
					cam.setPos(m_Player[0]->GetPosition().x, m_Player[1]->GetPosition().x);
					wsprintf(Playtime_t, TEXT("%d"), PlayTime);
					SetTimer(hWnd, 0, 100, NULL);
				}
				break;
			}
		case play:
			*pKeyBuffer = NULL;
			dwDirection = 0;
			GameReady = true;
			switch (wParam) {
			case 'Q':
				break;
			
			}
			if (GetKeyboardState(pKeyBuffer))
			{
				SetTimer(hWnd, 2, 16, NULL);//점프타이머
											//------PLAYER CHANGE------//
			}
			break;
		case ranking:
			if (mode == 1) {
				switch (wParam) {
				case 'A':
					state = cho_map;
					reset();
					KillTimer(hWnd, 0);
					pSystem->playSound(FMOD_CHANNEL_REUSE, stateSound[cho_map], false, &pChannel[0]);
					break;
				case 'S': {
					state = ending;
					pSystem->playSound(FMOD_CHANNEL_REUSE, stateSound[ending - 2], false, &pChannel[0]);
					break;
				}
				}
			}
			else {
				switch (wParam) {
				case 'F':
				case VK_NUMPAD4:
					state = cho_map;
					reset();
					KillTimer(hWnd, 0);
					pSystem->playSound(FMOD_CHANNEL_REUSE, stateSound[cho_map], false, &pChannel[0]);
					break;
				case 'G':
				case VK_NUMPAD5:
				{
					state = ending;
					pSystem->playSound(FMOD_CHANNEL_REUSE, stateSound[ending - 2], false, &pChannel[0]);
					break;
				}
				}
			}

			break;
		case ending:
			exit(1);
			break;

		default:
			break;
		}
		if (wParam == VK_ESCAPE)exit(1);
		//쓸때없는 코드 삭제
		InvalidateRect(hWnd, NULL, FALSE);
		break;

	case WM_KEYUP:
	{
		switch (state)
		{
		case play:
			if (mode == 1) {
				if (m_Player[0]->GetStatus() == MOVE_RIGHT)
				{
					m_Player[0]->SetStatus(BASIC_RIGHT);
					break;
				}
				if (m_Player[0]->GetStatus() == MOVE_LEFT)
				{
					m_Player[0]->SetStatus(BASIC_LEFT);
					break;
				}
				if (m_Player[0]->GetStatus() == DEFENSE_RIGHT)
				{
					m_Player[0]->SetStatus(BASIC_RIGHT);
					break;
				}
				if (m_Player[0]->GetStatus() == DEFENSE_LEFT)
				{
					m_Player[0]->SetStatus(BASIC_LEFT);
					break;
				}
			}
			else {
				switch (wParam)
				{
				case 'A':
				case 'D':
				case 'F':
				case 'G':
				case 'H':
				case 'T':
					if (m_Player[0]->GetStatus() == MOVE_RIGHT)
					{
						m_Player[0]->SetStatus(BASIC_RIGHT);
						break;
					}
					if (m_Player[0]->GetStatus() == MOVE_LEFT)
					{
						m_Player[0]->SetStatus(BASIC_LEFT);
						break;
					}
					if (m_Player[0]->GetStatus() == DEFENSE_RIGHT)
					{
						m_Player[0]->SetStatus(BASIC_RIGHT);
						break;
					}
					if (m_Player[0]->GetStatus() == DEFENSE_LEFT)
					{
						m_Player[0]->SetStatus(BASIC_LEFT);
						break;
					}
					break;
				case VK_LEFT:
				case VK_RIGHT:
				case VK_NUMPAD4:
				case VK_NUMPAD5:
				case VK_NUMPAD6:
				case VK_NUMPAD8:
					if (m_Player[1]->GetStatus() == MOVE_RIGHT)
					{
						m_Player[1]->SetStatus(BASIC_RIGHT);
						break;
					}
					if (m_Player[1]->GetStatus() == MOVE_LEFT)
					{
						m_Player[1]->SetStatus(BASIC_LEFT);
						break;
					}
					if (m_Player[1]->GetStatus() == DEFENSE_RIGHT)
					{
						m_Player[1]->SetStatus(BASIC_RIGHT);
						break;
					}
					if (m_Player[1]->GetStatus() == DEFENSE_LEFT)
					{
						m_Player[1]->SetStatus(BASIC_LEFT);
						break;
					}
					break;
				default:
					break;
				}
			}

		}
	}
	InvalidateRect(hWnd, NULL, FALSE);
	break;
	case WM_TIMER:
	{
		if (state == play)
		{
			if (mode == 1)
				cam.realsetPos(m_Player[0]->GetPosition().x);
			else if (mode == 2)
				cam.realsetPos(m_Player[0]->GetPosition().x, m_Player[1]->GetPosition().x);

			cam.add();
			static int count = 0;
			if (count >= 3) {
				setranking();
				count = 0;
				KillTimer(hWnd, 5);
				break;
			}
			count = 0;
			for (int i = 0; i < nPlayer; ++i)
			{
				if (m_Player[i]->AI() == true) {
					m_Player[i]->distance(m_Player, nPlayer);
				}
				m.collision(*m_Player[i]);
				m_Player[i]->Playercollision(m_Player, nPlayer);
				m_Player[i]->gravity();
				m_Player[i]->attack(m_Player, nPlayer);
				m_Player[i]->defance(m_Player, nPlayer);
				if (m_Player[i]->GetStatus() == FLY_RIGHT || m_Player[i]->GetStatus() == FLY_LEFT)
					m_Player[i]->smashing(m_Player[m_Player[i]->getAttacker()]->getDamege_num(), 20, true);
				else if (m_Player[i]->GetStatus() == DYE_RIGHT || m_Player[i]->GetStatus() == DYE_LEFT)
					m_Player[i]->smashing(m_Player[m_Player[i]->getAttacker()]->getDamege_num(), 10, false);

				if (m_Player[i]->GetPosition().y > 1000 && m_Player[i]->live == true) {
					m_Player[i]->live = false;
					m_Player[i]->charSystem->playSound(FMOD_CHANNEL_REUSE, m_Player[i]->charSound[3], false, &m_Player[i]->pChannel);
					m_Player[i]->PlayTime_num = 99 - PlayTime;
				}
				if (m_Player[i]->live == false)++count;
				m_Player[i]->printdamege();
				m_Player[i]->smashadd();
			}
		}


		switch (wParam)
		{
		case 1:
			stage_view = rand() % 9;
			break;

		case 0:
			for (int i = 0; i < nPlayer; ++i)
				m_Player[i]->Timer();
			break;
		case 5: {
			--PlayTime;
			wsprintf(Playtime_t, TEXT("%d"), PlayTime);
			if (PlayTime == 0) {
				setranking();

				KillTimer(hWnd, 5);

			}
		}
				break;
		case 6: {

			if (m_Player != nullptr) {
				if (mode == 2) {
					m_Player[2]->KeyState(cam, state, 2);
					m_Player[3]->KeyState(cam, state, 2);
				}
				else
				{
					m_Player[1]->KeyState(cam, state, 2);
					m_Player[2]->KeyState(cam, state, 2);
					m_Player[3]->KeyState(cam, state, 2);
				}
			}
		}
		}
		InvalidateRect(hWnd, NULL, FALSE);
	}

	case WM_PAINT:
	{
		hdc = BeginPaint(hWnd, &ps);
		CImage img;
		img.Create(rectView.right, rectView.bottom, 24);
		HDC memDC = img.GetDC();

		switch (state)
		{
		case title:

			Title.Draw(memDC, 0, 0, rectView.right, rectView.bottom);
			RECT selectRC;

			selectRC.left = -150 + sel.x;
			selectRC.top = -40 + sel.y;
			selectRC.right = 150 + sel.x;
			selectRC.bottom = 40 + sel.y;
			FrameRect(memDC, &selectRC, hBrush);

			break;
		case cho_map: {
			RECT rc;
			RECT rc2;
			RECT selectRC;

			selectRC.left = -80 + sel.x + 100 - 5;
			selectRC.top = -45 + sel.y - 5;
			selectRC.right = 80 + sel.x + 100 + 5;
			selectRC.bottom = 45 + sel.y + 5;
			Background.Draw(memDC, 0, 0);

			rc2.left = -400;
			rc2.top = -225;
			rc2.right = 400;
			rc2.bottom = 225;
			rc.left = -80;
			rc.top = -45;
			rc.right = 80;
			rc.bottom = 45;
			SelectObject(memDC, hBrush);
			Rectangle(memDC, selectRC.left, selectRC.top, selectRC.right, selectRC.bottom);
			if (map_stage == 10) {
				mapEX[stage_view].Draw(memDC, rectView.right / 2 + rc2.left, rectView.bottom / 2 + rc2.top - 100, 800, 450, 0, 0, 800, 450);
			}
			else
				mapEX[map_stage - 1].Draw(memDC, rectView.right / 2 + rc2.left, rectView.bottom / 2 + rc2.top - 100, 800, 450, 0, 0, 800, 450);
			for (int i = 0; i < 10; ++i) {
				if (i % 2 == 0) {
					mapEX[i].Draw(memDC, (i / 2)*(40 + 160) + rc.left + 80 + 60 + 100, 650 + rc.top - 100, 160, 90, 0, 0, 800, 450);
				}
				else {
					mapEX[i].Draw(memDC, (i / 2)*(40 + 160) + rc.left + 80 + 60 + 100, 650 + rc.top, 160, 90, 0, 0, 800, 450);
				}
			}
			break; }
		case cho_cha: {
			RECT selectRC;
			Background.Draw(memDC, 0, 0);
			SelectObject(memDC, hBrush);
			selectRC.left = -125 + sel.x - 10;
			selectRC.top = -200 + sel.y - 10;
			selectRC.right = 125 + sel.x + 10;
			selectRC.bottom = 200 + sel.y + 10;
			Rectangle(memDC, selectRC.left, selectRC.top, selectRC.right, selectRC.bottom);
			if (mode == 2) {
				RECT selectRC2;
				selectRC2.left = -125 + sel2.x - 10;
				selectRC2.top = -200 + sel2.y - 10;
				selectRC2.right = 125 + sel2.x + 10;
				selectRC2.bottom = 200 + sel2.y + 10;
				SelectObject(memDC, hBrush2);
				Rectangle(memDC, selectRC2.left, selectRC2.top, selectRC2.right, selectRC2.bottom);
			}
			for (int i = 0; i < 4; ++i) {
				Choice_cha[i].Draw(memDC, 50 + i*(250 + 50), 120, 250, 400);
			}
			break;
		}
		case play:
			m.draw(memDC, rectView, cam, map_stage);
			SelectObject(memDC, UIFont);
			for (int i = 0; i < nPlayer; ++i)
			{

				m_Player[i]->DrawSprite(memDC,
					m_Player[i]->m_ppTexture[m_Player[i]->m_State].nSpriteCurrent, cam);
				
			
				m_Player[i]->Draw_Impact(memDC, cam);
				m_Player[i]->DrawUI(memDC, i);
				demage_UI.TransparentBlt(memDC, 140 + i * 300, 620, 150, 150, 0, 0, 170, 170, RGB(255, 255, 255));
				SetBkMode(memDC, TRANSPARENT);
				TextOut(memDC, 200 + i * 300 - 10, 690, TEXT(m_Player[i]->getDamege()), strlen(m_Player[i]->getDamege()));
			}


			SelectObject(memDC, TimeFont);
			SetBkMode(memDC, TRANSPARENT);
			TextOut(memDC, 650, 20, TEXT(Playtime_t), strlen(Playtime_t));
			break;
		case ranking:
			Rankstate.Draw(memDC, 0, 0, rectView.right, rectView.bottom);
			for (int i = 0; i < nPlayer; ++i) {
				m_Player[i]->rank_state.Draw(memDC, 100, 180 * i, 1080, 200, 0, 0, 550, 115);
				m_Player[i]->DrawSprite(memDC, m_Player[i]->m_ppTexture[m_Player[i]->m_State].nSpriteCurrent, 150, 50 + 180 * i);
				SetTextColor(memDC, RGB(255, 255, 255));
				SetBkMode(memDC, TRANSPARENT);
				TextOut(memDC, 300, 50 + 195 * i, TEXT(m_Player[i]->damage), strlen(m_Player[i]->damage));
				TextOut(memDC, 600, 50 + 195 * i, TEXT(m_Player[i]->PlayTime), strlen(m_Player[i]->PlayTime));
				TextOut(memDC, 820, 50 + 195 * i, TEXT(m_Player[i]->total_score), strlen(m_Player[i]->total_score));
				TextOut(memDC, 1100, 50 + 195 * i, TEXT(m_Player[i]->ranking), strlen(m_Player[i]->ranking));
			}
			SetTextColor(memDC, RGB(0, 0, 0));
			break;
		case ending:
			Ending.Draw(memDC, -10, 0, rectView.right, rectView.bottom);
			break;

		default:
			break;
		}
		img.Draw(hdc, 0, 0);
		img.ReleaseDC();

		EndPaint(hWnd, &ps);
	}
	break;


	case WM_DESTROY:
		KillTimer(hWnd, 1);
		pSystem->release();
		//	시스템 닫기
		pSystem->close();
		PostQuitMessage(0);
		return 0;
	}
	return DefWindowProc(hWnd, iMsg, wParam, lParam);
}