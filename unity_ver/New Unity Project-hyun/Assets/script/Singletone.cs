using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Singletone
{
    private static Singletone instance = null;

    public static Singletone Instance
    {
        get
        {
            if(instance == null)
            {
                instance = new Singletone();
            }
            return instance;
        }
    }

    private Singletone() { }

    //여기에 싱글톤 변수를 추가한다.
    //처음에 -1로 초기화 디파인이 안됨. 
    public int Mapnumber = -1;
    public int Charnumber = -1;

    // Use this for initialization
    void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
