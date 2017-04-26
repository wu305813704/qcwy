package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.Rank;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by KouKi on 2017/1/6.
 */

public class RankAdapter extends BaseAdapter {
    private Context context;
    private List<Rank> rankList;
    private LayoutInflater inflater;

    public RankAdapter(Context context, List<Rank> rankList) {
        this.context = context;
        this.rankList = rankList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return rankList.size();
    }

    @Override
    public Rank getItem(int position) {
        return rankList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Rank rank = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_rank, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //如果排名id与自己id相同，则变色
        if (MyApplication.jobNo.equals(rank.getJob_no())) {
            holder.tvRank.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
            holder.tvName.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
            holder.tvBonus.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
        }
        holder.tvName.setText(rank.getName());
        holder.tvRank.setText("第" + rank.getRank() + "名");
        holder.tvBonus.setText((int) rank.getScore() + "单");
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_rank)
        TextView tvRank;
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_bonus)
        TextView tvBonus;
    }
}
