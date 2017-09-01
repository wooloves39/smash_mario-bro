using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Ranking : MonoBehaviour {
    public Player[] Player;
	// Use this for initialization
	void Start () {
        for (int i = 0; i < 4; i++)
        {
            if (Player[i].live == true) Player[i].PlayTime_num = 99;
            Player[i].total_score_num = (Player[i].PlayTime_num * 50) + Player[i].damage_num;
        }
        for (int i = 0; i < 4; ++i)
        {
            int rank = 1;
            for (int j = 0; j < 4; j++)
            {
                if (Player[i].total_score_num < Player[j].total_score_num) rank++;
            }
            Player[i].ranking_num = rank;
            if (Player[i].ranking_num == 1) ;
            else;
               
        }
    }
	
	// Update is called once per frame
	void Update () {
      for(int i = 0; i < 4; ++i)
        {
            //캐릭터 순서대로 백그라운드와 배경 그려주기
        }
    }
}
