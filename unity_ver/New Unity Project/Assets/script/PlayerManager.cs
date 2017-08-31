using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerManager : MonoBehaviour
{
    Animator anim;

	void Start ()
    {
        anim = GetComponent<Animator>();
	}

	void Update ()
    {
        // 왼쪽 입력 시에
		if (Input.GetKeyDown (KeyCode.LeftArrow))
        {
            Vector2 scale = transform.localScale;
            scale.x = Mathf.Abs(scale.x);
            transform.localScale = scale;

            anim.SetInteger("state", 1);
        }
        if (Input.GetKeyUp (KeyCode.LeftArrow))
        {
            Vector2 scale = transform.localScale;
            scale.x = Mathf.Abs(scale.x);
            transform.localScale = scale;

            anim.SetInteger("state", 0);
        }

        // 오른쪽 입력 시에
        if (Input.GetKeyDown(KeyCode.RightArrow))
        {
            Vector2 scale = transform.localScale;
            scale.x = -Mathf.Abs(scale.x);
            transform.localScale = scale;

            anim.SetInteger("state", 1);
        }
        if (Input.GetKeyUp(KeyCode.RightArrow))
        {
            Vector2 scale = transform.localScale;
            scale.x = -Mathf.Abs(scale.x);
            transform.localScale = scale;

            anim.SetInteger("state", 0);
        }

    }
}
