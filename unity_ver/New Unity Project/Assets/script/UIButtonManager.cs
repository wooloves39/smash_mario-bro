using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class UIButtonManager : MonoBehaviour
{

    GameObject player;
    PlayerManager playerScript;

    public void Init()
    {
        player = GameObject.FindGameObjectWithTag("Player");
        playerScript = player.GetComponent<PlayerManager>();
    }

    public void LeftDown()
    {
        playerScript.inputLeft = true;
    }

    public void LeftUp()
    {
        playerScript.inputLeft = false;
    }

    public void RightDown()
    {
        playerScript.inputRight = true;
    }

    public void RightUp()
    {
        playerScript.inputRight = false;
    }

    public void JumlClick()
    {
        playerScript.inputJump = true;
    }

    public void GardDown()
    {
        playerScript.inputGard = true;
    }

    public void GardUp()
    {
        playerScript.inputGard = false;
    }

    public void normalAttclick()
    {
        playerScript.inputNormalAtt = true;
    }

    public void hardAttclick()
    {
        playerScript.inputHardAtt = true;
    }
}