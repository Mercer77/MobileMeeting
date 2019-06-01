package com.example.mercer.mobilemeeting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mercer.mobilemeeting.fragments.frame.FragmentContact;
import com.example.mercer.mobilemeeting.fragments.frame.FragmentHome;
import com.example.mercer.mobilemeeting.fragments.frame.FragmentMessage;
import com.example.mercer.mobilemeeting.fragments.frame.FragmentMine;
import com.example.mercer.mobilemeeting.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    /*底部导航栏*/
    private FragmentHome fragmentHome;
    private FragmentMessage fragmentMessage;
    private FragmentContact fragmentContact;
    private FragmentMine fragmentMine;
    private List<Fragment> fragmentList = null;
    private FragmentPagerAdapter fragmentPagerAdapter;

    @BindView(R.id.viewpager_main) ViewPager viewPager;

    @BindView(R.id.iv_home_bottom) ImageView home;
    @BindView(R.id.iv_contact_bottom) ImageView contact;
    @BindView(R.id.iv_message_bottom) ImageView message;
    @BindView(R.id.iv_mine_bottom) ImageView mine;

    @BindView(R.id.ll_home_bottom) LinearLayout llhome;
    @BindView(R.id.ll_message_bottom) LinearLayout llmessage;
    @BindView(R.id.ll_contact_bottom) LinearLayout llcontact;
    @BindView(R.id.ll_mine_bottom) LinearLayout llmine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUser();

        init();
    }

    private void initUser() {
//        SharedPreferencesUtils.setUserName("userId",String.valueOf(0));
//        SharedPreferencesUtils.setUserName("username",String.valueOf("oppo"));
    }

    private void init() {
        initFragment();
    }

    /*下面是切换逻辑*/
    private void initFragment() {
        fragmentHome = new FragmentHome();
        fragmentMessage = new FragmentMessage();
        fragmentContact = new FragmentContact();
        fragmentMine = new FragmentMine();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(fragmentHome);
        fragmentList.add(fragmentMessage);
        fragmentList.add(fragmentContact);
        fragmentList.add(fragmentMine);

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentList.get(i);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                selectFragment(viewPager.getCurrentItem());
            }

            @Override
            public void onPageSelected(int i) {}
            @Override
            public void onPageScrollStateChanged(int i) {}
        });
    }

    @OnClick({R.id.ll_home_bottom,R.id.ll_message_bottom,R.id.ll_contact_bottom,R.id.ll_mine_bottom})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_home_bottom:viewPager.setCurrentItem(0);break;
            case R.id.ll_message_bottom:viewPager.setCurrentItem(1);break;
            case R.id.ll_contact_bottom:viewPager.setCurrentItem(2);break;
            case R.id.ll_mine_bottom:viewPager.setCurrentItem(3);break;
            default:break;
        }
    }

    private void selectFragment(int position) {
        initSelectNone();
        switch (position){
            case 0:home.setImageResource(R.mipmap.home_press);break;
            case 1:message.setImageResource(R.mipmap.message_press);break;
            case 2:contact.setImageResource(R.mipmap.contact_press);break;
            case 3:mine.setImageResource(R.mipmap.mine_press);break;
            default:break;
        }
    }
    private void initSelectNone() {
        home.setImageResource(R.mipmap.home);
        message.setImageResource(R.mipmap.message);
        contact.setImageResource(R.mipmap.contact);
        mine.setImageResource(R.mipmap.mine);
    }
}
