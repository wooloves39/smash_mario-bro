using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Camera_Script : MonoBehaviour {
    POINT pos;//실제 카메라 위치
    POINT realpos;//카메라가 도착해야할 위치
                  // Use this for initialization
    void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
   
    void SetPos(int x)
    {
        if (x <= -700 || x >= 700) ;
        else
        {
            pos.x = x * (1280 / 940) - 640; pos.y = 0;
            realpos.x = x * (1280 / 940) - 640; realpos.y = 0;
        }
    }
    //1p모드에서 사용하는 카메라, 1p의 초기값을 설정
    POINT GetPos()
    {
        return pos;//카메라의 포지션을 출력
    }
    void RealsetPos(int x)
    {
        if (x <= -700 || x >= 700) ;
        else
        {
            realpos.x = x * (1280 / 940) - 640; realpos.y = 0;
        }
    }//1p 카메라가 움직일 위치를 저장
    void Add()
    {
        if ((realpos.x != pos.x))
            pos.x += (realpos.x - pos.x) / 20;
    } //1프레임당 움직일  위치의 1/10만큼 실제 카메라에 더한다. 이는 카메라가 완전히 붙어 있는것보다 게임의 효과를 증대시킬수 있는 요소로 작용한다. 카메라 웍이라 칭함
}
