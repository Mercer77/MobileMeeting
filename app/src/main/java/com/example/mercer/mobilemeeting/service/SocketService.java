package com.example.mercer.mobilemeeting.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketService extends Service {

    BufferedReader reader;
    public BufferedWriter writer;
    private Socket socket;
    public boolean isConnectSocket;

    private Context mcontext;

    public SocketService() {
    }

    public SocketService(Context mcontext) {
        this.mcontext = mcontext;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        initScoket();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder{

        public SocketService getService(){
            return SocketService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
//            Toast.makeText(mcontext, "socketService stop！", Toast.LENGTH_SHORT).show();
            Log.e("SocketService",""+"socketService stop！");
            if(socket!=null) {
                isConnectSocket = false;
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //初始化
    private void initScoket() {
        new Thread(() -> {
            try {
                boolean isReceiving = true;
                    socket = new Socket("192.168.44.120",520);
//                socket = new Socket("192.168.43.133",520);
                Log.e("sss","后台连接");
                //这个时候soocket初始化完成 连接完成
                isConnectSocket = true;

                //获取socket的输入流
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));

                //接受服务端的数据
                while (isReceiving){
                    boolean isready = reader.ready();
                    if(isready){
                        Log.e("服务传来的数据到达：","服务传来的数据到达");

                        myServiceCallBack.onDataChanged(reader.readLine());
                    }
                    Thread.sleep(200);
                }
                writer.close();
                reader.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private MyServiceCallBack myServiceCallBack;

    public void setMyServiceCallBack(MyServiceCallBack myServiceCallBack) {
        this.myServiceCallBack = myServiceCallBack;
    }

    public MyServiceCallBack getMyServiceCallBack() {
        return myServiceCallBack;
    }

    public interface MyServiceCallBack{
        void onDataChanged(String data);
    }

}
