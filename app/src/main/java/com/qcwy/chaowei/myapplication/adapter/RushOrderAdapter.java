package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.entity.OrderDetail;
import com.qcwy.chaowei.myapplication.entity.WxUser;
import com.qcwy.chaowei.myapplication.utils.DateUtils;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.entity.JsonResult;
import com.qcwy.chaowei.myapplication.utils.MyLog;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by KouKi on 2016/12/29.
 */

public class RushOrderAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orders;
    private LayoutInflater inflater;

    public RushOrderAdapter(Context context, List<Order> orders) {
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
        final Order order = getItem(position);
        OrderDetail orderDetail = order.getOrderDetail();
        WxUser wxUser = order.getWxUser();
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_rush_order, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(wxUser.getNickname());
        try {
            Date date = DateUtils.parse(orderDetail.getSend_time(), "yyyy-MM-dd HH:mm:ss.0");
            String time = DateUtils.format(date, "HH:mm");
            holder.tvTime.setText(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvDetail.setText(order.getFaultDescription());
        holder.tvAddress.setText(orderDetail.getLoc());
        holder.tvDistance.setText("约" + order.getDistance());
        int remainTime = order.getRemainTime();
        if (remainTime > 0) {
            holder.tvRemainTime.setVisibility(View.VISIBLE);
            holder.btnRushOrder.setText("抢单");
            holder.btnRushOrder.setEnabled(true);
            holder.tvRemainTime.setText(remainTime + "s");
        } else {
            holder.tvRemainTime.setVisibility(View.INVISIBLE);
            holder.btnRushOrder.setText("已失效");
            holder.btnRushOrder.setEnabled(false);
        }
        holder.btnRushOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnRushOrder.setEnabled(false);
                //抢单
                OkGo.get(Urls.RUSH_ORDER)
                        .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                        .cacheKey(Urls.RUSH_ORDER)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                        .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                        .params("jobNo", MyApplication.jobNo)
                        .params("orderNo", order.getOrder_no())
                        .params("lon", String.valueOf(MyApplication.getLongitude()))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Gson gson = new Gson();
                                JsonResult jsonResult = gson.fromJson(s, JsonResult.class);
                                if (jsonResult.getState() == 0) {
                                    order.setState(1);
                                    //抢单成功
                                    new AlertDialog.Builder(context)
                                            .setTitle("提示")
                                            .setMessage("恭喜你，抢单成功!请到当前订单查看详情。")
                                            .setPositiveButton("确定", null)
                                            .create()
                                            .show();
                                    //当前订单中添加此订单
                                    MyApplication.getCurrentOrders().add(order);
                                    //发送广播更新当前订单
                                    Intent intent = new Intent();
                                    intent.setAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
                                    context.sendBroadcast(intent);
                                } else {
                                    new AlertDialog.Builder(context)
                                            .setTitle("提示")
                                            .setMessage(jsonResult.getMessage())
                                            .setPositiveButton("确定", null)
                                            .create()
                                            .show();
                                    holder.btnRushOrder.setText("已被抢");
                                }
                                holder.btnRushOrder.setEnabled(true);
                                //抢成功或者订单已被抢，待抢订单中移除此订单
                                MyApplication.getRushOrders().remove(order);
                                //发送广播更新待抢订单
                                Intent intent2 = new Intent();
                                intent2.setAction(GlobalContants.RECEIVER_UPDATE_RUSH_ORDERS);
                                context.sendBroadcast(intent2);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                if (response == null) {
                                    MyToast.show(context, "网络连接失败!");
                                } else {
                                    MyToast.show(context, e);
                                }
                            }
                        });
            }
        });
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
        @ViewInject(R.id.btn_rush_order)
        Button btnRushOrder;
        @ViewInject(R.id.tv_remain_time)
        TextView tvRemainTime;
    }
}
