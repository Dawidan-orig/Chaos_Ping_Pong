package com.example.chaos_ping_pong;

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

import java.util.ArrayList;
import java.util.Collections;

import static com.example.chaos_ping_pong.Crate.skx;
import static com.example.chaos_ping_pong.Crate.sky;

public class DrawerSingle extends SurfaceView implements SurfaceHolder.Callback {
    Canvas canvas;
    SurfaceHolder surfaceHolder;
    float width;
    float height;
    String difficulty;
//TODO сделать аниматор отдельныи потоком, а все вычисления перенисти только на этот класс
    Ability gameAbility1;
    Ability gameAbility2;
    Ability gameAbility3;

    public DrawerSingle(Context context) {
        super(context);
        getHolder().addCallback(this);
        init();
    }

    public DrawerSingle(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        init();
    }

    public DrawerSingle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        init();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    Timer timer;
    DrawerThread drawerThread;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.difficulty = Crate.difficulty;
        this.playerImg = Crate.playerImg;
        this.ability1 = Crate.ability1;
        this.ability2 = Crate.ability2;
        this.ability3 = Crate.ability3;
        onSVDestroy = false;
        Crate.isSuspended = false;
        Crate.leavedGame = false;

        DrawerThread dt = new DrawerThread();
        surfaceHolder = holder;
        Timer timer = new Timer();
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

    static boolean onSVDestroy; //TODO все похожие статики надо будет заменить и получать SV через findviewbyid
    private boolean fakeLeave = false;

    Bitmap obstImg = BitmapFactory.decodeResource(getResources(), R.drawable.event_obstacle);
    Bitmap mgObstImg = BitmapFactory.decodeResource(getResources(), R.drawable.minigame_event_obstacle);
    Bitmap playerImg;

    public Player p1;
    public Player ai;

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

    //TODO Все изменения отсюда - в сетевую часть тоже надо переносить!

    static Eventer eventer;
    static Eventer secEventer;

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

            skx = width/1280.0f;
            sky = canvas.getHeight()/670.0f;

            Paint lineTillPowers = new Paint();
            lineTillPowers.setARGB(255, 53, 105, 64);

            Obstacle.setAppSize(height, width);

            Ball ball;
            if(!fakeLeave) {
                ball = new Ball(1280.0f / 2, 670.0f / 2, 5);
                Crate.balls.add(ball);
            }
            else ball = Crate.balls.get(0);

            if(!fakeLeave) {
                p1 = new Player((1280.0f / 8 - 15)*skx, (670.0f / 2 - 60)*sky, 30, 120, playerImg);
                ai = new Player((1280.0f - 1280.0f / 8 - 15)*skx, (670.0f / 2 - 60)*sky, 30, 120, playerImg, difficulty);

                Crate.animator.obstSpawn(p1);
                Crate.animator.obstSpawn(ai);
            }
            else
            {
                p1 =(Player)Crate.obstacles.get(0);
                ai = Crate.bots.get(0);
            }

            Paint abilityPaint = new Paint();
            abilityPaint.setAlpha(255);

            Paint darkPaint = new Paint();
            darkPaint.setAlpha(0);

            gameAbility1 = new Ability(Crate.abilities[0], p1, ai,skx, sky);
            gameAbility2 = new Ability(Crate.abilities[1], p1, ai,skx, sky);
            gameAbility3 = new Ability(Crate.abilities[2], p1, ai,skx, sky);

            Crate.animator.setAppSize(height, width);

            //TODO мини-штуки из середины --- до сдачи делать не буду.


            if(!fakeLeave) {
                eventer = new Eventer(p1, ai, ball, width, height, obstImg, mgObstImg);
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

                            p1.draw(canvas);
                            for (int i = 0; i < Crate.balls.size(); i++) {
                                Crate.balls.get(i).draw(canvas);
                            }

                            p1.destChecker();
                            ai.draw(canvas);

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
                                secEventer = new Eventer(p1, ai, ball, width, height, obstImg, mgObstImg);

                            eventer.timer();
                            eventer.globalTimer();
                            eventer.check();
                            if (secEventer != null) {
                                secEventer.timer();
                                secEventer.globalTimer();
                                secEventer.check();
                            }

                            Crate.animator.animationCheck(canvas, p1, ai, BitmapFactory.decodeResource(getResources(), R.drawable.smoke_unit), BitmapFactory.decodeResource(getResources(), R.drawable.event_marker), BitmapFactory.decodeResource(getResources(), R.drawable.global_event_marker));


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
}

