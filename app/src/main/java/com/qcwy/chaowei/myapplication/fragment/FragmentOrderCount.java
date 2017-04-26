package com.qcwy.chaowei.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.JsonResult;
import com.qcwy.chaowei.myapplication.utils.DateUtils;
import com.qcwy.chaowei.myapplication.utils.GsonUtils;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

public class FragmentOrderCount extends BaseFragment {
    @ViewInject(R.id.tv_time)
    private TextView tvTime;
    @ViewInject(R.id.tv_order_count)
    private TextView tvCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_order_count, null);
            x.view().inject(this, rootView);
            initData();
        }
        return rootView;
    }

    private void initData() {
        tvTime.setText(DateUtils.format(new Date(), "yyyy年MM月dd日"));
        OkGo.get(Urls.GET_ORDER_COUNT)     // 请求方式和请求url
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_ORDER_COUNT)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("jobNo", MyApplication.jobNo)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JsonResult result = GsonUtils.getInstance().fromJson(s, JsonResult.class);
                        if (result.getState() == 0) {
                            double count = (double) result.getData();
                            tvCount.setText(String.valueOf((int) count));
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
                });
    }
}
