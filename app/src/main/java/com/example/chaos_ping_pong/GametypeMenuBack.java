package com.example.chaos_ping_pong;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GametypeMenuBack extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;
    Canvas canvas;


    public GametypeMenuBack(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public GametypeMenuBack(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public GametypeMenuBack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        new MenuThread().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        onSVDestroy = true;
    }
    boolean onSVDestroy = false;

    private class MenuThread extends Thread
    {

        @Override
        public void run() {
            canvas=surfaceHolder.lockCanvas();
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            float skx =  canvas.getWidth()/1280.0f; //TODO абсолютно все процессы должны быть под кооф-ом., потому что иначе тот же мяч на каком-нибудь огромном планшете будет двигаться медленно и соответствуя размеру маленького телефона.
            float sky =  canvas.getHeight()/670.0f;
            Obstacle.setAppSize(height, width);
            Random rand = new Random();
            int k = 5 + rand.nextInt(5);
            Ball[] balls = new Ball[k];
            for(int i = 0; i < balls.length; i++)
            {
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);
                balls[i] = new Ball(x, y,rand.nextInt(8) ,true);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);

            while(!onSVDestroy)
            {
                canvas=surfaceHolder.lockCanvas();
                if(canvas != null) {
                    synchronized (canvas) {
                        canvas.drawRGB(0, 0, 0);
                        for(int i = 0; i < balls.length; i++)
                        {
                            balls[i].draw(canvas);
                            balls[i].move();
                            balls[i].inverseCheck();
                            Crate.animator.setTrailToBall(balls[i], canvas);
                        }
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
