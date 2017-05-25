package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.entity.WarehouseEmployee;
import com.qcwy.chaowei.myapplication.utils.MyToast;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

/**
 * Created by KouKi on 2016/12/23.
 */

public class PartAdapter extends BaseAdapter {
    private Context context;
    private List<WarehouseEmployee> list;
    private HashMap<Integer, Boolean> isSelected;

    public PartAdapter(Context context, List<WarehouseEmployee> list) {
        this.context = context;
        this.list = list;
        initData();
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return this.isSelected;
    }

    private void initData() {
        isSelected = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public WarehouseEmployee getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        WarehouseEmployee warehouseEmployee = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_add_part, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSelected.put(position, isChecked);
            }
        });
        holder.tvName.setText(warehouseEmployee.getName());
        holder.tvModel.setText(warehouseEmployee.getModle());
        holder.tvCount.setText(String.valueOf(warehouseEmployee.getCount()));
        holder.cb.setChecked(isSelected.get(position));
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_model)
        TextView tvModel;
        @ViewInject(R.id.tv_count)
        TextView tvCount;
        @ViewInject(R.id.cb_checked)
        CheckBox cb;
    }
}
