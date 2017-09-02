using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
public class map_choice : MonoBehaviour
{
   public GameObject[] map;
    public GameObject map_Rect;
    private int choice = 100;
    private bool choicing = false;
    private GameObject choice_map;
    private Vector3 map_pos;
    private bool click = false;
    private float timer = 0;
    // Use this for initialization
    void Start()
    {
        map_pos.x = 0;
        map_pos.y = 2;
        map_pos.z = -1;
    }
    // Update is called once per frame
    void Update()
    {
        if (Input.GetMouseButtonDown(0))
        {
            choicing = false;
            Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);
            RaycastHit[] hit = Physics.SphereCastAll(ray, 0.5f);
            for (int i = 0; i < hit.Length; ++i)
            {
                if (hit[i].collider != null)
                {
                    int before_choice = choice;
                    if (hit[i].collider.tag == "map1")
                    {
                        choice = 1;
                    }
                    else if (hit[i].collider.tag == "map2")
                    {
                        choice = 2;
                    }
                    else if (hit[i].collider.tag == "map3")
                    {
                        choice = 3;
                    }
                    else if (hit[i].collider.tag == "map4")
                    {
                        choice = 4;
                    }
                    else if (hit[i].collider.tag == "map5")
                    {
                        choice = 5;
                    }
                    else if (hit[i].collider.tag == "map6")
                    {
                        choice = 6;
                    }
                    else if (hit[i].collider.tag == "map7")
                    {
                        choice = 7;
                    }
                    else if (hit[i].collider.tag == "map8")
                    {
                        choice = 8;
                    }
                    else if (hit[i].collider.tag == "map9")
                    {
                        choice = 9;
                    }
                    else if (hit[i].collider.tag == "random")
                    {
                        choice = 0;
                    }
                    if (hit[i].collider.tag == "choice_map")
                        choicing = true;
                    if (before_choice != choice)
                    {
                        click = false;
                        if (choice_map != null) Destroy(choice_map);
                        map_Rect.transform.position = hit[i].collider.transform.position;
                    }
                }

            }
        }

        if (choice != 0 && choice != 100&&click==false)
        {
            click = true;
            choice_map = Instantiate(map[choice-1], map_pos, Quaternion.identity);
            timer = 0;
            //choicing = true;
        }
        else if (choice == 0)
        {
           
            if (click==false)
            {
                int map_num = Random.Range(0, 8);
                choice_map = Instantiate(map[map_num], map_pos, Quaternion.identity);
                click = true;
            }
            else if (click == true)
            {
                timer += Time.deltaTime;
                Debug.Log(timer);
                if (timer > 0.3f)
                {
                    timer = 0;
                    click = false;
                    Destroy(choice_map);
                }
            }
        }
        if (choicing)
        { //초이스 되었을때 캐릭터 씬으로 바뀐다.
            Debug.Log("맵선정완료");
            //테스트용 
            Singletone.Instance.Mapnumber = choice;
            Debug.LogWarning(Singletone.Instance.Mapnumber);
            // index 5-13까지가 플레이맵 씬임 
            Application.LoadLevel(2);
        }

    }
    void random_map()
    { 
    }

}
