#pragma once
#include "stdafx.h"
#include "Camera.h"
#include "Player.h"

//맵 전체 보완 6.15
class Tile {
private:
	CImage tileimage;//맵 타일이미지
	int object_num;//타일의 수
	POINT *object_pos;//그려지는 좌표
					  //	POINT *object_realpos;//충돌할 영역
	RECT *object_realpos;//충돌할 영역 바운딩 박스 영역
	POINT size;//그려지는 오브젝트의 가로 세로 길이
	POINT realsize;//바운딩 박스의 가로 세로 길이
	int downbalance;//바운딩 박스와 그려지는 오브젝트의 높이 벨런스

public:
	Tile() {}
	void load(LPCTSTR pstname) { tileimage.Load(pstname); }//이미지 로드
	void setPos(int x, int y, int num) {
		object_pos[num].x = x; object_pos[num].y = y;//같은 이미지의 타일들의 좌표 설정
	}
	POINT getPos(int num) { return object_pos[num]; }//해당 인덱스 타일의 좌표
	void setobject(int num);//오브젝트의 수많큼 생성
	void draw(HDC hdc, CCamera cam);//해당 타일들을 그려낸다
	void setsize(int xsize, int ysize) { size.x = xsize; size.y = ysize; }//오브젝트의 가로세로 길이 설정
	void setRealsize(int xsize, int ysize, int balance) {//바운딩 박스의 가로세로 길이 설정과 실제 오브젝트와의 높이차 설정
		realsize.x = xsize, realsize.y = ysize;
		downbalance = balance;
	}
	RECT collisionPos(int num) { return object_realpos[num]; }//바운딩 박스의 점의 좌표 출력
	int Get_ob_num() { return object_num; }//같은 타일 오브젝트의 수 출력
	~Tile() {}
};
class map
{
private:
	CImage background;//배경
	Tile* tiles;//각 맵의 다른 모형의 타일
	int obnum;//다른 모형의 타일 수

public:
	System*	mapSystem;//
	Sound* mapSound;//fmod의 의거한 사운드와 시스템 
	map();
	void draw(HDC hdc, RECT rectView, CCamera cam, int stage);//배경과 해당 타일들을 그려냄
	void load(int stage, RECT rectView);//해당 맵의 모든 배경과 타일, 사운드를 불러들임
	void collision(CPlayer& player);//맵과 플레이어의 충돌체크
	void release() {//리플레이 시를 위해 등록된 맵데이터를 초기화
		delete[] tiles;
		background.Destroy();

	}
	~map();
};
