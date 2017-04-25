package com.qcwy.chaowei.myapplication.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.utils.DateUtils;

import java.util.Date;

public class CustomListView extends ListView {
    View headerView;
    int height;
    //完成状态
    final static int STATE_DONE = 1;
    //下拉状态
    final static int STATE_PULL = 2;
    //松开状态
    final static int STATE_RELEASE = 3;
    //正在刷新
    final static int STATE_REFRESHING = 4;
    //当前状态
    private int currentState;

    private TextView tvState;
    private TextView tvUpdateTime;
    private ProgressBar progressBar;
    private ImageView ivArrow;
    private int downY;

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        headerView = View.inflate(context, R.layout.listview_header, null);
        // getHeight()要求控件显示出来，height=0
        // height=headerView.getHeight();

        // 控件没显示出来，得高度必须用getMeasuredHeight
        // 用getMeasuredHeight必须先调用measure（）
        // 0是一种测量方式，不指定大小，相当于match_parent
        headerView.measure(0, 0);
        // Measure 测量
        height = headerView.getMeasuredHeight();

        headerView.setPadding(0, -height, 0, 0);
        this.addHeaderView(headerView);

        // 找出控件
        tvState = (TextView) headerView.findViewById(R.id.tv_state);
        tvUpdateTime = (TextView) headerView.findViewById(R.id.tv_updateTime);
        progressBar = (ProgressBar) headerView.findViewById(R.id.progressBar);
        ivArrow = (ImageView) headerView.findViewById(R.id.iv_arrow);

        // 设置第一个状态
        currentState = STATE_DONE;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            int action = ev.getAction();
            switch (action) {
                // 按下
                case MotionEvent.ACTION_DOWN:
                    if (currentState == STATE_DONE) {
                        downY = (int) ev.getY();
                        // 切换状态
                        currentState = STATE_PULL;
                    }
                    break;
                // 移动
                case MotionEvent.ACTION_MOVE:
                    int currentY = (int) ev.getY();
                    int top = currentY - downY - height;
                    headerView.setPadding(0, top, 0, 0);

                    if (currentY - downY > 1.5 * height) {
                        currentState = STATE_RELEASE;
                        tvState.setText("松开刷新");
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(
//                                ObjectAnimator.ofFloat(ivArrow, "rotationX", 0, 360),
//                                ObjectAnimator.ofFloat(ivArrow, "rotationY", 0, 180),
                                ObjectAnimator.ofFloat(ivArrow, "rotation", 0, 180)
//                                ObjectAnimator.ofFloat(ivArrow, "translationX", 0, 90),
//                                ObjectAnimator.ofFloat(ivArrow, "translationY", 0, 90),
//                                ObjectAnimator.ofFloat(ivArrow, "scaleX", 1, 1.5f),
//                                ObjectAnimator.ofFloat(ivArrow, "scaleY", 1, 0.5f),
//                                ObjectAnimator.ofFloat(ivArrow, "alpha", 1, 0.25f, 1)
                        );
                        set.setDuration(0).start();
                    } else {
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(
//                                ObjectAnimator.ofFloat(ivArrow, "rotationX", 0, 360),
//                                ObjectAnimator.ofFloat(ivArrow, "rotationY", 0, 180),
                                ObjectAnimator.ofFloat(ivArrow, "rotation", 180, 0)
//                                ObjectAnimator.ofFloat(ivArrow, "translationX", 0, 90),
//                                ObjectAnimator.ofFloat(ivArrow, "translationY", 0, 90),
//                                ObjectAnimator.ofFloat(ivArrow, "scaleX", 1, 1.5f),
//                                ObjectAnimator.ofFloat(ivArrow, "scaleY", 1, 0.5f),
//                                ObjectAnimator.ofFloat(ivArrow, "alpha", 1, 0.25f, 1)
                        );
                        set.setDuration(0).start();
                        currentState = STATE_DONE;
                        tvState.setText("下拉刷新");
                    }
                    break;
                // 松开
                case MotionEvent.ACTION_UP:
                    if (currentState == STATE_RELEASE) {
                        tvState.setText("刷新中");
                        currentState = STATE_REFRESHING;
                        ivArrow.setVisibility(GONE);
                        progressBar.setVisibility(VISIBLE);
                        headerView.setPadding(0, 0, 0, 0);
                        // 4在框架中调用实现类
                        if (this.onRefreshListener != null) {
                            this.onRefreshListener.onRefresh(this);
                        }
                    } else {
                        headerView.setPadding(0, -height, 0, 0);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onTouchEvent(ev);
    }

    // 1定义接口
    interface OnRefreshListener {
        // 让调用者使用框架中的CustomListView对象
        void onRefresh(CustomListView customListView);
    }

    // 2 申明接口
    OnRefreshListener onRefreshListener;

    // 3 写个方法接收实现类
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    // 刷新完成之后，隐藏headerView
    public void refreshComplete() {
        currentState = STATE_DONE;
        progressBar.setVisibility(GONE);
        ivArrow.setVisibility(VISIBLE);
        tvUpdateTime.setText("上次刷新时间:" + DateUtils.format(new Date(), "HH:mm"));
        tvState.setText("刷新完成");
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                headerView.setPadding(0, -height, 0, 0);
//                tvState.setText("下拉刷新");
//            }
//        }, 300);
        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for (int i = 0; i < height; i++) {
                    try {
                        Thread.sleep(500 / height);
                        publishProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
            @Override
            protected void onProgressUpdate(Integer... values) {
                headerView.setPadding(0, -values[0], 0, 0);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                headerView.setPadding(0, -height, 0, 0);
            }
        }.execute();
    }
}
