package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.entity.Part;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by KouKi on 2017/1/1.
 */

public class AddPartAdapter extends RecyclerView.Adapter<AddPartAdapter.ViewHolder> {
    private Context context;
    private List<Part> parts;

    public AddPartAdapter(Context context, List<Part> parts) {
        this.context = context;
        this.parts = parts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_add_part, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Part part = parts.get(position);
        holder.tvName.setText("123");
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
