using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerManager : MonoBehaviour
{
    Animator anim;

    public float MoveSpeed;

	void Start ()
    {
        Screen.SetResolution(1280, 720, true); // 플레이 해상도 고정
        anim = GetComponent<Animator>(); // 스프라이트 재생용 애니메이터 생성
	}

	void Update ()
    {
        // 왼쪽 입력 시에
        if (Input.GetKeyDown (KeyCode.LeftArrow))
        {
            Vector3 scale = transform.localScale;
            scale.x = Mathf.Abs(scale.x);
            transform.localScale = scale;

            anim.SetInteger("state", 1);
        }
        if (Input.GetKeyUp (KeyCode.LeftArrow))
        {
            Vector3 scale = transform.localScale;
            scale.x = Mathf.Abs(scale.x);
            transform.localScale = scale;

            anim.SetInteger("state", 0);
        }

        // 오른쪽 입력 시에
        if (Input.GetKeyDown(KeyCode.RightArrow))
        {
            Vector3 scale = transform.localScale;
            scale.x = -Mathf.Abs(scale.x);
            transform.localScale = scale;

            anim.SetInteger("state", 1);
        }
        if (Input.GetKeyUp(KeyCode.RightArrow))
        {
            Vector3 scale = transform.localScale;
            scale.x = -Mathf.Abs(scale.x);
            transform.localScale = scale;

            anim.SetInteger("state", 0);
        }

    }
}
