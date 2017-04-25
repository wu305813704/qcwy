package com.qcwy.chaowei.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.JsonResult;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.StringUtils;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import okhttp3.Call;
import okhttp3.Response;

@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(R.id.et_username)
    private EditText etUsername;
    @ViewInject(R.id.et_password)
    private EditText etPassword;
    @ViewInject(R.id.btn_login)
    private Button btnLogin;
    @ViewInject(R.id.tv_forget_password)
    private TextView tvForgetPassword;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
        setListeners();
    }

    private void init() {
        preferences = getSharedPreferences("qcwy", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                submit();
                break;
            case R.id.tv_forget_password:
                MyToast.show(this, "忘记密码");
                break;
        }
    }

    private void setListeners() {
        btnLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
    }

    private void submit() {
        boolean isBlank = false;
        String jobNo = etUsername.getText().toString();
        String pwd = etPassword.getText().toString();
        if (StringUtils.isBlank(jobNo)) {
            etUsername.setError("用户名不能为空");
            isBlank = true;
        }
        if (StringUtils.isBlank(pwd)) {
            etPassword.setError("密码不能为空");
            isBlank = true;
        }
        if (!isBlank) {
            login(jobNo, pwd);
        }
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
                    public void onBefore(BaseRequest request) {
                        //禁止按钮点击
                        btnLogin.setEnabled(false);
                        btnLogin.setText("登陆中,请稍候");
                        etUsername.setEnabled(false);
                        etPassword.setEnabled(false);
                    }

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
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            MyToast.show(getApplicationContext(), jsonResult.getMessage());
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
                        //禁止按钮点击
                        btnLogin.setEnabled(true);
                        btnLogin.setText("登录");
                        etUsername.setEnabled(true);
                        etPassword.setEnabled(true);
                    }
                });
    }
}
