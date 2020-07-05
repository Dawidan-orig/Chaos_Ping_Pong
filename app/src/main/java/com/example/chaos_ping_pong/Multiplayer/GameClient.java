package com.example.chaos_ping_pong.Multiplayer;

import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.example.chaos_ping_pong.Animator;
import com.example.chaos_ping_pong.Ball;
import com.example.chaos_ping_pong.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;

import static com.example.chaos_ping_pong.MainActivity.HOST;
import static com.example.chaos_ping_pong.MainActivity.NETWORK_TAG;
import static com.example.chaos_ping_pong.MainActivity.PORT;

public class GameClient extends Listener {

    private Client client;

    public String command = "";

    DataToSend dts;

    public void run() { //отвечает за единоразовое подключение
        client = new Client(1000000, 1000000);

        //Регистрируем пакет
        Kryo kryo = client.getKryo();
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

        //Запускаем клиент
        client.start();
        //Клиент начинает подключатся к серверу

        Log.w(NETWORK_TAG, "Client trying to connect");
        try {
            client.connect(5000, HOST, PORT, PORT);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(NETWORK_TAG, "Connection failed, Port: " + PORT +" host IP is: " + HOST);
        }

        client.addListener(this); //Клиент подключается к серверу
        Log.w(NETWORK_TAG,"Connected to server");
    }

    void sendDataToServer(Object o)
    {
        client.sendTCP(o);
        dts = null;
    }

    @Override
    public void received(Connection c, Object p){
        /*
        //Проверяем какой отправляется пакет
        if(p instanceof PacketMessage){
            //Если мы получили PacketMessage .
            PacketMessage packet = (PacketMessage) p;
            System.out.println("Ответ от сервера: "+packet.message);

            //Мы получили сообщение
            messageReceived = true;
        }
         */
        Log.i(NETWORK_TAG, "Server send a message :" + p.toString());
        if(p instanceof DataToSend)
        {
            dts = (DataToSend) p;
        }
        else if(p instanceof String)
        {
            //commands
            if(p.equals("startGame"))
            {
                command = (String) p;
            }
        }
    }
}
