package com.example.chaos_ping_pong.Multiplayer;

import com.example.chaos_ping_pong.Ball;

import java.util.ArrayList;
import java.util.List;

class DataToSend {
    static class ObstacleData
    {
        ObstacleData() {}
        float x,y,width,height;
    }
    ObstacleData p1 = new ObstacleData();
    ObstacleData p2 = new ObstacleData();
    int p1Imgi, p2Imgi;

    ArrayList<ObstacleData> obstacles;

    List<Ball> balls;
    int leftPt, rightPt, time;
}
