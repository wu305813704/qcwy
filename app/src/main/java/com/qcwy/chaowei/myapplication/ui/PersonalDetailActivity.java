package com.qcwy.chaowei.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.AppUser;
import com.qcwy.chaowei.myapplication.entity.response.ResponseAppUser;
import com.qcwy.chaowei.myapplication.utils.DateUtils;
import com.qcwy.chaowei.myapplication.utils.GsonUtils;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

@ContentView(R.layout.activity_personal_detail)
public class PersonalDetailActivity extends BaseActivity {
    @ViewInject(R.id.btn_modify_icon)
    private Button btnModifyIcon;
    @ViewInject(R.id.tv_name)
    private TextView tvName;
    @ViewInject(R.id.tv_idcard)
    private TextView tvIdcard;
    @ViewInject(R.id.tv_job_no)
    private TextView tvJobNo;
    @ViewInject(R.id.tv_time)
    private TextView tvTime;

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
        OkGo.get(Urls.GET_USER)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_USER)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("jobNo", MyApplication.jobNo)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ResponseAppUser result = GsonUtils.getInstance().fromJson(s, ResponseAppUser.class);
                        if (result.getState() == 0) {
                            AppUser appUser = result.getData();
                            tvName.setText(appUser.getName());
                            tvIdcard.setText(appUser.getId_card());
                            tvJobNo.setText(appUser.getJob_no());
                            tvTime.setText(DateUtils.format(new Date(appUser.getRegist_time()), "yyyy年MM月dd日"));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        if (response == null) {
                            MyToast.show(getApplicationContext(), "网络连接失败!");
                        } else {
                            MyToast.show(getApplicationContext(), e);
                        }
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                    }
                });
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
