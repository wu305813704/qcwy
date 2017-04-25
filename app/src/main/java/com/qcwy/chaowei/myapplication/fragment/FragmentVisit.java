package com.qcwy.chaowei.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.VisitAdapter;
import com.qcwy.chaowei.myapplication.entity.Visit;
import com.qcwy.chaowei.myapplication.utils.DateUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentVisit extends BaseFragment implements View.OnClickListener{
    @ViewInject(R.id.lv_visit)
    private ListView lvVisit;
    //日期布局
    @ViewInject(R.id.ll_date)
    private LinearLayout llDate;
    //日期文本
    @ViewInject(R.id.tv_date)
    private TextView tvDate;

    private List<Visit> visits;
    private VisitAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_visit, null);
            x.view().inject(this, rootView);
            initData();
            setListeners();
        }
        return rootView;
    }

    private void initData() {
        //初始化时间输入框
        tvDate.setText(DateUtils.format(new Date(), "yyyy年MM月"));
        visits = new ArrayList<>();
        visits.add(new Visit());
        visits.add(new Visit());
        visits.add(new Visit());
        visits.add(new Visit());
        visits.add(new Visit());
        adapter = new VisitAdapter(getContext(), visits);
        lvVisit.setAdapter(adapter);
    }
    public void setListeners(){
        llDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date:
                //弹出日期选择
                DateUtils.selectDate1(getContext(), tvDate);
                break;
        }
    }
}
