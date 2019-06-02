package com.example.mercer.mobilemeeting.fragments.frame;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mercer.mobilemeeting.Constant;
import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.fragments.FragmentMeetingDetail;
import com.example.mercer.mobilemeeting.pojo.BannerModel;
import com.example.mercer.mobilemeeting.pojo.Contact;
import com.example.mercer.mobilemeeting.pojo.Meeting;
import com.example.mercer.mobilemeeting.utils.DataC;
import com.example.mercer.mobilemeeting.widget.Friend.ContactAdapter;
import com.example.mercer.mobilemeeting.widget.Friend.HanziToPinyin;
import com.example.mercer.mobilemeeting.widget.Lamp.LampView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sivin.Banner;
import com.sivin.BannerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/28 18:26.
 */
public class FragmentHome extends Fragment implements View.OnClickListener ,
        PullToRefreshBase.OnRefreshListener2 {

    //跑马灯
    @BindView(R.id.lamp_view)
    LampView lampView;
    @BindView(R.id.code_iv)
    ImageView code_iv;//二维码
    @BindView(R.id.welfare_bar_view)
    RelativeLayout welfareBarView;
    @BindView(R.id.pull_refresh_scrollview)
    PullToRefreshScrollView pullRefreshScrollview;
    @BindView(R.id.id_banner)
    Banner banner;
    List<BannerModel> mDatas;
    @BindView(R.id.gg_lv)
    ListView gglv;

    List<Meeting> meetings = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.DATA_CHANGE:
                    MyLVAdapter adapter1 = new MyLVAdapter();
                    gglv.setAdapter(adapter1);
                    break;
                default:break;

            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootview);
        code_iv.setOnClickListener(this);

        initPullRefreshScrollview();
        initLamp();

        MyLVAdapter adapter1 = new MyLVAdapter();
        gglv.setAdapter(adapter1);
        getMeetingData();

        gglv.setOnItemClickListener((parent, view, position, id) -> {
//            Intent intent = new Intent(getContext(),ActivityGG.class);

            Bundle bundle = new Bundle();
            bundle.putInt("position",position);
//            intent.putExtra("data",bundle);
//
//            intent.putExtra("position",position);
//            Log.e("position",position+"");
//            startActivity(intent);
            FragmentMeetingDetail fragmentMeetingDetail =new FragmentMeetingDetail();
            //把位置传过去
            fragmentMeetingDetail.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().hide(this)
                    .replace(R.id.main_content,fragmentMeetingDetail)
                    .addToBackStack(null)
                    .commit();
        });
        mDatas = new ArrayList<>();
        //初始化mdatas图像数据
        getData();
        //绑定banner适配器
        BannerAdapter adapter = new BannerAdapter<BannerModel>(mDatas) {

            @Override
            protected void bindTips(TextView tv, BannerModel bannerModel) {
                tv.setText(bannerModel.getTips());
            }

            @Override
            public void bindImage(ImageView imageView, BannerModel bannerModel) {
                Glide.with(getActivity())
                        .load(bannerModel.getImageUrl())
                        //研究中。。。
//                        .placeholder(R.mipmap.empty)
//                        .error(R.mipmap.error)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Toast.makeText(getContext(), "load..error", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }

                        })
                        .into(imageView);
            }

        };
        banner.setBannerAdapter(adapter);

        banner.setOnBannerItemClickListener(position -> {
            Intent intent = new Intent();
            switch (position) {
                default:
                    break;
                case 0:
//                        intent.setClass(getContext() , WebViewActivity.class);
//                        intent.putExtra("url","http://www.baidu.com/");
//                        startActivity(intent);
                    Toast.makeText(getContext(), "欢迎使用智宿app", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
//                        intent.setClass(getContext() , CarQuesActivity.class);
//                        startActivity(intent);
                    break;
                case 2:
//                        intent.setClass(getContext() , WebViewActivity.class);
//                        intent.putExtra("url","http://www.hjenglish.com/");
//                        startActivity(intent);
//                        intent.setClass(getContext() , AudioActivity.class);
//                        startActivity(intent);
                    break;
//                    case 3:
//                        Toast.makeText(getActivity(), "banner", Toast.LENGTH_SHORT).show();
//                        break;
            }
        });
        //实现网络加载数据更新

        //banner.notifyDataHasChanged();

        return rootview;
    }

    private void getMeetingData() {
        meetings.clear();
        //同步请求
        new Thread(()->{
            try {
                OkHttpClient client = new OkHttpClient();
                //这里也不用声明get  默认GET请求
                //获取会议列表数据
                Request request = new Request.Builder()
                        .url("http://"+ Constant.IP_LINUX +":8080/MeetingSystem/meeting/queryAllMeetings/1.do")
                        .build();

                Response response = client.newCall(request).execute();//得到Response 对象

                if (response.isSuccessful()) {//注意response.body().string()只能调用一次
                    //所以这样写：
                    String responseData = response.body().string();
                    Log.d("okhttp","response.code:"+response.code());
                    Log.d("okhttp","response.message:"+response.message());
                    Log.d("okhttp","res:"+responseData);
                    Message msg = new Message();
                    msg.what = Constant.SHOW_RSPONSE;
                    msg.obj = "response.code:"+response.code()
                            +"response.message:"+response.message()
                            +"res:"+responseData;

                    parseEasyJson(responseData);


                    handler.sendMessageDelayed(msg , 2000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    private void parseEasyJson(String json){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
            Log.e("",jsonArray+"");
            List<Meeting> lists = new ArrayList<>();
            for(int i = 0;i < jsonArray.length();i++){
                //拿到第一个json对象
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Meeting meeting = new Meeting();
                meeting.setMeetingTitle(jsonObject.getString("meetingTitle"));
                meeting.setMeetingContent(jsonObject.getString("meetingContent"));
                meeting.setCreateTime(jsonObject.getString("createTime"));
                meeting.setTime(jsonObject.getString("time"));
                meeting.setPath(jsonObject.getString("path"));
                meeting.setAddress(jsonObject.getString("address"));

                //将第二次层封装到新的JSONObject
//                JSONObject friendUser = jsonObject.getJSONObject("friendUser");
//                meeting.setName(friendUser.getString("name"));
//                meeting.setUrl(friendUser.getString("url"));
//                meeting.setPinyin(HanziToPinyin.getPinYin(friendUser.getString("name")));

                //放进list


                lists.add(meeting);

            }
            meetings.addAll(lists);
            //通知handler更新
            handler.sendEmptyMessage(Constant.DATA_CHANGE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            default:
                break;
            case R.id.code_iv:
                //OCR：
//                Toast.makeText(getContext(), "点击扫描", Toast.LENGTH_SHORT).show();
//                intent.setClass(getContext() , TransImgAct.class);
//                startActivity(intent);
                break;
        }
    }

    private void initPullRefreshScrollview() {
        welfareBarView.getBackground().mutate().setAlpha(0);
        welfareBarView.setVisibility(View.GONE);
//        Log.e("是否可见", "gone");
        //下拉刷新
        pullRefreshScrollview.getRefreshableView()
                .getViewTreeObserver()
                .addOnScrollChangedListener(() -> {
                    if ((pullRefreshScrollview.getRefreshableView().getScrollY()) > 1) {
                        welfareBarView.setVisibility(View.VISIBLE);
                    } else {
                        welfareBarView.setVisibility(View.GONE);
                    }

//                    Log.e("是否可见", "y:" + pullRefreshScrollview.getRefreshableView().getScrollY());
                    int alpha = pullRefreshScrollview.getRefreshableView().getScrollY();
                    welfareBarView.getBackground().mutate().setAlpha(alpha <= 255 ? alpha < 10 ? 0 : alpha : 255);
                });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        refreshView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        refreshView.onRefreshComplete();
    }

    class MyLVAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return meetings.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rootview;

            //ListView的优化
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                rootview = inflater.inflate(R.layout.simplenewsitem2, null);
            } else {
                rootview = view;
            }
            TextView title = rootview.findViewById(R.id.simple_title2);
            TextView content = rootview.findViewById(R.id.simple_content2);
            TextView time = rootview.findViewById(R.id.simple_time2);
            ImageView imageView = rootview.findViewById(R.id.imageview002);

            Meeting meeting = meetings.get(i);
            title.setText(meeting.getMeetingTitle());
            content.setText(meeting.getMeetingContent());
            time.setText(meeting.getCreateTime());
            imageView.setImageResource(DataC.picture[i]);


            return rootview;
        }
    }

    private void getData() {
        mDatas.clear();
        BannerModel model = null;
        model = new BannerModel();
        model.setImageUrl
                ("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1903820104,1575044093&fm=26&gp=0.jpg");
        model.setTips("大气上档次");
        mDatas.add(model);

        model = new BannerModel();
        model.setImageUrl("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2599773408,192690803&fm=26&gp=0.jpg");
        model.setTips("温馨舒适");
        mDatas.add(model);

        model = new BannerModel();
        model.setImageUrl("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3295833366,790422706&fm=26&gp=0.jpg");
        model.setTips("明亮宽敞");
        mDatas.add(model);
    }

    /**
     * 走马灯
     */
    private void initLamp() {
        //啦取服务器数据
        List<String> list = new ArrayList<>();
        list.add("关于楼道内杂物的通知");
        list.add("关于五一放假时间调整的通知");
        list.add("关于楼道内杂物的通知");
        list.add("关于五一放假时间调整的通知");

        lampView.setLampData(list);

        lampView.setOnClickListener((View v) -> {
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(R.id.main_content,new Fragmentnews());
//            transaction.addToBackStack(null).commit();
        });
    }
}
