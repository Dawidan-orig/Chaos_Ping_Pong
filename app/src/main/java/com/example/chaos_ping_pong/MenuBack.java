package com.example.chaos_ping_pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Random;

public class MenuBack extends SurfaceView implements  SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Canvas canvas;

    public MenuBack(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public MenuBack(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public MenuBack(Context context, AttributeSet attrs, int defStyleAttr) {
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

    boolean onSVDestroy = false;

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        onSVDestroy = true;
    }


    private class MenuThread extends Thread
    {

        @Override
        public void run() {

            canvas=surfaceHolder.lockCanvas();
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            final int kx = 20;
            final int ky = 10;
            final int stroke = 10;
            int widthCb = width/kx - stroke;  //размер ребра квадрата
            int heightCb = height/ky - stroke;
            Paint[] paints = new Paint[kx*ky];
            Random rand = new Random();
            HashMap<Paint, Boolean> map = new HashMap<>();

            for(int i = 0; i < paints.length; i++)
            {
                paints[i] = new Paint();
                int k = rand.nextInt(6);
                switch (k) {
                    case 0:
                        paints[i].setARGB(255, 255, 20, 100);
                        break;
                    case 1:
                        paints[i].setARGB(255, 55, 130, 67);
                        break;
                    case 2:
                        paints[i].setARGB(255, 175, 100, 20);
                        break;
                    case 3:
                        paints[i].setARGB(255, 0, 200, 100);
                        break;
                    case 4:
                        paints[i].setARGB(255, 100, 20, 167);
                        break;
                    case 5:
                        paints[i].setARGB(255, 55, 50, 200);
                        break;
                }
                paints[i].setStyle(Paint.Style.FILL_AND_STROKE);
                paints[i].setStrokeWidth(stroke);
                int a = rand.nextInt(255) /5;
                paints[i].setAlpha(a);
                map.put(paints[i], false);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);

            while(!onSVDestroy)
            {
                canvas=surfaceHolder.lockCanvas();
                if(canvas != null) {
                    synchronized (canvas) {
                        canvas.drawRGB(0, 0, 0);
                        for (int i = 0; i < kx; i++) {
                            for (int j = 0; j < ky; j++) {
                                int a = paints[j + ky*i].getAlpha();
                                if(a + 5 > 255) {map.remove(paints[j + ky*i]); map.put(paints[j + ky*i], false);}
                                else if(a - 5 < 0) {map.remove(paints[j + ky*i]); map.put(paints[j + ky*i], true);}

                                if(map.get(paints[i])) paints[j + ky*i].setAlpha(a + 5);
                                else paints[j + ky*i].setAlpha(a - 5);

                                canvas.drawRect(i * (widthCb + 13),j * (heightCb + 13),widthCb + i * (widthCb + 13),heightCb + j * (heightCb + 13),paints[j + ky*i]);
                            }
                        }
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
