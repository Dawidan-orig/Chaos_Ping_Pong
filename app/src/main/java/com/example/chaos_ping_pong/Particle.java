package com.example.chaos_ping_pong;

import java.util.Random;

import static com.example.chaos_ping_pong.Crate.skx;
import static com.example.chaos_ping_pong.Crate.sky;

public class Particle
{
    int r,g,b;
    float x,y, speed, radius;
    float destX, destY;
    int n;
    float vx, vy;
    Particle(float x, float y, int radius, float speed, int n, int r, int g, int b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.x = x;
        this.y = y;
        this.radius = radius*skx;
        this.speed = speed;
        this.n = n;
        destX = x;
        destY = y;
        Crate.animator.particles.add(this);
    }

    void destinate()
    {
        if ((y < destY + Math.abs(vy) && y > destY - Math.abs(vy)) || destY == y) {
            destY = y;
        }
        if ((x < destX + Math.abs(vx) && x > destX - Math.abs(vx)) || destX == x) {
            destX = x;
        }

        if(((y < destY + Math.abs(vy) && y > destY - Math.abs(vy)) || destY == y) && ((x < destX + Math.abs(vx) && x > destX - Math.abs(vx)) || destX == x) && n > -1)
        {
            n--;
            Random rand = new Random();
            int k = rand.nextInt(360);
            destX = (float) (Math.sin(k*Math.PI/180) * radius) + x;
            destY = (float) (Math.cos(k*Math.PI/180) * radius) + y;

            if(destY < y)
                vy = speed *-1;
            else if(destY > y)
                vy = speed;
            if(destX < x)
                vx = speed * -1;
            else if(destX > x)
                vx = speed;
        }
        else
            move();
    }

    private void move()
    {
        if (!((y < destY + Math.abs(vy) && y > destY - Math.abs(vy)) || destY == y)) {
            y += vy;
        }
        if (!((x < destX + Math.abs(vx) && x > destX - Math.abs(vx)) || destX == x)) {
            x += vx;
        }
    }
}