package com.example.chaos_ping_pong;

import android.graphics.Bitmap;

import java.util.LinkedList;
import java.util.Random;

    public class Eventer {
        private Player p1, p2;
        private Ball ball;
        private float appWidth, appHeight;
        private Bitmap obstImg;
        private Bitmap mgObstImage;
        private Random rand = new Random();

        private int eventCooldown;
        private int gEventCooldown;

        public Eventer(Player p1, Player p2, Ball ball, float appWidth, float appHeight, Bitmap obstImg, Bitmap mgObstImage) {
            this.p1 = p1;
            this.p2 = p2;
            this.ball = ball;
            this.obstImg = obstImg;
            this.mgObstImage = mgObstImage;
            this.appHeight = appHeight;
            this.appWidth = appWidth;
            eventCooldown = Crate.time - 15000;
            gEventCooldown = Crate.time - 60000;
        }

        public void timer() {
            if (eventCooldown > Crate.time) {

                startEvent();
                for (Ball ball : Crate.balls) {
                    ball.setSpeed(ball.vx + 0.15f, ball.vy + 0.15f);
                }
                eventCooldown = Crate.time - 15000;
            }
        }

        public void globalTimer() {

            if (gEventCooldown > Crate.time) {
                startGlobal();
                gEventCooldown = Crate.time - 60000;
            }
        }


        private LinkedList<Obstacle> obstacles = new LinkedList<>();
        private LinkedList<Player> movings = new LinkedList<>();
        private LinkedList<Player> eventAiEasyMidList = new LinkedList<>();
        private LinkedList<Ball> balls = new LinkedList<>();

        void startEvent() {
            int index = rand.nextInt(10);
            //index = 0;
            switch (index) {
                case 0: {
                    //банальное ускорение шариков
                    for (int i = 0; i < Crate.balls.size(); i++) {
                        Crate.balls.get(i).setSpeed(Crate.balls.get(i).vx * 1.5f, Crate.balls.get(i).vy * 1.5f);
                    }
                    new Checker(20000, "speedBalls").start(); //20000
                    speedBall = true;
                    break;
                }
                case 1: {
                    //+3 препятствия
                    Obstacle a = new Obstacle(rand.nextInt((int) Math.abs(appWidth)) - 7.5f, rand.nextInt((int) Math.abs(appHeight)), 30, 120, obstImg);
                    Obstacle b = new Obstacle(rand.nextInt((int) Math.abs(appWidth)) - 7.5f, rand.nextInt((int) Math.abs(appHeight)), 30, 120, obstImg);
                    Obstacle c = new Obstacle(rand.nextInt((int) Math.abs(appWidth)) - 7.5f, rand.nextInt((int) Math.abs(appHeight)), 30, 120, obstImg);
                    if (a.y + a.height > appHeight) {
                        a.y = appHeight - a.height;
                    }
                    if (b.y + b.height > appHeight) {
                        b.y = appHeight - b.height;
                    }
                    if (c.y + c.height > appHeight) {
                        c.y = appHeight - c.height;
                    }
                    Crate.obstacles.add(a);
                    Crate.obstacles.add(b);
                    Crate.obstacles.add(c);
                    obstacles.offer(a);
                    obstacles.offer(b);
                    obstacles.offer(c);
                    Crate.soundPool.play(Crate.s_ID_onSpawn, 0.3f,0.3f,0,0,1);
                    new Checker(35000, "moreObst").start(); // 25000
                    break;
                }
                case 2: {
                    //Easy lvl AI на середине
                    Player eventAiEasyMid = new Player(appWidth / 2, appHeight / 2 - 30, 30, 120, obstImg, "easy", 28500); //28500
                    eventAiEasyMidList.offer(eventAiEasyMid);
                    new Checker(30000, "eventAiEasyMid").start();
                    Crate.animator.trailObsts.add(eventAiEasyMid);
                    Crate.soundPool.play(Crate.s_ID_onSpawn, 0.3f,0.3f,0,0,1);
                    break;
                }
                case 3: {
                    //невидимые платформы
                    p1.makeInvisible();
                    p2.makeInvisible();
                    new Checker(10000, "pInvis").start();
                    break;
                }
                case 4: {
                    //Телепорт (не структура) один раз
                    for (int i = 0; i < Crate.balls.size(); i++) {
                        float stopx = rand.nextInt((int) appWidth);
                        float stopY = rand.nextInt((int) appHeight);
                        Crate.animator.setTeleportLineData(Crate.balls.get(i).x, stopx, Crate.balls.get(i).y, stopY);
                        Crate.balls.get(i).x = stopx;
                        Crate.balls.get(i).y = stopY;
                    }
                    Crate.soundPool.play(Crate.s_ID_teleport, 1f,1f,0,0,1);
                    break;
                }
                case 5: {
                    //движущийся obst
                    Player moving = new Player(appWidth / 2, appHeight / 2 - 30, 30, 120, obstImg, "random", 28500); //28500
                    Player moving2 = new Player(appWidth / 2, appHeight / 2 - 30, 30, 120, obstImg, "random", 28500);
                    movings.offer(moving);
                    movings.offer(moving2);
                    new Checker(30000, "moveObst").start(); //30000
                    Crate.animator.trailObsts.add(moving);
                    Crate.animator.trailObsts.add(moving2);
                    Crate.soundPool.play(Crate.s_ID_onSpawn, 0.3f,0.3f,0,0,1);
                    break;
                }
                case 6: {
                    //TODO парный арканоид. за каждые три сломанных кирпича - по одному очку. спавнится два шарика летящие из и от платформ. Им ещё цвета надо разные дать
                }
                case 7: {

                    //спавн ещё 2-х шариков на 30 сек.
                    Ball ba = new Ball(appWidth / 2, appHeight / 2, 5, false);
                    Crate.balls.add(ba);
                    Ball bb = new Ball(appWidth / 2, appHeight / 2, 5, false);
                    Crate.balls.add(bb);
                    ba.setColor(ball.getColorRed(), ball.getColorGreen(), ball.getColorBlue());
                    bb.setColor(ball.getColorRed(), ball.getColorGreen(), ball.getColorBlue());
                    balls.offer(ba);
                    balls.offer(bb);
                    new Checker(30000, "moreBalls").start(); //30000
                    break;


                }
                case 8: {
                    //массовые телепорты шариков
                    massTeleporting = true;
                    new Checker(5000, "massTeleport").start();
                    break;
                }
                case 9: {
                    //шарики начинают постоянно менять цвет
                    colorChanger = true;
                    new Checker(20000, "colorChanger").start();
                    break;
                }
                //идея - нашествие фейковых шариков
                //иконки способностей перемешиваются
                //игра превращается в настольгый футболл на минуту (мини-игра) --- на сетевую игру
                //здоровенный obstSpawn
                //2 дыма на карту
                //мигающий шарик - каждый промежуток времени он исчезает, а потом появляется
            }

        }

        void startGlobal() {
            int index = rand.nextInt(4);
            //index = 3;
            switch (index) {
                case 0:
                    //разом 3 обычных ивента
                    startEvent();
                    startEvent();
                    break;
                case 1:
                    //инвертирование управления
                    DrawerSingle.playersInversed = !DrawerSingle.playersInversed;
                    break;
                case 2:
                    //спавн ещё одного вечного шарика
                    Ball ball = new Ball(appWidth / 2, appHeight / 2, 5);
                    Crate.balls.add(ball);
                    break;
                case 3:
                    //Двойные ивенты
                    DrawerSingle.doubleEvents = true;
                    break;
                case 4:
                    //переворот экрана
                    //TODO setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); - не робит нигде, кроме onCreate. сохранение состояния? - а то при даже перевороте оно с самого начала перезапустится
                    break;
            }
        }
        //идея - сужение поля - ?
        //Внезапные Титры - через аниматор
        //платформа едет до предела, как в оригинале.

        public void check() {
            colorfulMadness();
            massTeleporting();
            speedBallPSpawn();
        }

        private boolean massTeleporting = false;
        private boolean colorChanger = false;
        private boolean speedBall = false;

        private void colorfulMadness() {

            if (colorChanger && cmCooldown > Crate.time) {
                int ballIndex = rand.nextInt(Crate.balls.size());

                Crate.balls.get(ballIndex).setColor(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));

                cmCooldown = Crate.time - 300;
            }

        }

        private int mtCooldown = 300000;
        private int cmCooldown = 300000;
        private int sbpsCooldown=300000;

        private void massTeleporting() {

            if (massTeleporting && mtCooldown > Crate.time) {
                int ballIndexToTeleport = rand.nextInt(Crate.balls.size());

                float stopx = rand.nextInt((int) appWidth);
                float stopY = rand.nextInt((int) appHeight - 5);
                Crate.animator.setTeleportLineData(Crate.balls.get(ballIndexToTeleport).x, stopx, Crate.balls.get(ballIndexToTeleport).y, stopY);
                Crate.balls.get(ballIndexToTeleport).x = stopx;
                Crate.balls.get(ballIndexToTeleport).y = stopY;

                Crate.soundPool.play(Crate.s_ID_teleport, 0.8f,0.8f,0,0,1.2f);

                mtCooldown = Crate.time - 300;
            }
        }

        private void speedBallPSpawn() {
            if (speedBall && sbpsCooldown > Crate.time) {
                for(Ball ball : Crate.balls) {
                    new Particle(ball.x, ball.y, 30, 1.5f, 5, 237, 145, 33);
                    sbpsCooldown = Crate.time - 560;
                }
            }
        }

        class Checker extends Thread {
            int lifetime;
            String eventName;

            Checker(int lifetime, String name) {
                this.lifetime = lifetime;
                this.eventName = name;
            }

            public void run() {
                try {
                    Thread.sleep(lifetime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (eventName.equals("speedBalls")) {
                    for (int i = 0; i < Crate.balls.size(); i++) {
                        Crate.balls.get(i).setSpeed(Crate.balls.get(i).vx / 1.5f, Crate.balls.get(i).vy / 1.5f);
                    }
                    speedBall = false;
                }
                if (eventName.equals("moreObst")) {
                    Crate.obstacles.remove(obstacles.poll());
                    Crate.obstacles.remove(obstacles.poll());
                    Crate.obstacles.remove(obstacles.poll());
                }
                if (eventName.equals("eventAiEasyMid")) {
                    Crate.animator.killTrail(eventAiEasyMidList.poll());
                }
                if (eventName.equals("moveObst")) {
                    Crate.animator.killTrail(movings.poll());
                    Crate.animator.killTrail(movings.poll());
                }
                if (eventName.equals("moreBalls")) {
                    Ball a, b;
                    a = balls.poll();
                    b = balls.poll();
                    Crate.animator.addDiedBall(a);
                    Crate.animator.addDiedBall(b);
                    Crate.balls.remove(a);
                    Crate.balls.remove(b);
                }
                if (eventName.equals("pInvis")) {
                    p1.makeVisible();
                    p2.makeVisible();
                }
                if (eventName.equals("massTeleport")) massTeleporting = false;
                if (eventName.equals("colorChanger")) colorChanger = false;
            }
        }
    }