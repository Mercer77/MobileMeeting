package com.example.mercer.mobilemeeting;

import okhttp3.MediaType;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/28 18:19.
 */
public class Constant {
    public static final String IP_LOCAL = "localhost";
    public static final String IP_LIANG_BLUETOOTH = "192.168.44.120";
    public static final String IP_LIANG_WIFI = "192.168.42.139";
    public static final String IPMercer = "10.113.24.128";
    public static final String IPMercer2 = "10.151.6.131";


    public static final String IP_LINUX = "120.78.123.233";



    public static final int SHOW_RSPONSE = 2001;//必须申明为final 常量 因为switch里面需要常量
    public static final int SHOW_RSPONSEASYNC = 2002;//必须申明为final 常量 因为switch里面需要常量
    public static final int DATA_CHANGE = 33;


    public static final MediaType JSON = MediaType.parse("application/json");

}
