package com.qcwy.chaowei.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.utils.MyToast;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_illegal_detail)
public class IllegalDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("违规记录详情");
    }

    @Override
    protected void init() {
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
                //跳转到Message界面
                startActivity(new Intent(this, MessageActivity.class));
                break;

        }
    }
}
