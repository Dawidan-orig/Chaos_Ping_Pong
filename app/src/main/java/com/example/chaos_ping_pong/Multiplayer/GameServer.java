package com.example.chaos_ping_pong.Multiplayer;

import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.example.chaos_ping_pong.Animator;
import com.example.chaos_ping_pong.Ball;
import com.example.chaos_ping_pong.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;

import static com.example.chaos_ping_pong.MainActivity.HOST_WAIT;
import static com.example.chaos_ping_pong.MainActivity.NETWORK_TAG;
import static com.example.chaos_ping_pong.MainActivity.PORT;

public class GameServer extends Listener implements Runnable {

    public GameServer(){}

    private Server server;

    DataToSend dts;

    @Override
    public void run() {//отвечает за единоразовое подключение
        server = new Server(1000000, 1000000);

        //Регистрируем классы
        Kryo kryo = server.getKryo();
        kryo.register(Ball.class);
        kryo.register(DataToSend.class);
        kryo.register(com.example.chaos_ping_pong.Multiplayer.DataToSend.ObstacleData.class);
        kryo.register(byte[].class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            kryo.register(LocaleList.class);
        }
        kryo.register(java.util.LinkedHashMap.class);
        kryo.register(java.util.Locale[].class);
        kryo.register(java.util.Locale.class);
        kryo.register(java.util.List.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(Collections.synchronizedList(new ArrayList<>()).getClass());
        SynchronizedCollectionsSerializer.registerSerializers( kryo );
        kryo.register(com.example.chaos_ping_pong.Player.class);

        //Регистрируем порт
        try {
            server.bind(PORT, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();

        server.addListener(this);
        Log.w(NETWORK_TAG, "Server deployed");
    }

    public void sendDataToClient(Object o)
    {
        server.sendToAllTCP(o);
        dts = null;
    }

    @Override
    public void connected(Connection c){
        Log.w(NETWORK_TAG, "Client connected");
        //System.out.println("На сервер подключился "+c.getRemoteAddressTCP().getHostString());
        //Создаем сообщения пакета.
        //PacketMessage packetMessage = new PacketMessage();
        //Пишем текст который будем отправлять клиенту.
        //packetMessage.message = "Сейчас время: "+new Date().getHours()+":"+new Date().getMinutes();
        //Отправляем текст
        //c.sendTCP(packetMessage); // Так же можно отправить через UDP c.sendUDP(packetMessage);
        HOST_WAIT = true;
    }

    //Используется когда клиент отправляет пакет серверу
    @Override
    public void received(Connection c, Object p)
    {
        Log.i(NETWORK_TAG, "Client sent data: " + p.toString());
        if(p instanceof DataToSend)
        {
            dts = (DataToSend) p;
        }
    }

    //Используется когда клиент покидает сервер.
    @Override
    public void disconnected(Connection c){
        Log.w(NETWORK_TAG, "Client Disconnected");
    }
}
