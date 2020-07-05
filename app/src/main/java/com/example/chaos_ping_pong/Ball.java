package com.example.chaos_ping_pong;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

import static com.example.chaos_ping_pong.Crate.skx;
import static com.example.chaos_ping_pong.Crate.sky;

public class Ball {
    public float x, y;
    float vx = 0, vy = 0;
    public float radius;
    Boolean isInvis = false;
    private int red = 255, green = 165, blue = 0;

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setARGB(255, red, green, blue);
        if(!isInvis && !isDisabled) {
            canvas.drawCircle(x, y, radius, paint);
        }
    }

    void setColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    int getColorRed() {
        return red;
    }

    int getColorGreen() {
        return green;
    }

    int getColorBlue() {
        return blue;
    }

    void setSpeed(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public void move() {
        if (!isDisabled) {
            x += vx*skx;
            y += vy*sky;
        }
    }

    public Ball() {}

    public Ball(float x, float y, float radius) {
        Random rand = new Random();
        int k = rand.nextInt() % 4;
        k = Math.abs(k);
        if (k == 0) {
            setSpeed(3.5f, 3.5f);
        } else if (k == 1) setSpeed(3.5f, -3.5f);
        else if (k == 2) setSpeed(-3.5f, 3.5f);
        else setSpeed(-3.5f, -3.5f);
        this.x = x * skx;
        this.y = y * sky;
        this.radius = radius * skx;
    }

    private Boolean isNonPlaying = false;

    public Ball(float x, float y, float radius, Boolean isNonPlaying) {
        Random rand = new Random();
        if (isNonPlaying) {
            float speed = rand.nextFloat() * 100 % 6f*skx;
            int k = rand.nextInt() % 4;
            k = Math.abs(k);
            if (k == 0) {
                setSpeed(speed, speed);
            } else if (k == 1) setSpeed(speed, speed * -1);
            else if (k == 2) setSpeed(speed * -1, speed);
            else setSpeed(speed * -1, speed * -1);
        }
        else {
            float speed = rand.nextInt(50) /10;
            if(speed < 2) speed = 2;
            int k = rand.nextInt() % 4;
            k = Math.abs(k);
            if (k == 0) setSpeed(speed, speed);
            else if (k == 1) setSpeed(speed, speed * -1);
            else if (k == 2) setSpeed(speed * -1, speed);
            else setSpeed(speed * -1, speed * -1);
        }
        this.x = x * skx;
        this.y = y * sky;
        this.radius = radius * skx;
        this.isNonPlaying = isNonPlaying;
    }

    private Boolean isDisabled = false;

    void disable() {
        isDisabled = true;
        isInvis = true;
    }

    void respawn(float x, float y) {
        this.x = x;
        this.y = y;
        isDisabled =false;
    }

    public void inverseCheck() { //TODO определять часть экрана для звука
        if (y + vy + radius > Obstacle.appHeight || y + vy + radius < 0) {
            setSpeed(vx, vy * -1);
            if(!isNonPlaying) Crate.soundPool.play(Crate.s_ID_downBall,0.1f, 0.1f,0,0, 1);
        }
        if (x + vx > Obstacle.appWidth) {
            setSpeed(vx * -1, vy);
            if (!isNonPlaying) {
                Crate.leftPt++;
                Crate.soundPool.play(Crate.s_ID_upBall, 0.5f, 0.5f, 0, 0, 0.4f);
            }
        }
        if (x + vx < 0) {
            setSpeed(vx * -1, vy);
            if (!isNonPlaying) {
                Crate.rightPt++;
                Crate.soundPool.play(Crate.s_ID_upBall, 0.5f, 0.5f, 0, 0, 0.4f);
            }
        }

        //TODO оптимизация кода: если мячик резко сменил своё местоположение больше чем наибольшая скорость по оси + 1 - то он считается телепортированным.

        obstInverser();
    }

    private void obstInverser() {
        boolean isReverse = false;
        boolean isReverseUp = false;
        boolean isReverseDown = false;

        for (int i = 0; i < Crate.obstacles.size(); i++) {
            if ((Crate.obstacles.get(i).x < x + vx + radius && Crate.obstacles.get(i).x + Crate.obstacles.get(i).width > x + vx + radius) &&
                    (y + vy + radius > Crate.obstacles.get(i).y + Crate.obstacles.get(i).height / 3 &&
                            y + vy + radius < Crate.obstacles.get(i).y + Crate.obstacles.get(i).height * 2 / 3)) {
                isReverse = true;
            } else if ((Crate.obstacles.get(i).x < x + vx + radius && Crate.obstacles.get(i).x + Crate.obstacles.get(i).width > x + vx + radius) &&
                    (y + vy + radius > Crate.obstacles.get(i).y &&
                            y + vy + radius < Crate.obstacles.get(i).y + Crate.obstacles.get(i).height / 3)) {
                isReverseUp = true;
            } else if ((Crate.obstacles.get(i).x < x + vx + radius && Crate.obstacles.get(i).x + Crate.obstacles.get(i).width > x + vx + radius) &&
                    (y + vy + radius > Crate.obstacles.get(i).y + Crate.obstacles.get(i).height * 2 / 3 &&
                            y + vy + radius < Crate.obstacles.get(i).y + Crate.obstacles.get(i).height)) {
                isReverseDown = true;
            }
        }
        if (isReverse) {
            setSpeed(vx * -1, vy); //TODO Определенить нахождения мяча слева и справа, чтобы звук сделать красивее
            Crate.soundPool.play(Crate.s_ID_midBall, 0.3f,0.3f,0,0,1);
        } else if (isReverseUp) {
            rotate(-20);

            setSpeed(vx * -1, vy);
            Crate.soundPool.play(Crate.s_ID_upBall, 0.6f,0.6f,0,0,1);

            if(!isSpeedChecking) {
                new speedChecker().start();
            }
        } else if (isReverseDown) {
            rotate(20);

            setSpeed(vx * -1, vy);
            if(!isSpeedChecking) {
                new speedChecker().start();
            }
            Crate.soundPool.play(Crate.s_ID_upBall, 0.6f,0.6f,0,0,1);
        }

    }

    private void rotate(double a) {
        a = a * Math.PI / 180;
        double xr = vx * Math.cos(a) - vy * Math.sin(a);
        double yr = vy * Math.cos(a) + vx * Math.sin(a);
        this.vx = (float) xr;
        this.vy = (float) yr;
    }

    private boolean isSlowed = false;
    private boolean isSpeedChecking = false;

    class speedChecker extends Thread {
        @Override
        public void run() {

                if (isSlowed) {
                    isSlowed = false;
                    vy /= 3;
                    vx /= 3;
                }
                if (Math.abs(vy) > Math.abs(vx) * 3 && !isSlowed) {
                    isSpeedChecking = true;
                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (Math.abs(vy) > Math.abs(vx) * 3) {
                        vy *= 3;
                        vx *= 3;
                        isSlowed = true;
                    }
                    isSpeedChecking = false;
            }
        }
    }
}
