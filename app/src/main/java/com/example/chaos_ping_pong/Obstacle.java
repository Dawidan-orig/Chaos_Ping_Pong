package com.example.chaos_ping_pong;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.annotation.Nullable;

import java.util.Random;

import static com.example.chaos_ping_pong.Crate.skx;
import static com.example.chaos_ping_pong.Crate.sky;

public class Obstacle {
    public float x,y;
    float vx = 0, vy = 0;
    public float height, width;
    private Bitmap img;
    static float appWidth, appHeight;

    public static void setAppSize(float height, float width)
    {
        appHeight = height;
        appWidth = width;
    }

    Obstacle() {}

    public Obstacle(float x, float y, float width, float height, Bitmap img)
    {
        this.x = x;
        this.y = y;
        this.height = height * sky;
        this.width = width * skx;
        this.img = img;
        Crate.animator.obstSpawn(this);
        Random rand = new Random();
        for(int i = 0; i < 5; i++)
        {
            new Particle(rand.nextInt((int)width) + this.x, rand.nextInt((int)height) + this.y, 40, 1, 5, 0, 0, 145);
        }
    }

    public Obstacle(float x, float y, float width, float height)  //невидимый муляж, на который пересаживается вся тень, чтобы она дорисовалась
    {
        this.x = x;
        this.y = y;
        this.height = height * sky;
        this.width = width * skx;
        isInvis = true;
    }

    boolean isInvis = false;

    void makeInvisible() {
        isInvis = true;
    }

    void makeVisible() {
        isInvis = false;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.img = bitmap;
    }

    public void draw(Canvas canvas) //sk = Scale Koof.
    {
        if(!isKilled || !isInvis) {
            Paint paint = new Paint();
            Rect r = new Rect();
            r.left = (int)x;
            r.top = (int) y;
            r.right = (int) (x + width);
            r.bottom = (int) (y +height);
            paint.setAlpha(255);
            canvas.drawBitmap(img, null,r, paint);
        }
    }

    void setSpeed(float vx, float vy)
    {
        this.vx = vx;
        this.vy = vy;
    }

    public void move()
    {
        if(!isKilled) {
            x += vx*skx;
            y += vy*sky;
        }
    }

    float vxM = 1;
    float vyM = 1;

    void setSpeedModifier(float vx, float vy)
    {
        vxM = vx;
        vyM = vy;
    }

    boolean isKilled = false;
    void kill()
    {
        isKilled = true;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
