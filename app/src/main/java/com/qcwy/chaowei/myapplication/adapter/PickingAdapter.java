package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.Cart;
import com.qcwy.chaowei.myapplication.entity.Part;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by KouKi on 2017/1/10.
 */

public class PickingAdapter extends BaseAdapter {
    private Context context;
    private List<Part> parts;
    private LayoutInflater inflater;
    private Cart cart;

    public PickingAdapter(Context context, List<Part> parts) {
        this.context = context;
        this.parts = parts;
        inflater = LayoutInflater.from(context);
        cart = MyApplication.getApp().getCart();
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
        final Part part = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_picking, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //TODO
        holder.ibSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(holder.etCount.getText().toString());
                if (count <= 0) {
                    return;
                } else {
                    holder.etCount.setText(String.valueOf(--count));
                    cart.subtractPart(part);
                }
            }
        });

        holder.ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(holder.etCount.getText().toString());
                holder.etCount.setText(String.valueOf(++count));
                cart.addPart(part);
            }
        });
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_model)
        TextView tvModel;
        @ViewInject(R.id.tv_quality)
        TextView tvQuality;
        @ViewInject(R.id.ib_subtract)
        ImageButton ibSubtract;
        @ViewInject(R.id.et_count)
        EditText etCount;
        @ViewInject(R.id.ib_add)
        ImageButton ibAdd;
    }
}
