package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.entity.Part;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by KouKi on 2017/1/9.
 */

public class PartPriceAdapter extends BaseAdapter {
    private Context context;
    private List<Part> parts;
    private LayoutInflater inflater;

    public PartPriceAdapter(Context context, List<Part> parts) {
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
            convertView = inflater.inflate(R.layout.item_part_price, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context)
                .load(Urls.BASE + part.getImage())
                .placeholder(R.drawable.loading)
                .crossFade()//淡入淡出
                .into(holder.ivPart);
        holder.tvPart.setText(part.getName());
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.iv_part)
        ImageView ivPart;
        @ViewInject(R.id.tv_part)
        TextView tvPart;
    }
}
