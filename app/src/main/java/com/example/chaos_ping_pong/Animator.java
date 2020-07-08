package com.example.chaos_ping_pong;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.chaos_ping_pong.Multiplayer.ClientDrawer;
import com.example.chaos_ping_pong.Multiplayer.HostDrawer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.example.chaos_ping_pong.Crate.skx;
import static com.example.chaos_ping_pong.Crate.sky;


public class Animator {

    List<Particle> particles = Collections.synchronizedList(new ArrayList<Particle>());

    public void setAppSize(float appHeight, float appWidth) {
        Animator.appHeight = appHeight;
        Animator.appWidth = appWidth;
    }

    public Boolean isLockerAbility = false;
    Boolean isTrackerAbil = false;

    private void onLockerAbility(Canvas canvas) {
        Paint paint = new Paint();
        paint.setARGB(255, 0, 0, 255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        for (int i = 0; i < 5; i++) {
            paint.setARGB(255 - 51 * i, 0, 0, 255);
            canvas.drawRect(i * 2, i * 2, appWidth - i * 2, appHeight - i * 2, paint);
        }
    }

    private static float appHeight, appWidth;
    private LinkedHashMap<Obstacle, Integer> alphaRedacter = new LinkedHashMap<>();
    private LinkedHashMap<teleportData, Integer> tAlphaRedacter = new LinkedHashMap<>();
    private int cooldownCheckerObst = 300000;
    private int cooldownCheckerTeleport = 300000;
    private int cooldownCheckerSmoke = 300000;
    private int cooldownCheckerRevereY = 300000;
    private int cooldownCheckerSpawnCircle = 300000;

    private void checker() //разнообразные вычисления процессов аниматора
    {
        if(!alphaRedacter.isEmpty() && cooldownCheckerObst > Crate.time)
        {
            ArrayList<Obstacle> list = new ArrayList<>(alphaRedacter.keySet());
            for(Obstacle obst : list) {
                int a = alphaRedacter.get(obst);
                alphaRedacter.remove(obst);
                if(!(a - 15 < 0)) {
                    alphaRedacter.put(obst, a - 15);
                    cooldownCheckerObst = Crate.time - 25;
                }
            }
        }

        if(!tAlphaRedacter.isEmpty() && cooldownCheckerTeleport > Crate.time)
        {
            ArrayList<teleportData> list = new ArrayList<>(tAlphaRedacter.keySet());
            for(teleportData tData : list)
            {
                int a = tAlphaRedacter.get(tData);
                tAlphaRedacter.remove(tData);
                if(!(a - 15 < 0)) {
                    tAlphaRedacter.put(tData, a - 15);
                    cooldownCheckerTeleport = Crate.time - 25;
                }
            }
        }

        if(!smokes.isEmpty() && cooldownCheckerSmoke > Crate.time)
        {
            LinkedList<SmokeData> list = new LinkedList<>(smokes.keySet());
            for(SmokeData sd : list)
            {
                if(sd.alpha - 5 < 246 && !sd.isUp) sd.isUp = true;
                else if(sd.alpha + 5 > 255 && sd.isUp) sd.isUp = false;

                if(sd.lifetime < Crate.time) {
                    if (sd.isUp) {
                        sd.alpha += 5;
                    } else {
                        sd.alpha -= 5;
                    }
                }
                else
                    {
                        if(sd.alpha - 5 < 0)
                        {
                            smokes.remove(sd);
                        }
                        else
                            {
                                sd.alpha -= 5;
                            }
                    }
                cooldownCheckerSmoke = Crate.time - 40;
            }
        }
        if(!this.rData.isEmpty() && cooldownCheckerRevereY > Crate.time) {
            for (reverseYData rData : this.rData) {
                if (rData.alpha - 5 >= 0) {

                    rData.alpha -= 5;
                    rData.radius += 4;
                }
                cooldownCheckerRevereY = Crate.time - 20;
            }
        }
        if(!scData.isEmpty() && cooldownCheckerSpawnCircle > Crate.time)
        for(spawnCircleData scd : scData)
        {
            if(scd.radius - 5 >= 0)
            {
                scd.alpha -= 8.5f;
                scd.radius -= 5;
            }
            else
                {
                    scd.alpha = 0;
                    scd.radius = 0;
                }
            cooldownCheckerSpawnCircle = Crate.time - 20;
        }
    }

    private HashMap<SmokeData, SmokeUnitData> smokes = new HashMap<>(); //TODO Передавать это целиком
    class SmokeUnitData
    {
        float x, y;
    }

    class SmokeData
    {
        float cx, cy;
        int alpha = 255;
        boolean isUp = false;
        int lifetime;
    }

    void setSmoke(float x, float y, int time)
    {
        SmokeData sd = new SmokeData();
        sd.cx = x;
        sd.cy = y;
        sd.lifetime = Crate.time - time;

        //smokes.put(sd, new ArrayList<SmokeUnitData>());
        SmokeUnitData sud = new SmokeUnitData();
        sud.x = sd.cx - 100;
        sud.y = sd.cy - 100;
        smokes.put(sd, sud);
        /*
        for(int i = 0; i < 25; i++)
        {
            SmokeUnitData sud = new SmokeUnitData();
            sud.x = sd.cx - 125 + i%5 * 50;
            sud.y = sd.cy - 125 + i/5 * 50;
            smokes.get(sd).add(sud);
        }
         */
    }

    public void obstSpawn(Obstacle obst) {
        alphaRedacter.put(obst, 255);

        spawnCircleData scd = new spawnCircleData();
        scd.x = obst.x + obst.width/2;
        scd.y = obst.y + obst.height/2;
        scd.radius = 150;
        scd.alpha = 255;

        scData.add(scd);
    }

    private class teleportData {
        float startX, stopX, startY, stopY;
    }

    private ArrayList<teleportData> tDataArr = new ArrayList<>();

    void setTeleportLineData(float startX, float stopX, float startY, float stopY) {
        teleportData tData = new teleportData();
        tData.startX = startX;
        tData.startY = startY;
        tData.stopX = stopX;
        tData.stopY = stopY;
        tDataArr.add(tData);
        tAlphaRedacter.put(tData, 255);
    }

    private class reverseYData
    {
        float x, y, radius;
        int alpha;
        reverseYData(float x, float y, float radius, int alpha)
        {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.alpha = alpha;
        }
    }

    private ArrayList<reverseYData> rData = new ArrayList<>();

    void reverseY(float x, float y)
    {
        reverseYData rData = new reverseYData(x, y, 5, 255);
        this.rData.add(rData);
    }

    private void drawCheck(Canvas canvas)
    {
        for(reverseYData rData : this.rData) {
            if(rData.alpha > 0) {
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                paint.setARGB(rData.alpha, 200, 0,0);
                canvas.drawCircle(rData.x, rData.y, rData.radius, paint);
            }
        }

        for(spawnCircleData scd : this.scData)
        {
            if(scd.radius > 0) {
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                paint.setARGB(scd.alpha, 200, 0,0);
                canvas.drawCircle(scd.x, scd.y, scd.radius, paint);
            }
        }
    }

    private ArrayList<spawnCircleData> scData = new ArrayList<>();

    private class spawnCircleData
    {
        float x, y, radius;
        int alpha;
    }

    class BallShadowData
    {
        float x, y, radius;
        boolean isInvis;
        int a,r,g,b;
    }
    private HashMap<Ball, LinkedList<BallShadowData>> shadowsMap = new HashMap<>();
    private List<Ball> diedBalls = Collections.synchronizedList(new ArrayList<Ball>());

    public void addDiedBall(Ball ball)
    {
        diedBalls.add(ball);
        ball.disable();
    }

    void setTrailToBall(Ball ball, Canvas canvas) {
            int ballR, ballG, ballB;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            int shadows = 50;
            if (!shadowsMap.containsKey(ball)) {
                shadowsMap.put(ball, new LinkedList<BallShadowData>());
            }

            ballR = ball.getColorRed();
            ballG = ball.getColorGreen();
            ballB = ball.getColorBlue();

            BallShadowData bsd = new BallShadowData();
            bsd.x = ball.x;
            bsd.y = ball.y;
            bsd.isInvis = ball.isInvis;
            bsd.r = ballR;
            bsd.b = ballB;
            bsd.g = ballG;
            bsd.radius = ball.radius;
            bsd.a =255;
            shadowsMap.get(ball).offer(bsd);

            for (BallShadowData data : shadowsMap.get(ball)) {
                paint.setARGB(data.a, data.r, data.g, data.b);
                if(!data.isInvis) canvas.drawCircle(data.x, data.y, data.radius, paint);
                data.a -= 255/shadows;
            }
            if(shadowsMap.get(ball).getFirst().a <= 0) shadowsMap.get(ball).poll();
    }

    private class ObstShadowData
    {
        float x,y,ex,ey;
        boolean isInvis;
        boolean isKilled;
        int a;
    }

    private HashMap<Obstacle, LinkedList<ObstShadowData>> oShadowsMap = new HashMap<>();
    List<Obstacle> trailObsts = Collections.synchronizedList(new ArrayList<Obstacle>());

    public void killTrail(Obstacle obstacle)
    {
        if(trailObsts.contains(obstacle)) {
            Obstacle obst = new Obstacle(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
            LinkedList<ObstShadowData> list = oShadowsMap.get(obstacle);
            oShadowsMap.put(obst, list);
            trailObsts.add(obst);

            oShadowsMap.remove(obstacle);
            trailObsts.remove(obstacle);

            obst.kill(); //можно потом этот obst вырубить. только это async Task, и куча работы с переделыванием.
        }
    }

    private void drawObstTrail(Obstacle obst, Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        int shadows = 50;
        if (!oShadowsMap.containsKey(obst)) {
            oShadowsMap.put(obst, new LinkedList<ObstShadowData>());
        }

        ObstShadowData osd = new ObstShadowData();
        osd.x = obst.x;
        osd.y = obst.y;
        osd.isInvis = obst.isInvis;
        osd.ex = obst.x + obst.width;
        osd.ey = obst.y + obst.height;
        osd.a =255;
        osd.isKilled = obst.isKilled;
        oShadowsMap.get(obst).offer(osd);

        for (ObstShadowData data : oShadowsMap.get(obst)) {
            paint.setARGB(data.a, 28,28,128);
            if(!data.isInvis || !data.isKilled) canvas.drawRect(data.x, data.y, data.ex,data.ey, paint);
            data.a -= 255/shadows;
        }
        if(oShadowsMap.get(obst).getFirst().a <= 0) oShadowsMap.get(obst).poll();
    }

    private HashMap<Particle, LinkedList<BallShadowData>> pShadowsMap = new HashMap<>();

    private void setTrailToParticle(Particle p, Canvas canvas) {
        int ballR, ballG, ballB;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        int shadows = 8;
        if (!pShadowsMap.containsKey(p)) {
            pShadowsMap.put(p, new LinkedList<BallShadowData>());
        }

        ballR = p.r;
        ballG = p.g;
        ballB = p.b;

        BallShadowData bsd = new BallShadowData();
        bsd.x = p.x;
        bsd.y = p.y;
        bsd.r = ballR;
        bsd.b = ballB;
        bsd.g = ballG;
        bsd.a = 255;
        pShadowsMap.get(p).offer(bsd);

        for (BallShadowData data : pShadowsMap.get(p)) {
            paint.setARGB(data.a, data.r, data.g, data.b);
            if (!data.isInvis) canvas.drawCircle(data.x, data.y, 3.5f, paint);
            data.a -= 255 / shadows;
        }
        if (pShadowsMap.get(p).getFirst().a <= 0) pShadowsMap.get(p).poll();
    }

    private int eventCooldown = 300000 - 15000;
    private int gEventCooldown = 300000 - 60000;

    public void animationCheck(Canvas canvas, Player pl, Player pr, Bitmap smoke, Bitmap eMarker, Bitmap gEMarker) {

        if (!Crate.isTutor) {
            if (eventCooldown > Crate.time) {
                eventCooldown = Crate.time - 15000;
                makeEvent(false);
                Crate.soundPool.play(Crate.s_ID_event, 1, 1, 0, 0, 1);
            }
        if (gEventCooldown > Crate.time) {
            gEventCooldown = Crate.time - 60000;
            makeEvent(true);
            Crate.soundPool.play(Crate.s_ID_event, 1, 1, 0, 0, 0.5f);
        }
    }

        checker();

            synchronized (Crate.balls) {
                for (Ball ball : Crate.balls) {
                    setTrailToBall(ball, canvas);
                }
            }

            synchronized (diedBalls) {
                for (Ball ball : diedBalls) {
                    setTrailToBall(ball, canvas);
                }
            }

            synchronized (trailObsts)
            {
                for(Obstacle obst : trailObsts)
                {
                    drawObstTrail(obst, canvas);
                }
            }

        synchronized (particles) {
            Iterator<Particle> i =particles.iterator();
            while (i.hasNext()) {
                Particle particle = i.next();
                particle.destinate();
                Paint paint = new Paint();
                paint.setARGB(180, particle.r, particle.g, particle.b);
                canvas.drawCircle(particle.x, particle.y, 3.5f, paint);
                setTrailToParticle(particle, canvas);
                if(particle.n <= -1)
                    i.remove();
            }
        }

            if (!alphaRedacter.isEmpty()) {

                    ArrayList<Obstacle> list = new ArrayList<>(alphaRedacter.keySet());
                    for (Obstacle obst : list) {
                        Paint paint = new Paint();
                        paint.setStyle(Paint.Style.FILL);
                        //noinspection ConstantConditions
                        paint.setARGB(alphaRedacter.get(obst), 255, 255, 255);
                        canvas.drawRect(obst.x, obst.y, obst.x + obst.width, obst.y + obst.height, paint);
                    }
            }

            drawCheck(canvas);

            if (isTrackerAbil) {
                final int step = 100;
                Paint paint = new Paint();
                paint.setARGB(200, 255, 0, 0);
                synchronized (Crate.balls) {
                    for (Ball ball : Crate.balls) {
                        if (ball.vx > 0) {
                            for (int j = 0; j < (pr.x - ball.x) / step; j++) {
                                drawBeauteCircle(canvas, ball.x + j * step, tracker(ball.vx, ball.vy, ball.y, j * step), 7, paint);
                            }
                        } else if (ball.vx < 0) {
                            for (int j = 0; j < (ball.x - pl.x) / step; j++) {
                                drawBeauteCircle(canvas, ball.x - j * step, tracker(ball.vx, ball.vy*-1, ball.y, j * step), 7, paint);
                            }
                        }
                    }
                }
            }

            if (!tDataArr.isEmpty()) {

                        for (int i = 0; i < tDataArr.size(); i++) {
                            if (tDataArr.get(i) != null && tAlphaRedacter.get(tDataArr.get(i)) != null) {
                                Paint tPaint = new Paint();
                                tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                                tPaint.setARGB(tAlphaRedacter.get(tDataArr.get(i)), 128, 0, 0);
                                tPaint.setStrokeWidth(10);
                                canvas.drawLine(tDataArr.get(i).startX, tDataArr.get(i).startY, tDataArr.get(i).stopX, tDataArr.get(i).stopY, tPaint);
                            }
                        }
            }

            if(!smokes.isEmpty())
            {
                LinkedList<SmokeData> list = new LinkedList<>(smokes.keySet());
                for(SmokeData sd : list)
                {
                    //for(int i = 0; i < 25; i++) {
                        Paint paint = new Paint();
                        paint.setAlpha(sd.alpha);
                        Matrix matrix = new Matrix();
                        //matrix.postTranslate(smokes.get(sd).get(i).x, smokes.get(sd).get(i).y);
                        matrix.postTranslate(smokes.get(sd).x, smokes.get(sd).y);
                        matrix.preScale(4*skx, 4*sky);
                        canvas.drawBitmap(smoke, matrix, paint);
                    //}
                }
            }

            eventCheck(canvas, eMarker, gEMarker);

            if (isLockerAbility) {
                onLockerAbility(canvas);
            }

    }

    private ArrayList<Float> eventFir = new ArrayList<>();
    private ArrayList<Float> eventSec = new ArrayList<>();
    private ArrayList<Boolean> isGlobal = new ArrayList<>();

    void makeEvent(boolean isGlobal) {
        eventFir.add(appWidth / 2);
        eventSec.add(appWidth / 2);
        this.isGlobal.add(isGlobal);
    }

    private void eventCheck(Canvas canvas, Bitmap marker, Bitmap glMarker) {
        if (!isGlobal.isEmpty()) {
            for (int i = 0; i < isGlobal.size(); i++) {
                Matrix matrix1 = new Matrix();
                Matrix matrix2 = new Matrix();
                Paint paint = new Paint();
                paint.setAlpha(255);
                if (!isGlobal.get(i)) {
                    float xFir = eventFir.get(i);
                    eventFir.remove(i);
                    eventFir.add(i, xFir - 20);
                    matrix1.postTranslate(eventFir.get(i), 0);
                    canvas.drawBitmap(marker, matrix1, paint);
                    float xSec = eventSec.get(i);
                    eventSec.remove(i);
                    eventSec.add(i, xSec + 20);
                    matrix2.postTranslate(eventSec.get(i), 0);
                    matrix2.preScale(-1*skx, 1*sky);
                    canvas.drawBitmap(marker, matrix2, paint);
                } else {
                    float xFir = eventFir.get(i);
                    eventFir.remove(i);
                    eventFir.add(i, xFir - 18);
                    matrix1.postTranslate(eventFir.get(i), 0);
                    canvas.drawBitmap(glMarker, matrix1, paint);
                    float xSec = eventSec.get(i);
                    eventSec.remove(i);
                    eventSec.add(i, xSec + 18);
                    matrix2.postTranslate(eventSec.get(i) + 50, 0);
                    matrix2.preScale(-1*skx, 1*sky);
                    canvas.drawBitmap(glMarker, matrix2, paint);
                }
            }
        }
    }

    private void drawBeauteCircle(Canvas canvas, float cx, float cy, float radius, Paint paint) {
        canvas.drawCircle(cx, cy, radius - 3, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawCircle(cx, cy, radius, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private float tracker(float ballvx, float ballvy, float bally, float dx) {
        float dy = ballvy * dx / ballvx;
        float ys = (bally + dy) % (2 * appHeight);
        if (ys < 0)
            ys += 2 * appHeight;
        if (ys > appHeight)
            ys = 2 * appHeight - ys;
        return ys;
    }
}