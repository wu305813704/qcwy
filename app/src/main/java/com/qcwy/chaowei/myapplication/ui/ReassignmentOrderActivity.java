package com.qcwy.chaowei.myapplication.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.JsonResult;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.StringUtils;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import okhttp3.Call;
import okhttp3.Response;

@ContentView(R.layout.activity_reassignment_order)
public class ReassignmentOrderActivity extends BaseActivity {
    @ViewInject(R.id.ll_content)
    private LinearLayout llContent;
    @ViewInject(R.id.rg_type)
    private RadioGroup rgType;
    @ViewInject(R.id.et_number)
    private EditText etNumber;
    @ViewInject(R.id.tv_name)
    private TextView tvName;
    @ViewInject(R.id.ll_engineer)
    private LinearLayout llEngineer;
    @ViewInject(R.id.rg_cause)
    private RadioGroup rgCause;
    @ViewInject(R.id.et_other)
    private EditText etOther;
    @ViewInject(R.id.btn_submit)
    private Button btnSubmit;

    //改派类型(指定工程师true/后台false)
    private boolean reassignmentType;

    //工号是否合法
    private boolean isLegal = false;
    //当前订单
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("改派订单");
    }

    @Override
    protected void init() {
        //整个布局获得焦点
        getFocus();
        //默认选择改派到工程师
        rgType.check(R.id.rb_engineer);
        rgCause.check(R.id.rb_3);
        order = MyApplication.getApp().getOrder();
        reassignmentType = true;
    }

    @Override
    protected void setListeners() {
        llContent.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                getFocus();
                return false;
            }
        });
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_engineer:
                        reassignmentType = true;
                        llEngineer.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_background:
                        reassignmentType = false;
                        llEngineer.setVisibility(View.GONE);
                        break;
                }
            }
        });
        rgCause.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_0:
                    case R.id.rb_1:
                    case R.id.rb_2:
                        etOther.setVisibility(View.GONE);
                        break;
                    case R.id.rb_3:
                        etOther.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        etNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideInputMethod();
                    if (StringUtils.isBlank(etNumber.getText().toString())) {
                        etNumber.setError("请输入工号");
                        tvName.setText("点击空白处验证工号");
                    } else {
                        if (llEngineer.getVisibility() == View.VISIBLE) {
                            //失去焦点，且可见状态，请求网络查询工程师姓名
                            OkGo.get(Urls.GET_ENGINEER_NAME)
                                    .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                                    .cacheKey(Urls.GET_ENGINEER_NAME)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                                    .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                                    .params("jobNo", etNumber.getText().toString())
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            Gson gson = new Gson();
                                            JsonResult jsonResult = gson.fromJson(s, JsonResult.class);
                                            if (jsonResult.getState() == 0) {
                                                tvName.setText((String) jsonResult.getData());
                                                isLegal = true;
                                            } else {
                                                tvName.setText("点击空白处验证工号");
                                                isLegal = false;
                                                MyToast.show(getApplicationContext(), jsonResult.getMessage());
                                            }
                                        }

                                        @Override
                                        public void onError(Call call, Response response, Exception e) {
                                            isLegal = false;
                                            if (response == null) {
                                                MyToast.show(getApplicationContext(), "网络连接失败!");
                                            } else {
                                                MyToast.show(getApplicationContext(), e);
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });
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
                String name = tvName.getText().toString();
                if (reassignmentType) {
                    if (!isLegal) {
                        MyToast.show(this, "工号不存在，请重新填写");
                        return;
                    }
                } else {
                    name = "后台";
                }

                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定改派订单给" + name + "?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                submit();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                break;
        }
    }

    private void submit() {
        RadioButton rbCause = (RadioButton) findViewById(rgCause.getCheckedRadioButtonId());
        String cause = rbCause.getText().toString();
        if ("其他".equals(cause)) {
            if (StringUtils.isEmpty(etOther.getText().toString())) {
                MyToast.show(getApplicationContext(), "请填写改派原因");
                return;
            }
            cause = etOther.getText().toString();
        }
        //指定工程师
        if (reassignmentType) {
            String jobNo = etNumber.getText().toString();
            if (jobNo.equals(MyApplication.jobNo)) {
                MyToast.show(getApplicationContext(), "请不要改派给自己");
                return;
            }
            OkGo.get(Urls.REASSIGNMENT_TO_ENGINEER)
                    .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                    .cacheKey(Urls.REASSIGNMENT_TO_ENGINEER)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                    .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                    .params("orderNo", order.getOrder_no())
                    .params("oldNo", MyApplication.jobNo)
                    .params("newNo", jobNo)
                    .params("cause", cause)
                    .execute(new StringCallback() {
                        @Override
                        public void onBefore(BaseRequest request) {
                            btnSubmit.setEnabled(false);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Gson gson = new Gson();
                            JsonResult jsonResult = gson.fromJson(s, JsonResult.class);
                            if (jsonResult.getState() == 0) {
                                MyToast.show(getApplicationContext(), "改派成功，等待对方确认");
                                order.setState(6);
                                startActivity(new Intent(getApplicationContext(), OrderManagerActivity.class));
                                //发送广播更新当前订单
                                Intent intent = new Intent();
                                intent.setAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
                                sendBroadcast(intent);
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
                            btnSubmit.setEnabled(true);
                        }
                    });
        } else {
            //改派给后台
            OkGo.get(Urls.REASSIGNMENT_TO_BACKGROUND)
                    .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                    .cacheKey(Urls.REASSIGNMENT_TO_BACKGROUND)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                    .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                    .params("orderNo", order.getOrder_no())
                    .params("jobNo", MyApplication.jobNo)
                    .params("cause", cause)
                    .execute(new StringCallback() {
                        @Override
                        public void onBefore(BaseRequest request) {
                            btnSubmit.setEnabled(false);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Gson gson = new Gson();
                            JsonResult jsonResult = gson.fromJson(s, JsonResult.class);
                            if (jsonResult.getState() == 0) {
                                MyToast.show(getApplicationContext(), "提交成功，等待后台改派");
                                order.setState(6);
                                startActivity(new Intent(getApplicationContext(), OrderManagerActivity.class));
                                //发送广播更新当前订单
                                Intent intent = new Intent();
                                intent.setAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
                                sendBroadcast(intent);
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
                            btnSubmit.setEnabled(true);
                        }
                    });
        }

    }

    //从当前订单列表中删除此订单
    private void deleteCurrentOrder() {
        MyApplication.getCurrentOrders().remove(order);
        //发送广播更新当前订单列表
        Intent intent = new Intent();
        intent.setAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
        sendBroadcast(intent);
        finish();
    }

    private void getFocus() {
        hideInputMethod();
        llContent.setFocusable(true);
        llContent.setFocusableInTouchMode(true);
        llContent.requestFocus();
    }

    private void hideInputMethod() {
        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //隐藏输入法
        imm.hideSoftInputFromWindow(llContent.getWindowToken(), 0);
    }

}
