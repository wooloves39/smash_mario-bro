using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Character_choice : MonoBehaviour
{
    private int Player;
    private int[] others;
    private int choice = 100;
    public AudioClip touch;
    private AudioSource touchsound;
    //추가 
    public GameObject[] cha;
    private GameObject choice_char;
    //public GameObject char_Rect;
    private Vector3 char_pos;
    private bool click = false;
    //private bool choicing = false;
    private float timer = 0;

    void Start()
    {
        this.touchsound = this.gameObject.AddComponent<AudioSource>();
        this.touchsound.clip = this.touch;
        this.touchsound.loop = false;
        char_pos.x = 0;
        char_pos.y = 2;
        char_pos.z = -1;
        choice = 0;
    }

    // Update is called once per frame
    void Update()
    {

        //충돌체크 추가
        
       
        if (Input.GetMouseButtonDown(0))
        {
            Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);
            RaycastHit[] hit = Physics.SphereCastAll(ray, 0.5f);
            for (int i = 0; i < hit.Length; ++i)
            {
                int before_choice = choice;
                if (hit[i].collider != null)
                {
                    touchsound.Play();
                    if (hit[i].collider.tag == "Mario")
                    {
                        choice = 1;
                    }
                    else if (hit[i].collider.tag == "Luizy")
                    {
                        choice = 2;
                    }
                    else if (hit[i].collider.tag == "Wario")
                    {
                        choice = 3;
                    }
                    else if (hit[i].collider.tag == "Waluizy")
                    {
                        choice = 4;
                    }
                    //if (before_choice != choice)
                    //{
                    //    click = false;
                    //    if (choice_char != null) Destroy(choice_char);
                    //    //char_Rect.transform.position = hit[i].collider.transform.position;
                    //}
                }
            }
        }
        if (choice != 0)
        {
            Singletone.Instance.Charnumber = choice;
            if (!touchsound.isPlaying)
                Application.LoadLevel(Singletone.Instance.Mapnumber + 4);
            Debug.LogWarning(Singletone.Instance.Charnumber);
            Debug.LogWarning(choice);
        }

        //if (choicing)
        //{ //초이스 되었을때 캐릭터 씬으로 바뀐다.
        //    Debug.Log("캐릭터 선정 완료");
        //    //테스트용 
        //    Singletone.Instance.Charnumber = choice;
        //    Debug.LogWarning(Singletone.Instance.Charnumber);
        //    Application.LoadLevel(3);
        //}

        //switch (Player)
        //{
        //    case 0:
        //        others[0] = 1;
        //        others[1] = 2;
        //        others[2] = 3;
        //        break;
        //    case 1:
        //        others[0] = 0;
        //        others[1] = 2;
        //        others[2] = 3;

        //        break;
        //    case 2:
        //        others[0] = 0;
        //        others[1] = 1;
        //        others[2] = 3;
        //        break;
        //    case 3:
        //        others[0] = 0;
        //        others[1] = 1;
        //        others[2] = 2;
        //        break;
        //    default:
        //        break;
        //}
        //}
        //if (choice)
        //{
        //    //다음씬으로
        //}
    }
}
