package com.example.mercer.mobilemeeting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.dao.MsgDao;
import com.example.mercer.mobilemeeting.pojo.MessageEntity;
import com.example.mercer.mobilemeeting.pojo.User;
import com.example.mercer.mobilemeeting.utils.PictureUtil;
import com.example.mercer.mobilemeeting.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * 上拉加载更多
 * Created by yangle on 2017/10/12.
 */

public class LoadMoreWrapperMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessageEntity> messageEsays;
    private Context mcontext;
    private OnItemClickListener mListener;

    public LoadMoreWrapperMessageAdapter(Context mcontext , List<MessageEntity> dataList , OnItemClickListener mListener) {
        this.mcontext = mcontext;
        this.messageEsays = dataList;
        this.mListener =  mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_message_recyclerview, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

        //拿到最新信息
        MessageEntity messageEasy = messageEsays.get(position);
        //拿到是谁
        int who = getWho(position);

        //需要查询出发送者的信息
        MsgDao msgDao1 = new MsgDao(mcontext);
        List<User> users = msgDao1.queryUser(who);

        recyclerViewHolder.ivpicture.setImageResource(PictureUtil.getThatPicture(SharedPreferencesUtils.getUserName("userId")));
        recyclerViewHolder.tvtitle.setText(String.valueOf(users.get(0).getName()));
        recyclerViewHolder.tvtime.setText(String.valueOf(messageEasy.getTime()));
        recyclerViewHolder.tvcontent.setText(messageEasy.getMessage());

        //回调
        recyclerViewHolder.item.setOnClickListener(v -> mListener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return messageEsays.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        LinearLayout item;
        TextView tvtitle;
        TextView tvcontent;
        TextView tvtime;
        ImageView ivpicture;
        
        RecyclerViewHolder(View itemView) {
            super(itemView);
            item = (LinearLayout) itemView.findViewById(R.id.ms_ll);
            tvcontent = (TextView) itemView.findViewById(R.id.ms_tv_content);
            tvtitle = (TextView) itemView.findViewById(R.id.ms_tv_title);
            tvtime = (TextView) itemView.findViewById(R.id.ms_tv_time);
            ivpicture = (ImageView) itemView.findViewById(R.id.ms_iv_picture);
        }
    }

    //判断对方是谁 需要根据是否是到达的信息和from 、to来判断
    private int getWho(int position) {
        int isComeMsg = messageEsays.get(position).isComeMsg();
        int to = messageEsays.get(position).getTo();
        int from = messageEsays.get(position).getFrom();
        return isComeMsg == 1 ? from : to;
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

}
