package com.example.mercer.mobilemeeting.fragments.frame;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.pojo.Contact;
import com.example.mercer.mobilemeeting.widget.Friend.ContactAdapter;
import com.example.mercer.mobilemeeting.widget.Friend.HanziToPinyin;
import com.example.mercer.mobilemeeting.widget.Friend.SideBar;

import java.util.ArrayList;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/3/28 18:26.
 */
public class FragmentContact extends Fragment implements SideBar.OnTouchingLetterChangedListener, TextWatcher{

    private ListView mListView;
    private TextView mFooterView;
    private ImageView friend_back;
    private ArrayList<Contact> datas = new ArrayList<>();
    private ContactAdapter mAdapter;

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

    private void initData() {

    }

    public void initWidget() {
        // 给listView设置adapter
        mFooterView = (TextView) View.inflate(getActivity(),R.layout.item_list_contact_count,null);
        mListView.addFooterView(mFooterView);

        //不联网 传个null
        parser(null);
    }

    private void parser(String json) {
        datas.clear();
        //构造数据
        Contact data = new Contact();
        data.setName("张三");
        data.setUrl("http://b-ssl.duitang.com/uploads/item/201701/18/20170118163218_28szy.jpeg");
        data.setId(1);
        data.setPinyin(HanziToPinyin.getPinYin(data.getName()));
        datas.add(data);

        Contact data0 = new Contact();
        data0.setName("里斯");
        data0.setUrl("http://b-ssl.duitang.com/uploads/item/201701/18/20170118163218_28szy.jpeg");
        data0.setId(9);
        data0.setPinyin(HanziToPinyin.getPinYin(data0.getName()));
        datas.add(data0);

        Contact data1 = new Contact();
        data1.setName("老铁");
        data1.setUrl("http://img5.duitang.com/uploads/item/201512/18/20151218165511_AQW4B.jpeg");
        data1.setId(6);
        data1.setPinyin(HanziToPinyin.getPinYin(data1.getName()));
        datas.add(data1);

        Contact data2 = new Contact();
        data2.setName("王五");
        data2.setUrl("http://img0.pconline.com.cn/pconline/1509/24/6993216_03_thumb.jpg");
        data2.setId(7);
        data2.setPinyin(HanziToPinyin.getPinYin(data2.getName()));
        datas.add(data2);

        Contact data3 = new Contact();
        data3.setName("凉凉");
        data3.setUrl("http://img.52z.com/upload/news/image/20180213/20180213062641_35687.jpg");
        data3.setId(8);
        data3.setPinyin(HanziToPinyin.getPinYin(data3.getName()));
        datas.add(data3);

        mFooterView.setText(datas.size() + "位联系人");
        mAdapter = new ContactAdapter(mListView, datas,getActivity());
        mListView.setAdapter(mAdapter);

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
