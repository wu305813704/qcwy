package com.qcwy.chaowei.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.utils.MyToast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_score)
public class ScoreActivity extends BaseActivity {
    @ViewInject(R.id.cpv_score)
    private CircleProgressView cpvScore;
    @ViewInject(R.id.btn_score_detail)
    private Button btnScoreDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("积分");
        //当查询到用户积分后在回调中使用此方法
        //TODO
        setProgressAnimation(651, 1000);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setListeners() {
        btnScoreDetail.setOnClickListener(this);
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
            case R.id.btn_score_detail:
                //跳转到积分明细
                MyToast.show(this, "积分明细");
                break;
        }
    }

    /**
     * 设置积分动画效果
     * @param currentScore 当前积分
     * @param maxScore     总积分
     */
    public void setProgressAnimation(final int currentScore, int maxScore) {
        cpvScore.setmTxtHint1("目前总积分");
//        cpvScore.setmTxtHint2("查看积分明细>");
        cpvScore.setMaxProgress(maxScore);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= currentScore; i++) {
                    try {
                        cpvScore.setProgressNotInUiThread(i);
                        Thread.currentThread().sleep((long) (2000f / currentScore));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
