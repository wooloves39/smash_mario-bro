using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerManager : MonoBehaviour
{
    private float movePower = 2.0f;
    private float jumpPower = 12.0f;
    public int maxHealth = 1;

    public bool inputLeft = false;
    public bool inputRight = false;
    public bool inputJump = false;
    public bool inputGard = false;
    public bool inputNormalAtt = false;
    public bool inputHardAtt = false;

    Rigidbody2D rigid;
    Animator animator;

    Vector3 movement;
    bool isDie = false;
    bool isJumping = false;

    float AttackTime = 0; // 연속공격을 위함이다

    int health = 1;

	void Start ()
    {
        Screen.SetResolution(1280, 720, true); // 플레이 해상도 고정
        animator = gameObject.GetComponentInChildren<Animator>();
        rigid = gameObject.GetComponent<Rigidbody2D>();

        health = maxHealth;

        UIButtonManager ui = GameObject.FindGameObjectWithTag("Manager").GetComponent<UIButtonManager>();
        ui.Init();
    }

	void Update ()
    {
        if (health == 0)
        {
            if(!isDie)
            {
                Die();
            }
            return;
        }


        // UI조작임
        if ((!inputRight && !inputLeft))
        {
            animator.SetBool("isMoving", false);
        }

        else if (inputLeft)
        {
            animator.SetBool("isMoving", true);

            transform.localScale = new Vector3(2, 2, 2);
        }

        else if (inputRight)
        {
            animator.SetBool("isMoving", true);

            transform.localScale = new Vector3(-2, 2, 2);
        }
        
        if (!inputGard) // 가드 입력이 안 된 상태라면 반환
        {
            animator.SetBool("isGard", false);
        }

        else if (inputGard) // 가드 입력이 안 된 상태면서 가드 누르면 가드 실행
        {
            animator.SetBool("isGard", true);
        }


        /// 이 아래는 공격////////////////////////////////////////////////
        if (!inputNormalAtt) // 공격상태가 아니라면 애니메이션 재생 안 함 노말공격임
        {
            animator.SetBool("doNormalAttack", false);
        }

        else if (inputNormalAtt) // 공격상태가 아닐 때 공격키 누르면 애니메이션 재생
        {
            // 이 부분에다가 점프 중에 약공격 했을 때의 상태를 추가해야 한다
       
            inputNormalAtt = false;
            animator.SetBool("doNormalAttack", true);
            animator.SetTrigger("isAttack");

            AttackTime = Time.time; // 연속공격을 할 때를 위함
        }

        if (!inputHardAtt) // 공격상태가 아니면 애니메이션 재생 안 함
        {
            animator.SetBool("doHardAttack", false);
        }

        else if (inputHardAtt) // 공격상태가 아닐 때 공격키 누르면 애니메이션 재생
        {
            inputHardAtt = false;
            animator.SetBool("doHardAttack", true);
            animator.SetTrigger("isAttack");
        }
          
        ////////////////////////////////////////////////////

        //이건 점프다
        if (inputJump&&!animator.GetBool("isJumping") ) // 점프 애니 재생중이 아니면서 점프키를 누르면 점프 실행
        {
            isJumping = true;
            inputJump = false;
            animator.SetBool("isJumping", true);
            animator.SetTrigger("doJumping");
        }

        /*



        if (Input.GetAxisRaw ("Horizontal") == 0)
        {
            animator.SetBool("isMoving", false);
        }

        else if (Input.GetAxisRaw ("Horizontal") < 0) // 왼쪽 이동
        {
            Vector3 scale = transform.localScale; // 이 부분은 스프라이트 뒤집어주는 코드임
            // 아예 이미지를 -1로 해서 뒤집어버림!
            scale.x = Mathf.Abs(scale.x);
            transform.localScale = scale;

            animator.SetBool("isMoving", true);
        }

        else if (Input.GetAxisRaw ("Horizontal") > 0) // 오른쪽 이동
        {
            Vector3 scale = transform.localScale;
            scale.x = -Mathf.Abs(scale.x);
            transform.localScale = scale;

            animator.SetBool("isMoving", true);
        }


        if (Input.GetKeyDown(KeyCode.UpArrow) && !animator.GetBool ("isJumping")) // 점프
        {
            if (Input.GetAxis("Horizontal") < 0) // 레프트 점프
            {
                Vector3 scale = transform.localScale; // 이 부분은 스프라이트 뒤집어주는 코드임
                                                      // 아예 이미지를 -1로 해서 뒤집어버림!
                scale.x = -Mathf.Abs(scale.x);
                transform.localScale = scale;
                isJumping = true;
                animator.SetBool("isJumping", true);
                animator.SetTrigger("doJumping");
            }
            else if (Input.GetAxis("Horizontal") > 0) // 라이트 점프
            {
                Vector3 scale = transform.localScale; // 이 부분은 스프라이트 뒤집어주는 코드임
                                                      // 아예 이미지를 -1로 해서 뒤집어버림!
                scale.x = Mathf.Abs(scale.x);
                transform.localScale = scale;
                isJumping = true;
                animator.SetBool("isJumping", true);
                animator.SetTrigger("doJumping");
            }
        }

  */


        /*
         * ㅠㅠㅠㅠㅠ점프 구버전...
        if (Input.GetKeyDown(KeyCode.UpArrow))
        {
            if (Input.GetAxisRaw ("Horizontal") < 0)
            {
                Vector3 scale = transform.localScale; 
                scale.x = Mathf.Abs(scale.x);
                isJumping = true;
                animator.SetBool("isJumping", true);
                animator.SetTrigger("doJumping");
            }
            else
            {
                Vector3 scale = transform.localScale;
                scale.x = -Mathf.Abs(scale.x);
                isJumping = true;
                animator.SetBool("isJumping", true);
                animator.SetTrigger("doJumping");
            }
        }
        */
    }
    
    void Die()
    {
        isDie = true;

        rigid.velocity = Vector2.zero;

        BoxCollider2D[] colls = gameObject.GetComponents<BoxCollider2D>();
        colls[0].enabled = false;
        colls[1].enabled = false;

        Vector2 dieVelocity = new Vector2(0, 10f);
        rigid.AddForce(dieVelocity, ForceMode2D.Impulse);
    }
    
    void OnTriggerEnter2D (Collider2D other)
    {
        Debug.Log("맵 충돌");
        if (other.gameObject.tag == "floor" && rigid.velocity.y < 0) // 땅 밟은거
            animator.SetBool("isJumping", false);

        else if (other.gameObject.tag == "death" && rigid.velocity.y < 0) // 낙사
        {
            health = 0;
        }
    }
    void FixedUpdate()
    {
        if (health == 0)
        {
            return;
        }
        if (animator.GetBool("isJumping") && rigid.velocity.y > 0)
        {
            Physics2D.IgnoreLayerCollision(8, 9, true);
        }
        else if(rigid.velocity.y <=0)
        {
            Debug.Log(rigid.velocity.y);
            Physics2D.IgnoreLayerCollision(8, 9, false);
        }
        Move();
        Jump();
    }
    public void Move()
    {
        Vector3 moveVelocity = Vector3.zero;

        if (inputLeft)
        {
            moveVelocity = Vector3.left;
        }

        else if (inputRight)
        {
            moveVelocity = Vector3.right;
        }
        transform.position += moveVelocity * movePower * Time.deltaTime * 2.5f;
    }

    public void Jump()
    {
        if (!isJumping)
        {
            return;
        }
        rigid.velocity = Vector2.zero;

        Vector2 jumpVelocity = new Vector2(0, jumpPower);
        rigid.AddForce(jumpVelocity, ForceMode2D.Impulse);
        Debug.Log(rigid.velocity.y);
        isJumping = false;
    }
}
