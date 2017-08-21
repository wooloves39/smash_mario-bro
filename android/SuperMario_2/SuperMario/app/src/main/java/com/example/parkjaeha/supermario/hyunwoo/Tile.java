package com.example.parkjaeha.supermario.hyunwoo;

/**
 * Created by woolo_so5omoy on 2017-08-21.
 */

public class Tile {
    int object_num;//타일의 수
    POINT[] object_pos;//그려지는 좌표
    //	POINT *object_realpos;//충돌할 영역
    RECT[] object_realpos;//충돌할 영역 바운딩 박스 영역
    POINT size;//그려지는 오브젝트의 가로 세로 길이
    POINT realsize;//바운딩 박스의 가로 세로 길이
    int downbalance;//바운딩 박스와 그려지는 오브젝트의 높이 벨런스
    void setPos(int x, int y, int num) {
        object_pos[num].x = x; object_pos[num].y = y;//같은 이미지의 타일들의 좌표 설정
    }
    POINT getPos(int num) { return object_pos[num]; }//해당 인덱스 타일의 좌표
    void setsize(int xsize, int ysize) { size.x = xsize; size.y = ysize; }//오브젝트의 가로세로 길이 설정
    void setRealsize(int xsize, int ysize, int balance) {//바운딩 박스의 가로세로 길이 설정과 실제 오브젝트와의 높이차 설정
        realsize.x = xsize;
        realsize.y = ysize;
        downbalance = balance;
    }
    RECT collisionPos(int num) { return object_realpos[num]; }//바운딩 박스의 점의 좌표 출력
    int Get_ob_num() { return object_num; }//같은 타일 오브젝트의 수 출력
    void setobject(int num) {
        object_num = num;
        object_pos = new POINT[object_num];
        object_realpos = new RECT[object_num];
    }

}
