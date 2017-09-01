using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Character_choice : MonoBehaviour {
    private int Player;
    private int[] others;
    private bool choice;
	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
        switch (Player)
        {
            case 0:
                others[0]=1;
                others[1]=2;
                others[2]=3;
                break;
            case 1:
                others[0] = 0;
                others[1] = 2;
                others[2] = 3;
               
                break;
            case 2:
                others[0] = 0;
                others[1] = 1;
                others[2] = 3;
                break;
            case 3:
                others[0] = 0;
                others[1] = 1;
                others[2] = 2;
                break;
            default:
                break;
        }
        if (choice)
        {
            //다음씬으로
        }
    }
}
