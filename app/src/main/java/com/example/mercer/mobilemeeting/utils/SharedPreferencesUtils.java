package com.example.mercer.mobilemeeting.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * 注意要点：1、需要在application中调用init()，
 *           2、需要在清单文件中声名
 *           3、需要在每个get和put方法中先调用throwInit()
 *           4、创建一个Constants类，里面定义一些静态final常量，存放文件名或者key值(脑袋智障了忘掉咋整?)
 * @author admin
 * @version 1.0
 * @create 2018/6/28
 */
public class SharedPreferencesUtils {

    private static Context mContext;
    private static String fileName="shared_pres";

    public static void init(Context context) {
        mContext = context;
    }

    private static void throwInit() {
        if (mContext == null) {
            throw new NullPointerException("在使用该方法前，需要init()方法，推荐init()放在application里");
        }
    }
    public static boolean setUserName(String key,String name) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.edit().putString(key,name).commit();
    }
    public static String getUserName(String key) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getString(key,"error");
    }

    public static boolean setUserImagePath(String key,String imagepath) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.edit().putString(key,imagepath).commit();
    }
    public static String getUserImagePath(String key) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getString(key,"error");
    }

    public static void clear(){
        SharedPreferences sp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

}
