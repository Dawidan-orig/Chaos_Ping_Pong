package com.example.chaos_ping_pong;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class AbilityIcons {
    float x, y;
    public float sk;
    Bitmap img;
    Paint paint = new Paint();

    public AbilityIcons(float x, float y, float sk, Bitmap img)
    {
        this.x = x;
        this.y = y;
        this.sk = sk;
        this.img = img;
    }

    public void drawAbility(Canvas canvas)
    {
        Matrix matrix = new Matrix();
        matrix.setScale(sk, sk);
        matrix.postTranslate(x, y);
        paint.setAlpha(255);
        canvas.drawBitmap(img, matrix, paint);
    }

    public void onCooldown(Canvas canvas, Bitmap blurImg)
    {
        Matrix matrix = new Matrix();
        matrix.setScale(sk, sk);
        matrix.postTranslate(x, y);
        paint.setAlpha(128);
        canvas.drawBitmap(blurImg, matrix, paint);
    }
}
