package com.qcwy.chaowei.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.MessageAdapter;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.OrderMessage;
import com.qcwy.chaowei.myapplication.entity.response.ResponseOrderMessage;
import com.qcwy.chaowei.myapplication.ui.OrderMessageActivity;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by KouKi on 2016/12/26.
 */

public class FragmentOrderMessage extends BaseFragment {
    @ViewInject(R.id.tv_msg)
    private TextView tvMsg;
    @ViewInject(R.id.swipe_container)
    private SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.lv_order_msg)
    private ListView lvOrderMsg;
    private MessageAdapter adapter;
    private List<OrderMessage> messages;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_order_message, null);
            x.view().inject(this, rootView);
            init();
            lvOrderMsg.setAdapter(adapter);
            setListeners();
        }
        return rootView;
    }

    private void init() {
        //设置刷新条颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        //调整进度条距离屏幕顶部的距离(是否缩放，起始距离，结束距离)
//        swipeRefreshLayout.setProgressViewOffset(true, -10, 0);
        messages = new ArrayList<>();
        adapter = new MessageAdapter(getContext(), messages);
        getMessages();
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //连接网络查询数据
                getMessages();
            }
        });
        lvOrderMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderMessage msg = messages.get(position);
                Intent intent = new Intent(getContext(), OrderMessageActivity.class);
                switch (msg.getType()) {
                    //抢单
                    case 0:
                        break;
                    //收到的改派订单
                    case 1:
                        intent.putExtra("type", msg.getType());
                        intent.putExtra("orderNo", msg.getOrder_no());
                        startActivity(intent);
                        break;
                    //发起的改派订单
                    case 2:
                        intent.putExtra("type", msg.getType());
                        intent.putExtra("orderNo", msg.getOrder_no());
                        startActivity(intent);
                        break;
                }
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

    private void getMessages() {
        OkGo.get(Urls.GET_ORDER_MESSAGES)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_ORDER_MESSAGES)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("jobNo", MyApplication.jobNo)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        ResponseOrderMessage orderMessage = gson.fromJson(s, ResponseOrderMessage.class);
                        if (orderMessage.getData().size() == 0) {
                            tvMsg.setVisibility(View.VISIBLE);
                        } else {
                            tvMsg.setVisibility(View.INVISIBLE);
                        }
                        messages.clear();
                        messages.addAll(orderMessage.getData());
                        adapter.notifyDataSetChanged();
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
