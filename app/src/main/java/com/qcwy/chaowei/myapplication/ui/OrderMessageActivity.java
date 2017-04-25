package com.qcwy.chaowei.myapplication.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.qcwy.chaowei.myapplication.entity.OrderDetail;
import com.qcwy.chaowei.myapplication.entity.OrderReassignment;
import com.qcwy.chaowei.myapplication.entity.response.ResponseOrder;
import com.qcwy.chaowei.myapplication.entity.response.ResponseOrderReassignment;
import com.qcwy.chaowei.myapplication.entity.WxUser;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.OrderSettingUtils;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import okhttp3.Call;
import okhttp3.Response;

@ContentView(R.layout.activity_order_msg_detail)
public class OrderMessageActivity extends BaseActivity {
    @ViewInject(R.id.ll_reassignment)
    private LinearLayout llReassignment;
    @ViewInject(R.id.ll_rush)
    private LinearLayout llRush;
    @ViewInject(R.id.ll_button)
    private LinearLayout llButton;
    @ViewInject(R.id.tv_send_time)
    private TextView tvSendTime;
    @ViewInject(R.id.tv_send_name)
    private TextView tvSendName;
    @ViewInject(R.id.tv_send_cause)
    private TextView tvSendCause;
    @ViewInject(R.id.tv_order_number)
    private TextView tvOrderNo;
    @ViewInject(R.id.tv_time)
    private TextView tvTime;
    @ViewInject(R.id.tv_address)
    private TextView tvAddress;
    @ViewInject(R.id.tv_username)
    private TextView tvUsername;
    @ViewInject(R.id.tv_mobile_no)
    private TextView tvMobileNo;
    @ViewInject(R.id.tv_trouble)
    private TextView tvTrouble;
    @ViewInject(R.id.tv_rush_time)
    private TextView tvRushTime;
    @ViewInject(R.id.btn_accept)
    private Button btnAccept;
    @ViewInject(R.id.btn_refuse)
    private Button btnRefuse;
    private Intent intent;
    private int type;
    private int orderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }

    @Override
    protected void init() {
        setTitle("消息详情");
        getIvMsg().setVisibility(View.INVISIBLE);
        intent = getIntent();
        type = intent.getIntExtra("type", -1);
        orderNo = intent.getIntExtra("orderNo", -1);
    }

    @Override
    protected void setListeners() {
        getBtnBack().setOnClickListener(this);
        tvMobileNo.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
        btnRefuse.setOnClickListener(this);
    }

    //设置数据
    private void setData() {
        //获取订单
        OkGo.get(Urls.GET_ORDER_BY_ORDER_NO)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_ORDER_BY_ORDER_NO)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("orderNo", orderNo)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        ResponseOrder responseOrder = gson.fromJson(s, ResponseOrder.class);
                        if (responseOrder.getState() == 0) {
                            Order order = responseOrder.getData();
                            OrderSettingUtils.setOrderDetail(order);
                            //改派中
                            if (order.getState() == 6) {
                                switch (type) {
                                    case 1://接收的改派
                                        llButton.setVisibility(View.VISIBLE);
                                        break;
                                    case 2://发起的改派
                                        llButton.setVisibility(View.GONE);
                                        break;
                                }
                                OrderDetail orderDetail = order.getOrderDetail();
                                WxUser wxUser = order.getWxUser();
                                tvOrderNo.setText(String.valueOf(order.getOrder_no()));
                                tvTime.setText(orderDetail.getSend_time());
                                tvAddress.setText(orderDetail.getLoc());
                                tvUsername.setText(wxUser.getNickname());
                                tvMobileNo.setText(wxUser.getTel());
                                tvTrouble.setText(order.getFaultDescription());
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        MyToast.show(getApplicationContext(), "请求数据异常，请重试!");
                    }
                });

        switch (type) {
            case 0://抢单消息
                llReassignment.setVisibility(View.GONE);
                llRush.setVisibility(View.VISIBLE);
                llButton.setVisibility(View.GONE);
                break;
            case 1://收到的改派消息
            case 2://发起的改派消息
                llReassignment.setVisibility(View.VISIBLE);
                llRush.setVisibility(View.GONE);
                OkGo.get(Urls.GET_REASSIGNMENT_ORDER)
                        .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                        .cacheKey(Urls.GET_REASSIGNMENT_ORDER)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                        .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                        .params("orderNo", orderNo)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Gson gson = new Gson();
                                ResponseOrderReassignment responseOrderReassignment = gson.fromJson(s, ResponseOrderReassignment.class);
                                if (responseOrderReassignment.getState() == 0) {
                                    OrderReassignment reassignment = responseOrderReassignment.getData();
                                    tvSendTime.setText(reassignment.getSend_time());
                                    tvSendName.setText(reassignment.getSendName());
                                    tvSendCause.setText(reassignment.getCause());
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
                        });
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_mobile_no:
                new AlertDialog.Builder(OrderMessageActivity.this)
                        .setTitle("确定呼叫：")
                        .setMessage(tvMobileNo.getText().toString())
                        .setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    MyToast.show(getApplicationContext(), "没有授权");
                                    return;
                                }
                                //用intent启动拨打电话
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvMobileNo.getText().toString()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                break;
            case R.id.btn_accept:
                accept();
                break;
            case R.id.btn_refuse:
                refuse();
                break;
        }
    }

    //接受改派
    private void accept() {
        OkGo.get(Urls.ACCEPT_ORDER)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.ACCEPT_ORDER)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("orderNo", orderNo)
                .params("jobNo", MyApplication.jobNo)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        btnAccept.setEnabled(false);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        JsonResult jsonResult = gson.fromJson(s, JsonResult.class);
                        if (jsonResult.getState() == 0) {
                            MyToast.show(getApplicationContext(), "订单已接受，请刷新当前订单查看");
                            finish();
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
                        btnAccept.setEnabled(true);
                    }
                });
    }

    //拒绝改派
    private void refuse() {
        OkGo.get(Urls.REFUSE_ORDER)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.REFUSE_ORDER)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("orderNo", orderNo)
                .params("jobNo", MyApplication.jobNo)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        btnRefuse.setEnabled(false);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        JsonResult jsonResult = gson.fromJson(s, JsonResult.class);
                        if (jsonResult.getState() == 0) {
                            MyToast.show(getApplicationContext(), "订单已拒绝");
                            finish();
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
                        btnRefuse.setEnabled(true);
                    }
                });
    }
}
