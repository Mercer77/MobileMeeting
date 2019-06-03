package com.example.mercer.mobilemeeting.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/8 15:15.
 */
public class TimeUtil {

    /**
     * 得到自己定义的时间格式的样式
     * @param millTime
     * @return
     */
    public static String getTime( long millTime) {
        Date d = new Date(millTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        System. out.println(sdf.format(d));
        return sdf.format(d);
    }

}
