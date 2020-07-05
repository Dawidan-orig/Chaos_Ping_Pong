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
import com.example.chaos_ping_pong.Eventer;
import com.example.chaos_ping_pong.Obstacle;
import com.example.chaos_ping_pong.Player;
import com.example.chaos_ping_pong.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HostDrawer extends SurfaceView implements SurfaceHolder.Callback {
    Canvas canvas;
    SurfaceHolder surfaceHolder;
    float width;
    float height;

    Ability gameAbility1;
    Ability gameAbility2;
    Ability gameAbility3;

    public HostDrawer(Context context) {
        super(context);
        getHolder().addCallback(this);
        init();
    }

    public HostDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        init();
    }

    public HostDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        init();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    HostDrawer.Timer timer;
    HostDrawer.DrawerThread drawerThread;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.playerImg = Crate.playerImg;
        this.ability1 = Crate.ability1;
        this.ability2 = Crate.ability2;
        this.ability3 = Crate.ability3;
        onSVDestroy = false;
        Crate.isSuspended = false;
        Crate.leavedGame = false;

        HostDrawer.DrawerThread dt = new HostDrawer.DrawerThread();
        surfaceHolder = holder;
        HostDrawer.Timer timer = new HostDrawer.Timer();
        timer.start();
        dt.start();
        this.drawerThread = dt;
        this.timer = timer;
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
                                    p1.setDestY(destY - p1.height / 2);
                                } else p1.setDestY(height - destY - p1.height / 2);
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

    class Timer extends Thread {
        @Override
        public void run() {
            while (Crate.time >= 0 && !onSVDestroy) {
                if(!Crate.isSuspended) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Crate.time--;
                }
                else
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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
                timer.join();
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
                eventer = null;
                secEventer = null;
            }
            else fakeLeave = true;
        }
    }

    public static boolean onSVDestroy; //TODO все похожие статики надо будет заменить и получать SV через findviewbyid
    private boolean fakeLeave = false;

    Bitmap obstImg = BitmapFactory.decodeResource(getResources(), R.drawable.event_obstacle);
    Bitmap mgObstImg = BitmapFactory.decodeResource(getResources(), R.drawable.minigame_event_obstacle);
    Bitmap playerImg;

    public Player p1;
    public Player p2;

    Bitmap ability1;
    Bitmap ability2;
    Bitmap ability3;
    Bitmap blurImg = BitmapFactory.decodeResource(getResources(), R.drawable.res);

    AbilityIcons abil1;
    AbilityIcons abil2;
    AbilityIcons abil3;

    float totalImgWidth;

    static boolean doubleEvents = false;

    private boolean isPaused = false;
    Bitmap enemyImg;

    GameServer server;

    float skx;
    float sky;

    //TODO Все изменения отсюда - в сетевую часть тоже надо переносить!

    static Eventer eventer;
    static Eventer secEventer;

    class DrawerThread extends Thread {
        @Override
        public void run() {
            server = Crate.server;
            server.dts = null;

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

            Ball ball = new Ball(width / 2, height / 2, 5);
            Crate.balls.add(ball);

            p1 = new Player(width / 8 + 15, height / 2 - 30, 30, 120, playerImg); //30 - это height - или в данном случае 60 пополам
            Crate.animator.obstSpawn(p1);
            p2 = new Player(width - width / 8 - 15, height / 2 - 30, 30, 120, playerImg);
            Crate.animator.obstSpawn(p2);

            DataToSend dts = new DataToSend();
            dts.p1.x = p1.x;
            dts.p1.y = p1.y;
            dts.p1.width = p1.width;
            dts.p1.height = p1.height;
            dts.p1Imgi = Crate.playerBitmapIndex;

            dts.p2.x = p2.x;
            dts.p2.y = p2.y;
            dts.p2.width = p2.width;
            dts.p2.height = p2.height;
            server.sendDataToClient(dts); //TODO отправить клиенту данные о размере экрана, чтобы он мог подстроиться под них

            dts = null;
            while(dts == null) //TODO Ожидание получения dts --- заменить на проверку "мимоходом"
            {
                if(server.dts != null) dts = server.dts;
                try {
                    Thread.sleep(0, 200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            p2.setBitmap(BitmapFactory.decodeResource(getResources(), Crate.panelIds[dts.p2Imgi]));

            Paint abilityPaint = new Paint();
            abilityPaint.setAlpha(255);

            Paint darkPaint = new Paint();
            darkPaint.setAlpha(0);

            gameAbility1 = new Ability(Crate.abilities[0], p1, p2,skx, sky);
            gameAbility2 = new Ability(Crate.abilities[1], p1, p2,skx, sky);
            gameAbility3 = new Ability(Crate.abilities[2], p1, p2,skx, sky);

            Crate.animator.setAppSize(height, width);

            //TODO мини-штуки из середины --- до сдачи делать не буду.


            if(!fakeLeave) {
                eventer = new Eventer(p1, p2, ball, width, height, obstImg, mgObstImg);
                secEventer = null;
            }
            fakeLeave = false;

            surfaceHolder.unlockCanvasAndPost(canvas);
            while (Crate.time >= 0 && !onSVDestroy) {
                if (!Crate.isSuspended) {
                    isPaused = false;
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        synchronized (canvas) {
                            canvas.drawRGB(0, 0, 0);
                            gameAbility1.check();
                            gameAbility2.check();
                            gameAbility3.check();

                            for (int i = 0; i < Crate.balls.size(); i++) {
                                Crate.balls.get(i).draw(canvas);
                            }

                            p1.destChecker();

                            for (Player player : Crate.bots) {
                                player.botCheck();
                            }

                            synchronized (Crate.balls) {
                                for (Ball ballItr : Crate.balls) {
                                    ballItr.inverseCheck();
                                    ballItr.move();
                                }
                            }

                            for (int i = 0; i < Crate.obstacles.size(); i++) {
                                Crate.obstacles.get(i).move();
                                Crate.obstacles.get(i).draw(canvas);
                            }

                            if (doubleEvents && secEventer == null)
                                secEventer = new Eventer(p1, p2, ball, width, height, obstImg, mgObstImg);

                            eventer.timer();
                            eventer.globalTimer();
                            eventer.check();
                            if (secEventer != null) {
                                secEventer.timer();
                                secEventer.globalTimer();
                                secEventer.check();
                            }

                            Crate.animator.animationCheck(canvas,  p1, p2, BitmapFactory.decodeResource(getResources(), R.drawable.smoke_unit), BitmapFactory.decodeResource(getResources(), R.drawable.event_marker), BitmapFactory.decodeResource(getResources(), R.drawable.global_event_marker));


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

                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            //Отправка всех результатов вычислений

                            //Компоновка и отправка dts с базовыми данными.
                            dts = new DataToSend();
                            dts.leftPt = Crate.leftPt;
                            dts.rightPt = Crate.rightPt;
                            dts.time = Crate.time;
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

                            server.sendDataToClient(dts);

                            /*TODO
                             * Аниматор: локален.
                             * Если хост сам сможет всё это преспокойно рисовать, то уже клиент:
                             * Рисует и вычисляет сам: След от всех мячей; след от всех ботов;
                             * Просчитывает сам: Маркеры событий; проверяет на отпавку новых препятствий (checkForNew());
                             */

                            //TODO Как вариант - сделать отдельный класс с данными использованных способностей. - проверять их... мимоходом.


                            dts = null;
                            while(dts == null) //TODO Ожидание получения dts --- заменить на проверку "мимоходом"
                            {
                                if(server.dts != null) dts = server.dts;
                                try {
                                    Thread.sleep(0, 200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            syncLists(Crate.obstacles, dts.obstacles);
                            syncLists(Crate.balls, dts.balls);

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

        if(list1.size() != 0) //TODO сделать два класса локальных аниматоров; Хост вычисляет, клиент - только рисует.
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
                    if(i != 0) {
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