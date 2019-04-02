package com.example.mercer.mobilemeeting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.pojo.MessageEntity;

import java.util.List;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/1/28 20:46.
 */
public class MyMsgAdapter extends BaseAdapter{

    private Context mcontext;
    private List<MessageEntity> msgs;
    private LayoutInflater layoutInflater;
    private int that1 = 0;
    private Bitmap this1 = null;
    private String thatName = "";

    public MyMsgAdapter(Context mcontext, List<MessageEntity> msgs , int that1 , Bitmap this1 , String thatName){
        this.mcontext = mcontext;
        this.msgs = msgs;
        this.that1 = that1;
        this.this1 = this1;
        this.thatName = thatName;
        layoutInflater = LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageEntity messageEntity = msgs.get(position);
        boolean isC = messageEntity.isComeMsg() == 1 ? true : false;
        ViewHolder viewHolder = new ViewHolder();
            //发来的消息和自己发出的消息有不同的布局，也就是左右之分
        if(!isC){
            convertView = layoutInflater.inflate(R.layout.chatting_item_msg_right,parent,false);
        }else{
            convertView = layoutInflater.inflate(R.layout.chatting_item_msg_left,parent,false);
        }

        viewHolder.tvUserName = convertView.findViewById(R.id.lr_username);
        viewHolder.tvSendTime = convertView.findViewById(R.id.tv_sendtime);
        viewHolder.tvContent = convertView.findViewById(R.id.tv_chatcontent);
        viewHolder.ivPicture = convertView.findViewById(R.id.iv_userhead);
        if(!isC){
//            viewHolder.ivPicture.setImageBitmap(this1);
            viewHolder.ivPicture.setImageResource(R.mipmap.lihua1);
        }else{
            viewHolder.ivPicture.setImageResource(that1);
            viewHolder.tvUserName.setText(thatName);
//            Log.e("详情1",thatName);
//            Log.e("详情2",viewHolder.lr_username.getText().toString());
        }

//        viewHolder.tvUserName.setText(String.valueOf(messageEntity.getFrom()));
        viewHolder.tvSendTime.setText(messageEntity.getTime());
//        Toast.makeText(mcontext,"message:"+messageEntity.getMessage(),Toast.LENGTH_LONG).show();
        viewHolder.tvContent.setText(messageEntity.getMessage());
//        Log.e("详情3",viewHolder.lr_username.getText().toString());
        return convertView;
    }

    class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public ImageView ivPicture;
        public boolean isComMsg = true;
    }
}
