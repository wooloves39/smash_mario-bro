using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameManager : MonoBehaviour {

    public GameObject player;

    Vector3 StartingPos;
    Quaternion StartingRotate;

	void Start ()
    {
        StartingPos = GameObject.FindGameObjectWithTag("start").transform.position;
        StartingRotate = GameObject.FindGameObjectWithTag("start").transform.rotation;
    }
	
	void Update ()
    {
		
	}

    void OnGUI()
    {

    }
}
