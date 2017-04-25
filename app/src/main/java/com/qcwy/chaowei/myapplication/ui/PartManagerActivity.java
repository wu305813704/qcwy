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
import com.qcwy.chaowei.myapplication.fragment.FragmentInvenrotyManager;
import com.qcwy.chaowei.myapplication.fragment.FragmentPartPrice;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_part_manager)
public class PartManagerActivity extends BaseActivity {
    @ViewInject(R.id.tab_part_manager)
    private TabLayout tabLayout;
    @ViewInject(R.id.vp_part_manager)
    private ViewPager vpPartManager;
    private Fragment fragmentPartPrice;
    private Fragment fragmentInventoryManager;

    private List<Fragment> fragments;
    private List<String> titles;
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("零件管理");
    }

    @Override
    protected void init() {
        fragmentPartPrice = new FragmentPartPrice();
        fragmentInventoryManager = new FragmentInvenrotyManager();
        //初始化fragments
        fragments = new ArrayList<>();
        fragments.add(fragmentPartPrice);
        fragments.add(fragmentInventoryManager);
        //初始化titles
        titles = new ArrayList<>();
        titles.add("零件价目");
        titles.add("库存管理");
        //设置TabLayout模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        fragmentPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        vpPartManager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(vpPartManager);
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
