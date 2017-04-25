package com.qcwy.chaowei.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.PickingAdapter;
import com.qcwy.chaowei.myapplication.entity.Part;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KouKi on 2017/1/10.
 */

public class FragmentPicking0 extends BaseFragment {
    @ViewInject(R.id.lv_part)
    private ListView lvPart;
    private List<Part> parts;
    private PickingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_picking, null);
            x.view().inject(this, rootView);
            init();
            setListeners();
            lvPart.setAdapter(adapter);
        }
        return rootView;
    }

    private void init() {
        parts = new ArrayList<>();
        adapter = new PickingAdapter(getContext(), parts);
    }

    private void setListeners() {
    }
}
