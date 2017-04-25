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
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.CurrentOrderAdapter;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.entity.response.ResponseOrders;
import com.qcwy.chaowei.myapplication.ui.CurrentOrderDetailActivity;
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

public class FragmentCurrentOrder extends BaseFragment {
    @ViewInject(R.id.swipe_container)
    private SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.lv_current_order)
    private ListView lvCurrentOrder;
    private List<Order> orders;
    private CurrentOrderAdapter adapter;
    private InnerReceiver receiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_current_order, null);
            x.view().inject(this, rootView);
            init();
            register();
            lvCurrentOrder.setAdapter(adapter);
            setListeners();
            getCurrentOrders();
        }
        return rootView;
    }

    private void setListeners() {
        lvCurrentOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //添加到全局变量中
                MyApplication.getApp().setOrder(orders.get(position));
                startActivity(new Intent(getContext(), CurrentOrderDetailActivity.class));
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //连接网络查询数据
                getCurrentOrders();
            }
        });
    }

    private void getCurrentOrders() {
        MyApplication.getLocationClient().requestLocation();
        OkGo.get(Urls.GET_CURRENT_ORDERS)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_CURRENT_ORDERS)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("jobNo", MyApplication.jobNo)
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

    private void init() {
        //设置刷新条颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        //调整进度条距离屏幕顶部的距离(是否缩放，起始距离，结束距离)
//        swipeRefreshLayout.setProgressViewOffset(true, -10, 0);
        orders = MyApplication.getCurrentOrders();
        adapter = new CurrentOrderAdapter(getContext(), orders);
        receiver = new InnerReceiver();
    }

    private void register() {
        //收到更新当前订单广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
        getActivity().registerReceiver(receiver, filter);
    }

    class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getCurrentOrders();
        }
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

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }
}
