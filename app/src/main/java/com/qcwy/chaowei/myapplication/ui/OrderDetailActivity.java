package com.qcwy.chaowei.myapplication.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.JsonResult;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.entity.OrderDetail;
import com.qcwy.chaowei.myapplication.entity.WxUser;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import okhttp3.Call;
import okhttp3.Response;

@ContentView(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {
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
    @ViewInject(R.id.tv_state)
    private TextView tvState;
    //地图
    @ViewInject(R.id.btn_map)
    private Button btnMap;
    //维修完成
    @ViewInject(R.id.btn_complete)
    private Button btnComplete;
    private Order order;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("订单详情");
    }

    @Override
    protected void init() {
        order = MyApplication.getApp().getOrder();
        OrderDetail orderDetail = order.getOrderDetail();
        WxUser wxUser = order.getWxUser();
        tvOrderNo.setText(String.valueOf(order.getOrder_no()));
        tvTime.setText(orderDetail.getSend_time());
        tvAddress.setText(orderDetail.getLoc());
        tvUsername.setText(wxUser.getNickname());
        tvMobileNo.setText(wxUser.getTel());
        tvTrouble.setText(order.getFaultDescription());
        gson = new Gson();
        switch (order.getState()) {
            case 1:
                tvState.setText("未开始");
                tvState.setTextColor(Color.BLACK);
                btnComplete.setEnabled(false);
                break;
            case 2:
                tvState.setText("请及时确认故障");
                tvState.setTextColor(Color.GREEN);
                break;
            case 3:
                tvState.setText("暂停中");
                tvState.setTextColor(Color.RED);
                btnComplete.setEnabled(false);
                break;
            case 4:
                tvState.setText("预约中");
                tvState.setTextColor(Color.YELLOW);
                btnComplete.setEnabled(false);
                break;
            case 6:
                tvState.setText("改派中");
                tvState.setTextColor(Color.BLUE);
                btnComplete.setEnabled(false);
                break;
            case 7:
                tvState.setText("等待用户确认故障");
                tvState.setTextColor(Color.BLUE);
                btnComplete.setEnabled(false);
                break;
            case 8:
                tvState.setText("维修中");
                tvState.setTextColor(Color.BLUE);
                btnComplete.setEnabled(true);
                break;
            case 9:
                tvState.setText("等待用户验收");
                tvState.setTextColor(Color.YELLOW);
                btnComplete.setEnabled(false);
                break;
            case 10:
                tvState.setText("等待用户付款");
                tvState.setTextColor(Color.YELLOW);
                btnComplete.setText("线下付款");
                break;
        }
    }

    @Override
    protected void setListeners() {
        btnMap.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
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
            case R.id.btn_map:
                //地图
                finish();
                break;
            case R.id.btn_complete:
                //维修完成
                if (btnComplete.getText().toString().equals("维修完成")) {
                    finishService();
                } else if (btnComplete.getText().toString().equals("线下付款")) {
                    offonlinePay();
                }
                break;
        }
    }

    private void offonlinePay() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定用户已线下付款完毕?")
                .setPositiveButton("我已确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        btnComplete.setEnabled(false);
                        OkGo.get(Urls.OFFLINE_PAY)     // 请求方式和请求url
                                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                                .cacheKey(Urls.OFFLINE_PAY)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                                .params("jobNo", MyApplication.jobNo)
                                .params("orderNo", order.getOrder_no())
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        JsonResult result = gson.fromJson(s, JsonResult.class);
                                        if (result.getState() == 0) {
                                            MyToast.show(getApplicationContext(), "订单已完成");
                                            Intent intent = new Intent();
                                            intent.setAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
                                            sendBroadcast(intent);
                                            Intent skip = new Intent(getApplicationContext(), OrderManagerActivity.class);
                                            skip.putExtra(GlobalContants.INTENT_SKIP, GlobalContants.INTENT_CURRENT_ORDER);
                                            skip.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(skip);
                                        } else {
                                            if ("该订单已付款".equals(result.getMessage())) {
                                                Intent intent = new Intent();
                                                intent.setAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
                                                sendBroadcast(intent);
                                                Intent skip = new Intent(getApplicationContext(), OrderManagerActivity.class);
                                                skip.putExtra(GlobalContants.INTENT_SKIP, GlobalContants.INTENT_CURRENT_ORDER);
                                                skip.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(skip);
                                            }
                                            MyToast.show(getApplicationContext(), result.getMessage());
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
                                        btnComplete.setEnabled(true);
                                    }
                                });
                    }
                })
                .setNegativeButton("我点错了", null)
                .create()
                .show();
    }

    private void finishService() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认维修完成?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        btnComplete.setEnabled(false);
                        OkGo.get(Urls.COMPELETE)     // 请求方式和请求url
                                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                                .cacheKey(Urls.COMPELETE)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                                .params("jobNo", MyApplication.jobNo)
                                .params("orderNo", order.getOrder_no())
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        JsonResult result = gson.fromJson(s, JsonResult.class);
                                        if (result.getState() == 0) {
                                            MyToast.show(getApplicationContext(), "维修完成，请等待客户验收");
                                            Intent intent = new Intent();
                                            intent.setAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
                                            sendBroadcast(intent);
                                        } else {
                                            MyToast.show(getApplicationContext(), result.getMessage());
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
                                        btnComplete.setEnabled(true);
                                    }
                                });
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }
}
