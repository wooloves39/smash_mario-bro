package com.example.parkjaeha.supermario;

/**
 * Created by yeb01 on 2017-08-25.
 */

public class player_options
{
    float speed;
    float velocityX_max;
    int JumpCount;
    int smash_count;
    float collsion_Length;
    void setting(int character)
    {
        switch (character)
        {
            case 0:
                speed = 5.0f;
                velocityX_max = 10;
                JumpCount = 2;
                smash_count = 3;
                collsion_Length = 80;
                break;
            case 1:
                speed = 3.0f;
                velocityX_max = 5;
                JumpCount = 2;
                smash_count = 4;
                collsion_Length = 80;
                break;
            case 2:
                speed = 3.0f;
                velocityX_max = 5;
                JumpCount = 3;
                smash_count = 3;
                collsion_Length = 80;
                break;
            case 3:
                speed = 3.0f;
                velocityX_max = 5;
                JumpCount = 2;
                smash_count = 3;
                collsion_Length = 110;
                break;
            default:
                break;
        }
    }
};
