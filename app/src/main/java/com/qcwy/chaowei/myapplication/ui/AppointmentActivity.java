package com.qcwy.chaowei.myapplication.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.SearchResultAdapter;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.JsonResult;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.utils.DateUtils;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.StringUtils;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

@ContentView(R.layout.activity_appointment)
public class AppointmentActivity extends BaseActivity {
    @ViewInject(R.id.ll_main)
    private LinearLayout llMain;
    @ViewInject(R.id.tv_city)
    private TextView tvCity;
    @ViewInject(R.id.rg_cause)
    private RadioGroup rgCause;
    @ViewInject(R.id.et_other)
    private EditText etOther;
    @ViewInject(R.id.tv_time)
    private TextView tvTime;
    @ViewInject(R.id.tv_address)
    private TextView tvAddress;
    @ViewInject(R.id.btn_submit)
    private Button btnSubmit;

    //搜索布局
    @ViewInject(R.id.ll_search)
    private LinearLayout llSearch;
    @ViewInject(R.id.et_search)
    private EditText etSearch;
    @ViewInject(R.id.btn_cancel)
    private Button btnCancel;
    @ViewInject(R.id.lv_search_result)
    private ListView lvSearchResult;

    //时间选择器
    private TimePickerView pvTime;
    //选择的时间
    private long time = System.currentTimeMillis();

    //POI搜索
    private PoiSearch mPoiSearch;
    private List<PoiInfo> poiInfos;
    private SearchResultAdapter adapter;
    //经纬度
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("改预约单");
        lvSearchResult.setAdapter(adapter);
    }

    @Override
    protected void init() {
        tvCity.setText(MyApplication.getCity());
        tvAddress.setText(MyApplication.getAddress());
        latLng = new LatLng(MyApplication.getLatitude(), MyApplication.getLongitude());
        rgCause.check(R.id.rb3);
        pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        tvTime.setText(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
        mPoiSearch = PoiSearch.newInstance();
        poiInfos = new ArrayList<>();
        adapter = new SearchResultAdapter(this, poiInfos);
    }

    @Override
    protected void setListeners() {
        llMain.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                time = date.getTime();
                tvTime.setText(DateUtils.format(date, "yyyy-MM-dd HH:mm"));
            }
        });
        //输入改变的监听
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!StringUtils.isBlank(charSequence.toString())) {
                    search();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //点击搜索的监听
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    hideInputMethod();
                    search();
                }
                return false;
            }
        });

        //放置点击穿透
        llSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        //POI
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                    return;
                }

                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                    if (poiResult.getAllPoi() != null) {
                        //设置数据源
                        poiInfos.clear();
                        poiInfos.addAll(poiResult.getAllPoi());
                        //更新列表
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                //获取详情页检索结果
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
                //获取室内检索结果
            }
        });

        lvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PoiInfo poiInfo = poiInfos.get(i);
                latLng = poiInfo.location;
                tvAddress.setText(poiInfo.name + "\n" + poiInfo.address);
                cancel();
            }
        });

        rgCause.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb0:
                    case R.id.rb1:
                    case R.id.rb2:
                        hideInputMethod();
                        etOther.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rb3:
                        etOther.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
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
            case R.id.ll_main:
                hideInputMethod();
                break;
            case R.id.tv_time:
                selectTime();
                break;
            case R.id.tv_address:
                showSearch(300);
                break;
            case R.id.btn_cancel:
                cancel();
                break;
            case R.id.btn_submit:
                confirm();
                break;
        }
    }

    private void cancel() {
        etSearch.setText("");
        poiInfos.clear();
        adapter.notifyDataSetChanged();
        hideInputMethod();
        hideSearch(300);
    }

    //改预约单
    private void confirm() {
        if (rgCause.getCheckedRadioButtonId() == R.id.rb3
                && etOther.getText().toString().isEmpty()) {
            MyToast.show(getApplicationContext(), "请填写预约原因");
            return;
        }
        RadioButton rb = (RadioButton) findViewById(rgCause.getCheckedRadioButtonId());
        String cause = rb.getText().toString();
        if ("其他原因".equals(cause)) {
            cause = etOther.getText().toString();
        }
        StringBuffer sb = new StringBuffer();
        sb.append("原因:" + cause)
                .append("\n预约时间:" + tvTime.getText().toString())
                .append("\n预约地点:" + tvAddress.getText().toString());
        final String finalCause = cause;
        new AlertDialog.Builder(this)
                .setTitle("请确认预约信息")
                .setMessage(sb.toString())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //提交预约信息
                        submit(finalCause, time, latLng, tvAddress.getText().toString());
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void submit(String cause, final long time, final LatLng latLng, final String loc) {
        final Order order = MyApplication.getApp().getOrder();
        OkGo.get(Urls.APPOINTMENT_ORDER)
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.APPOINTMENT_ORDER)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("orderNo", order.getOrder_no())
                .params("jobNo", MyApplication.jobNo)
                .params("cause", cause)
                .params("time", time)
                .params("lon", latLng.longitude)
                .params("lati", latLng.latitude)
                .params("loc", loc)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        btnSubmit.setEnabled(false);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        JsonResult jsonResult = gson.fromJson(s, JsonResult.class);
                        if (jsonResult.getState() == 0) {
                            MyToast.show(getApplicationContext(), "改约成功");
                            Intent intent = new Intent();
                            intent.setAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
                            sendBroadcast(intent);
                            startActivity(new Intent(getApplicationContext(), OrderManagerActivity.class));
                        } else {
                            MyToast.show(getApplicationContext(), jsonResult.getMessage());
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
                        btnSubmit.setEnabled(true);
                    }
                });
    }

    //POI搜索
    private void search() {
        String address = etSearch.getText().toString();
        if (StringUtils.isBlank(address)) {
            MyToast.show(this, "您还没有输入地址");
            return;
        } else {
            PoiCitySearchOption option = new PoiCitySearchOption();
            option
                    .city(tvCity.getText().toString())
                    .keyword(address)
                    .pageNum(0);
            mPoiSearch.searchInCity(option);
        }

    }

    //隐藏搜索布局
    private void hideSearch(long duration) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(llSearch, "translationY", 0, llSearch.getHeight())
        );
        set.setDuration(duration).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                llSearch.setVisibility(View.INVISIBLE);
            }
        }, duration);
    }

    //显示搜索布局
    private void showSearch(long duration) {
        llSearch.setVisibility(View.VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(llSearch, "translationY", llSearch.getHeight(), 0)
        );
        set.setDuration(duration).start();
    }

    private void selectTime() {
        pvTime.setTime(new Date(time));
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        pvTime.show();
    }

    private void hideInputMethod() {
        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //隐藏输入法
        imm.hideSoftInputFromWindow(llSearch.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        super.onDestroy();
    }
}
