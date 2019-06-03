package com.example.mercer.mobilemeeting.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mercer.mobilemeeting.Constant;
import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.pojo.Contact;
import com.example.mercer.mobilemeeting.widget.Friend.ContactAdapter;
import com.example.mercer.mobilemeeting.widget.Friend.HanziToPinyin;
import com.example.mercer.mobilemeeting.widget.Friend.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/28 18:26.
 */
public class FragmentJoinUser extends Fragment implements SideBar.OnTouchingLetterChangedListener, TextWatcher{

    private ListView mListView;
    private TextView mFooterView;
    private ImageView friend_back;
    private List<Contact> datas = new ArrayList<>();
    private ContactAdapter mAdapter;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.DATA_CHANGE:
                    mFooterView.setText(datas.size() + "人");
                    mAdapter = new ContactAdapter(mListView, datas,getActivity());
                    mListView.setAdapter(mAdapter);

                    break;
                default:break;

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact,container,false);
        SideBar mSideBar = (SideBar) rootView.findViewById(R.id.school_friend_sidrbar);
        TextView mDialog = (TextView) rootView.findViewById(R.id.school_friend_dialog);
        EditText mSearchInput = (EditText) rootView.findViewById(R.id.school_friend_member_search_input);
        mListView = (ListView) rootView.findViewById(R.id.school_friend_member);
        friend_back = rootView.findViewById(R.id.back);
        initWidget();
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mSearchInput.addTextChangedListener(this);

        return rootView;
    }

    public void initWidget() {
        // 给listView设置adapter
        mFooterView = (TextView) View.inflate(getActivity(),R.layout.item_list_contact_count,null);
        mListView.addFooterView(mFooterView);

        //不联网 传个null
        parser();
    }

    private void parser() {
        datas.clear();

        //服务器获取数据
        getokhttp();

        mFooterView.setText(datas.size() + "人");
        mAdapter = new ContactAdapter(mListView, datas,getActivity());
        mListView.setAdapter(mAdapter);

    }

    private void getokhttp() {
        //同步请求
        new Thread(()->{
            try {
                OkHttpClient client = new OkHttpClient();
                //这里也不用声明get  默认GET请求
                //获取当前会议参会人员
                Request request = new Request.Builder()
                        .url("http://"+ Constant.IP_LINUX +":8080/MeetingSystem/meeting/queryAllInvitation/" +
                                "1" +
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
            List<Contact> lists = new ArrayList<>();
            for(int i = 0;i < jsonArray.length();i++){
                //拿到第一个json对象
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(jsonObject.getString("userId")));

                //将第二次层封装到新的JSONObject
                JSONObject friendUser = jsonObject.getJSONObject("friendUser");
                contact.setName(friendUser.getString("name"));
                contact.setUrl(friendUser.getString("url"));
                contact.setPinyin(HanziToPinyin.getPinYin(friendUser.getString("name")));
                //放进list
                lists.add(contact);

            }
            datas.addAll(lists);
            //通知handler更新
            handler.sendEmptyMessage(Constant.DATA_CHANGE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 0;
        // 该字母首次出现的位置
        if (mAdapter != null) {
            position = mAdapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            mListView.setSelection(position);
        } else if (s.contains("#")) {
            mListView.setSelection(0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ArrayList<Contact> temp = new ArrayList<>(datas);
        for (Contact data : datas) {
            if (data.getName().contains(s) || data.getPinyin().contains(s)) {
            } else {
                temp.remove(data);
            }
        }
        if (mAdapter != null) {
            mAdapter.refresh(temp);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}
