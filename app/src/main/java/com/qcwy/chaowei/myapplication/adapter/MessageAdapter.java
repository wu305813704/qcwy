package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.entity.OrderMessage;
import com.qcwy.chaowei.myapplication.utils.DateUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by KouKi on 2017/2/23.
 */

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private List<OrderMessage> messages;
    private LayoutInflater inflater;

    public MessageAdapter(Context context, List<OrderMessage> messages) {
        this.context = context;
        this.messages = messages;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public OrderMessage getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderMessage msg = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_message, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (msg.getType()) {
            case 0://抢单消息
                holder.tvTitle.setText("抢单消息");
                holder.tvContent.setText("抢单成功！订单编号：" + msg.getOrder_no());
                break;
            case 1://收到的改派订单消息
                holder.tvTitle.setText("收到的改派订单");
                holder.tvContent.setText("收到改派订单！订单编号：" + msg.getOrder_no());
                break;
            case 2://发起的改派订单消息
                holder.tvTitle.setText("发起的改派订单");
                holder.tvContent.setText("您发起了一个改派订单！订单编号：" + msg.getOrder_no());
        }
        try {
            Date date = DateUtils.parse(msg.getSend_time(), "yyyy-MM-dd HH:mm:ss.0");
            String time = DateUtils.format(date, "yy-MM-dd HH:mm:ss");
            holder.tvTime.setText(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_title)
        TextView tvTitle;
        @ViewInject(R.id.tv_time)
        TextView tvTime;
        @ViewInject(R.id.tv_content)
        TextView tvContent;
    }
}
