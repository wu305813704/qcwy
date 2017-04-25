package com.qcwy.chaowei.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.utils.MyToast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_personal_center)
public class PersonalCenterActivity extends BaseActivity {
    @ViewInject(R.id.ll_personal_msg)
    private LinearLayout llPersonalMsg;
    @ViewInject(R.id.ll_score_statistics)
    private LinearLayout llScoreStatistics;
    @ViewInject(R.id.ll_disorder_record)
    private LinearLayout llDisorderRecord;
    @ViewInject(R.id.ll_train_course)
    private LinearLayout llTrain_course;
    @ViewInject(R.id.ll_setting)
    private LinearLayout llSetting;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("个人中心");
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setListeners() {
        llPersonalMsg.setOnClickListener(this);
        llScoreStatistics.setOnClickListener(this);
        llDisorderRecord.setOnClickListener(this);
        llTrain_course.setOnClickListener(this);
        llSetting.setOnClickListener(this);
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
            case R.id.ll_personal_msg:
                //跳转到个人信息界面
                startActivity(new Intent(this,PersonalDetailActivity.class));
                break;
            case R.id.ll_score_statistics:
                //跳转到积分统计界面
                startActivity(new Intent(this,ScoreActivity.class));
                break;
            case R.id.ll_disorder_record:
                //跳转到违规记录界面
                startActivity(new Intent(this,IllegalRecordActivity.class));
                break;
            case R.id.ll_train_course:
                //跳转到培训教程界面
                MyToast.show(getApplicationContext(), "培训教程");
                break;
            case R.id.ll_setting:
                //跳转到设置界面
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}
