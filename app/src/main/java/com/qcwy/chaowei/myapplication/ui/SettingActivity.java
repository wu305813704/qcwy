package com.qcwy.chaowei.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.utils.MyToast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity{
    @ViewInject(R.id.rl_about)
    private RelativeLayout rlAbout;
    @ViewInject(R.id.rl_connect_us)
    private RelativeLayout rlConnectUs;
    @ViewInject(R.id.rl_logout)
    private RelativeLayout rlLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置");
    }

    @Override
    protected void init() {

    }
    @Override
    protected void setListeners() {
        rlAbout.setOnClickListener(this);
        rlConnectUs.setOnClickListener(this);
        rlLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.iv_msg:
                //跳转到Message界面
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.rl_about:
                //关于骑程无忧
                MyToast.show(this,"关于骑程无忧");
                break;
            case R.id.rl_connect_us:
                //联系客服
                MyToast.show(this,"联系客服");
                break;
            case R.id.rl_logout:
                //注销
                MyToast.show(this,"注销");
                break;
        }
    }
}
