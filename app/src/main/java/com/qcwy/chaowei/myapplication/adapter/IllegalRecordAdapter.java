package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by KouKi on 2016/12/19.
 */

public class IllegalRecordAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public IllegalRecordAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String s = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_illegal_record, null);
            holder = new ViewHolder();
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvOrderNumber.setText(s);
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_order_number)
        TextView tvOrderNumber;
        @ViewInject(R.id.tv_time)
        TextView tvTime;
        @ViewInject(R.id.tv_sum_fine)
        TextView tvSumFine;
        @ViewInject(R.id.tv_deduction)
        TextView tvDeduction;
    }
}
