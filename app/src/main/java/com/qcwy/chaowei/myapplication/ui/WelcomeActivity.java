package com.qcwy.chaowei.myapplication.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.JsonResult;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.MyLog;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import okhttp3.Call;
import okhttp3.Response;

@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String username;
    private String pwd;
    private InnerReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        init();
        register();
//        if (MyApplication.getLongitude() != 0 && MyApplication.getLatitude() != 0) {
//            unregisterReceiver(receiver);
//            loaded();
//        }
    }

    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalContants.LOCATION_COMPELETE);
        registerReceiver(receiver, filter);
    }

    private void init() {
        receiver = new InnerReceiver();
        preferences = getSharedPreferences("qcwy", Context.MODE_PRIVATE);
        editor = preferences.edit();
        username = preferences.getString("username", null);
        pwd = preferences.getString("pwd", null);
    }

    private void login(final String jobNo, final String pwd) {
        OkGo.post(Urls.LOGIN)     // 请求方式和请求url
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.LOGIN)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("jobNo", jobNo)
                .params("pwd", pwd)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        JsonResult jsonResult = gson.fromJson(s, JsonResult.class);
                        if (jsonResult.getState() == 0) {
                            //登录成功
                            editor.putString("username", jobNo);
                            editor.putString("pwd", pwd);
                            editor.commit();
                            MyApplication.jobNo = jobNo;
                            //设置Jpush别名
                            MyApplication.getApp().setAlias();
                        } else {
                            MyToast.show(getApplicationContext(), jsonResult.getMessage());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        if (response == null) {
                            MyToast.show(getApplicationContext(), "网络连接失败!");
                            login(username, pwd);
                        } else {
                            MyToast.show(getApplicationContext(), e);
                        }
                    }
                });
    }

    class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (GlobalContants.LOCATION_COMPELETE.equals(intent.getAction())) {
                loaded();
            }
        }
    }

    private void loaded() {
        Intent intent = new Intent();
        if (username != null && pwd != null) {
            login(username, pwd);
            intent.setClass(getApplicationContext(), MainActivity.class);
            MyLog.d("启动主界面");
        } else {
            intent.setClass(getApplicationContext(), LoginActivity.class);
            MyLog.d("启动登录界面");
        }
        startActivity(intent);
        WelcomeActivity.this.finish();
    }
}