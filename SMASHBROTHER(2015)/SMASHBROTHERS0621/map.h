#pragma once
#include "stdafx.h"
#include "Camera.h"
#include "Player.h"

//�� ��ü ���� 6.15
class Tile {
private:
	CImage tileimage;//�� Ÿ���̹���
	int object_num;//Ÿ���� ��
	POINT *object_pos;//�׷����� ��ǥ
					  //	POINT *object_realpos;//�浹�� ����
	RECT *object_realpos;//�浹�� ���� �ٿ�� �ڽ� ����
	POINT size;//�׷����� ������Ʈ�� ���� ���� ����
	POINT realsize;//�ٿ�� �ڽ��� ���� ���� ����
	int downbalance;//�ٿ�� �ڽ��� �׷����� ������Ʈ�� ���� ������

public:
	Tile() {}
	void load(LPCTSTR pstname) { tileimage.Load(pstname); }//�̹��� �ε�
	void setPos(int x, int y, int num) {
		object_pos[num].x = x; object_pos[num].y = y;//���� �̹����� Ÿ�ϵ��� ��ǥ ����
	}
	POINT getPos(int num) { return object_pos[num]; }//�ش� �ε��� Ÿ���� ��ǥ
	void setobject(int num);//������Ʈ�� ����ŭ ����
	void draw(HDC hdc, CCamera cam);//�ش� Ÿ�ϵ��� �׷�����
	void setsize(int xsize, int ysize) { size.x = xsize; size.y = ysize; }//������Ʈ�� ���μ��� ���� ����
	void setRealsize(int xsize, int ysize, int balance) {//�ٿ�� �ڽ��� ���μ��� ���� ������ ���� ������Ʈ���� ������ ����
		realsize.x = xsize, realsize.y = ysize;
		downbalance = balance;
	}
	RECT collisionPos(int num) { return object_realpos[num]; }//�ٿ�� �ڽ��� ���� ��ǥ ���
	int Get_ob_num() { return object_num; }//���� Ÿ�� ������Ʈ�� �� ���
	~Tile() {}
};
class map
{
private:
	CImage background;//���
	Tile* tiles;//�� ���� �ٸ� ������ Ÿ��
	int obnum;//�ٸ� ������ Ÿ�� ��

public:
	System*	mapSystem;//
	Sound* mapSound;//fmod�� �ǰ��� ����� �ý��� 
	map();
	void draw(HDC hdc, RECT rectView, CCamera cam, int stage);//���� �ش� Ÿ�ϵ��� �׷���
	void load(int stage, RECT rectView);//�ش� ���� ��� ���� Ÿ��, ���带 �ҷ�����
	void collision(CPlayer& player);//�ʰ� �÷��̾��� �浹üũ
	void release() {//���÷��� �ø� ���� ��ϵ� �ʵ����͸� �ʱ�ȭ
		delete[] tiles;
		background.Destroy();

	}
	~map();
};
