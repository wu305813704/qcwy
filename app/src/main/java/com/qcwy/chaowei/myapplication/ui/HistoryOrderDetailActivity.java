package com.qcwy.chaowei.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.HistoryOrderData;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.entity.OrderDetail;
import com.qcwy.chaowei.myapplication.entity.OrderFault;
import com.qcwy.chaowei.myapplication.entity.OrderPart;
import com.qcwy.chaowei.myapplication.entity.WxUser;
import com.qcwy.chaowei.myapplication.entity.response.ResponseHistoryOrder;
import com.qcwy.chaowei.myapplication.entity.response.ResponseOrder;
import com.qcwy.chaowei.myapplication.entity.response.ResponseOrderFault;
import com.qcwy.chaowei.myapplication.entity.response.ResponseOrderParts;
import com.qcwy.chaowei.myapplication.utils.DateUtils;
import com.qcwy.chaowei.myapplication.utils.FaultUtils;
import com.qcwy.chaowei.myapplication.utils.GsonUtils;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Response;

@ContentView(R.layout.activity_history_order_detail)
public class HistoryOrderDetailActivity extends BaseActivity {
    @ViewInject(R.id.ll_old_order_no)
    private LinearLayout llOldOrderNo;
    @ViewInject(R.id.tv_order_no)
    private TextView tvOrderNo;
    @ViewInject(R.id.tv_old_order_no)
    private TextView tvOldOrderNo;
    @ViewInject(R.id.tv_send_time)
    private TextView tvSendTime;
    @ViewInject(R.id.tv_loc)
    private TextView tvLoc;
    @ViewInject(R.id.tv_name)
    private TextView tvName;
    @ViewInject(R.id.tv_tel)
    private TextView tvTel;
    @ViewInject(R.id.tv_order_type)
    private TextView tvOrderTpye;
    @ViewInject(R.id.tv_fault)
    private TextView tvFault;
    @ViewInject(R.id.tv_real_fault)
    private TextView tvRealFault;
    @ViewInject(R.id.tv_part)
    private TextView tvPart;
    @ViewInject(R.id.tv_total_price)
    private TextView tvTotalPrice;
    @ViewInject(R.id.tv_pay_method)
    private TextView tvPayMethod;
    @ViewInject(R.id.tv_remark)
    private TextView tvReamark;

    private int orderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        setTitle("历史订单详情");
        Intent intent = getIntent();
        orderNo = intent.getIntExtra("orderNo", -1);
        loadOrderDetail();
    }

    private void loadOrderDetail() {
        if (orderNo == -1) {
            MyToast.show(getApplicationContext(), "订单号异常");
            return;
        }
        OkGo.get(Urls.GET_ORDER_BY_ORDER_NO)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_ORDER_BY_ORDER_NO)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("orderNo", orderNo)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ResponseOrder result = GsonUtils.getInstance().fromJson(s, ResponseOrder.class);
                        if (result.getState() == 0) {
                            Order order = result.getData();
                            OrderDetail orderDetail = order.getOrderDetail();
                            WxUser wxUser = order.getWxUser();
                            if (order.getType() == 1) {
                                //售后订单
                                tvOldOrderNo.setText(String.valueOf(order.getOld_order_no()));
                                tvOrderTpye.setText("售后订单");
                            } else {
                                llOldOrderNo.setVisibility(View.GONE);
                                tvOrderTpye.setText("普通订单");
                            }
                            tvOrderNo.setText(String.valueOf(order.getOrder_no()));
                            tvSendTime.setText(DateUtils.format(new Date(order.getSend_time()), "yyyy-MM-dd HH:mm:ss"));
                            tvLoc.setText(orderDetail.getLoc());
                            tvName.setText(wxUser.getNickname());
                            tvTel.setText(wxUser.getTel());
                            String[] faultIds = orderDetail.getFault_id().split("-");
                            StringBuilder fault = new StringBuilder();
                            for (String str : faultIds) {
                                fault.append(FaultUtils.parseWx(Integer.valueOf(str)) + "/");
                            }
                            fault.append(orderDetail.getFault_description());
                            tvFault.setText(fault.toString());
                        } else {
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

        OkGo.get(Urls.GET_REAL_ORDER_FAULT)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_REAL_ORDER_FAULT)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("orderNo", orderNo)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ResponseOrderFault result = GsonUtils.getInstance().fromJson(s, ResponseOrderFault.class);
                        if (result.getState() == 0) {
                            OrderFault orderFault = result.getData();
                            if (orderFault == null) {
                                return;
                            }
                            String[] faultIds = orderFault.getFault_id().split("-");
                            StringBuilder fault = new StringBuilder();
                            for (String str : faultIds) {
                                fault.append(FaultUtils.parseWx(Integer.valueOf(str)) + "/");
                            }
                            fault.append(orderFault.getFault_description());
                            tvRealFault.setText(fault.toString());
                        } else {

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
        OkGo.get(Urls.GET_ORDER_PART)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_ORDER_PART)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("orderNo", orderNo)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ResponseOrderParts result = GsonUtils.getInstance().fromJson(s, ResponseOrderParts.class);
                        if (result.getState() == 0) {
                            List<OrderPart> orderParts = result.getData();
                            if (orderParts.isEmpty()) {
                                return;
                            }
                            StringBuilder parts = new StringBuilder();
                            double totalPrice = 0;
                            int i = 1;
                            for (OrderPart orderPart : orderParts) {
                                if (i == orderParts.size()) {
                                    parts.append(orderPart.getPartMode() + "(X" + orderPart.getCount() + ")");
                                    totalPrice += orderPart.getPrice() * orderPart.getCount();
                                    break;
                                }
                                parts.append(orderPart.getPartMode() + "(X" + orderPart.getCount() + ")" + "\n");
                                totalPrice += orderPart.getPrice() * orderPart.getCount();
                                i++;
                            }
                            tvPart.setText(parts.toString());
                            tvTotalPrice.setText(String.valueOf(totalPrice) + "元");
                        } else {

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
