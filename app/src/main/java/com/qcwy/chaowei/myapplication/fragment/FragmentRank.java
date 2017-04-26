package com.qcwy.chaowei.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.RankAdapter;
import com.qcwy.chaowei.myapplication.entity.Rank;
import com.qcwy.chaowei.myapplication.entity.response.ResponseRank;
import com.qcwy.chaowei.myapplication.utils.CommonCallback;
import com.qcwy.chaowei.myapplication.utils.DateUtils;
import com.qcwy.chaowei.myapplication.utils.GsonUtils;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class FragmentRank extends BaseFragment implements View.OnClickListener {
    //日期布局
    @ViewInject(R.id.ll_date)
    private LinearLayout llDate;
    //日期文本
    @ViewInject(R.id.tv_date)
    private TextView tvDate;
    @ViewInject(R.id.tv_tips)
    private TextView tvTips;
    @ViewInject(R.id.lv_rank)
    private ListView lvRank;
    private List<Rank> rankList;
    private RankAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_rank, null);
            x.view().inject(this, rootView);
            initData();
            setLiseners();
            lvRank.setAdapter(adapter);
        }
        return rootView;
    }

    private void initData() {
        //初始化时间输入框
        tvDate.setText(DateUtils.format(new Date(), "yyyy-MM-dd"));
        rankList = new ArrayList<>();
        adapter = new RankAdapter(getContext(), rankList);
        getRank();
    }

    private void getRank() {
        String date = null;
        try {
            date = DateUtils.format(DateUtils.parse(tvDate.getText().toString(), "yyyy-MM-dd"), "yyyyMMdd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        OkGo.get(Urls.GET_ORDER_COUNT_RANK)     // 请求方式和请求url
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_ORDER_COUNT_RANK)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("date", date)
                .params("pageNum", 1)
                .params("pageSize", 6)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ResponseRank result = GsonUtils.getInstance().fromJson(s, ResponseRank.class);
                        if (result.getState() == 0) {
                            if (result.getData().size() != 0) {
                                tvTips.setVisibility(View.GONE);
                                lvRank.setVisibility(View.VISIBLE);
                                rankList.clear();
                                rankList.addAll(result.getData());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            tvTips.setVisibility(View.VISIBLE);
                            tvTips.setText(result.getMessage());
                            lvRank.setVisibility(View.GONE);
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

    private void setLiseners() {
        llDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date:
                //弹出日期选择
                DateUtils.selectDate(getContext(), tvDate, new CommonCallback() {
                    @Override
                    public void onSeccess() {
                        getRank();
                    }
                });
                break;
        }
    }
}
