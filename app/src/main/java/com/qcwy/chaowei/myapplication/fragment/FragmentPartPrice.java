package com.qcwy.chaowei.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.PartPriceAdapter;
import com.qcwy.chaowei.myapplication.entity.Part;
import com.qcwy.chaowei.myapplication.entity.PartDetail;
import com.qcwy.chaowei.myapplication.entity.response.ResponsePart;
import com.qcwy.chaowei.myapplication.entity.response.ResponsePartDetail;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by KouKi on 2017/1/9.
 */

public class FragmentPartPrice extends BaseFragment {
    @ViewInject(R.id.rg_part)
    private RadioGroup rgPart;
    @ViewInject(R.id.gv_part)
    private GridView gvPart;
    private List<PartDetail> parts;
    private PartPriceAdapter adapter;
    private Gson gson;

    private List<PartDetail> parts0;
    private List<PartDetail> parts1;
    private List<PartDetail> parts2;
    private List<PartDetail> parts3;
    private List<PartDetail> parts4;
    private List<PartDetail> parts5;
    private List<PartDetail> parts6;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_part_price, null);
            x.view().inject(this, rootView);
            init();
            setListeners();
            gvPart.setAdapter(adapter);
            rgPart.check(R.id.rb_0);
        }
        return rootView;
    }

    private void init() {
        gson = new Gson();
        parts = new ArrayList<>();
        parts0 = new ArrayList<>();
        parts1 = new ArrayList<>();
        parts2 = new ArrayList<>();
        parts3 = new ArrayList<>();
        parts4 = new ArrayList<>();
        parts5 = new ArrayList<>();
        parts6 = new ArrayList<>();
        adapter = new PartPriceAdapter(getContext(), parts);
    }

    private void setListeners() {
        rgPart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_0:
                        //四大部件
                        if (parts0.isEmpty()) {
                            getParts(1, parts0);
                        } else {
                            updateList(parts0);
                        }
                        break;
                    case R.id.rb_1:
                        //制动系统
                        if (parts1.isEmpty()) {
                            getParts(2, parts1);
                        } else {
                            updateList(parts1);
                        }
                        break;
                    case R.id.rb_2:
                        //结构部件
                        if (parts2.isEmpty()) {
                            getParts(3, parts2);
                        } else {
                            updateList(parts2);
                        }
                        break;
                    case R.id.rb_3:
                        //电器配件
                        if (parts3.isEmpty()) {
                            getParts(4, parts3);
                        } else {
                            updateList(parts3);
                        }
                        break;
                    case R.id.rb_4:
                        //通用配件
                        if (parts4.isEmpty()) {
                            getParts(5, parts4);
                        } else {
                            updateList(parts4);
                        }
                        break;
                    case R.id.rb_5:
                        //三轮车配件
                        if (parts5.isEmpty()) {
                            getParts(6, parts5);
                        } else {
                            updateList(parts5);
                        }
                        break;
                    case R.id.rb_6:
                        //水电瓶车配件
                        if (parts6.isEmpty()) {
                            getParts(7, parts6);
                        } else {
                            updateList(parts6);
                        }
                        break;
                }
            }
        });
    }

    private void updateList(List<PartDetail> partList) {
        parts.clear();
        parts.addAll(partList);
        adapter.notifyDataSetChanged();
    }

    private void getParts(int classify, final List<PartDetail> parts) {
        OkGo.get(Urls.GET_PART_BY_CLASSIFY)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_PART_BY_CLASSIFY)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("classify", classify)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ResponsePartDetail result = gson.fromJson(s, ResponsePartDetail.class);
                        if (result.getState() == 0) {
                            parts.clear();
                            parts.addAll(result.getData());
                            updateList(parts);
                        } else {
                            MyToast.show(getContext(), result.getMessage());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        if (response == null) {
                            MyToast.show(getContext(), "网络异常");
                        } else {
                            MyToast.show(getContext(), e.getMessage());
                        }
                    }
                });

    }
}
