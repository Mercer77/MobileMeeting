package com.example.mercer.mobilemeeting.fragments.frame;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.adapter.LoadMoreWrapper;
import com.example.mercer.mobilemeeting.adapter.LoadMoreWrapperMessageAdapter;
import com.example.mercer.mobilemeeting.dao.MsgDao;
import com.example.mercer.mobilemeeting.fragments.FragmentAddFriend;
import com.example.mercer.mobilemeeting.fragments.FragmentMmessageDetails3;
import com.example.mercer.mobilemeeting.listener.EndlessRecyclerOnScrollListener;
import com.example.mercer.mobilemeeting.pojo.MessageEntity;
import com.example.mercer.mobilemeeting.pojo.User;
import com.example.mercer.mobilemeeting.service.SocketService;
import com.example.mercer.mobilemeeting.utils.SharedPreferencesUtils;
import com.example.mercer.mobilemeeting.utils.TimeUtil;
import com.example.mercer.mobilemeeting.widget.PopWindow.CustomPopWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/28 18:26.
 */
public class FragmentMessage extends Fragment implements ServiceConnection {

    private SocketService.MyBinder binder;
    private SocketService socketService;

    @BindView(R.id.mult) ImageView mult;
    @BindView(R.id.recycler_view_message) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_message_layout) SwipeRefreshLayout swipeRefreshLayout;

    private LoadMoreWrapper loadMoreWrapper;
    private List<MessageEntity> messageEsays = new ArrayList<>();
    private LoadMoreWrapperMessageAdapter loadMoreWrapperAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    loadMoreWrapper.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message,container,false);
        ButterKnife.bind(this,rootView);

        //加号菜单栏
        mult.setOnClickListener((View v)-> {
            View v1 = LayoutInflater.from(getActivity()).inflate(R.layout.pop_layout1,null);

            CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                    .setView(v1)
                    .enableBackgroundDark(true)
                    .setBgDarkAlpha(0.8f)
                    .setFocusable(true)
                    .setOutsideTouchable(true)
                    .create();
            popWindow.showAsDropDown(mult,-30,20);

            LinearLayout addfriend = v1.findViewById(R.id.tv_addfriend);
            LinearLayout saoyisao = v1.findViewById(R.id.tv_saoyisao);
            View.OnClickListener listener = v2 -> {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (v2.getId()) {
                    case R.id.tv_addfriend:
                        Log.e("sd","addfriend");
                        fragmentTransaction.replace(R.id.main_content, new FragmentAddFriend())
                                .hide(FragmentMessage.this)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.tv_saoyisao:
//                        Intent intent = new Intent();
//                        intent.setClass(getContext() , TransImgAct.class);
//                        startActivity(intent);
                        break;
                    case R.id.tv_fastSend_pop:
//                        Intent intent2 = new Intent();
//                        intent2.setClass(getContext() , FastActivity.class);
//                        startActivity(intent2);
                        break;
                }
                popWindow.dissmiss();
            };
            addfriend.setOnClickListener(listener);
            saoyisao.setOnClickListener(listener);
        });

        // 模拟获取数据
        getData();
        //在LoadMoreWrapperAdapter设置自定义样式
        newLoad();
        initRecyclerView();

        return rootView;
    }

    private void initRecyclerView() {
        //分割线
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        loadMoreWrapper = new LoadMoreWrapper(loadMoreWrapperAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(loadMoreWrapper);

        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // 刷新数据
            getData();

            // 延时1s关闭下拉刷新
            swipeRefreshLayout.postDelayed(() -> {
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    //关闭隐藏的刷新bar
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);

            Log.e("刷新",messageEsays.get(0).getMessage());
            loadMoreWrapper.notifyDataSetChanged();

            loadMoreWrapperAdapter.notifyDataSetChanged();
            loadMoreWrapper.notifyDataSetChanged();

        });

        // 设置加载更多监听
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

                if (messageEsays.size() < 52) {
                    // 模拟获取网络数据，延时1s
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //主线程刷新UI
                            getActivity().runOnUiThread(() -> {
                                getData();
                                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                            });
                        }
                    }, 1000);
                } else {
                    // 显示加载到底的提示
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("FragmentMS2","onCreate");
        //开启SocketService
        getActivity().bindService(new Intent(getActivity(), SocketService.class),this, Context.BIND_AUTO_CREATE);
        Log.e("bindService","bindService");
    }

    private void newLoad() {
        //自定义接口
        loadMoreWrapperAdapter = new LoadMoreWrapperMessageAdapter(getContext()
                , messageEsays
                , position -> {
            Log.e("点击了","点击了");
//            startActivity(new Intent(getActivity(), SearchDemoActivity.class));
                    //消息详情页面跳转
                    FragmentMmessageDetails3 fragmentMmessageDetails3 = new FragmentMmessageDetails3(
                            (pos, messageEsay) -> {
                                //pos等于-1表示数据库为空
                                if(pos != -1) {
                                    //将数据跟新到数据
                                    messageEsays.get(pos).setMessage(messageEsay.getMessage());
                                    messageEsays.get(pos).setTime(messageEsay.getTime());
                                    //通知Adapter数据改变
                                    handler.sendEmptyMessage(0);
                                }
                            }
                    );
                    Bundle bundle = new Bundle();
                    bundle.putInt("item",position);
                    bundle.putInt("who",getWho(position));
                    fragmentMmessageDetails3.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_content,fragmentMmessageDetails3)
                            .hide(this)
                            .addToBackStack(null)
                            .commit();
        });
    }

    //判断对方是谁 需要根据是否是到达的信息和from 、to来判断
    private int getWho(int position) {
        int isComeMsg = messageEsays.get(position).isComeMsg();
        int to = messageEsays.get(position).getTo();
        int from = messageEsays.get(position).getFrom();
        return isComeMsg == 1 ? from : to;
    }

    private void getData() {
        messageEsays.clear();
        //创造模拟数据
//        createData();

//        数据库中查询数据
        MsgDao msgDao = new MsgDao(getActivity());
//        messageEsays  = msgDao.getSimpleHistoryMessage
// (Integer.parseInt(SharedPreferencesUtils.getUserName("userId")));
        //这里不能这样写 因为recyclerView的观察者首先确认观察对象 也就是观察数据  数据控制住了就能更新UI
        //而 这里Observer直接锁定了后面产生的临时对象 并没有观察messageEsays 造成数据刷新失败
        messageEsays.addAll(msgDao.getSimpleHistoryMessage(Integer.parseInt(SharedPreferencesUtils.getUserName("userId"))));

        handler.sendEmptyMessage(0);

    }

    private void createData() {
        MsgDao msgDao = new MsgDao(getActivity());

        MessageEntity message = new MessageEntity();
        message.setTime(TimeUtil.getTime(System.currentTimeMillis()));
        message.setFrom(0);
        message.setTo(1);
        message.setComeMsg(isCome(0));
        message.setMessage("我是0");
        msgDao.addMessage(message);

        MessageEntity message2 = new MessageEntity();
        message2.setTime(TimeUtil.getTime(System.currentTimeMillis()));
        message2.setFrom(1);
        message2.setTo(0);
        message2.setComeMsg(isCome(1));
        message2.setMessage("我是1");
        msgDao.addMessage(message2);
        MessageEntity lastmessage = new MessageEntity();
        lastmessage.setTime(TimeUtil.getTime(System.currentTimeMillis()));
        lastmessage.setFrom(1);
        lastmessage.setTo(0);
        lastmessage.setComeMsg(isCome(1));
        lastmessage.setMessage("我是1");
        lastMessage(lastmessage);

        MessageEntity message3 = new MessageEntity();
        message3.setTime(TimeUtil.getTime(System.currentTimeMillis()));
        message3.setFrom(0);
        message3.setTo(1);
        message3.setComeMsg(isCome(0));
        message3.setMessage("我是0");
        msgDao.addMessage(message3);
        MessageEntity lastmessage2 = new MessageEntity();
        lastmessage2.setTime(TimeUtil.getTime(System.currentTimeMillis()));
        lastmessage2.setFrom(0);
        lastmessage2.setTo(1);
        lastmessage2.setComeMsg(isCome(0));
        lastmessage2.setMessage("我是0");
        lastMessage(lastmessage2);

        msgDao.addUser(new User(0,
                "oppo",
                R.mipmap.oppo,
                "宇宙无敌超级大帅哥",
                TimeUtil.getTime(System.currentTimeMillis())));
        msgDao.addUser(new User(1,
                "小米",
                R.mipmap.xiaomi,
                "我不如你",
                TimeUtil.getTime(System.currentTimeMillis())));
    }

    private void lastMessage(MessageEntity messageEntity) {
        MsgDao msgDao = new MsgDao(getActivity());
        //查询最新消息是否有历史消息
        List<MessageEntity> messageEntities = msgDao.queryLastMessage(1);
        if(messageEntities.size() != 0) {
            msgDao.updateLastMessage(messageEntity , 1);
        }else{
            msgDao.addLastMessage(messageEntity);
        }
    }

    private int isCome(int from) {
        Integer id = Integer.parseInt(SharedPreferencesUtils.getUserName("userId"));
        return id == 1 ? from == 0 ? 1 : 0 : id == 0 ? from == 1 ? 1 : 0 : -1;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (SocketService.MyBinder) service;
        //拿到服务的对象
        socketService = binder.getService();
        //设置回调
        socketService.setMyServiceCallBack(new SocketService.MyServiceCallBack() {
            @Override
            public void onDataChanged(String data) {
                Log.e("onServiceConnected!","onServiceConnected");
                JSONObject json = null;
                try {
                    json = new JSONObject(data);
//                    MainActivity activity = (MalinActivity)getActivity();
                    MsgDao msgDao = new MsgDao(getActivity());

                    //将收到的消息写入消息队列
                    MessageEntity messageCome = new MessageEntity();
                    messageCome.setComeMsg(1);
                    messageCome.setTime(json.getString("time"));
                    //将发过来的to消息封装到from里面 Adapter里面用来显示发送者的name
                    messageCome.setFrom(json.getInt("from"));
                    messageCome.setTo(json.getInt("to"));
                    messageCome.setMessage(json.getString("msg"));
                    msgDao.addMessage(messageCome);

                    if(messageEsays.get(messageCome.getFrom()) != null) {
                        //更新替换新的messageEsays
                        messageEsays.set(messageCome.getFrom(), messageCome);
                    }else{
                        messageEsays.add(messageCome);
                    }
                    //替换之后通知handle更新UI
                    handler.obtainMessage(0).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }



}
