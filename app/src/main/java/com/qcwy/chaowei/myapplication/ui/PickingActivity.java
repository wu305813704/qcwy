package com.qcwy.chaowei.myapplication.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.MyFragmentAdapter;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.Part;
import com.qcwy.chaowei.myapplication.fragment.FragmentPicking0;
import com.qcwy.chaowei.myapplication.fragment.FragmentPicking1;
import com.qcwy.chaowei.myapplication.fragment.FragmentPicking2;
import com.qcwy.chaowei.myapplication.fragment.FragmentPicking3;
import com.qcwy.chaowei.myapplication.fragment.FragmentPicking4;
import com.qcwy.chaowei.myapplication.fragment.FragmentPicking5;
import com.qcwy.chaowei.myapplication.fragment.FragmentPicking6;
import com.qcwy.chaowei.myapplication.utils.MyToast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_picking)
public class PickingActivity extends BaseActivity {
    @ViewInject(R.id.tab_picking)
    private TabLayout tabLayout;
    @ViewInject(R.id.vp_picking)
    private ViewPager vpPartManager;
    @ViewInject(R.id.btn_submit)
    private Button btnSubmit;
    private Fragment fragment0;
    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    private Fragment fragment4;
    private Fragment fragment5;
    private Fragment fragment6;

    private List<Fragment> fragments;
    private List<String> titles;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private List<Part> parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("领料");
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    @Override
    protected void init() {
        fragment0 = new FragmentPicking0();
        fragment1 = new FragmentPicking1();
        fragment2 = new FragmentPicking2();
        fragment3 = new FragmentPicking3();
        fragment4 = new FragmentPicking4();
        fragment5 = new FragmentPicking5();
        fragment6 = new FragmentPicking6();
        //初始化fragments
        fragments = new ArrayList<>();
        fragments.add(fragment0);
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        fragments.add(fragment5);
        fragments.add(fragment6);
        //初始化titles
        titles = new ArrayList<>();
        titles.add("四大部件");
        titles.add("制动系统");
        titles.add("结构部件");
        titles.add("电器配件");
        titles.add("通用配件");
        titles.add("三轮车配件");
        titles.add("水电瓶车配件");
        //设置TabLayout模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(3)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(4)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(5)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(6)));
        fragmentPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        vpPartManager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(vpPartManager);
    }

    @Override
    protected void setListeners() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.iv_msg:
                //跳转到Message界面
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.btn_submit:
                if (MyApplication.getApp().getCart().getCartItems().isEmpty()) {
                    MyToast.show(this, "您还未添加任何零件");
                    return;
                }
                new AlertDialog.Builder(this)
                        .setTitle("确认已选择的零件")
                        .setMessage(MyApplication.getApp().getCart().toString())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO

                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        //清空购物车，防止下次进来还有数据
        MyApplication.getApp().getCart().getCartItems().clear();
        super.onDestroy();
    }
}
