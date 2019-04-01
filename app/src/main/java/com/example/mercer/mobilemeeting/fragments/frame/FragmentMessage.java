package com.example.mercer.mobilemeeting.fragments.frame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.fragments.FragmentAddFriend;
import com.example.mercer.mobilemeeting.pojo.Message;
import com.example.mercer.mobilemeeting.widget.Friend.RefreshAdapter;
import com.example.mercer.mobilemeeting.widget.PopWindow.CustomPopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/28 18:26.
 */
public class FragmentMessage extends Fragment{

    @BindView(R.id.mult)
    ImageView mult;
    @BindView(R.id.recycler_view_message)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_message_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<Message> mDatas = new ArrayList<>();
    private RefreshAdapter mRefreshAdapter;
    private LinearLayoutManager mLinearLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message,container,false);
        ButterKnife.bind(this,rootView);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initView();
        initData();
        initListener();
        return rootView;
    }
    private void initView() {

        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN);

    }

    private void initData() {

        Message m1 = new Message();
        m1.setName("张三");
        m1.setContent("速度快垃圾上单");
        m1.setDate("2019/3/2 21:32:23");
        Message m2 = new Message();
        m2.setName("李四");
        m2.setContent("速度快垃圾上单");
        m2.setDate("2019/3/2 21:32:23");
        Message m3 = new Message();
        m3.setName("王五");
        m3.setContent("速度快问问圾上单");
        m3.setDate("2019/3/2 21:32:23");
        mDatas.add(m1);
        mDatas.add(m2);
        mDatas.add(m3);
        initRecylerView();
    }

    private void initRecylerView() {

        mRefreshAdapter = new RefreshAdapter(getContext(),mDatas);
        mLinearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mRefreshAdapter);

    }

    private void initListener() {

        initPullRefresh();

        initLoadMoreListener();

    }



    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<Message> headDatas = new ArrayList<Message>();

                            Message m1 = new Message();
                            m1.setName("张三");
                            m1.setContent("速度快垃圾上单");
                            m1.setDate("2019/3/2 21:32:23");
                            headDatas.add(m1);

                        mRefreshAdapter.AddHeaderItem(headDatas);

                        //刷新完成
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "更新了 "+headDatas.size()+" 条目数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

    private void initLoadMoreListener() {

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem ;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItem+1==mRefreshAdapter.getItemCount()){

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            List<Message> footerDatas = new ArrayList<Message>();

                                Message m1 = new Message();
                                m1.setName("张三");
                                m1.setContent("速度快垃圾上单");
                                m1.setDate("2019/3/2 21:32:23");
                                footerDatas.add(m1);

                            mRefreshAdapter.AddFooterItem(footerDatas);
                            Toast.makeText(getActivity(), "更新了 "+footerDatas.size()+" 条目数据", Toast.LENGTH_SHORT).show();
                        }
                    }, 3000);


                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
            }
        });

    }






    @OnClick(R.id.mult)
    public void onClick(View v){
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
//                    fragmentTransaction.replace(R.id.main_content, new FragmentAddFriend())
//                            .hide(FragmentMS2.this)
//                            .addToBackStack(null)
//                            .commit();
                    break;
                case R.id.tv_saoyisao:
//                    Intent intent = new Intent();
//                    intent.setClass(getContext() , TransImgAct.class);
//                    startActivity(intent);
                    break;
                case R.id.tv_fastSend_pop:
//                    Intent intent2 = new Intent();
//                    intent2.setClass(getContext() , FastActivity.class);
//                    startActivity(intent2);
                    break;
            }
            popWindow.dissmiss();
        };
        addfriend.setOnClickListener(listener);
        saoyisao.setOnClickListener(listener);
    }
}
