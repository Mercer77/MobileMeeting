package com.example.mercer.mobilemeeting.fragments.frame;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mercer.mobilemeeting.R;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/28 18:26.
 */
public class FragmentHome extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,null);

        return rootView;
    }
}
