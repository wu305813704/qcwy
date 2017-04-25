package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.entity.Visit;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by KouKi on 2017/1/6.
 */

public class VisitAdapter extends BaseAdapter {
    private Context context;
    private List<Visit> visits;
    private LayoutInflater inflater;

    public VisitAdapter(Context context, List<Visit> visits) {
        this.context = context;
        this.visits = visits;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return visits.size();
    }

    @Override
    public Visit getItem(int position) {
        return visits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Visit visit = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_visit, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //TODO
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_date)
        private TextView tvDate;
        @ViewInject(R.id.tv_times)
        private TextView tvTimes;
        @ViewInject(R.id.tv_cost)
        private TextView tvCost;
    }
}
