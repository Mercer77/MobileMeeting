package com.example.mercer.mobilemeeting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.mercer.mobilemeeting.fragments.frame.FragmentContact;
import com.example.mercer.mobilemeeting.fragments.frame.FragmentHome;
import com.example.mercer.mobilemeeting.fragments.frame.FragmentMessage;
import com.example.mercer.mobilemeeting.fragments.frame.FragmentMine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    /*底部导航栏*/
    private FragmentHome fragmentHome;
    private FragmentContact fragmentContact;
    private FragmentMessage fragmentMessage;
    private FragmentMine fragmentMine;
    private List<Fragment> fragmentList = null;
    private FragmentPagerAdapter fragmentPagerAdapter;

    @BindView(R.id.viewpager_main)
    ViewPager viewPager;
    @BindView(R.id.iv_home_bottom)
    ImageView home;
    @BindView(R.id.iv_contact_bottom)
    ImageView contact;
    @BindView(R.id.iv_message_bottom)
    ImageView message;
    @BindView(R.id.iv_mine_bottom)
    ImageView mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        initFragment();
    }



    /*下面是切换逻辑*/
    private void initFragment() {
        fragmentHome = new FragmentHome();
        fragmentContact = new FragmentContact();
        fragmentMessage = new FragmentMessage();
        fragmentMine = new FragmentMine();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(fragmentHome);
        fragmentList.add(fragmentContact);
        fragmentList.add(fragmentMessage);
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
                selectFragment(i);
            }

            @Override
            public void onPageSelected(int i) {}
            @Override
            public void onPageScrollStateChanged(int i) {}
        });
    }
    private void selectFragment(int position) {
        initSelectNone();
        switch (position){
            case 0:home.setImageResource(R.mipmap.home_press);break;
            case 1:contact.setImageResource(R.mipmap.contact_press);break;
            case 2:message.setImageResource(R.mipmap.message_press);break;
            case 3:mine.setImageResource(R.mipmap.mine_press);break;
            default:break;
        }
    }
    private void initSelectNone() {
        home.setImageResource(R.mipmap.home);
        contact.setImageResource(R.mipmap.contact);
        message.setImageResource(R.mipmap.message);
        mine.setImageResource(R.mipmap.mine);
    }
}
