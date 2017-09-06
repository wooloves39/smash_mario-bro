using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using UnityEngine.UI;

public class Timer : MonoBehaviour {

    public Text TimerLabel;
    public float timeCount = 0;
	// Use this for initialization
	void Start () {

	}
	
	// Update is called once per frame
	void Update () {
        timeCount += Time.deltaTime;
        int intTime = (int)timeCount;
        TimerLabel.text = intTime.ToString();
	}
}
