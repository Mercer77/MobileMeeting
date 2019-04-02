package com.example.mercer.mobilemeeting.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mercer.mobilemeeting.DBUtil.DBHepler;
import com.example.mercer.mobilemeeting.pojo.MessageEntity;
import com.example.mercer.mobilemeeting.pojo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/2/2 11:09.
 */
public class MsgDao {
    SQLiteDatabase sqLiteDatabase = null;
    DBHepler dbHepler = null;

    public MsgDao(Context mcontext){
        //各种初始化
        dbHepler = new DBHepler(mcontext,"msg.db",null,1);
        sqLiteDatabase = dbHepler.getWritableDatabase();
    }

    public List<MessageEntity> getSimpleHistoryMessage(int who){
        //查询相应from
        List<MessageEntity> messageEntities = new ArrayList<MessageEntity>();
        String sql = "select * from lastmessage where towho = ? or fromwho= ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql , new String[]{String.valueOf(who),String.valueOf(who)});
        while(cursor.moveToNext()){
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setFrom(cursor.getInt(cursor.getColumnIndex("fromwho")));
            messageEntity.setTo(cursor.getInt(cursor.getColumnIndex("towho")));
            messageEntity.setMessage(cursor.getString(cursor.getColumnIndex("msg")));
            messageEntity.setTime(cursor.getString(cursor.getColumnIndex("time")));
            messageEntity.setComeMsg(cursor.getInt(cursor.getColumnIndex("isComemsg")));
            messageEntities.add(messageEntity);
        }
        return messageEntities;
    }

    public List<MessageEntity> getHistoryMessage(int who){
//        Log.e("MsgDao:::::::",position+"回显!!!");
        //查询相应from
        List<MessageEntity> messageEntities = new ArrayList<MessageEntity>();
        String sql = "select * from message where towho = ? or fromwho= ? order by time asc";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,new String[]{String.valueOf(who),String.valueOf(who)});
        while(cursor.moveToNext()){
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setFrom(cursor.getInt(cursor.getColumnIndex("fromwho")));
            messageEntity.setTo(cursor.getInt(cursor.getColumnIndex("towho")));
            messageEntity.setMessage(cursor.getString(cursor.getColumnIndex("msg")));
            messageEntity.setTime(cursor.getString(cursor.getColumnIndex("time")));
            messageEntity.setComeMsg(cursor.getInt(cursor.getColumnIndex("isComemsg")));
            messageEntities.add(messageEntity);
        }
        return messageEntities;
    }

    public int getMessageRows(){
        int rows = 0;//rows默认是0
        //查询不同来源的fromwho的数量
        String sql = "select count(DISTINCT fromwho) as count from message";
        Cursor cursor = sqLiteDatabase.rawQuery(sql , new String[]{});
        while(cursor.moveToNext()){
            rows = cursor.getInt(cursor.getColumnIndex("count"));
        }
        return rows;
    }

    public List<MessageEntity> getMessage(){
        List<MessageEntity> messageEntities = new ArrayList<MessageEntity>();
        String sql = "select * from message";
        Cursor cursor = sqLiteDatabase.rawQuery(sql , new String[]{});
        while(cursor.moveToNext()){
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setFrom(cursor.getInt(cursor.getColumnIndex("fromwho")));
            messageEntity.setTo(cursor.getInt(cursor.getColumnIndex("towho")));
            messageEntity.setMessage(cursor.getString(cursor.getColumnIndex("msg")));
            messageEntity.setTime(cursor.getString(cursor.getColumnIndex("time")));
            messageEntity.setComeMsg(cursor.getInt(cursor.getColumnIndex("isComemsg")));
            messageEntities.add(messageEntity);
        }
        return messageEntities;
    }

    public void addMessage(MessageEntity messageEntity){
        String sql = "insert into message(fromwho,towho,msg,time,isComemsg) values(?,?,?,?,?)";
        sqLiteDatabase.execSQL(sql,new String[]{String.valueOf(messageEntity.getFrom()),
                String.valueOf(messageEntity.getTo()),
                messageEntity.getMessage(),
                messageEntity.getTime(),
                String.valueOf(messageEntity.isComeMsg())
        });
    }

    //lastmessage

    public List<MessageEntity> queryLastMessage(int index){
        List<MessageEntity> messageEntities = new ArrayList<MessageEntity>();
        String index2 = String.valueOf(index);
        String sql = "select * from lastmessage where  towho = ? or fromwho= ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,new String[]{index2 , index2});
        while(cursor.moveToNext()){
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setFrom(cursor.getInt(cursor.getColumnIndex("fromwho")));
            messageEntity.setTo(cursor.getInt(cursor.getColumnIndex("towho")));
            messageEntity.setMessage(cursor.getString(cursor.getColumnIndex("msg")));
            messageEntity.setTime(cursor.getString(cursor.getColumnIndex("time")));
            messageEntity.setComeMsg(cursor.getInt(cursor.getColumnIndex("isComemsg")));
            messageEntities.add(messageEntity);
        }
        return messageEntities;
    }
    public void addLastMessage(MessageEntity messageEntity){
        String sql = "insert into lastmessage(fromwho,towho,msg,time,isComemsg) values(?,?,?,?,?)";
        sqLiteDatabase.execSQL(sql,new String[]{String.valueOf(messageEntity.getFrom()),
                String.valueOf(messageEntity.getTo()),
                messageEntity.getMessage(),
                messageEntity.getTime(),
                String.valueOf(messageEntity.isComeMsg())
        });
    }
    public void updateLastMessage(MessageEntity messageEntity , int index){
        String index2 = String.valueOf(index);
        String sql = "update lastmessage set fromwho = ?," +
                "towho = ?, msg = ?,time = ?,isComemsg = ?" +
                "where towho = ? or fromwho= ?";
        sqLiteDatabase.execSQL(sql,new String[]{String.valueOf(messageEntity.getFrom()),
                String.valueOf(messageEntity.getTo()),
                messageEntity.getMessage(),
                messageEntity.getTime(),
                String.valueOf(messageEntity.isComeMsg()),
                index2,
                index2
        });
    }


    //user
    public void addUser(User user){
        String sql = "insert into user(u_id,name,picture,description,birthday) values(?,?,?,?,?)";
        sqLiteDatabase.execSQL(sql,new String[]{String.valueOf(user.getId()),
                user.getName(),
                String.valueOf(user.getPicture()),
                user.getDescription(),
                user.getBirthday()
        });
    }

    //查询id不等于的User
    public List<User> queryUser(int id){
        List<User> users = new ArrayList<User>();
        String index2 = String.valueOf(id);
        String sql = "select * from user where  id != ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,new String[]{index2 });
        while(cursor.moveToNext()){
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("u_id")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setPicture(cursor.getInt(cursor.getColumnIndex("picture")));
            user.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            user.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
            users.add(user);
        }
        return users;
    }
}
