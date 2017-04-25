package com.qcwy.chaowei.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.PartAdapter;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.WarehouseEmployee;
import com.qcwy.chaowei.myapplication.entity.response.ResponseWarehouseEmployee;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

@ContentView(R.layout.activity_add_part)
public class AddPartActivity extends BaseActivity {
    @ViewInject(R.id.lv_part)
    private ListView lvPart;
    @ViewInject(R.id.btn_submit)
    private Button btnSubmit;
    private List<WarehouseEmployee> list;
    private PartAdapter adapter;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("添加零件");
        getParts();
    }

    @Override
    protected void init() {
        gson = new Gson();
    }

    @Override
    protected void setListeners() {
        btnSubmit.setOnClickListener(this);
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
            case R.id.btn_submit:
                HashMap<Integer, Boolean> isSelected = adapter.getIsSelected();
                List<String> parts = new ArrayList<>();
                List<Integer> count = new ArrayList<>();
                for (int i = 0; i < isSelected.size(); i++) {
                    if (isSelected.get(i)) {
                        parts.add(String.valueOf(list.get(i).getPart_detail_id()));
                        count.add(list.get(i).getCount());
                    }
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra(GlobalContants.KEY_ADD_PART, (ArrayList<String>) parts);
                intent.putIntegerArrayListExtra(GlobalContants.KEY_ADD_PART_COUNT, (ArrayList<Integer>) count);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    public void getParts() {
        OkGo.get(Urls.GET_PARTS)     // 请求方式和请求url
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.GET_PARTS)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("jobNo", MyApplication.jobNo)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ResponseWarehouseEmployee result = gson.fromJson(s, ResponseWarehouseEmployee.class);
                        if (result.getState() == 0) {
                            list = result.getData();
                            adapter = new PartAdapter(AddPartActivity.this, list);
                            lvPart.setAdapter(adapter);
                        } else {
                            MyToast.show(getApplicationContext(), "您还没有零件");
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
}
