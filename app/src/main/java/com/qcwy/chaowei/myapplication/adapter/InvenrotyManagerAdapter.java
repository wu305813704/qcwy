package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.entity.Part;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by KouKi on 2017/1/10.
 */

public class InvenrotyManagerAdapter extends BaseAdapter {
    private Context context;
    private List<Part> parts;
    private LayoutInflater inflater;

    public InvenrotyManagerAdapter(Context context, List<Part> parts) {
        this.context = context;
        this.parts = parts;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return parts.size();
    }

    @Override
    public Part getItem(int position) {
        return parts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Part part = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_part, null);
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
        @ViewInject(R.id.tv_name)
        private TextView tvName;
        @ViewInject(R.id.tv_model)
        private TextView tvModel;
        @ViewInject(R.id.tv_quality)
        private TextView tvQuality;
        @ViewInject(R.id.tv_count)
        private TextView tvCount;
    }
}
