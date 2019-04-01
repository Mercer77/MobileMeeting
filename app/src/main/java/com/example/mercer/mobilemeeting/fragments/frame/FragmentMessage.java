package com.example.mercer.mobilemeeting.fragments.frame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.fragments.FragmentAddFriend;
import com.example.mercer.mobilemeeting.widget.PopWindow.CustomPopWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/28 18:26.
 */
public class FragmentMessage extends Fragment{

    @BindView(R.id.mult)
    ImageView mult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this,rootView);

        return rootView;
    }

    @OnClick(R.id.mult)
    public void onClick(View v){
        View v1 = LayoutInflater.from(getActivity()).inflate(R.layout.pop_layout1,null);

        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(v1)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.8f)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .create();
        popWindow.showAsDropDown(mult,-30,20);

        LinearLayout addfriend = v1.findViewById(R.id.tv_addfriend);
        LinearLayout saoyisao = v1.findViewById(R.id.tv_saoyisao);
        View.OnClickListener listener = v2 -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (v2.getId()) {
                case R.id.tv_addfriend:
                    Log.e("sd","addfriend");
//                    fragmentTransaction.replace(R.id.main_content, new FragmentAddFriend())
//                            .hide(FragmentMS2.this)
//                            .addToBackStack(null)
//                            .commit();
                    break;
                case R.id.tv_saoyisao:
//                    Intent intent = new Intent();
//                    intent.setClass(getContext() , TransImgAct.class);
//                    startActivity(intent);
                    break;
                case R.id.tv_fastSend_pop:
//                    Intent intent2 = new Intent();
//                    intent2.setClass(getContext() , FastActivity.class);
//                    startActivity(intent2);
                    break;
            }
            popWindow.dissmiss();
        };
        addfriend.setOnClickListener(listener);
        saoyisao.setOnClickListener(listener);
    }
}
