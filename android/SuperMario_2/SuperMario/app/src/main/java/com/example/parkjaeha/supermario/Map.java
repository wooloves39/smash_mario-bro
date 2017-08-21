package com.example.parkjaeha.supermario;

/**
 * Created by woolo_so5omoy on 2017-08-21.
 */

public class Map {
    Tile[] tiles;//각 맵의 다른 모형의 타일
    int obnum;//다른 모형의 타일 수

    //void collision(CPlayer& player) {
    //    player.mapobject_collsion = false;
    //    if (player.down == false && player.GetStatus() != FLY_LEFT&&player.GetStatus() != FLY_RIGHT) {
    //        for (int i = 0; i < obnum; ++i) {
    //            for (int j = 0; j < tiles[i].Get_ob_num(); j++) {
    //                if (player.GetPosition().x <= tiles[i].collisionPos(j).left)continue;
    //                if (player.GetPosition().x >= tiles[i].collisionPos(j).right)continue;
    //                if (player.GetPosition().y <= tiles[i].collisionPos(j).top - 30)continue;
    //                if (player.GetPosition().y >= tiles[i].collisionPos(j).bottom)continue;
    //                player.SetPosition(player.GetPosition().x, tiles[i].collisionPos(j).top - 20);
    //                if (player.GetStatus() == JUMP_LEFT || player.GetStatus() == JUMP_RIGHT ||
    //                        player.GetStatus() == FLY_LEFT || player.GetStatus() == FLY_RIGHT) {
    //                    player.SetStatus(player.GetStatus() % 2 + BASIC_RIGHT);
    //                }
    //                player.mapobject_collsion = true;//맵에 붙어 있을때
    //                player.fly = false;
    //                player.m_bJump = false;
    //                return;
    //            }
    //        }
    //    }
    //}
}
