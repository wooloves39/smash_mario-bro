using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerManager : MonoBehaviour
{
    public float movePower = 1.0f;
    public float jumpPower = 1.0f;
    public int maxHealth = 1;

    public bool inputLeft = false;
    public bool inputRight = false;
    public bool inputJump = false;

    Rigidbody2D rigid;
    Animator animator;

    Vector3 movement;
    bool isDie = false;
    bool isJumping = false;

    int health = 1;



	void Start ()
    {
        Screen.SetResolution(1280, 720, true); // 플레이 해상도 고정
        animator = gameObject.GetComponentInChildren<Animator>();
        rigid = gameObject.GetComponent<Rigidbody2D>();

        health = maxHealth;
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

        /*  플레이어 이동하는거 구버전 스크립트
        // 왼쪽 입력 시에
        if (Input.GetKeyDown (KeyCode.LeftArrow))
        {
            Vector3 scale = transform.localScale;
            scale.x = Mathf.Abs(scale.x);
            transform.localScale = scale;

            anim.SetInteger("state", 1);
        } 
        // 왼쪽을 입력하다가 키를 뗐을 경우
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
        // 오른쪽을 입력하다가 키를 뗐을 경우
        if (Input.GetKeyUp(KeyCode.RightArrow))
        {
            Vector3 scale = transform.localScale;
            scale.x = -Mathf.Abs(scale.x);
            transform.localScale = scale;

            anim.SetInteger("state", 0);
        }
        */

        if ((!inputRight && !inputLeft))
        {
            animator.SetBool("isMoving", false);
        }

        else if (inputLeft)
        {
            Vector3 scale = transform.localScale; // 이 부분은 스프라이트 뒤집어주는 코드임
            // 아예 이미지를 -1로 해서 뒤집어버림!
            scale.x = Mathf.Abs(scale.x);
            transform.localScale = scale;

            animator.SetInteger("Direction", -1);
            animator.SetBool("isMoving", true);
        }

        else if (inputRight)
        {
            Vector3 scale = transform.localScale;
            scale.x = -Mathf.Abs(scale.x);
            transform.localScale = scale;

            animator.SetInteger("Direction", 1);
            animator.SetBool("isMoving", true);
        }


        if (inputJump && !animator.GetBool("isJumping"))
        {
            if (Input.GetAxis("Horizontal") < 0)
            {
                Vector3 scale = transform.localScale; // 이 부분은 스프라이트 뒤집어주는 코드임
                                                      // 아예 이미지를 -1로 해서 뒤집어버림!
                scale.x = -Mathf.Abs(scale.x);
                transform.localScale = scale;
                isJumping = true;
                animator.SetBool("isJumping", true);
                animator.SetTrigger("doJumping");
                animator.SetInteger("Direction", 1);
            }
            else if (Input.GetAxis("Horizontal") > 0)
            {
                Vector3 scale = transform.localScale; // 이 부분은 스프라이트 뒤집어주는 코드임
                                                      // 아예 이미지를 -1로 해서 뒤집어버림!
                scale.x = Mathf.Abs(scale.x);
                transform.localScale = scale;
                isJumping = true;
                animator.SetBool("isJumping", true);
                animator.SetTrigger("doJumping");
                animator.SetInteger("Direction", -1);
            }
        }

        /*
         * 클릭방식 무빙!
        if (Input.GetAxisRaw ("Horizontal") == 0)
        {
            animator.SetBool("isMoving", false);
        }

        else if (Input.GetAxisRaw ("Horizontal") < 0)
        {
            Vector3 scale = transform.localScale; // 이 부분은 스프라이트 뒤집어주는 코드임
            // 아예 이미지를 -1로 해서 뒤집어버림!
            scale.x = Mathf.Abs(scale.x);
            transform.localScale = scale;

            animator.SetInteger("Direction", -1);
            animator.SetBool("isMoving", true);
        }

        else if (Input.GetAxisRaw ("Horizontal") > 0)
        {
            Vector3 scale = transform.localScale;
            scale.x = -Mathf.Abs(scale.x);
            transform.localScale = scale;

            animator.SetInteger("Direction", 1);
            animator.SetBool("isMoving", true);
        }


        if (Input.GetKeyDown(KeyCode.UpArrow) && !animator.GetBool ("isJumping"))
        {
            if (Input.GetAxis("Horizontal") < 0)
            {
                Vector3 scale = transform.localScale; // 이 부분은 스프라이트 뒤집어주는 코드임
                                                      // 아예 이미지를 -1로 해서 뒤집어버림!
                scale.x = -Mathf.Abs(scale.x);
                transform.localScale = scale;
                isJumping = true;
                animator.SetBool("isJumping", true);
                animator.SetTrigger("doJumping");
                animator.SetInteger("Direction", 1);
            }
            else if (Input.GetAxis("Horizontal") > 0)
            {
                Vector3 scale = transform.localScale; // 이 부분은 스프라이트 뒤집어주는 코드임
                                                      // 아예 이미지를 -1로 해서 뒤집어버림!
                scale.x = Mathf.Abs(scale.x);
                transform.localScale = scale;
                isJumping = true;
                animator.SetBool("isJumping", true);
                animator.SetTrigger("doJumping");
                animator.SetInteger("Direction", -1);
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
        if (other.gameObject.layer == 10 && rigid.velocity.y < 0) // 땅 밟은거
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

        Move();
        Jump();
    }

    void Move()
    {
        Vector3 moveVelocity = Vector3.zero;

        if (Input.GetAxisRaw("Horizontal") < 0)
        {
            moveVelocity = Vector3.left;
        }

        else if (Input.GetAxisRaw("Horizontal") > 0)
        {
            moveVelocity = Vector3.right;
        }
        transform.position += moveVelocity * movePower * Time.deltaTime * 2.5f;
    }

    void Jump()
    {
        if (!isJumping)
        {
            return;
        }
        rigid.velocity = Vector2.zero;

        Vector2 jumpVelocity = new Vector2(0, jumpPower);
        rigid.AddForce(jumpVelocity*5.5f, ForceMode2D.Impulse);

        isJumping = false;
    }

}
