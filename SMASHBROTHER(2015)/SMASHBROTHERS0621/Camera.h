#pragma once
#include "stdafx.h"

class CCamera//2d 화면에 카메라를 만든다 모델좌표에 월드 좌표를 더해 화면이 바뀌더라도 실제 오브젝트의 좌표는 일정하다.
{
private:
	POINT pos;//실제 카메라 위치
	POINT realpos;//카메라가 도착해야할 위치
	int mode;//플레이 모드 1p 2p
public:
	void setCam(int game_mode) {
		mode = game_mode;//모드를 정한다 1p와 2p의 카메라 형태가 다르다. 1p는 카메라에 종속되어 따라가며, 2p모드는 1p와 2p의 중점에 카메라가 위치한다.
	}
	void setPos(int x);//1p모드에서 사용하는 카메라, 1p의 초기값을 설정
	void setPos(int x1, int x2);//2p모드에서 사용하는 카메라, 1p와 2p 카메라의 초기값을 설정
	POINT getPos() {
		return pos;//카메라의 포지션을 출력
	}
	void realsetPos(int x);//1p 카메라가 움직일 위치를 저장
	void realsetPos(int x1, int x2);//2p 카메라가 움직일 위치를 저장
	void add(); //1프레임당 움직일  위치의 1/10만큼 실제 카메라에 더한다. 이는 카메라가 완전히 붙어 있는것보다 게임의 효과를 증대시킬수 있는 요소로 작용한다. 카메라 웍이라 칭함
	CCamera() {};
	~CCamera() {};
};

