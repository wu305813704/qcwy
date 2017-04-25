package com.qcwy.chaowei.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.utils.DateUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

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
    }
}
