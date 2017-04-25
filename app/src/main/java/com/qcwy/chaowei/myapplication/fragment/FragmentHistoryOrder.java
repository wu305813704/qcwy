package com.qcwy.chaowei.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.HistoryOrderAdapter;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.HistoryOrderData;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.entity.response.ResponseHistoryOrder;
import com.qcwy.chaowei.myapplication.entity.response.ResponseOrders;
import com.qcwy.chaowei.myapplication.ui.HistoryOrderDetailActivity;
import com.qcwy.chaowei.myapplication.ui.SettingActivity;
import com.qcwy.chaowei.myapplication.utils.CommonCallback;
import com.qcwy.chaowei.myapplication.utils.DateUtils;
import com.qcwy.chaowei.myapplication.utils.GsonUtils;
import com.qcwy.chaowei.myapplication.utils.MyItemClickListener;
import com.qcwy.chaowei.myapplication.utils.MyItemDecoration;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by KouKi on 2016/12/27.
 */

public class FragmentHistoryOrder extends BaseFragment implements View.OnClickListener {
    //开始日期布局
    @ViewInject(R.id.ll_start_date)
    private LinearLayout llStartDate;
    //开始日期文本
    @ViewInject(R.id.tv_start_date)
    private TextView tvStartDate;
    //结束日期布局
    @ViewInject(R.id.ll_end_date)
    private LinearLayout llEndDate;
    //结束日期文本
    @ViewInject(R.id.tv_end_date)
    private TextView tvEndDate;
    //订单数量
    @ViewInject(R.id.tv_count)
    private TextView tvCount;
    //订单总额
    @ViewInject(R.id.tv_total_price)
    private TextView tvTotalPrice;
    //抢单数
    @ViewInject(R.id.tv_rush_count)
    private TextView tvRushCount;
    //丢单数
    @ViewInject(R.id.tv_lose_count)
    private TextView tvLoseCount;
    //抢单率
    @ViewInject(R.id.tv_percent_rush)
    private TextView tvPercentRush;
    //丢单率
    @ViewInject(R.id.tv_percent_lose)
    private TextView tvPercentLose;

    @ViewInject(R.id.swipe_container)
    private SwipeRefreshLayout swipeRefreshLayout;
    //RecycleView
    @ViewInject(R.id.rv_orders)
    private RecyclerView rvOrders;
    private HistoryOrderAdapter adapter;
    private List<Order> orders;

    private LinearLayoutManager layoutManager;
    private int pageNum = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_history_order, null);
            x.view().inject(this, rootView);
            init();
            setListeners();
            rvOrders.setAdapter(adapter);
            getHistoryOrders();
            getHistoryOrderList(pageNum++, 10);
        }
        return rootView;
    }

    private void init() {
        //设置刷新条颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        //初始化时间输入框
        tvStartDate.setText(DateUtils.format(new Date(), "yyyy-MM-dd"));
        tvEndDate.setText(DateUtils.format(new Date(), "yyyy-MM-dd"));
        //设置静态变量值，防止其他地方改动
        DateUtils.startTime = System.currentTimeMillis();
        DateUtils.endTime = System.currentTimeMillis();
        orders = new ArrayList<>();
        adapter = new HistoryOrderAdapter(getActivity(), orders);
        //设置布局格式
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecycleView添加分割线
        rvOrders.addItemDecoration(new MyItemDecoration(getActivity(), layoutManager.getOrientation()));
        rvOrders.setLayoutManager(layoutManager);
    }

    private void setListeners() {
        llStartDate.setOnClickListener(this);
        llEndDate.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reLoad();
            }
        });

        adapter.setLoadCallback(new HistoryOrderAdapter.onLoadCallback() {
            @Override
            public void onLoad() {
                getHistoryOrderList(pageNum++, 10);
            }
        });
        adapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(getContext(), HistoryOrderDetailActivity.class);
                intent.putExtra("orderNo", orders.get(postion).getOrder_no());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_start_date:
                //弹出开始日期选择
                DateUtils.selectDate(getContext(), tvStartDate, new CommonCallback() {
                    @Override
                    public void onSeccess() {
                        reLoad();
                    }
                });
                break;
            case R.id.ll_end_date:
                //弹出结束日期选择
                DateUtils.selectDate(getContext(), tvEndDate, new CommonCallback() {
                    @Override
                    public void onSeccess() {
                        reLoad();
                    }
                });
                break;
        }
    }

    private void reLoad() {
        pageNum = 1;
        orders.clear();
        getHistoryOrders();
        getHistoryOrderList(pageNum++, 10);
    }

    private void getHistoryOrders() {
        String startTime = tvStartDate.getText().toString();
        String endTime = tvEndDate.getText().toString() + " 23:59:59";
        OkGo.get(Urls.GET_HISTORY_ORDERS)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_HISTORY_ORDERS)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("jobNo", MyApplication.jobNo)
                .params("startTime", startTime)
                .params("endTime", endTime)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ResponseHistoryOrder result = GsonUtils.getInstance().fromJson(s, ResponseHistoryOrder.class);
                        if (result.getState() == 0) {
                            HistoryOrderData historyOrderData = result.getData();
                            tvCount.setText(String.valueOf(historyOrderData.getCount()));
                            tvTotalPrice.setText(String.valueOf(historyOrderData.getTotalPrice()));
                            tvRushCount.setText(String.valueOf(historyOrderData.getRushCount()));
                            tvLoseCount.setText(String.valueOf(historyOrderData.getLoseCount()));
                            tvPercentRush.setText(historyOrderData.getPercentRush() * 100 + "%");
                            tvPercentLose.setText(historyOrderData.getPercentLose() * 100 + "%");
                        } else {
                            tvCount.setText("0");
                            tvTotalPrice.setText("0.0");
                            tvRushCount.setText("0");
                            tvLoseCount.setText("0");
                            tvPercentRush.setText("0.0%");
                            tvPercentLose.setText("0.0%");
//                            MyToast.show(getContext(), result.getMessage());
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
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void getHistoryOrderList(int pageNum, int pageSize) {
        String startTime = tvStartDate.getText().toString();
        String endTime = tvEndDate.getText().toString() + " 23:59:59";
        OkGo.get(Urls.GET_HISTORY_ORDER_LIST)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_HISTORY_ORDER_LIST)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("jobNo", MyApplication.jobNo)
                .params("startTime", startTime)
                .params("endTime", endTime)
                .params("pageNum", pageNum)
                .params("pageSize", pageSize)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        adapter.setCanLoad(true);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ResponseOrders result = GsonUtils.getInstance().fromJson(s, ResponseOrders.class);
                        if (result.getState() == 0) {
                            if (result.getData().size() == 0) {
                                adapter.setCanLoad(false);
                            }
                            orders.addAll(result.getData());
                            adapter.notifyDataSetChanged();
                        } else {
//                            MyToast.show(getContext(), result.getMessage());
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
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}
