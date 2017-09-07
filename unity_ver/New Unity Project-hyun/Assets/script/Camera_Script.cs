using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Camera_Script : MonoBehaviour {
    POINT realpos;//카메라가 도착해야할 위치
                  // Use this for initialization
   private GameObject target;
    void Start () {
        target = GameObject.FindGameObjectWithTag("Player");
        SetPos(target);
	}
	
	// Update is called once per frame
	void Update () {
        if (target == null)
        {
            target = GameObject.FindGameObjectWithTag("Player");
            SetPos(target);
        }
        else
        {
            RealsetPos();
            Add();
        }
	}
   
    void SetPos(GameObject target)
    {
        if (target.transform.position.x <= -11 || target.transform.position.x >= 11) ;
        else
        {
          
            realpos.x = target.transform.position.x; realpos.y = 0;
            Vector3 camera_pos = new Vector3(target.transform.position.x, 0,-10);
            transform.position = camera_pos;
        }
    }
    //1p모드에서 사용하는 카메라, 1p의 초기값을 설정
    POINT GetrealPos()
    {
        return realpos;//카메라의 포지션을 출력
    }
    void RealsetPos()
    {
        if (target.transform.position.x <= -11 || target.transform.position.x >= 11) ;
        else
        {
            realpos.x = target.transform.position.x; realpos.y = 0;
        }
    }//1p 카메라가 움직일 위치를 저장
    void Add()
    {
        if ((realpos.x != transform.position.x))
            transform.Translate((realpos.x - transform.position.x) / 20,0,0);
    } //1프레임당 움직일  위치의 1/10만큼 실제 카메라에 더한다. 이는 카메라가 완전히 붙어 있는것보다 게임의 효과를 증대시킬수 있는 요소로 작용한다. 카메라 웍이라 칭함
}
