package com.example.chaos_ping_pong.Multiplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.chaos_ping_pong.Ability;
import com.example.chaos_ping_pong.AbilityIcons;
import com.example.chaos_ping_pong.Animator;
import com.example.chaos_ping_pong.Ball;
import com.example.chaos_ping_pong.Crate;
import com.example.chaos_ping_pong.Obstacle;
import com.example.chaos_ping_pong.Player;
import com.example.chaos_ping_pong.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.chaos_ping_pong.Crate.skx;
import static com.example.chaos_ping_pong.Crate.sky;

public class ClientDrawer extends SurfaceView implements SurfaceHolder.Callback {
    Canvas canvas;
    SurfaceHolder surfaceHolder;
    float width;
    float height;

    Ability gameAbility1;
    Ability gameAbility2;
    Ability gameAbility3;

    public ClientDrawer(Context context) {
        super(context);
        getHolder().addCallback(this);
        init();
    }

    public ClientDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        init();
    }

    public ClientDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        init();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    ClientDrawer.DrawerThread drawerThread;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        client = Crate.client;
        this.playerImg = Crate.playerImg;
        this.ability1 = Crate.ability1;
        this.ability2 = Crate.ability2;
        this.ability3 = Crate.ability3;
        onSVDestroy = false;
        Crate.isSuspended = false;
        Crate.leavedGame = false;

        ClientDrawer.DrawerThread dt = new ClientDrawer.DrawerThread();
        surfaceHolder = holder;
        dt.start();
        this.drawerThread = dt;
    }

    void init()
    {
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!Crate.isSuspended) {
                    if (Crate.time >= 0) {
                        if (event.getY() < height) {
                            if (!isLock) {
                                float destY = event.getY();
                                float destX = event.getX();
                                if (!playersInversed) {
                                    p2.setDestY(destY - p2.height / 2);
                                } else p2.setDestY(height - destY - p2.height / 2);
                            } else {
                                isLock = false;
                                if (abil1Lock) {
                                    gameAbility1.use(event.getX(), event.getY(), obstImg);
                                    abil1Lock = false;
                                    Crate.animator.isLockerAbility = false;
                                }
                                if (abil2Lock) {
                                    gameAbility2.use(event.getX(), event.getY(), obstImg);
                                    abil2Lock = false;
                                    Crate.animator.isLockerAbility = false;
                                }
                                if (abil3Lock) {
                                    gameAbility3.use(event.getX(), event.getY(), obstImg);
                                    abil3Lock = false;
                                    Crate.animator.isLockerAbility = false;
                                }
                            }
                        } else {
                            if (!isLock) {
                                if (event.getX() < totalImgWidth) {
                                    if (Crate.lockerAbilities.contains(gameAbility2.name)) {
                                        isLock = true;
                                        abil2Lock = true;
                                        if (gameAbility2.flag)
                                            Crate.animator.isLockerAbility = true;
                                    } else gameAbility2.use(event.getX(), event.getY(), obstImg);
                                } else if (event.getX() > width / 2 - totalImgWidth / 2 && event.getX() < width / 2 + totalImgWidth / 2) {
                                    if (Crate.lockerAbilities.contains(gameAbility1.name)) {
                                        isLock = true;
                                        abil1Lock = true;
                                        if (gameAbility1.flag)
                                            Crate.animator.isLockerAbility = true;
                                    } else gameAbility1.use(event.getX(), event.getY(), obstImg);
                                } else if (event.getX() > width - totalImgWidth) {
                                    if (Crate.lockerAbilities.contains(gameAbility3.name)) {
                                        isLock = true;
                                        abil3Lock = true;
                                        if (gameAbility3.flag)
                                            Crate.animator.isLockerAbility = true;
                                    } else gameAbility3.use(event.getX(), event.getY(), obstImg);
                                }
                            }
                /*
                else
                    {
                        if (abil1Lock) {
                            isLock = false;
                            abil1Lock = false;
                            Crate.animator.isLockerAbility = false;
                        }
                        if (abil2Lock) {
                            isLock = false;
                            abil2Lock = false;
                            Crate.animator.isLockerAbility = false;
                        }
                        if (abil3Lock) {
                            isLock = false;
                            abil3Lock = false;
                            Crate.animator.isLockerAbility = false;
                        }
                    }
                 */
                        }
                    }
                }
                return true;
            }
        });
    }

    static boolean playersInversed = false;

    boolean isLock = false;
    boolean abil1Lock = false;
    boolean abil2Lock = false;
    boolean abil3Lock = false;

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        onSVDestroy  = true;
        while (retry) {
            try {
                drawerThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
            if(Crate.leavedGame)
            {
                //Если проихошёл выход из игры - оннулировать всё
                Crate.obstacles = new ArrayList<>();
                Crate.bots = new ArrayList<>();
                Crate.balls = Collections.synchronizedList(new ArrayList<Ball>());
                Crate.leftPt = 0;
                Crate.rightPt = 0;
                Crate.time = 300000;
                Crate.animator = new Animator();
            }
        }
    }

    public static boolean onSVDestroy; //TODO все похожие статики надо будет заменить и получать SV через findviewbyid

    Bitmap obstImg = BitmapFactory.decodeResource(getResources(), R.drawable.event_obstacle);
    Bitmap mgObstImg = BitmapFactory.decodeResource(getResources(), R.drawable.minigame_event_obstacle);
    Bitmap playerImg;

    public Player p2;

    Bitmap ability1;
    Bitmap ability2;
    Bitmap ability3;
    Bitmap blurImg = BitmapFactory.decodeResource(getResources(), R.drawable.res);

    AbilityIcons abil1;
    AbilityIcons abil2;
    AbilityIcons abil3;

    float totalImgWidth;

    private boolean isPaused = false;

    GameClient client;

    class DrawerThread extends Thread {
        @Override
        public void run() {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawRGB(0, 0, 0);
            height = canvas.getHeight() - canvas.getHeight() / 6;
            width = canvas.getWidth();

            abil1 = new AbilityIcons(width / 2 - ability1.getWidth() / 2 * (height - ability3.getHeight()) / height, height - ability1.getHeight() * ((height - ability3.getHeight()) / height) + ability1.getHeight() * ((height - ability3.getHeight()) / height), (height - ability1.getHeight()) / height, ability1);
            abil2 = new AbilityIcons(0, height - ability1.getHeight() * ((height - ability3.getHeight()) / height) + ability1.getHeight() * ((height - ability3.getHeight()) / height), (height - ability2.getHeight()) / height, ability2);
            abil3 = new AbilityIcons(width - ability1.getWidth() * (height - ability3.getHeight()) / height, height - ability1.getHeight() * ((height - ability3.getHeight()) / height) + ability1.getHeight() * ((height - ability3.getHeight()) / height), (height - ability3.getHeight()) / height, ability3);

            totalImgWidth = ability1.getWidth() * abil1.sk;

            skx = canvas.getWidth()/1280.0f; //TODO абсолютно все процессы должны быть под кооф-ом., потому что иначе тот же мяч на каком-нибудь огромном планшете будет двигаться медленно и соответствуя размеру маленького телефона.
            sky = canvas.getHeight()/670.0f;

            Paint lineTillPowers = new Paint();
            lineTillPowers.setARGB(255, 53, 105, 64);

            Obstacle.setAppSize(height, width);

            Player p1;

            DataToSend dts = null;

            while(dts == null)
            {
                if(client.dts != null) {
                    dts = client.dts;
                }
                try {
                    Thread.sleep(0,200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            p1 = new Player(client.dts.p1.x,client.dts.p1.y, client.dts.p1.width, client.dts.p1.height, BitmapFactory.decodeResource(getResources(), Crate.panelIds[client.dts.p1Imgi]));
            p2 = new Player(client.dts.p2.x,client.dts.p2.y, client.dts.p2.width, client.dts.p2.height, playerImg);

            dts = new DataToSend();
            dts.p2Imgi = Crate.playerBitmapIndex;
            client.sendDataToServer(dts);

            Crate.animator.obstSpawn(p1);
            Crate.animator.obstSpawn(p2);

            Paint abilityPaint = new Paint();
            abilityPaint.setAlpha(255);

            Paint darkPaint = new Paint();
            darkPaint.setAlpha(0);

            gameAbility1 = new Ability(Crate.abilities[0], p1, p2,skx, sky);
            gameAbility2 = new Ability(Crate.abilities[1], p1, p2,skx, sky);
            gameAbility3 = new Ability(Crate.abilities[2], p1, p2,skx, sky);

            Crate.animator.setAppSize(height, width);

            surfaceHolder.unlockCanvasAndPost(canvas);
            while (Crate.time >= 0 && !onSVDestroy) {
                if (!Crate.isSuspended) {
                    isPaused = false;
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        synchronized (canvas) {
                            canvas.drawRGB(0, 0, 0);
                            dts = null;
                            while(dts == null) //Ожидаение получения dts от сервера
                            {
                                dts = client.dts;
                                try {
                                    Thread.sleep(0,200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                             //синхронизация от  сервера
                            Crate.time = dts.time;
                            Crate.rightPt = dts.rightPt;
                            Crate.leftPt = dts.leftPt;

                            syncLists(Crate.obstacles, dts.obstacles);
                            syncLists(Crate.balls, dts.balls);

                            gameAbility1.check();
                            gameAbility2.check();
                            gameAbility3.check();

                            for (int i = 0; i < Crate.balls.size(); i++) {
                                Crate.balls.get(i).draw(canvas);
                            }

                            for (int i = 0; i < Crate.obstacles.size(); i++) {
                                Crate.obstacles.get(i).draw(canvas);
                            }

                            p2.destChecker();
                            p2.move();

                            Crate.animator.animationCheck(canvas, p1, p2, BitmapFactory.decodeResource(getResources(), R.drawable.smoke_unit), BitmapFactory.decodeResource(getResources(), R.drawable.event_marker), BitmapFactory.decodeResource(getResources(), R.drawable.global_event_marker));

                            canvas.drawRect(0, height, width, canvas.getHeight(), darkPaint);
                            canvas.drawLine(0, height - 5, width, height - 5, lineTillPowers);
                            canvas.drawLine(0, height - 1, width, height - 1, lineTillPowers);

                            abil1.drawAbility(canvas);
                            abil2.drawAbility(canvas);
                            abil3.drawAbility(canvas);

                            if (!gameAbility1.flag) {
                                abil1.onCooldown(canvas, blurImg);
                            }
                            if (!gameAbility2.flag) {
                                abil2.onCooldown(canvas, blurImg);
                            }
                            if (!gameAbility3.flag) {
                                abil3.onCooldown(canvas, blurImg);
                            }

                            //компановка всех изменений и отправка обратно

                            dts = new DataToSend();
                            dts.balls = Crate.balls;
                            dts.obstacles = new ArrayList<>();
                            for(Obstacle obst : Crate.obstacles)
                            {
                                DataToSend.ObstacleData od = new DataToSend.ObstacleData();
                                od.x = obst.x;
                                od.y = obst.y;
                                od.width = obst.width;
                                od.height = obst.height;
                                dts.obstacles.add(od);
                            }

                            client.sendDataToServer(dts);
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
                else
                {
                    if(!isPaused) {
                        canvas = surfaceHolder.lockCanvas();
                        canvas.drawARGB(168, 0, 0, 0);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        isPaused = true;
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    /**
     * Проверяет, есть ли во втором переданном списке какие-либо новые Obj. Если есть - добавить их в первый список и сделать анимацию.
     * @param list1 изменяемый текущий
     * @param list2 сверяемый полученный
     */
    void syncLists(List list1, List list2)
    {
        if(list1.size() < list2.size()) //если полученный больше текущего
        {
            int delta = list2.size() - list1.size();
            for(int i = 0; i < delta; i++)
            {
                Object o = list2.get(0);
                if(o instanceof Obstacle) {
                    DataToSend.ObstacleData od = (DataToSend.ObstacleData) list2.get(list2.size() - i - 1);
                    Obstacle obstacle = new Obstacle(od.x, od.y, od.width, od.height, obstImg);
                    Crate.animator.obstSpawn(obstacle);
                    list1.add(obstacle);
                }
                else if(o instanceof Ball)
                {
                    list1.add(list2.get(list2.size() - i - 1));
                }
            }
        }
        else if(list1.size() > list2.size())
        {
            int delta = list1.size() - list2.size() ;
            for(int i = 0; i < delta; i++)
            {
                Object o = list1.get(0);
                if(o instanceof Ball) {
                    Crate.animator.addDiedBall((Ball) list1.get(list1.size() - i - 1));
                    list1.remove(list1.size() - 1);
                }
                else if(o instanceof Obstacle)
                {
                    list1.remove(list1.size() - 1);
                }
            }
        }

        if(list1.size() != 0)
        {
            Object o = list1.get(0);
            if(o instanceof Ball)
            {
                for(int i = 0; i < list1.size(); i++)
                {
                    Ball ball = (Ball) list1.get(i);
                    Ball toBeCloned = (Ball) list2.get(i);
                    ball.x = toBeCloned.x;
                    ball.y = toBeCloned.y; //TODO подвержено размерам экрана и SK
                }
            }
            else if(o instanceof Obstacle)
            {
                for(int i = 0; i < list1.size(); i++)
                {
                    if(i != 1) {
                        Obstacle obst = (Obstacle) list1.get(i);
                        DataToSend.ObstacleData toBeCloned = (DataToSend.ObstacleData) list2.get(i);
                        obst.x = toBeCloned.x;
                        obst.y = toBeCloned.y; //TODO подвержено размерам экрана и SK
                    }
                }
            }
        }
    }
}

