using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class select_player_manager : MonoBehaviour {

    public GameObject[] Myplayer = new GameObject[4];
    public GameObject[] AIplayer = new GameObject[4];
    // Use this for initialization
    void Start ()
    {
        Debug.Log("char number : ");
        int myPlayernumber = Singletone.Instance.Charnumber - 1;
        Debug.LogWarning(myPlayernumber);
        //일단 내 player 생성. 

        Myplayer[myPlayernumber].transform.position
             = new Vector3(myPlayernumber - 3, 0, 0); //1. 초기 포지션 설정. 
        Instantiate<Object>(Myplayer[myPlayernumber]);

        for(int i = 0 ; i < 4; ++i)
        {
            if (i != myPlayernumber)
            {
                AIplayer[i].transform.position
                 = new Vector3(i-3 , 0, 0); //1. 초기 포지션 설정. 
                Instantiate<Object>(AIplayer[i]);
            }
        }

    }
	
	// Update is called once per frame
	void Update () {
		
	}
}
