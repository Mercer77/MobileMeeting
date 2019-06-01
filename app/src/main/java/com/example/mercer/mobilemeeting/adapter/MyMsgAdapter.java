package com.example.mercer.mobilemeeting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.pojo.MessageEntity;
import com.example.mercer.mobilemeeting.utils.PictureUtil;
import com.example.mercer.mobilemeeting.utils.SharedPreferencesUtils;

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
    private String thatName = "";

    public MyMsgAdapter(Context mcontext, List<MessageEntity> msgs , String thatName){
        this.mcontext = mcontext;
        this.msgs = msgs;
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
        boolean isC = messageEntity.isComeMsg() == 1;
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
        if( ! isC){
            //发来的设置为对方的头像
            viewHolder.ivPicture.setImageResource(PictureUtil.getThisPicture(SharedPreferencesUtils.getUserName("userId")));
        }else{
            //否则设置自己的头像
            viewHolder.ivPicture.setImageResource(PictureUtil.getThatPicture(SharedPreferencesUtils.getUserName("userId")));
            if(R.mipmap.oppo == PictureUtil.getThisPicture(SharedPreferencesUtils.getUserName("userId"))){
                Log.e("拿到自己的头像","yes");
            }
            else Log.e("拿到自己的头像","yes");

            viewHolder.tvUserName.setText(thatName);
        }

        viewHolder.tvSendTime.setText(messageEntity.getTime());
        viewHolder.tvContent.setText(messageEntity.getMessage());
        return convertView;
    }

    class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public ImageView ivPicture;
    }
}
