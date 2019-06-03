package com.example.mercer.mobilemeeting.fragments;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mercer.mobilemeeting.Constant;
import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.fragments.frame.FragmentHome;
import com.example.mercer.mobilemeeting.pojo.Meeting;
import com.example.mercer.mobilemeeting.utils.DataC;
import com.example.mercer.mobilemeeting.utils.SharedPreferencesUtils;
import com.example.mercer.mobilemeeting.utils.blur.BitmapBlurUtil;
import com.example.mercer.mobilemeeting.utils.blur.GauseBulrHelper;
import com.example.mercer.mobilemeeting.widget.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/31 20:13.
 */
public class FragmentMeetingDetail extends Fragment {

    int position = 0;
    Meeting meeting;
    List<Meeting> meetings = new ArrayList<>();

    @BindView(R.id.meetingName) TextView meetingName;
    @BindView(R.id.meetingTime) TextView meetingTime;
    @BindView(R.id.meetingPicture) CircleImageView meetingPicture;
    @BindView(R.id.linearLayout_Back) LinearLayout linearLayout_Back;
    @BindView(R.id.ms_bt_publish) Button ms_bt_publish;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.DATA_CHANGE:
                    meetingName.setText(meeting.getMeetingTitle());
                    meetingTime.setText(meeting.getCreateTime());
                    break;
                case Constant.BUTTON_CHANGE:


                    break;
                default:break;

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meetingdetail,container , false);
        ButterKnife.bind(this , rootView);

        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        //初始化图片
        initPicture();
        //初始化数据
        getMeetingData(position);

        return rootView;
    }

    private void initPicture() {

        //参数 需要处理的图片
        //不用这种 会出现trying to use a recycled bitmap android.graphics

        Resources r = this.getContext().getResources();
        Bitmap bitmap =  BitmapFactory.decodeResource(r,R.mipmap.newsimg2);

//        use a recycled bitmap android.graphics
//        Bitmap bitmap = ((BitmapDrawable)meetingPicture.getDrawable()).getBitmap();

        blur(getActivity(),bitmap);
    }

    //自定义方法实现模糊
    void blur(Context context, Bitmap bitmap) {
        //GauseBulrHelper：
        if(bitmap!=null){
            Bitmap blur = GauseBulrHelper.blur(context, bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Drawable drawable = new BitmapDrawable(blur);
                linearLayout_Back.setBackground(drawable);
            }
        }
    }

    private void getMeetingData(int position) {
        //同步请求
        new Thread(()->{
            try {
                OkHttpClient client = new OkHttpClient();
                //这里也不用声明get  默认GET请求
                //获取会议列表数据
                Request request = new Request.Builder()
                        .url("http://"+ Constant.IP_LIANG_BLUETOOTH +":8080/MeetingSystem/meeting/queryAllMeetings/" +
                                SharedPreferencesUtils.getUserName("userId") + "/" +
                                (position+1) +
                                ".do")
                        .build();

                Response response = client.newCall(request).execute();//得到Response 对象

                if (response.isSuccessful()) {//注意response.body().string()只能调用一次
                    //所以这样写：
                    String responseData = response.body().string();
                    Log.d("okhttp","response.code:"+response.code());
                    Log.d("okhttp","response.message:"+response.message());
                    Log.d("okhttp","res:"+responseData);
                    Message msg = new Message();
                    msg.what = Constant.DATA_CHANGE;
                    msg.obj = "response.code:"+response.code()
                            +"response.message:"+response.message()
                            +"res:"+responseData;

                    parseEasyJson(responseData);
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    /**
     * 查询当前用户ID所参与的用户
     * @param position
     */
    private void judgeMeeting(int position) {
        //同步请求
        new Thread(()->{
            try {
                OkHttpClient client = new OkHttpClient();
                //这里也不用声明get  默认GET请求
                //获取会议列表数据
                Request request = new Request.Builder()
                        .url("http://"+ Constant.IP_LIANG_BLUETOOTH +":8080/MeetingSystem/meeting/queryAllInvitationMeetings/" +
                                SharedPreferencesUtils.getUserName("userId") +
                                ".do")
                        .build();

                Response response = client.newCall(request).execute();//得到Response 对象

                if (response.isSuccessful()) {//注意response.body().string()只能调用一次
                    //所以这样写：
                    String responseData = response.body().string();
                    Log.d("okhttp","response.code:"+response.code());
                    Log.d("okhttp","response.message:"+response.message());
                    Log.d("okhttp","res:"+responseData);
                    Message msg = new Message();
                    msg.what = Constant.DATA_CHANGE;
                    msg.obj = "response.code:"+response.code()
                            +"response.message:"+response.message()
                            +"res:"+responseData;

                    judgeParseEasyJson(responseData);
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    private void judgeParseEasyJson(String json){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
            Log.e("",jsonArray+"");
            List<Meeting> lists = new ArrayList<>();
            for(int i = 0;i < jsonArray.length();i++){
                //拿到第一个json对象
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Meeting meeting = new Meeting();
                meeting.setId(jsonObject.getInt("id"));
                lists.add(meeting);
            }
            meetings.addAll(lists);
            //通知handler更新
            handler.sendEmptyMessage(Constant.BUTTON_CHANGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseEasyJson(String json){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
            Log.e("",jsonArray+"");
            //拿到第一个json对象
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);

            meeting = new Meeting();
            meeting.setMeetingTitle(jsonObject.getString("meetingTitle"));
            meeting.setMeetingContent(jsonObject.getString("meetingContent"));
            meeting.setCreateTime(jsonObject.getString("createTime"));
            meeting.setTime(jsonObject.getString("time"));
            meeting.setPath(jsonObject.getString("path"));
            meeting.setAddress(jsonObject.getString("address"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
