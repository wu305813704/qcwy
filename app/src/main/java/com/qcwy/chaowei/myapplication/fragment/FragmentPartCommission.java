package com.qcwy.chaowei.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.PartCommissionAdapter;
import com.qcwy.chaowei.myapplication.entity.NearPartCommision;
import com.qcwy.chaowei.myapplication.entity.PartCommission;
import com.qcwy.chaowei.myapplication.utils.DateUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentPartCommission extends BaseFragment implements View.OnClickListener {
    //日期布局
    @ViewInject(R.id.ll_date)
    private LinearLayout llDate;
    //日期文本
    @ViewInject(R.id.tv_date)
    private TextView tvDate;
    @ViewInject(R.id.lv_part_commission)
    private ExpandableListView lvPartCommission;
    private List<NearPartCommision> groupList;
    private List<List<PartCommission>> childList;
    private PartCommissionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_part_commission, null);
            x.view().inject(this, rootView);
            initData();
            lvPartCommission.setAdapter(adapter);
            setListeners();
        }
        return rootView;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            //进行网络请求
            //TODO
//            MyToast.show(getContext(), "show");
        } else {
            //取消网络请求
            //TODO
        }
    }

    private void initData() {
        //初始化时间输入框
        tvDate.setText(DateUtils.format(new Date(), "yyyy年MM月"));
        //取消左边指示器
        lvPartCommission.setGroupIndicator(null);
        //父控件的list
        groupList = new ArrayList<>();
        childList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            NearPartCommision nearPartCommision = new NearPartCommision();
            nearPartCommision.setTime("201607" + i);
            groupList.add(nearPartCommision);
            List<PartCommission> partCommissions = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                PartCommission partCommission = new PartCommission();
                partCommission.setName("超威" + j);
                partCommissions.add(partCommission);
            }
            childList.add(partCommissions);
        }
        adapter = new PartCommissionAdapter(getContext(), groupList, childList);
        //默认打开第一个选项
//        lvPartCommission.expandGroup(0, true);
    }

    private void setListeners() {
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
