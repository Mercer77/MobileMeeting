package com.example.mercer.mobilemeeting.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mercer.mobilemeeting.R;

import scut.carson_ho.searchview.SearchView;


/**
 * Created by Carson_Ho on 17/8/11.
 */

public class SearchDemoActivity extends AppCompatActivity {

    // 1. 初始化搜索框变量
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 2. 绑定视图
        setContentView(R.layout.activity_search);
        // 3. 绑定组件
        searchView = (SearchView) findViewById(R.id.search_view);
        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(string -> System.out.println("我收到了" + string));
        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(() -> finish());

    }
}