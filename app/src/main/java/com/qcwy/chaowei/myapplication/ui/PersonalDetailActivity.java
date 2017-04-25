package com.qcwy.chaowei.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.qcwy.chaowei.myapplication.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_personal_detail)
public class PersonalDetailActivity extends BaseActivity {
    @ViewInject(R.id.btn_modify_icon)
    private Button btnModifyIcon;
    //修改头像布局
    @ViewInject(R.id.ll_options)
    private LinearLayout llOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("个人信息");
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setListeners() {
        btnModifyIcon.setOnClickListener(this);
        llOptions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hiddenOptions(500);
                return true;
            }
        });
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
            case R.id.btn_modify_icon:
                //修改头像
                showOptions(500);
                break;
        }
    }

    //隐藏布局
    private void hiddenOptions(long duration) {
        AnimatorSet hidden = new AnimatorSet();
        hidden.playTogether(
                ObjectAnimator.ofFloat(llOptions, "alpha", 1, 0)
        );
        hidden.setDuration(duration).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                llOptions.setVisibility(View.GONE);
            }
        }, duration);
    }

    //显示布局布局
    private void showOptions(long duration) {
        AnimatorSet hidden = new AnimatorSet();
        hidden.playTogether(
                ObjectAnimator.ofFloat(llOptions, "alpha", 0, 1)
        );
        llOptions.setVisibility(View.VISIBLE);
        hidden.setDuration(duration).start();
    }
}
