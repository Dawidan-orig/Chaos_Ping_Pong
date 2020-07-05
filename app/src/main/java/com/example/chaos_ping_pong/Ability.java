package com.example.chaos_ping_pong;

import android.graphics.Bitmap;

public class Ability {
    private int cooldown;
    private int useTime;
    public String name;
    private float skx,sky;
    private int abilityIndex; //indexes:
    //speedUp = 0;
    //spawnObst = 1;
    //reverseY = 2;
    //tracker = 3;
    //smoke = 4;
    //stunner = 5;
    //TODO Stun штука --- до сдачи делать не буду
    //TODO поставить ограничители, чтобы платформой мяч не зажать и не получать по 100 очков в секнду

    private Player p1, p2;

    public Ability(String name, Player p1, Player p2, float skx, float sky)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.name = name;
        cooldown = 400000;
        useTime = 400000;
        this.skx = skx;
        this.sky = sky;

        if(name.equals("speedUp")) abilityIndex = 0;
        else if(name.equals("spawnObst")) abilityIndex = 1;
        else if(name.equals("reverseY")) abilityIndex = 2;
        else if(name.equals("tracker")) abilityIndex = 3;
        else if(name.equals("smoke")) abilityIndex = 4;
    }

    private Obstacle obst;
    public boolean flag = true;

    public void use(float x, float y, Bitmap obstImg)
    {
        switch (abilityIndex) {
            case 0: {
                if (flag) {
                    flag = false;
                    cooldown = Crate.time - 25000;
                    useTime = Crate.time - 3000;
                    p1.setSpeedModifier(0, 3);
                    Crate.animator.trailObsts.add(p1);
                }
                break;
            }
            case 1:
            {
                if (flag) {
                    flag = false;
                    cooldown = Crate.time - 30000;
                    useTime = Crate.time - 15000;
                    obst = new Obstacle(x - 7.5f, y - 30, 30, 120, obstImg);
                    Crate.obstacles.add(obst);
                    Crate.soundPool.play(Crate.s_ID_onSpawn, 0.3f,0.3f,0,0,1);
                }
                break;
            }
            case 2 :
            {
                if (flag) {
                    flag = false;
                    cooldown = Crate.time - 15000;
                    for(Ball ball : Crate.balls)
                    {
                        ball.setSpeed(ball.vx, ball.vy * -1);
                        Crate.animator.reverseY(ball.x, ball.y);
                    }
                    Crate.soundPool.play(Crate.s_ID_reverse, 0.6f,0.6f,0,0,1);
                }
                break;
            }
            case 3 :
                {
                    if(flag)
                    {
                        flag = false;
                        cooldown = Crate.time - 60000;
                        useTime = Crate.time - 25000;
                        Crate.animator.isTrackerAbil = true;
                    }
                    break;
                }
            case 4:
                {
                    if(flag)
                    {
                        flag = false;
                        cooldown = Crate.time - 35000;
                        useTime = Crate.time - 20000;
                        Crate.animator.setSmoke(x, y, 25000);
                    }
                    break;
                }
        }
    }

    public void check()
    {
        if(useTime > Crate.time)
        {
            useChecker();
        }
        if(cooldown > Crate.time)
        {
            flag = true;
        }
    }

    void useChecker()
    {
            switch (abilityIndex) {
                case 0 : {
                    p1.setSpeedModifier(1, 1);
                    Crate.animator.killTrail(p1);
                    Crate.soundPool.stop(Crate.s_ID_obstTrail);
                    break;
                }
                case 1 :
                {
                    Crate.obstacles.remove(obst);
                    break;
                }
                case 3 :
                    Crate.animator.isTrackerAbil = false;
                    break;
            }
    }
}
