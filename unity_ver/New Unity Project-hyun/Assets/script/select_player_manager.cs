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
        Vector3 myPos= new Vector3(myPlayernumber - 3, 0, -1);
        Instantiate<Object>(Myplayer[myPlayernumber],myPos,Quaternion.identity);
        for(int i = 0 ; i < 4; ++i)
        {
            if (i != myPlayernumber)
            {
                 myPos= new Vector3(i-3 , 0, -1); //1. 초기 포지션 설정. 
                Instantiate<Object>(AIplayer[i],myPos,Quaternion.identity);
            }
        }

    }
	
	// Update is called once per frame
	void Update () {
		
	}
}
