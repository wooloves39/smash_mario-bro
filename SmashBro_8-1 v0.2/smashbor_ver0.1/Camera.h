#pragma once
#include "stdafx.h"

class CCamera//2d ȭ�鿡 ī�޶� ����� ����ǥ�� ���� ��ǥ�� ���� ȭ���� �ٲ���� ���� ������Ʈ�� ��ǥ�� �����ϴ�.
{
private:
	POINT pos;//���� ī�޶� ��ġ
	POINT realpos;//ī�޶� �����ؾ��� ��ġ
	int mode;//�÷��� ��� 1p 2p
public:
	void setCam(int game_mode) {
		mode = game_mode;//��带 ���Ѵ� 1p�� 2p�� ī�޶� ���°� �ٸ���. 1p�� ī�޶� ���ӵǾ� ���󰡸�, 2p���� 1p�� 2p�� ������ ī�޶� ��ġ�Ѵ�.
	}
	void setPos(int x);//1p��忡�� ����ϴ� ī�޶�, 1p�� �ʱⰪ�� ����
	void setPos(int x1, int x2);//2p��忡�� ����ϴ� ī�޶�, 1p�� 2p ī�޶��� �ʱⰪ�� ����
	POINT getPos() {
		return pos;//ī�޶��� �������� ���
	}
	void realsetPos(int x);//1p ī�޶� ������ ��ġ�� ����
	void realsetPos(int x1, int x2);//2p ī�޶� ������ ��ġ�� ����
	void add(); //1�����Ӵ� ������  ��ġ�� 1/10��ŭ ���� ī�޶� ���Ѵ�. �̴� ī�޶� ������ �پ� �ִ°ͺ��� ������ ȿ���� �����ų�� �ִ� ��ҷ� �ۿ��Ѵ�. ī�޶� ���̶� Ī��
	CCamera() {};
	~CCamera() {};
};

