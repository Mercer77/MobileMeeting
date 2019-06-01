package com.example.mercer.mobilemeeting.fragments;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercer.mobilemeeting.MainActivity;
import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.adapter.MyMsgAdapter;
import com.example.mercer.mobilemeeting.dao.MsgDao;
import com.example.mercer.mobilemeeting.pojo.MessageEntity;
import com.example.mercer.mobilemeeting.pojo.User;
import com.example.mercer.mobilemeeting.service.SocketService;
import com.example.mercer.mobilemeeting.utils.SharedPreferencesUtils;
import com.example.mercer.mobilemeeting.utils.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class FragmentMmessageDetails3 extends Fragment implements View.OnClickListener, ServiceConnection {

    private SocketService.MyBinder binder;
    private SocketService socketService;
    private MsgDao msgDao = null;

//    private Button startscoket;
    private Button send;
    private EditText message;
    private EditText display;
    private TextView chat_title_nick;
    private ImageView chat_title_back;
    private ImageView detail_friend;

    private List<MessageEntity> messageEntities = new ArrayList<>();
    private MyMsgAdapter myMsgAdapter;
    private ListView chat_content_list;

    private StringBuffer stringBuffer = new StringBuffer();

    BufferedReader reader;
    BufferedWriter writer;
    boolean isConnectSocket;

    private MyCallBack callBack;
    int pos = 0;
    int who = -1;

    public FragmentMmessageDetails3(MyCallBack callback){
        this.callBack = callback;
    }

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //更新消息Adapter
                    Log.e("notifyDataSetChanged","notifyDataSetChanged");
                    myMsgAdapter.notifyDataSetChanged();
                    chat_content_list.setSelection(messageEntities.size()-1);
                    break;
                    default:break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_mmessage_details2,container,false);
//        startscoket = (Button)view.findViewById(R.id.startScoket);
//        startscoket.setOnClickListener(this);
        send = (Button)view.findViewById(R.id.ms_details_btn_send2);
        send.setOnClickListener(this);
        message = (EditText) view.findViewById(R.id.ms_details_et_message2);
//        display = (EditText) view.findViewById(R.id.ms_details_et_display);
        chat_content_list = (ListView)view.findViewById(R.id.chat_content_list);
        chat_title_nick = view.findViewById(R.id.chat_title_nick);
        chat_title_back = view.findViewById(R.id.chat_title_back);
        chat_title_back.setOnClickListener(this);
        detail_friend = view.findViewById(R.id.detail_friend);
        detail_friend.setOnClickListener(this);

        pos = getArguments().getInt("item");
        who = getArguments().getInt("who");

        MsgDao msgDao1 = new MsgDao(getActivity());
        List<User> users = msgDao1.queryUser(who);
        chat_title_nick.setText(users.get(0).getName());

        //msg需要初始化之前的消息 将以前的聊天数据进行回显
        //请求后台数据
        getHistoryMessageData();

        myMsgAdapter = new MyMsgAdapter(getActivity(),messageEntities , chat_title_nick.getText().toString());
        chat_content_list.setAdapter(myMsgAdapter);
        chat_content_list.setSelection(messageEntities.size()-1);

        return view;
    }

    //获取数据
    private void getHistoryMessageData() {
        MsgDao msgDao = new MsgDao(getActivity());
        messageEntities = msgDao.getHistoryMessage(who);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("FragmentDetails","onCreate");
        msgDao = new MsgDao(getActivity());

        //开启SocketService
        getActivity().bindService(new Intent(getActivity(), SocketService.class),this, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //拿到listView的最后一行数据进行回调
        if(messageEntities.size()!=0) {
            MessageEntity messageEntity = messageEntities.get(messageEntities.size() - 1);
            MessageEntity messageEsay = new MessageEntity();
            messageEsay.setMessage(messageEntity.getMessage());
            messageEsay.setTime(messageEntity.getTime());
            //回调FragmentMS的callback设置content
            callBack.callBack(pos, messageEsay);
        }else{
            callBack.callBack(-1,null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_title_back:
                getActivity().onBackPressed();
                break;
            case R.id.ms_details_btn_send2:
                if(isConnectSocket) {
                    sendMsg();
                }else{
                    Toast.makeText(getContext(), "抱歉，请先连接服务器！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.detail_friend:
                //进入好友界面
//                startActivity(new Intent(getActivity(),FriendDetailActivity.class));
                break;
                default:break;
        }
    }

    private void sendMsg() {
        if(message.getText()!=null){
            try {
                //封装json数据上传服务器
                JSONObject json = new JSONObject();
                try {
                    json.put("to",who);
                    json.put("msg",message.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                writer.write(json.toString()+"\n");
                Log.e(null,"发出json:"+json.toString());
                //flush写入缓存里面的数据
                writer.flush();

                //将自己发出的消息写入消息队列
                MessageEntity messagesend = new MessageEntity();
                messagesend.setComeMsg(0);
                messagesend.setTime(TimeUtil.getTime(System.currentTimeMillis()));
                //这个from值需要确认................
                messagesend.setFrom(Integer.parseInt(SharedPreferencesUtils.getUserName("userId")));
                messagesend.setTo(who);
                Log.e("发送位置",""+who);
                messagesend.setMessage(message.getText().toString());
                //将自己发送出去的消息存到数据库中
                msgDao.addMessage(messagesend);
                lastMessage(messagesend);

                //将自己发送出去的消息更新到List集合 并且通知Adapter更新UI
                messageEntities.add(messagesend);

                Log.e("message的大小1：",String.valueOf(messageEntities.size()));
                handler.obtainMessage(0,messagesend).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void lastMessage(MessageEntity messageEntity) {
        //查询最新消息是否有历史消息
        List<MessageEntity> messageEntities = msgDao.queryLastMessage(1);
        if(messageEntities.size() != 0) {
            msgDao.updateLastMessage(messageEntity , 1);
        }else{
            msgDao.addLastMessage(messageEntity);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (SocketService.MyBinder) service;
        //拿到服务的对象
        socketService = binder.getService();
        isConnectSocket = socketService.isConnectSocket;
        writer = socketService.writer;
        //设置回调 接受信息的数据
        socketService.setMyServiceCallBack(data -> {
            Log.e("onServiceConnected!","onServiceConnected");
            JSONObject json = null;
            try {
                json = new JSONObject(data);

                //将收到的消息写入消息队列
                MessageEntity messageCome = new MessageEntity();
                messageCome.setComeMsg(1);
                messageCome.setTime(json.getString("time"));
                //将发过来的to消息封装到from里面 Adapter里面用来显示发送者的name
                messageCome.setFrom(json.getInt("from"));
                messageCome.setTo(json.getInt("to"));
                messageCome.setMessage(json.getString("msg"));
                Log.e("发送位置",""+messageCome.toString());
                msgDao.addMessage(messageCome);
                lastMessage(messageCome);

                //更新   messageEntities!!!!!!!
                messageEntities.add(messageCome);
                //替换之后通知handle更新UI
                handler.obtainMessage(0,messageCome).sendToTarget();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    //初始化
//    private void initScoket() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    boolean isReceiving = true;
//                    socket = new Socket("192.168.1.7",1234);
//                    //这个时候soocket初始化完成 连接完成
//                    isConnectSocket = true;
//
//                    //获取socket的输入流
//                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
//                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));
//
//                    //接受服务端的数据
//                    while (isReceiving){
//                        boolean isready = reader.ready();
//                        if(isready){
//                            Log.e("服务传来的数据到达：","服务传来的数据到达");
//                            JSONObject json = new JSONObject(reader.readLine());
//                            //将收到的消息写入消息队列
//                            MessageEntity messageCome = new MessageEntity();
//                            messageCome.setComeMsg(1);
//                            messageCome.setTime(json.getString("time"));
//                            //将发过来的to消息封装到from里面 Adapter里面用来显示发送者的name
//                            messageCome.setFrom(json.getInt("from"));
//                            messageCome.setMessage(json.getString("msg"));
//                            messageEntities.add(messageCome);
//                            Log.e("message的大小2：",String.valueOf(messageEntities.size()));
//
//                            handler.obtainMessage(0,messageCome).sendToTarget();
//                        }
//                        Thread.sleep(200);
//                    }
//                    writer.close();
//                    reader.close();
//                    socket.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }


    public interface MyCallBack{//自定义接口更新消息列表的简略消息显示
        public void callBack(int pos, MessageEntity messageEsay);
    }

}
