package com.example.mercer.mobilemeeting.pojo;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/8 09:14.
 */

//        String sql3 = "create table if not exists user(" +
//                "id Integer primary key AUTOINCREMENT not null ," + //主键id
//                "name varchar(10)," +//名字
//                "picture Integer," +
//                "description varchar(50)," +//个性签名
//                "birthday datetime)";//生日
public class User {
    private Integer id;
    private String name;
    private Integer picture;
    private String description;
    private String birthday;

    public User() {
    }

    public User(Integer id, String name, Integer picture, String description, String birthday) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.birthday = birthday;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPicture() {
        return picture;
    }

    public void setPicture(Integer picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
