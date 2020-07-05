package com.example.chaos_ping_pong;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

import static com.example.chaos_ping_pong.Crate.skx;
import static com.example.chaos_ping_pong.Crate.sky;

public class Player extends Obstacle {

    private float destY = y, destX = x;
    private String difficulty = "player";
    private int lifetime = 300000;

    public Player() {}

    public Player(float x, float y, float width, float height, Bitmap img) { //в случае игрока
        super(x, y, width, height, img);
        Crate.obstacles.add(this);
    }

    Player(float x, float y, float width, float height, Bitmap img, String difficulty) //в случае бота
    {
        super(x, y, width, height, img);
        this.difficulty = difficulty;
        Crate.obstacles.add(this);
        Crate.bots.add(this);
    }

    Player(float x, float y, float width, float height, Bitmap img, String difficulty, int lifetime)//в случае бота, который исчезнет
    {
        super(x, y, width, height, img);
        this.difficulty = difficulty;
        this.lifetime = lifetime;
        new Live().start();
        Crate.obstacles.add(this);
        Crate.bots.add(this);
    }

    private Boolean isRandGetX = true;
    private Boolean isRandGetY = true;

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public void setDestY(float destY) {
        this.destY = destY;
    }

    void setDestX(float destX) {
        this.destX = destX;
    }

    public void destChecker() {
        if (destY > y) {
            setSpeed(0, 3.5f *vyM* sky);
        } else if (destY < y) {
            setSpeed(0, -3.5f *vyM*skx);
        }

        if (destX > x) {
            setSpeed(1.5f * vxM*skx, 0);
        } else if (destX < x) {
            setSpeed(-1.5f * vxM*sky, 0);
        }

        if ((y < destY + vy && y > destY - vy) || destY == y) {
            setSpeed(vx, 0);
            setDestY(y);
            isRandGetY = true;
        }
        if ((x < destX + vx && x > destX - vx) || destX == x) {
            setSpeed(0, vy);
            setDestX(x);
            isRandGetX = true;
        }
    }

    private boolean isAlive = true;

    class Live extends Thread {
        public void run() {
            try {
                Thread.sleep(lifetime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isAlive = false;
            Crate.obstacles.remove(this);
            Crate.bots.remove(this);
        }
    }

    private float hardBrain(float ballvx, float ballvy, float bally, float dx) {
        float dy = ballvy * dx / ballvx;
        float ys = (bally + dy) % (2 * appHeight);
        if (ys < 0)
            ys += 2 * appHeight;
        if (ys > appHeight)
            ys = 2 * appHeight - ys;
        return ys;
    }


    public void botCheck() {
        if (isAlive) {
            if (Crate.balls.size() == 1) {
                    if (difficulty.equals("random") && isRandGetX && isRandGetY) {

                        Random rand = new Random();
                        setDestY(rand.nextInt((int) appHeight));
                        setDestX(rand.nextInt((int) appWidth));
                        if (destY + height > appHeight) destY = appHeight - height;
                        isRandGetX = false;
                        isRandGetY = false;

                    }

                    if (difficulty.equals("easy")) {
                        if (!Crate.balls.get(0).isInvis) {
                            if (Crate.balls.get(0).y - height / 2 < y)
                                setDestY(y - 10);
                            else
                                setDestY(y + 10);
                        }
                    }
                    if (difficulty.equals("medium")) {

                        if (!(Crate.balls.get(0).x > 7 * appWidth / 8) && !Crate.balls.get(0).isInvis) {
                            if (!(Crate.balls.get(0).x < 4 * appWidth / 7)) {
                                if (Crate.balls.get(0).y < y + height / 2)
                                    setDestY(y - 5);
                                else setDestY(y + 5);
                            } else {
                                setDestY(appHeight / 2 - height / 2);
                            }
                        } else {
                            if (Crate.balls.get(0).y < appHeight / 2 && Crate.balls.get(0).vy > 0 && y > appHeight / 2)
                                setDestY(appHeight - height);
                            else if (Crate.balls.get(0).y < appHeight / 2 && Crate.balls.get(0).vy > 0 && y < appHeight / 2)
                                setDestY(0);
                            else if (Crate.balls.get(0).y > appHeight / 2 && Crate.balls.get(0).vy < 0 && y > appHeight / 2)
                                setDestY(appHeight - height);
                            else if (Crate.balls.get(0).y > appHeight / 2 && Crate.balls.get(0).vy < 0 && y < appHeight / 2)
                                setDestY(0);
                        }

                    }
                    if (difficulty.equals("hard")) {

                        if (!(Crate.balls.get(0).x > 7 * appWidth / 8) && !Crate.balls.get(0).isInvis) {
                            if (Crate.balls.get(0).vx > 0) {
                                setDestY(hardBrain(Crate.balls.get(0).vx, Crate.balls.get(0).vy, Crate.balls.get(0).y, x - Crate.balls.get(0).x) - height / 2);
                            } else {
                                setDestY(appHeight / 2 - height / 2);
                            }
                        } else {
                            if (Crate.balls.get(0).y < appHeight / 2 && Crate.balls.get(0).vy > 0 && y > appHeight / 2)
                                setDestY(appHeight - height);
                            else if (Crate.balls.get(0).y < appHeight / 2 && Crate.balls.get(0).vy > 0 && y < appHeight / 2)
                                setDestY(0);
                            else if (Crate.balls.get(0).y > appHeight / 2 && Crate.balls.get(0).vy < 0 && y > appHeight / 2)
                                setDestY(appHeight - height);
                            else if (Crate.balls.get(0).y > appHeight / 2 && Crate.balls.get(0).vy < 0 && y < appHeight / 2)
                                setDestY(0);
                        }

                    }

            }else {

                if (difficulty.equals("random") && isRandGetX && isRandGetY) {

                    Random rand = new Random();
                    setDestY(rand.nextInt((int) appHeight));
                    setDestX(rand.nextInt((int) appWidth));
                    if (destY + height > appHeight) destY = appHeight - height;
                    isRandGetX = false;
                    isRandGetY = false;
                }

                if (difficulty.equals("easy")) {
                    synchronized (Crate.balls) {
                        float biggestX = 0;
                        int indexOfTheFarrest = 0;
                        for (int i = 0; i < Crate.balls.size(); i++) {
                            if (Crate.balls.get(i) != null) {
                                if (Crate.balls.get(i).x > biggestX && !Crate.balls.get(i).isInvis) {
                                    indexOfTheFarrest = i;
                                    biggestX = Crate.balls.get(i).x;
                                }
                            }
                        }
                        if (Crate.balls.get(indexOfTheFarrest).y - height / 2 < y)
                            setDestY(y - 10);
                        else
                            setDestY(y + 10);
                    }
                }

                if (difficulty.equals("medium")) {
                    synchronized (Crate.balls) {
                        float biggestX = 0;
                        int indexOfTheFarrest = 0;
                        for (int i = 0; i < Crate.balls.size(); i++) {
                            if (Crate.balls.get(i) != null) {
                                if (Crate.balls.get(i).x > biggestX && Crate.balls.get(i).vx > 0  && !Crate.balls.get(i).isInvis) {
                                    indexOfTheFarrest = i;
                                    biggestX = Crate.balls.get(i).x;
                                }
                            }
                        }
                        if (!(Crate.balls.get(indexOfTheFarrest).x > 7 * appWidth / 8)) {
                            if (!(Crate.balls.get(indexOfTheFarrest).x < 4 * appWidth / 7)) {
                                if (Crate.balls.get(indexOfTheFarrest).y < y + height / 2)
                                    setDestY(y - 5);
                                else setDestY(y + 5);
                            } else {
                                setDestY(appHeight / 2 - height / 2);
                            }
                        } else {
                            if (Crate.balls.get(indexOfTheFarrest).y < appHeight / 2 && Crate.balls.get(indexOfTheFarrest).vy > 0 && y > appHeight / 2)
                                setDestY(appHeight - height);
                            else if (Crate.balls.get(indexOfTheFarrest).y < appHeight / 2 && Crate.balls.get(indexOfTheFarrest).vy > 0 && y < appHeight / 2)
                                setDestY(0);
                            else if (Crate.balls.get(indexOfTheFarrest).y > appHeight / 2 && Crate.balls.get(indexOfTheFarrest).vy < 0 && y > appHeight / 2)
                                setDestY(appHeight - height);
                            else if (Crate.balls.get(indexOfTheFarrest).y > appHeight / 2 && Crate.balls.get(indexOfTheFarrest).vy < 0 && y < appHeight / 2)
                                setDestY(0);
                        }
                    }
                }

                if (difficulty.equals("hard")) { //TODO чтобы он был умнее - реагириует так же в зависимости от скорости.
                    synchronized (Crate.balls) {
                        float biggestX = 0;
                        int indexOfTheFarrest = 0;
                        for (int i = 0; i < Crate.balls.size(); i++) {
                            if (Crate.balls.get(i) != null) {
                                if (Crate.balls.get(i).x > biggestX && Crate.balls.get(i).vx > 0  && !Crate.balls.get(i).isInvis) {
                                    indexOfTheFarrest = i;
                                    biggestX = Crate.balls.get(i).x;
                                }
                            }
                        }
                        if (!(Crate.balls.get(indexOfTheFarrest).x > 7 * appWidth / 8)) {
                            if (Crate.balls.get(indexOfTheFarrest).vx > 0) {
                                setDestY(hardBrain(Crate.balls.get(indexOfTheFarrest).vx, Crate.balls.get(indexOfTheFarrest).vy, Crate.balls.get(indexOfTheFarrest).y, x - Crate.balls.get(indexOfTheFarrest).x) - height / 2);
                            } else {
                                setDestY(appHeight / 2 - height / 2);
                            }
                        } else {
                            if (Crate.balls.get(indexOfTheFarrest).y < appHeight / 2 && Crate.balls.get(indexOfTheFarrest).vy > 0 && y > appHeight / 2)
                                setDestY(appHeight - height);
                            else if (Crate.balls.get(indexOfTheFarrest).y < appHeight / 2 && Crate.balls.get(indexOfTheFarrest).vy > 0 && y < appHeight / 2)
                                setDestY(0);
                            else if (Crate.balls.get(indexOfTheFarrest).y > appHeight / 2 && Crate.balls.get(indexOfTheFarrest).vy < 0 && y > appHeight / 2)
                                setDestY(appHeight - height);
                            else if (Crate.balls.get(indexOfTheFarrest).y > appHeight / 2 && Crate.balls.get(indexOfTheFarrest).vy < 0 && y < appHeight / 2)
                                setDestY(0);
                        }
                    }
                }
            }
            destChecker();
        }
    }
}

