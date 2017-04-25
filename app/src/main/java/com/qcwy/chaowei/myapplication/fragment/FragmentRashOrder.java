package com.qcwy.chaowei.myapplication.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.RushOrderAdapter;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.entity.response.ResponseOrders;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.OrderSettingUtils;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by KouKi on 2016/12/27.
 */

public class FragmentRashOrder extends BaseFragment {
    @ViewInject(R.id.tv_msg)
    private TextView tvMsg;
    @ViewInject(R.id.swipe_container)
    private SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.lv_rush_order)
    private ListView lvRushOrder;
    private List<Order> orders;
    private RushOrderAdapter adapter;
    private InnerReceiver receiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_rush_order, null);
            x.view().inject(this, rootView);
            init();
            lvRushOrder.setAdapter(adapter);
            setListeners();
            register();
            getRushOrders();
        }
        return rootView;
    }

    private void register() {
        //注册广播
        //收到订单消息后更新List
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalContants.RECEIVER_UPDATE_RUSH_ORDERS);
        getActivity().registerReceiver(receiver, filter);
    }

    private void init() {
        //设置刷新条颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        //调整进度条距离屏幕顶部的距离(是否缩放，起始距离，结束距离)
//        swipeRefreshLayout.setProgressViewOffset(true, -10, 0);
        receiver = new InnerReceiver();
        orders = MyApplication.getRushOrders();
        adapter = new RushOrderAdapter(getContext(), orders);
    }

    private void getRushOrders() {
        tvMsg.setVisibility(View.INVISIBLE);
        MyApplication.getLocationClient().requestLocation();
        String lati = String.valueOf(MyApplication.getLatitude());
        String lon = String.valueOf(MyApplication.getLongitude());
        OkGo.get(Urls.GET_RUSH_ORDERS)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_RUSH_ORDERS)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("jobNo", MyApplication.jobNo)
                .params("lati", lati)
                .params("lon", lon)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        ResponseOrders responseOrder = gson.fromJson(s, ResponseOrders.class);
                        //请求成功
                        if (responseOrder.getState() == 0) {
                            //设置订单其他属性
                            for (Order order : responseOrder.getData()) {
                                OrderSettingUtils.setOrderDetail(order);
                            }
                            orders.clear();
                            orders.addAll(responseOrder.getData());
                            adapter.notifyDataSetChanged();
                        } else {
                            if ("订单数量达到上限".equals(responseOrder.getMessage())) {
                                MyApplication.getRushOrders().clear();
                                tvMsg.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        if (response == null) {
                            MyToast.show(getContext(), "网络连接失败!");
                        } else {
                            MyToast.show(getContext(), e);
                        }
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        //隐藏刷新条
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //连接网络查询数据
                getRushOrders();
            }
        });
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (!isVisible) {
            //如果不可见，停止刷新
            swipeRefreshLayout.setRefreshing(false);
            //取消网络连接
            OkGo.getInstance().cancelTag(this);
        }
    }

    class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //修改的MyApplication中的orders是数据源，不需要重新添加
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }
}
