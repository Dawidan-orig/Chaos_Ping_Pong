package com.example.chaos_ping_pong;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.SoundPool;

import com.example.chaos_ping_pong.Multiplayer.GameClient;
import com.example.chaos_ping_pong.Multiplayer.GameServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Crate {
    public static ArrayList<Obstacle> obstacles = new ArrayList<>();
    public static ArrayList<Player> bots = new ArrayList<>();
    public static int leftPt, rightPt; //TODO должно быть отправлено
    public static String[] abilities = new String[4];
    public static ArrayList<String> lockerAbilities = new ArrayList<>();
    public static List<Ball> balls = Collections.synchronizedList(new ArrayList<Ball>());
    public static Animator animator = new Animator();
    public static Integer[] panelIds = {R.drawable.panel, R.drawable.panel_ex1, R.drawable.brick_stone};
    public static Integer[] abilityIDs = {R.drawable.ability_reverse_y, R.drawable.ability_spawn_obst, R.drawable.ability_speed_up, R.drawable.ability_tracker, R.drawable.ability_smoke};
    @SuppressLint("UseSparseArrays")
    static HashMap<Integer, String> resourceToAbility = new HashMap<>();
    public static int time = 300000; //TODO должно быть отправлено
    public static boolean isSuspended = false;
    public static boolean leavedGame = false;

    public static float skx;
    public static float sky;

    static String difficulty;
    public static Bitmap playerImg;
    public static Bitmap ability1;
    public static Bitmap ability2;
    public static Bitmap ability3;

    static SoundPool soundPool;
    static int s_ID_reverse;
    static int s_ID_onSpawn;
    static int s_ID_teleport;
    static int s_ID_event;
    static int s_ID_obstTrail;
    static int s_ID_midBall;
    static int s_ID_upBall;
    static int s_ID_downBall;

    public static GameServer server;
    public static GameClient client;
    public static int playerBitmapIndex = 0;
}
