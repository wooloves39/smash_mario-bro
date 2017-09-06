using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Playstate_map : MonoBehaviour {
    int map_number;
    public GameObject[] maps;
    private GameObject Play_map;
	// Use this for initialization
	void Start ()
    {
        map_number = Singletone.Instance.Mapnumber;
        map_number = 1;
        Vector3 pos = new Vector3(0, 0, 0);
        Play_map = Instantiate(maps[map_number-1], pos, Quaternion.identity);
    }
	
	// Update is called once per frame
	void Update () {
		
	}
}
