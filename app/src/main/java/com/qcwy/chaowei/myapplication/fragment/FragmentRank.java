package com.qcwy.chaowei.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.RankAdapter;
import com.qcwy.chaowei.myapplication.entity.Rank;
import com.qcwy.chaowei.myapplication.utils.CommonCallback;
import com.qcwy.chaowei.myapplication.utils.DateUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentRank extends BaseFragment implements View.OnClickListener {
    //日期布局
    @ViewInject(R.id.ll_date)
    private LinearLayout llDate;
    //日期文本
    @ViewInject(R.id.tv_date)
    private TextView tvDate;
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
        Rank rank = new Rank();
        rank.setId("007");
        rank.setRank("第一名");
        Rank rank1 = new Rank();
        rank1.setRank("第二名");
        Rank rank2 = new Rank();
        rank2.setRank("第三名");
        Rank rank3 = new Rank();
        rank3.setRank("第四名");
        Rank rank4 = new Rank();
        rank4.setRank("第五名");
        Rank rank5 = new Rank();
        rank5.setRank("第六名");
        rankList.add(rank);
        rankList.add(rank1);
        rankList.add(rank2);
        rankList.add(rank3);
        rankList.add(rank4);
        rankList.add(rank5);
        adapter = new RankAdapter(getContext(), rankList);
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

                    }
                });
                break;
        }
    }
}
