package com.example.mercer.mobilemeeting.utils;

import com.example.mercer.mobilemeeting.R;

public class PictureUtil {

    //拿到自己的头像
    public static int getThisPicture(String suserId) {
        int userId = Integer.parseInt(suserId);
        return userId == 0 ? R.mipmap.oppo : R.mipmap.xiaomi;
    }

    //拿到对方的头像
    public static int getThatPicture(String suserId) {
        int userId = Integer.parseInt(suserId);
        return userId == 0 ? R.mipmap.xiaomi : R.mipmap.oppo;
    }

}
