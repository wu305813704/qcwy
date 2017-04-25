package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.entity.OrderDetail;
import com.qcwy.chaowei.myapplication.entity.WxUser;
import com.qcwy.chaowei.myapplication.utils.DateUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.List;

/**
 * Created by KouKi on 2016/12/30.
 */
public class CurrentOrderAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orders;
    private LayoutInflater inflater;

    public CurrentOrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Order getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);
        OrderDetail orderDetail = order.getOrderDetail();
        WxUser wxUser = order.getWxUser();
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_current_order, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(wxUser.getNickname());
        if (order.getState() == 4) {
            //预约单，设置预约时间
            holder.tvTime.setText(DateUtils.format(new Date(order.getAppointment_time()), "HH:mm"));
        } else {
            Date date = new Date(order.getSend_time());
            String time = DateUtils.format(date, "HH:mm");
            holder.tvTime.setText(time);
        }
        holder.tvDetail.setText(order.getFaultDescription());
        holder.tvAddress.setText(orderDetail.getLoc());
        holder.tvDistance.setText("约" + order.getDistance());
        switch (order.getState()) {
            case 1:
                holder.tvState.setText("未开始");
                holder.tvState.setTextColor(Color.BLACK);
                break;
            case 2:
            case 7:
            case 8:
                holder.tvState.setText("进行中");
                holder.tvState.setTextColor(Color.GREEN);
                break;
            case 3:
                holder.tvState.setText("暂停中");
                holder.tvState.setTextColor(Color.RED);
                break;
            case 4:
                holder.tvState.setText("预约单");
                holder.tvState.setTextColor(Color.YELLOW);
                break;
            case 6:
                holder.tvState.setText("改派中");
                holder.tvState.setTextColor(Color.BLUE);
                break;
            case 9:
                holder.tvState.setText("等待验收");
                holder.tvState.setTextColor(Color.YELLOW);
                break;
            case 10:
                holder.tvState.setText("等待付款");
                holder.tvState.setTextColor(Color.YELLOW);
                break;
        }
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_time)
        TextView tvTime;
        @ViewInject(R.id.tv_distance)
        TextView tvDistance;
        @ViewInject(R.id.tv_address)
        TextView tvAddress;
        @ViewInject(R.id.tv_detail)
        TextView tvDetail;
        @ViewInject(R.id.tv_state)
        TextView tvState;
    }
}
