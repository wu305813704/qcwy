package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.entity.PartDetail;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KouKi on 2017/3/8.
 */

public class FaultPartAdapter extends RecyclerView.Adapter<FaultPartAdapter.ViewHolder> {
    private Context context;
    private List<PartDetail> parts;
    private List<Integer> count;
    private LayoutInflater inflater;

    public FaultPartAdapter(Context context, List<PartDetail> parts, List<Integer> count) {
        this.context = context;
        this.parts = parts;
        this.count = count;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public FaultPartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_part_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        x.view().inject(holder, view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FaultPartAdapter.ViewHolder holder, final int position) {
        final PartDetail partDetail = parts.get(position);
        final int maxCount = count.get(position);
        holder.spList = new ArrayList<>();
        holder.spList.add("全新");
        if (partDetail.getPrice_new() != 0) {
            holder.spList.add("以旧换新");
        }
        if (partDetail.getPrice_old() != 0) {
            holder.spList.add("以旧换旧");
        }
        holder.spAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, holder.spList);

        Glide.with(context)
                .load(Urls.BASE + partDetail.getImage())
                .placeholder(R.drawable.loading)
                .crossFade()//淡入淡出
                .into(holder.iv);
        holder.tvName.setText(partDetail.getModel());
        holder.spPartType.setAdapter(holder.spAdapter);
        holder.spPartType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                partDetail.setType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.tvPrice.setText(String.valueOf(partDetail.getPrice()));
        holder.tvCount.setText("1");
        holder.btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(holder.tvCount.getText().toString());
                if (count <= 1) {
                    parts.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                } else {
                    holder.tvCount.setText(String.valueOf(--count));
                    partDetail.setCount(count);
                }
            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(holder.tvCount.getText().toString());
                if (count >= maxCount) {
                    MyToast.show(context, "你只有" + count + "个该零件");
                    return;
                }
                holder.tvCount.setText(String.valueOf(++count));
                partDetail.setCount(count);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.iv)
        ImageView iv;
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.sp_part_type)
        Spinner spPartType;
        @ViewInject(R.id.tv_total_price)
        TextView tvPrice;
        @ViewInject(R.id.tv_count)
        TextView tvCount;
        @ViewInject(R.id.btn_subtract)
        Button btnSubtract;
        @ViewInject(R.id.btn_add)
        Button btnAdd;
        ArrayAdapter<String> spAdapter;
        List<String> spList;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
