#pragma once
#include "stdafx.h"
#include"Camera.h"

typedef struct Image
{
	CImage Texture;
	UINT	nSpriteCount;		// ��������Ʈ ��ü �ε���
	UINT	nSpriteCurrent;		// ���� ��������Ʈ �ε���
	UINT	g_nSpriteX;			// ��������Ʈ ����
	UINT	g_nSpriteY;			// ��������Ʈ ����
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
	POINT m_Position;//�÷��̾��� ��ǥ
	POINT m_Velocity;//���ӵ�

	POINT m_dirRight;//�÷��̾��� ����
	player_options Player_option;
	int nTexture;//��������Ʈ�� ����
				 //���� ���� ������ �� ��ġ
	int smash_point = 2;
	int gage = 0;
	//���Ѱ����� ���⳪ �Ϲݰ��ݿ� ���� �������� ä������ �������� 10�� ������ ���Ž� �����Ͱ� 1�� �����. �ִ� ���� 3��
public:

	Image	*m_ppTexture;//��������Ʈ
	UINT attack_SpriteCount;//������ �÷��̾��� ��ü �ε��� �ѹ�
	UINT attack_SpriteCurrent;//������ �÷��̾��� �� ���� �ε��� �ѹ�
	bool impact = false;//���� �������� �ƴ���
	bool impact_de = false;//�÷��̾ ���� ���µ��� �ش� ���ݿ� �� �̻� �浹üũ�� ���� �ʴ� ����
	CImage rank_state;//��ŷ UI
	CImage UI;//�÷��̽� ���̴� UI
	CImage GageUI;
	CImage GageBackUI;
	int Point_sprite_index = 0;
	DWORD	m_State;			//������� 
	DWORD	m_BeforeState;		//�����ǻ��� 
	int		DIR;	//Ű�Է¿� ���� ����
	int		n_AttackCount;//�Ϲ� ���� ������ 1��2�� ����, ���ۿ� ���� ��������Ʈ ���� ����

	bool	FrameEnd;//�������� ������ ���۵��� �˸��� ����
	bool	m_bJump = false;//���������� �ƴ��� �Ǻ��ϴ� ���

	System*	charSystem;
	Channel* pChannel;
	Sound* charSound[6];//fmod�� �ǰ��� ����� ä�� ���Ѱ���, ���� ����, ����, ���Ѱ����� ������, ���Ѱ����� ������, ���� ��
	int JumpCount = 0;//������ �ִ� 2�� ����
	bool fly = false;//���Ѱ����� �¾� ���ư��� ���������� ���� ����
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
		// 0605. ��������Ʈ �÷��ִ� Ÿ�̸Ӹ� Player Ŭ�����ȿ� ������������? 
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
	CPlayer(int nStatus);//���� �� �ʱ�ȭ
	~CPlayer();
	UINT Get_SPcount() { return m_ppTexture->nSpriteCount; }//���� ���� �ε��� �ѹ�
	UINT Get_SPcurrent() { return m_ppTexture->nSpriteCurrent; }//��ü ���� �ε��� �ʹ�
	void SetStatus(int state);//������ ��Ȳ�� �´� ��������Ʈ�� �����Ѵ�.
	DWORD GetStatus(void) { return m_State; };//��������Ʈ�� ���� ����Ѵ�.
	void SetPosition(float x, float y) {//�ʱ� ������ ���� ����
		m_Position.x = x; m_Position.y = y;
	};
	void SetTexture(int nIndex, LPCTSTR pCImage, int nSpriteCount);//�ؽ�ó���� �����Ѵ�.
	POINT GetPosition() { return m_Position; };//������ ���
	void Move(DWORD dwDirection, float fDistance);//�ش���⿡ �ش� �Ÿ���ŭ �̵��Ѵ�.
	void MoveX(const POINT& d3dxvShift);//�ش� �������� x�� ��ŭ x���ӵ��� �����Ѵ�.
	void MoveY(const POINT& d3dxvShift);//�ش� �������� y�� ��ŭ y���ӵ��� �����Ѵ�.
	void MoveY(const float Yshift);//�ش� ������ŭ y���ӵ��� �����Ѵ�.
	void StateChangeX(void) { m_Velocity.x = 0.0; }//x���ӵ��� 0���� �ʱ�ȭ
	void StateChangeY() {
		m_Velocity.y = 0.0;//y���ӵ��� 0���� �ʱ�ȭ
	}
	void DrawSprite(HDC hDC, int g_nSpriteCurrent, CCamera cam);//play state���� �׷����°Ϳ� ���� �Լ�
	void DrawSprite(HDC hDC, int g_nSpriteCurrent, int x, int y);//ranking state���� �׷����� �͵鿡 ���� �Լ�
	void JumpTimer(void);//���� �� ���ӵ��� ��ġ�� ��ȭ
	void SetBasic(int State);//�ش� �������� ��� ��������Ʈ�� ��ȯ
	void Render(HDC hDC);//�ش� ��������Ʈ�� �ش� �ε����� �׷���
						 //-------------- �����߰� ���⼭���� ----------//
	void SetImage(Image* Image) {//�̹����� �ʱ�ȭ
		m_ppTexture = Image;

	};
	Image* GetImage(void) { return m_ppTexture; };//�ش��̹����� ���
	int damage_num = 100;//�ʱ� ���ݷ�
public:
	char damage[3];//ȭ�鿡 ��µǱ� ���� ����
	int PlayTime_num;//�÷��� �ð�
	char PlayTime[3];//�÷��� �ð��� ����ϱ� ���� ����
	int ranking_num;//��ŷ
	char ranking[2];//��ŷ�� ����ϱ� ���� ����
	int total_score_num;// �÷��� �ð��� �ʱ� ���ݷ��� ����� ��ü ����
	char total_score[10];   //���� �����Ϳ� ��ü ����, ��ŷ->text�� �׷��� �ϱ⶧���� ���� �ε����� ����

	int low_power = 12;//���� ������ ������
	int High_power = 30;  //���� ������ ���� ������ ���� power
	int attacker_num;//���� �÷��̾��� �ѹ�
	bool mapobject_collsion = false;//�ʰ� �浹������ �ƴ��� �Ǻ�
	bool sma = false;//���޽� ������ ���� �������� �ƴ��� �Ǻ�
	bool hit = false;//������ �¾Ƽ� ó���ϴ� ���� ���ư��� ����
	bool down = false;//�Ʒ� Ű�� ������ �������� ����� �� ���� �̿ϼ�
	bool live = true;//ȭ�鿡�� ���� �Ÿ� ������ ������ �״� ���
					 //----------- FUNCTION	DEFINE -------------//

public:
	virtual void KeyState(CCamera& cam, int state, int mode = 1, int player = 1);//Ű�Է��� ���� �Լ� /*���� �ּ� ó��*/
	int getVelocity() { return m_Velocity.x; }//x���� ���ӵ��� ���
	int getDamege_num() { return damage_num; }//������ ���� ���
	void printdamege() { wsprintf(damage, TEXT("%d"), damage_num); }//������ ���� �Լ� �������� �÷��� ���� ���ŵȴ�.
	char* getDamege() { return damage; }//������ ���� ���
	double Vx(double P) { return sin(45 * RAD)*P; }
	double Vy(double P) { return cos(45 * RAD)*P; }//���� �������� 45������ ������.
	void smashing(int damage, int power, bool smash);//���޽� ������ �¾� ó���ϴ� �Լ� �����Ӹ��� �߷°� ��������, ��������Ʈ�� ������ �޴´�.
	void gravity(void);//�������� �ʰ��浹���� ���� �� �߻��ϴ� �߷��� ó��

	int getAttacker() { return attacker_num; }//�����ڸ� �˰��ϴ� �Լ�
	void text_rank() {//ranking ȭ���� �Ǿ����� Ȱ��ȭ�Ǵ� �Լ�
		wsprintf(PlayTime, TEXT("%d"), PlayTime_num);
		wsprintf(ranking, TEXT("%d"), ranking_num);
		wsprintf(total_score, TEXT("%d"), total_score_num);
	}
	void attack(CPlayer **other, int player_num);//�����ڰ� ����ڸ� �Ǵ��ϰ�, �ش� �������� ��� ���� ó���� �ϴ� �Լ�
	void defance(CPlayer **other, int player_num);//�Ϻ� ������ �浹üũ 1���� �ϰ� ���� ����ڰ� �����ڸ� �Ǵ��ϰ� �ش� ������ ����, ó���� �ϴ� �Լ�
												  //bool Lowhit_num(); ���ϰ� �´� ��ŸȽ���� ����� ����
	int getSmashpoint() { return smash_point; }//�÷��̾ ������ �ִ� ���Ž� �����͸� ���
	void smashsub() { if (smash_point > 0)--smash_point; }//���� �������� ���Ž� ������ 1��������
	void smashadd() {
		if (gage >= 10) {
			if (smash_point < Player_option.smash_count)
				++smash_point;
			gage = 0;
		}
	}//�������� 10�̻��Ͻ� ���Ž� �����͸� 1���� ����
	int getgage() { return gage; }//������ ���
	virtual void Playercollision(CPlayer **other, int player_num);//�÷��̾� ���� �浹 �� ������ �о�� �Լ�
	void release() {//���÷��� �� �����ϵ��� ������ �ʱ�ȭ
		delete[] m_ppTexture;
		rank_state.Destroy();
		UI.Destroy();

	}
	virtual bool AI() {//AI���� �÷��̾����� �Ǻ��ϴ� �Լ�
		return false;
	}
	virtual void distance(CPlayer **other, int player_num) {}//AI����Լ�
	void Timer(void);//��������Ʈ �ε��� ó���Լ�

};
class CAIPlayer
	:public CPlayer
{
public:

	bool road_algo = false;//��밡 ������ �Ʒ��� �������� ���� ��ã�� �˰��� ����
	CPlayer* target;//Ÿ������ �� �÷��̾�
	bool targeting = false;//Ÿ������ �Ǿ����� �ƴ��� �Ǻ�

public:

	CAIPlayer(int nStatus);
	virtual void KeyState(CCamera& cam, int state, int mode = 1, int player = 1);//Ÿ���ÿ� ���� ����
																				 //���������� ���ϱ�

	virtual void distance(CPlayer **other, int player_num);//����� �Ÿ� �ľ�, Ÿ����
	virtual bool AI() {//AI���� �ƴ��� �Ǻ�
		return true;
	}
};

