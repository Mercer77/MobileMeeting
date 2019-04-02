package com.example.mercer.mobilemeeting.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.utils.DataC;
import com.example.mercer.mobilemeeting.utils.blur.BitmapBlurUtil;
import com.example.mercer.mobilemeeting.utils.blur.GauseBulrHelper;
import com.example.mercer.mobilemeeting.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/31 20:13.
 */
public class FragmentMeetingDetail extends Fragment {

    int position = 0;

    @BindView(R.id.meetingName) TextView meetingName;
    @BindView(R.id.meetingTime) TextView meetingTime;
    @BindView(R.id.meetingPicture) CircleImageView meetingPicture;
    @BindView(R.id.linearLayout_Back) LinearLayout linearLayout_Back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meetingdetail,container , false);
        ButterKnife.bind(this , rootView);

        Bundle bundle = getArguments();
        position = bundle.getInt("position");

        //参数 需要处理的图片
        Resources r = this.getContext().getResources();
        Bitmap bitmap =  BitmapFactory.decodeResource(r,R.mipmap.newsimg2);
        bitmap = ((BitmapDrawable)meetingPicture.getDrawable()).getBitmap();
        //让bitmap回收 不然会出现trying to use a recycled bitmap android.graphics
//        if (bitmap != null && !bitmap.isRecycled()) {
//            bitmap.recycle();
//            bitmap = null;
//        }
//        bitmap = ((BitmapDrawable)meetingPicture.getDrawable()).getBitmap();
        blur(getActivity(),bitmap);
        initData(position);

        return rootView;
    }

    //自定义方法实现模糊
    void blur(Context context, Bitmap bitmap) {
//        if (bitmap != null) {
//            //模糊处理
//            BitmapBlurUtil.addTask(bitmap, new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    Drawable drawable = (Drawable) msg.obj;
//                    //设置模糊背景图片
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        linearLayout_Back.setBackground(drawable);
//                    }
//                    //imageview.setImageDrawable(drawable);
//                    bitmap.recycle();
//                }
//            });
//        }

        //GauseBulrHelper：
        if(bitmap!=null){
            Bitmap blur = GauseBulrHelper.blur(context, bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                linearLayout_Back.setBackground(new BitmapDrawable(blur));
            }
        }
    }

    private void initData(int position) {
        meetingName.setText(DataC.title1[position]);
        meetingTime.setText(DataC.date[position]);
    }
}
