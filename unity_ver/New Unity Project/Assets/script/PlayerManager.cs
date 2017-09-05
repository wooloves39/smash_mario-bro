using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerManager : MonoBehaviour
{
    private float movePower = 2.0f;
    private float jumpPower = 12.0f;
   
    public bool inputLeft = false;
    public bool inputRight = false;
    public bool inputJump = false;
    public bool inputGard = false;
    public bool inputNormalAtt = false;
    public bool inputHardAtt = false;

    private int jumpcount = 0;

    Rigidbody2D rigid;
    Animator animator;

    Vector3 movement;
    bool isDie = false;
    bool isJumping = false;


    float AttackTime = 0; // 연속공격을 위함이다

    int health = 1;
   
    public AudioClip[] soundstate;
    private AudioSource[] sound;

    private int smash_point = 2;    //강한 공격 게이지 및 수치
    private int gage = 0;  //강한공격은 막기나 일반공격에 의해 게이지가 채워지고 게이지가 10이 넘으면 스매시 포인터가 1개 생긴다. 최대 개수 3개
    private int n_AttackCount;//일반 공격 어택은 1과2로 구성, 조작에 따른 스프라이트 변경 변수
    private bool fly = false;//강한공격을 맞아 날아가는 상태인지에 대한 변수
    private int PlayTime_num;//플레이 시간
    private int ranking_num;//랭킹
    private int total_score_num;// 플레이 시간과 초기 공격력을 계산한 전체 점수
    const int low_power = 12;//약한 공격의 데미지
    const int High_power = 30;  //약한 때림과 강한 때림에 따른 power
   private bool live = true;//화면에서 일정 거리 밖으로 나가면 죽는 요소
   private int damage_num = 100;//초기 공격력

    void Start ()
    {
        sound = new AudioSource[soundstate.Length];
        Screen.SetResolution(1280, 720, true); // 플레이 해상도 고정
        animator = gameObject.GetComponentInChildren<Animator>();
        rigid = gameObject.GetComponent<Rigidbody2D>();

        live = true;

        UIButtonManager ui = GameObject.FindGameObjectWithTag("Manager").GetComponent<UIButtonManager>();
        ui.Init();
        for(int i = 0; i < soundstate.Length; ++i)
        {
            this.sound[i] = this.gameObject.AddComponent<AudioSource>();
            this.sound[i].clip = this.soundstate[i];
            this.sound[i].loop = false;
            //유니티도 define이 가능한지 모르겠음..
            //0번 점프 1번 약한때리기 2번 강한때리기 3번 날아가기 4번 넘어지기 5번 죽음(AI) 6번 죽음(플레이어)
        }
    }

	void Update ()
    {
        if (live == false)
        {
            if(!isDie)
            {
                sound[5].Play();
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
            animator.SetBool("isNormalAttack", true);
            animator.SetTrigger("doNormalAttack");

            AttackTime = Time.time; // 연속공격을 할 때를 위함
        }


        ///////////// 강공격

        if (!inputHardAtt) // 공격상태가 아니면 애니메이션 재생 안 함
        {
            animator.SetBool("doHardAttack", false);
        }


        else if (inputHardAtt) // 공격상태가 아닐 때 공격키 누르면 애니메이션 재생
        {
            inputHardAtt = false;
            animator.SetBool("isHardAttack", true);
            animator.SetTrigger("doHardAttack");
        }
          
        ////////////////////////////////////////////////////

        //이건 점프다
        if (inputJump&&!animator.GetBool("isJumping") ) // 점프 애니 재생중이 아니면서 점프키를 누르면 점프 실행
        {
            while (animator.GetBool("isjumping"))
            {
                if (inputNormalAtt)
                {
                    animator.GetBool("isJumpAtt");
                }
            }
        }

        if (inputJump&&jumpcount<2 )

        {
            ++jumpcount;
            isJumping = true;
            inputJump = false;
            animator.SetBool("isJumping", true);
            animator.SetTrigger("doJumping");
            sound[0].Play();
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
        if (other.gameObject.tag == "floor" && rigid.velocity.y < 0) // 땅 밟은거
        {
            animator.SetBool("isJumping", false);
            jumpcount = 0;
        }
    }
    void FixedUpdate()
    {
        if (!live)
        {
            return;
        }
        if (animator.GetBool("isJumping") && rigid.velocity.y > 0)
        {
            Physics2D.IgnoreLayerCollision(8, 9, true);
        }
        else if(rigid.velocity.y <=0)
        {
            Physics2D.IgnoreLayerCollision(8, 9, false);
        }

        Move();
        Jump();

        if (transform.position.y < -6) // 낙사
        {
            live = false;
        }
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
        isJumping = false;
    }
}
