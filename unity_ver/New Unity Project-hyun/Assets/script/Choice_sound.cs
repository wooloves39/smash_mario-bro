
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Choice_sound : MonoBehaviour {
	// Use this for initialization
	void Start ()
    {
        DontDestroyOnLoad(this);
    }
	
	
	// Update is called once per frame
	void Update () {
        Debug.Log(Application.loadedLevel);

        if (Application.loadedLevel!=1&& Application.loadedLevel != 2)
        {
            Debug.Log("ss");
            Destroy(this.gameObject);
        }
	}
}
