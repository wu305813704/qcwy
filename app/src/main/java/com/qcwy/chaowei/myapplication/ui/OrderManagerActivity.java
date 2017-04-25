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
import com.qcwy.chaowei.myapplication.fragment.FragmentCurrentOrder;
import com.qcwy.chaowei.myapplication.fragment.FragmentHistoryOrder;
import com.qcwy.chaowei.myapplication.fragment.FragmentRashOrder;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_order_manager)
public class OrderManagerActivity extends BaseActivity {
    @ViewInject(R.id.tab_order_title)
    private TabLayout tabLayout;
    @ViewInject(R.id.vp_order_pager)
    private ViewPager vpOrderManager;
    private FragmentRashOrder fragmentRashOrder;
    private FragmentCurrentOrder fragmentCurrentOrder;
    private FragmentHistoryOrder fragmentHistoryOrder;
    private List<Fragment> fragments;
    private List<String> titles;
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("订单管理");
        hideTips();
        Intent intent = getIntent();
        String msg = intent.getStringExtra(GlobalContants.INTENT_SKIP);
        if (GlobalContants.INTENT_RUSH_ORDER.equals(msg)) {
            vpOrderManager.setCurrentItem(0);
        } else if (GlobalContants.INTENT_CURRENT_ORDER.equals(msg)) {
            vpOrderManager.setCurrentItem(1);
        }else if(GlobalContants.INTENT_HISTORY_ORDER.equals(msg)){
            vpOrderManager.setCurrentItem(2);
        }
    }

    @Override
    protected void init() {
        //初始化fragment
        fragmentRashOrder = new FragmentRashOrder();
        fragmentCurrentOrder = new FragmentCurrentOrder();
        fragmentHistoryOrder = new FragmentHistoryOrder();
        //初始化list
        fragments = new ArrayList<>();
        fragments.add(fragmentRashOrder);
        fragments.add(fragmentCurrentOrder);
        fragments.add(fragmentHistoryOrder);
        titles = new ArrayList<>();
        titles.add("待抢订单");
        titles.add("当前订单");
        titles.add("历史订单");
        //设置tabLayout模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));
        fragmentPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        vpOrderManager.setAdapter(fragmentPagerAdapter);
        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(vpOrderManager);
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
            case R.id.iv_msg:
                startActivity(new Intent(this, MessageActivity.class));
        }
    }
}
