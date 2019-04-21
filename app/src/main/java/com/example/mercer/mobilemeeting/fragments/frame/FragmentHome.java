package com.example.mercer.mobilemeeting.fragments.frame;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.fragments.FragmentMeetingDetail;
import com.example.mercer.mobilemeeting.pojo.BannerModel;
import com.example.mercer.mobilemeeting.utils.DataC;
import com.example.mercer.mobilemeeting.widget.Lamp.LampView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sivin.Banner;
import com.sivin.BannerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    //    @BindView(R.id.activity)
//    TextView activity;
//    @BindView(R.id.shop)
//    TextView shop;
//    @BindView(R.id.fastsend)
//    TextView fast;
//    @BindView(R.id.news)
//    TextView news;
    private int mYear, mMonth, mDay, mWay, mHour, mMinute;
    @BindView(R.id.gg_lv)
    ListView gglv;

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
        mDatas = new ArrayList<BannerModel>();
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

        banner.setOnBannerItemClickListener(new Banner.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
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
            }
        });
        //实现网络加载数据更新

        //banner.notifyDataHasChanged();

        return rootview;
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
        Log.e("是否可见", "gone");
        //下拉刷新
        pullRefreshScrollview.getRefreshableView().
                getViewTreeObserver().
                addOnScrollChangedListener(() -> {
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

    public void time() {
        Calendar c = Calendar.getInstance();//
        mYear = c.get(Calendar.YEAR); // 获取当前年份
        mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        mWay = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
        mHour = c.get(Calendar.HOUR_OF_DAY);//时
        mMinute = c.get(Calendar.MINUTE);//分
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
            return 6;
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
            Log.e("________", "&*********" + i);
            title.setText(DataC.title1[i]);
            content.setText(DataC.content1[i]);
            time.setText(DataC.date[i]);
            imageView.setImageResource(DataC.picture[i]);
//            imageView.setImageResource(R.drawable.news1);


            return rootview;
        }
    }

    private void getData() {
        mDatas.clear();
        BannerModel model = null;
        model = new BannerModel();
        //天猫
        model.setImageUrl("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1620743413,3027851616&fm=26&gp=0.jpg");
        model.setTips("1号会议室");
        mDatas.add(model);

        //驾考
        model = new BannerModel();
        model.setImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1553508929783&di=df17ba9a0bab01fcca07f579cfd22530&imgtype=0&src=http%3A%2F%2Fimg105.job1001.com%2Fupload%2Falbum%2F2014-08-22%2F1408686171-7QHFFF7_960_600.jpg");
        model.setTips("2号会议室");
        mDatas.add(model);

        //学习英语
        model = new BannerModel();
        model.setImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554103693&di=5a1365b3759f5ad1163e7efd243e3427&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.yw2005.com%2Fbaike%2Fuploads%2Fallimg%2F160618%2F1-16061Q52032434.jpg");
        model.setTips("3号会议室");
        mDatas.add(model);
    }

    private void initLamp() {
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
