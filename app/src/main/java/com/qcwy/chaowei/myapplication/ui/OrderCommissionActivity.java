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
import com.qcwy.chaowei.myapplication.fragment.FragmentOrderCount;
import com.qcwy.chaowei.myapplication.fragment.FragmentPartCommission;
import com.qcwy.chaowei.myapplication.fragment.FragmentRank;
import com.qcwy.chaowei.myapplication.fragment.FragmentVisit;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_order_commission)
public class OrderCommissionActivity extends BaseActivity {
    @ViewInject(R.id.tab_commission)
    private TabLayout tabLayout;
    @ViewInject(R.id.vp_commission_pager)
    private ViewPager vpCommission;
    private Fragment fragmentPart;
    private Fragment fragmentVisit;
    private Fragment fragmentRank;
    private Fragment fragmentOrder;

    private List<Fragment> fragments;
    private List<String> titles;
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("订单提成");
    }

    @Override
    protected void init() {
        //初始化fragment
        fragmentPart = new FragmentPartCommission();
        fragmentVisit = new FragmentVisit();
        fragmentRank = new FragmentRank();
        fragmentOrder = new FragmentOrderCount();
        //初始化list
        fragments = new ArrayList<>();
        fragments.add(fragmentPart);
        fragments.add(fragmentVisit);
        fragments.add(fragmentRank);
        fragments.add(fragmentOrder);
        titles = new ArrayList<>();
        titles.add("零件提成");
        titles.add("上门费");
        titles.add("排名奖励");
        titles.add("订单数量");
        //设置tabLayout模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(3)));
        fragmentPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        vpCommission.setAdapter(fragmentPagerAdapter);
        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(vpCommission);
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
