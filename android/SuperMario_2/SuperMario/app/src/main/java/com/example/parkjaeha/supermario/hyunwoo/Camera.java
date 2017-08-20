package com.example.parkjaeha.supermario.hyunwoo;

/**
 * Created by woolo_so5omoy on 2017-08-21.
 */
import com.example.parkjaeha.supermario.hyunwoo.POINT;
public class Camera {
    POINT pos;//실제 카메라 위치
    POINT realpos;//카메라가 도착해야할 위치
    int mode;//플레이 모드 1p 2p

    void setCam(int game_mode) {
        mode = game_mode;//모드를 정한다 1p와 2p의 카메라 형태가 다르다. 1p는 카메라에 종속되어 따라가며, 2p모드는 1p와 2p의 중점에 카메라가 위치한다.
    }
    POINT getPos() {
        return pos;//카메라의 포지션을 출력
    }

    void setPos(int x) {
        if (x <= -700 || x >= 700);
        else {
            pos.x = x*(1280 / 940) - 640; pos.y = 0;
            realpos.x = x*(1280 / 940) - 640; realpos.y = 0;
        }
    }
    void setPos(int x1, int x2) {
        pos.x = ((x1 + x2) / 2)*(1280 / 940) - 640; pos.y = 0;
        realpos.x = ((x1 + x2) / 2)*(1280 / 940) - 640; realpos.y = 0;
    }
    void realsetPos(int x) {
        if (x <= -700 || x >= 700);
        else {
            realpos.x = x*(1280 / 940) - 640; realpos.y = 0;
        }
    }
    void realsetPos(int x1, int x2) {
        realpos.x = ((x1 + x2) / 2)*(1280 / 940) - 640; realpos.y = 0;
    }
    void add() {
        if ((realpos.x != pos.x))
            pos.x += (realpos.x - pos.x) / 20;
    }
}
