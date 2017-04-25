package com.qcwy.chaowei.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.MyFragmentAdapter;
import com.qcwy.chaowei.myapplication.fragment.FragmentOrderMessage;
import com.qcwy.chaowei.myapplication.fragment.FragmentSystemMessage;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_message)
public class MessageActivity extends BaseActivity {
    @ViewInject(R.id.tab_message_title)
    private TabLayout tabLayout;
    @ViewInject(R.id.vp_message_pager)
    private ViewPager vpMessage;
    private FragmentSystemMessage fragmentSystemMessage;
    private FragmentOrderMessage fragmentOrderMessage;
    private List<Fragment> fragments;
    private List<String> titles;
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("消息管理");
        getIvMsg().setVisibility(View.GONE);
        hideTips();
        Intent intent = getIntent();
        String msg = intent.getStringExtra(GlobalContants.INTENT_SKIP);
        if (GlobalContants.INTENT_SYSTEM_MESSAGE.equals(msg)) {
            vpMessage.setCurrentItem(0);
        } else if (GlobalContants.INTENT_ORDER_MESSAGE.equals(msg)) {
            vpMessage.setCurrentItem(1);
        }
    }

    @Override
    protected void init() {
        //初始化fragment
        fragmentSystemMessage = new FragmentSystemMessage();
        fragmentOrderMessage = new FragmentOrderMessage();
        //初始化list
        fragments = new ArrayList<>();
        fragments.add(fragmentSystemMessage);
        fragments.add(fragmentOrderMessage);
        titles = new ArrayList<>();
        titles.add("系统消息");
        titles.add("订单消息");
        //设置tabLayout模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        fragmentPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        vpMessage.setAdapter(fragmentPagerAdapter);
        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(vpMessage);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
