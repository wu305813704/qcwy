package com.qcwy.chaowei.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.InvenrotyManagerAdapter;
import com.qcwy.chaowei.myapplication.entity.Part;
import com.qcwy.chaowei.myapplication.ui.PickingActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KouKi on 2017/1/9.
 */

public class FragmentInvenrotyManager extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.lv_part)
    private ListView lvPart;
    @ViewInject(R.id.btn_picking)
    private Button btnPicking;
    private List<Part> parts;
    private InvenrotyManagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_inventory_manager, null);
            x.view().inject(this, rootView);
            init();
            setListeners();
            lvPart.setAdapter(adapter);
        }
        return rootView;
    }

    private void init() {
        parts = new ArrayList<>();
        parts.add(new Part());
        parts.add(new Part());
        parts.add(new Part());
        parts.add(new Part());
        parts.add(new Part());
        adapter = new InvenrotyManagerAdapter(getContext(), parts);
    }

    private void setListeners() {
        btnPicking.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_picking:
                //领料
                startActivity(new Intent(getContext(), PickingActivity.class));
                break;
        }
    }
}
