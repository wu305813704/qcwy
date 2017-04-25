package com.qcwy.chaowei.myapplication.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.IllegalRecordAdapter;
import com.qcwy.chaowei.myapplication.utils.CommonCallback;
import com.qcwy.chaowei.myapplication.utils.DateUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ContentView(R.layout.activity_illegal_record)
public class IllegalRecordActivity extends BaseActivity {
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
    //自定义ListView
    @ViewInject(R.id.lv_record)
    private ListView lvRecord;

    private IllegalRecordAdapter adapter;
    private List<String> list;
    //当前时间
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("违规记录");
        //初始化时间输入框
        tvStartDate.setText(DateUtils.format(new Date(), "yyyy-MM-dd"));
        tvEndDate.setText(DateUtils.format(new Date(), "yyyy-MM-dd"));
        //设置静态变量值，防止其他地方改动
        DateUtils.startTime = System.currentTimeMillis();
        DateUtils.endTime = System.currentTimeMillis();
    }

    @Override
    protected void init() {
        //设置日期选择器的日期为当前日期
        date = new Date();
        tvStartDate.setText(DateUtils.format(date, "yyyy-MM-dd"));
        tvEndDate.setText(DateUtils.format(date, "yyyy-MM-dd"));
        list = new ArrayList<>();
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        adapter = new IllegalRecordAdapter(this, list);
        lvRecord.setAdapter(adapter);
    }

    @Override
    protected void setListeners() {
        llStartDate.setOnClickListener(this);
        llEndDate.setOnClickListener(this);
        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(IllegalRecordActivity.this, IllegalDetailActivity.class);
//                intent.putExtra("违规记录")
                startActivity(intent);
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
            case R.id.ll_start_date:
                //弹出开始日期选择
                DateUtils.selectDate(this, tvStartDate, new CommonCallback() {
                    @Override
                    public void onSeccess() {

                    }
                });
                break;
            case R.id.ll_end_date:
                //弹出结束日期选择
                DateUtils.selectDate(this, tvEndDate, new CommonCallback() {
                    @Override
                    public void onSeccess() {

                    }
                });
                break;
        }
    }
}
