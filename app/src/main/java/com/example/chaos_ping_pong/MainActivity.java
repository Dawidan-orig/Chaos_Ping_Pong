package com.example.chaos_ping_pong;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chaos_ping_pong.Multiplayer.ClientDrawer;
import com.example.chaos_ping_pong.Multiplayer.GameClient;
import com.example.chaos_ping_pong.Multiplayer.GameServer;
import com.example.chaos_ping_pong.Multiplayer.HostDrawer;
import com.example.chaos_ping_pong.Multiplayer.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static com.example.chaos_ping_pong.Crate.soundPool;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SoundPool.OnLoadCompleteListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(this);
        Crate.s_ID_event = soundPool.load(this, R.raw.event_s, 1);
        Crate.s_ID_obstTrail = soundPool.load(this, R.raw.obst_trail, 1);
        Crate.s_ID_midBall = soundPool.load(this, R.raw.mid_ball_s, 1);
        Crate.s_ID_upBall = soundPool.load(this, R.raw.up_ball_s, 1);
        Crate.s_ID_downBall = soundPool.load(this, R.raw.down_ball_s, 1);
        Crate.s_ID_reverse = soundPool.load(this, R.raw.reverse, 1);
        Crate.s_ID_onSpawn = soundPool.load(this, R.raw.on_spawn, 1);
        Crate.s_ID_teleport = soundPool.load(this, R.raw.teleport, 1);

        setContentView(R.layout.main_menu);
        tutorial = findViewById(R.id.tutorial);
        tutorial.setOnClickListener(this);

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        options = findViewById(R.id.options_button);
        options.setOnClickListener(this);

        startButton.setText("Play");
        options.setText("Options");

        Crate.resourceToAbility.put(R.drawable.ability_reverse_y, "reverseY");
        Crate.resourceToAbility.put(R.drawable.ability_spawn_obst, "spawnObst");
        Crate.resourceToAbility.put(R.drawable.ability_speed_up, "speedUp");
        Crate.resourceToAbility.put(R.drawable.ability_tracker, "tracker");
        Crate.resourceToAbility.put(R.drawable.ability_smoke, "smoke");


        Crate.abilities[1] = "spawnObst";
        Crate.abilities[0] = "speedUp";
        Crate.abilities[2] = "reverseY";

        abil2 = BitmapFactory.decodeResource(getResources(), R.drawable.ability_spawn_obst);
        abil1 = BitmapFactory.decodeResource(getResources(), R.drawable.ability_speed_up);
        abil3 = BitmapFactory.decodeResource(getResources(), R.drawable.ability_reverse_y);

        Crate.lockerAbilities.add("spawnObst");
        Crate.lockerAbilities.add("reverseY");
        Crate.lockerAbilities.add("smoke");

        playerImg = BitmapFactory.decodeResource(getResources(), R.drawable.panel);
    }

    Button startButton;
    Button easyButton;
    Button medButton;
    Button hardButton;
    Button ai_backButton;

    Bitmap playerImg;

    private AdView mAdView;


    Button options;
    Button o_abilities;
    Button o_skins;
    Button tutorial;
    Button t_back;
    Button t_example;

    Button o_s_back;
    Button o_a_back;
    Button o_back;
    Button o_a_change;

    Button p_single;
    Button p_multy;
    Button p_back;

    Bitmap abil1;
    Bitmap abil2;
    Bitmap abil3;

    ImageView curAbil1;
    ImageView curAbil2;
    ImageView curAbil3;
    TextView desc;

    Boolean isa_change = false;
    int a_counter = 0;

    TextView timeExit;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.pauseBtn) {
            if (!Crate.isSuspended) {
                Crate.isSuspended = true;
                leaveBtn.setVisibility(View.VISIBLE);
                leaveBtn.setEnabled(true);
                mAdView = findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice("65891E7B2E36FA0B09FC23775D894529")
                        .build();
                mAdView.loadAd(adRequest);
                mAdView.setVisibility(View.VISIBLE);
            } else {
                Crate.isSuspended = false;
                leaveBtn.setVisibility(View.INVISIBLE);
                leaveBtn.setEnabled(false);
                mAdView.setVisibility(View.INVISIBLE);
            }
        }
        if (v.getId() == R.id.leaveGameBtn) {
            Crate.leavedGame = true;
            setContentView(R.layout.main_menu);
            tutorial = findViewById(R.id.tutorial);
            tutorial.setOnClickListener(this);
            startButton = findViewById(R.id.start_button);
            startButton.setOnClickListener(this);
            options = findViewById(R.id.options_button);
            options.setOnClickListener(this);

            startButton.setText("Play");
            options.setText("Options");
        }

        if (v.getId() == R.id.p_gtype_btn) {
            setContentView(R.layout.main_menu);
            tutorial = findViewById(R.id.tutorial);
            tutorial.setOnClickListener(this);
            startButton = findViewById(R.id.start_button);
            startButton.setOnClickListener(this);
            options = findViewById(R.id.options_button);
            options.setOnClickListener(this);

            startButton.setText("Play");
            options.setText("Options");
        }

        if(v.getId() == R.id.tutorial)
        {
            setContentView(R.layout.tutorial);
            t_example = findViewById(R.id.interactiveBtn);
            t_example.setOnClickListener(this);
            t_back = findViewById(R.id.t_back);
            t_back.setOnClickListener(this);
        }

        if(v.getId() == R.id.t_back)
        {
            setContentView(R.layout.main_menu);
            tutorial = findViewById(R.id.tutorial);
            tutorial.setOnClickListener(this);
            startButton = findViewById(R.id.start_button);
            startButton.setOnClickListener(this);
            options = findViewById(R.id.options_button);
            options.setOnClickListener(this);

            startButton.setText("Play");
            options.setText("Options");
        }

        if (v.getId() == R.id.start_button) {
            setContentView(R.layout.p_gametype);

            p_single = findViewById(R.id.single);
            p_multy = findViewById(R.id.multi);
            p_back = findViewById(R.id.p_gtype_btn);
            p_back.setOnClickListener(this);
            p_multy.setOnClickListener(this);
            p_single.setOnClickListener(this);
        }

        if (v.getId() == R.id.b_easy) {
            Crate.difficulty = "easy";
            Crate.playerImg = playerImg;
            Crate.ability1 = abil1;
            Crate.ability2 = abil2;
            Crate.ability3 = abil3;
            setContentView(R.layout.main_game);
            pauseBtn = findViewById(R.id.pauseBtn);
            pauseBtn.setOnClickListener(this);
            leaveBtn = findViewById(R.id.leaveGameBtn);
            leaveBtn.setOnClickListener(this);
            endGameTv = findViewById(R.id.endGameText);
            new textDynamicCheck().start();
        } else if (v.getId() == R.id.b_med) {
            Crate.difficulty = "medium";
            Crate.playerImg = playerImg;
            Crate.ability1 = abil1;
            Crate.ability2 = abil2;
            Crate.ability3 = abil3;
            setContentView(R.layout.main_game);
            pauseBtn = findViewById(R.id.pauseBtn);
            pauseBtn.setOnClickListener(this);
            leaveBtn = findViewById(R.id.leaveGameBtn);
            leaveBtn.setOnClickListener(this);
            endGameTv = findViewById(R.id.endGameText);
            new textDynamicCheck().start();
        } else if (v.getId() == R.id.b_hard) {
            Crate.difficulty = "hard";
            Crate.playerImg = playerImg;
            Crate.ability1 = abil1;
            Crate.ability2 = abil2;
            Crate.ability3 = abil3;
            setContentView(R.layout.main_game);
            pauseBtn = findViewById(R.id.pauseBtn);
            pauseBtn.setOnClickListener(this);
            leaveBtn = findViewById(R.id.leaveGameBtn);
            leaveBtn.setOnClickListener(this);
            endGameTv = findViewById(R.id.endGameText);
            new textDynamicCheck().start();
        }
        if (v.getId() == R.id.b_ai_back) {
            setContentView(R.layout.p_gametype);

            p_single = findViewById(R.id.single);
            p_multy = findViewById(R.id.multi);
            p_back = findViewById(R.id.p_gtype_btn);
            p_back.setOnClickListener(this);
            p_multy.setOnClickListener(this);
            p_single.setOnClickListener(this);
        }
        if (v.getId() == R.id.single) {
            setContentView(R.layout.p_menu_ai_difficult);
            easyButton = findViewById(R.id.b_easy);
            medButton = findViewById(R.id.b_med);
            hardButton = findViewById(R.id.b_hard);
            ai_backButton = findViewById(R.id.b_ai_back);

            easyButton.setOnClickListener(this);
            medButton.setOnClickListener(this);
            hardButton.setOnClickListener(this);
            ai_backButton.setOnClickListener(this);
            Crate.server = null;
            Crate.client = null;
        }
        if (v.getId() == R.id.options_button) {
            setContentView(R.layout.options);

            o_abilities = findViewById(R.id.abilities_button);
            o_skins = findViewById(R.id.skins_button);

            o_skins.setOnClickListener(this);
            o_abilities.setOnClickListener(this);

            o_skins.setText("Change Skin");
            o_abilities.setText("Change Abilities");

            o_back = findViewById(R.id.o_button);
            o_back.setOnClickListener(this);
            o_back.setText("<<Back");

        } else if (v.getId() == R.id.skins_button) {
            setContentView(R.layout.o_skins);
            o_s_back = findViewById(R.id.o_s_button);
            o_s_back.setOnClickListener(this);
            final ImageView curPlayerSkin = findViewById(R.id.currentSkin);
            curPlayerSkin.setImageBitmap(playerImg);
            GridView gridview = findViewById(R.id.skinsGrid);
            gridview.setAdapter(new ImageAdapterPanels(this));

            GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    curPlayerSkin.setImageBitmap(BitmapFactory.decodeResource(getResources(), Crate.panelIds[position]));
                    playerImg = BitmapFactory.decodeResource(getResources(), Crate.panelIds[position]);
                    Crate.playerBitmapIndex = position;
                }
            };

            gridview.setOnItemClickListener(gridviewOnItemClickListener);
        } else if (v.getId() == R.id.abilities_button) {
            setContentView(R.layout.o_abilities);
            o_a_back = findViewById(R.id.o_a_button);
            o_a_back.setOnClickListener(this);
            o_a_change = findViewById(R.id.o_a_change);
            o_a_change.setOnClickListener(this);
            desc = findViewById(R.id.description);
            curAbil1 = findViewById(R.id.currentAbility1);
            curAbil2 = findViewById(R.id.currentAbility2);
            curAbil3 = findViewById(R.id.currentAbility3);
            curAbil1.setImageBitmap(abil1);
            curAbil2.setImageBitmap(abil2);
            curAbil3.setImageBitmap(abil3);
            GridView gridview = findViewById(R.id.abilitiesGrid);
            gridview.setAdapter(new ImageAdapterAbilities(this));

            GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    switch (position)
                    {
                        case 0 :
                            desc.setText(R.string.reverse_desc);
                            break;
                        case 1 :
                            desc.setText(R.string.spawn_obst_desc);
                            break;
                        case 2 :
                            desc.setText(R.string.speed_up_desc);
                            break;
                        case 3 :
                            desc.setText(R.string.tracker_desc);
                            break;
                        case 4 :
                            desc.setText(R.string.smoke_desc);
                            break;
                    }

                    if (isa_change) {
                        if (a_counter == 0) {
                            curAbil2.setImageBitmap(BitmapFactory.decodeResource(getResources(), Crate.abilityIDs[position]));
                            abil2 = BitmapFactory.decodeResource(getResources(), Crate.abilityIDs[position]);

                            Crate.abilities[1] = Crate.resourceToAbility.get(Crate.abilityIDs[position]);
                        }
                        if (a_counter == 1) {
                            curAbil1.setImageBitmap(BitmapFactory.decodeResource(getResources(), Crate.abilityIDs[position]));
                            abil1 = BitmapFactory.decodeResource(getResources(), Crate.abilityIDs[position]);
                            Crate.abilities[0] = Crate.resourceToAbility.get(Crate.abilityIDs[position]);
                        }
                        if (a_counter == 2) {
                            curAbil3.setImageBitmap(BitmapFactory.decodeResource(getResources(), Crate.abilityIDs[position]));
                            abil3 = BitmapFactory.decodeResource(getResources(), Crate.abilityIDs[position]);
                            Crate.abilities[2] = Crate.resourceToAbility.get(Crate.abilityIDs[position]);
                            a_counter = -1;
                            isa_change = false;
                        }
                        a_counter++;
                    }
                }
            };
            gridview.setOnItemClickListener(gridviewOnItemClickListener);
        }
        if (v.getId() == R.id.o_a_change) {
            curAbil1.setImageBitmap(null);
            curAbil2.setImageBitmap(null);
            curAbil3.setImageBitmap(null);
            a_counter = 0;
            isa_change = true;
        }
        if (v.getId() == R.id.o_a_button) {
            setContentView(R.layout.options);

            o_abilities = findViewById(R.id.abilities_button);
            o_skins = findViewById(R.id.skins_button);

            o_skins.setOnClickListener(this);
            o_abilities.setOnClickListener(this);

            o_skins.setText("Change Skin");
            o_abilities.setText("Change Abilities");

            o_back = findViewById(R.id.o_button);
            o_back.setOnClickListener(this);
            o_back.setText("<<Back");
        }
        if (v.getId() == R.id.o_s_button) {
            setContentView(R.layout.options);
            o_abilities = findViewById(R.id.abilities_button);
            o_skins = findViewById(R.id.skins_button);

            o_skins.setOnClickListener(this);
            o_abilities.setOnClickListener(this);

            o_skins.setText("Change Skin");
            o_abilities.setText("Change Abilities");

            o_back = findViewById(R.id.o_button);
            o_back.setOnClickListener(this);
            o_back.setText("<<Back");
        }
        if (v.getId() == R.id.o_button) {
            setContentView(R.layout.main_menu);
            tutorial = findViewById(R.id.tutorial);
            tutorial.setOnClickListener(this);
            startButton = findViewById(R.id.start_button);
            startButton.setOnClickListener(this);
            options = findViewById(R.id.options_button);
            options.setOnClickListener(this);

            startButton.setText("Play");
            options.setText("Options");
        }

        if (v.getId() == R.id.p_m_btn) {
            setContentView(R.layout.p_gametype);

            p_single = findViewById(R.id.single);
            p_multy = findViewById(R.id.multi);
            p_back = findViewById(R.id.p_gtype_btn);
            p_back.setOnClickListener(this);
            p_multy.setOnClickListener(this);
            p_single.setOnClickListener(this);
        }

        //------------------------------------Multiplayer Section----------------------------------//
        if (v.getId() == R.id.m_host_back) {
            setContentView(R.layout.p_multiplayer);
            hostBtn = findViewById(R.id.host_btn);
            joinBtn = findViewById(R.id.client_btn);
            p_m_backBtn = findViewById(R.id.p_m_btn);
            hostBtn.setOnClickListener(this);
            joinBtn.setOnClickListener(this);
            p_m_backBtn.setOnClickListener(this);
            isInMultiplayer = false;
        }

        if (v.getId() == R.id.client_backBtn) {
            setContentView(R.layout.p_multiplayer);
            hostBtn = findViewById(R.id.host_btn);
            joinBtn = findViewById(R.id.client_btn);
            p_m_backBtn = findViewById(R.id.p_m_btn);
            hostBtn.setOnClickListener(this);
            joinBtn.setOnClickListener(this);
            p_m_backBtn.setOnClickListener(this);
            isInMultiplayer = false;
        }

        if (v.getId() == R.id.multi) {
            setContentView(R.layout.p_multiplayer);
            hostBtn = findViewById(R.id.host_btn);
            joinBtn = findViewById(R.id.client_btn);
            p_m_backBtn = findViewById(R.id.p_m_btn);
            hostBtn.setOnClickListener(this);
            joinBtn.setOnClickListener(this);
            p_m_backBtn.setOnClickListener(this);
            isInMultiplayer = false;
        }

        if (v.getId() == R.id.host_btn) {
            isInMultiplayer = true;
            setContentView(R.layout.p_m_host);
            host_back = findViewById(R.id.m_host_back);
            host_back.setOnClickListener(this);
            host_IP_tv = findViewById(R.id.IPTextView);
            host_IP_tv.setText("Your IP is: \n" + Utils.getIPAddress(true) + "\n Send it to your friend");
            host_start = findViewById(R.id.m_host_start);
            host_start.setOnClickListener(this);
            server = new GameServer();
            new Thread() {
                @Override
                public void run() {
                    server.run();
                }
            }.start();
            Crate.server = server;
            new somethingCheck().start();
        }
        if (v.getId() == R.id.client_btn) {
            isInMultiplayer = true;

            setContentView(R.layout.p_m_client);

            clientJoinBtn = findViewById(R.id.client_joinBtn);
            clientBackBtn = findViewById(R.id.client_backBtn);
            enterIP = findViewById(R.id.clientToHostIP);
            clientJoinBtn.setOnClickListener(this);
            clientBackBtn.setOnClickListener(this);
        }
        if (v.getId() == R.id.client_joinBtn) {
            //FUN PART STARTS HERE
            HOST = enterIP.getText() + "";
            client = new GameClient();
            new Thread() {
                @Override
                public void run() {
                    client.run();
                }
            }.start();
            Crate.client = client;
            new somethingCheck().start();
        }
        if (v.getId() == R.id.m_host_start) {
            new Thread() {
                @Override
                public void run() {
                    Crate.server.sendDataToClient("startGame");
                }
            }.start();

            setContentView(R.layout.server_main_game);
            Crate.playerImg = playerImg;
            Crate.ability1 = abil1;
            Crate.ability2 = abil2;
            Crate.ability3 = abil3;
            isInMultiplayer = false;
            new textDynamicCheck().start();
        }

    }

    Button hostBtn, joinBtn, p_m_backBtn;

    TextView host_IP_tv;
    Button host_back, host_start;

    Button clientBackBtn, clientJoinBtn;
    EditText enterIP;

    GameServer server;
    GameClient client;

    public static boolean HOST_WAIT = false;
    public static boolean isInMultiplayer = false;

    public static String HOST = "";
    public static int PORT = 16767;
    public static String NETWORK_TAG = "SOCKET";

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }

    class somethingCheck extends Thread {
        @Override
        public void run() {
            while (isInMultiplayer) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(Crate.client != null) {
                            if (Crate.client.command.equals("startGame")) {
                                Crate.client.command = "";
                                setContentView(R.layout.client_main_game);
                                Crate.playerImg = playerImg;
                                Crate.ability1 = abil1;
                                Crate.ability2 = abil2;
                                Crate.ability3 = abil3;
                                isInMultiplayer = false;
                                new textDynamicCheck().start();
                            }
                        }
                        if (HOST_WAIT && host_start != null) { if(!host_start.isEnabled()) host_start.setEnabled(true); }
                    }
                });
                try {
                    Thread.sleep(0,200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//------------------------End of Multiplayer Section-----------------------------------------------//

    TextView endGameTv;

    TextView rightPt, leftPt;
    Button pauseBtn, leaveBtn;

    class textDynamicCheck extends Thread {
        @Override
        public void run() {
            timeExit = findViewById(R.id.time_exit);
            rightPt = findViewById(R.id.rightPt);
            leftPt = findViewById(R.id.leftPt);
            while (!DrawerSingle.onSVDestroy || !HostDrawer.onSVDestroy || !ClientDrawer.onSVDestroy) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if ((Crate.time / 1000) % 60 >= 10) {
                            timeExit.setText("Time left: " + (Crate.time / 1000) / 60 + ":" + (Crate.time / 1000) % 60);
                        } else
                            timeExit.setText("Time left: " + (Crate.time / 1000) / 60 + ":0" + (Crate.time / 1000) % 60);
                        rightPt.setText(String.valueOf(Crate.rightPt));
                        leftPt.setText(String.valueOf(Crate.leftPt));

                        if(Crate.time <= 0)
                        {
                            endGameTv = findViewById(R.id.endGameText);
                            endGameTv.setVisibility(View.VISIBLE);
                            if (Crate.leftPt > Crate.rightPt) {
                                endGameTv.setText("Player Won!");
                            } else if (Crate.leftPt == Crate.rightPt) {
                                endGameTv.setText("Friendship Won!");
                            } else endGameTv.setText("AI won.");
                            pauseBtn.setEnabled(false);
                            Crate.isSuspended = true;
                            leaveBtn.setVisibility(View.VISIBLE);
                            leaveBtn.setEnabled(true);
                        }
                    }
                });
                try {
                    Thread.sleep(0,200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
