package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.utils.DateUtils;
import com.qcwy.chaowei.myapplication.utils.MyItemClickListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.List;

/**
 * Created by KouKi on 2017/3/14.
 */

public class HistoryOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //布局类型
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private Context context;
    private List<Order> orders;
    private LayoutInflater inflater;
    private onLoadCallback mCallback;
    private boolean canLoad;
    private MyItemClickListener myItemClickListener;

    public HistoryOrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        inflater = LayoutInflater.from(context);
        canLoad = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_history_order, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            x.view().inject(holder, view);
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.footer_view, parent, false);
            FooterViewHolder holder = new FooterViewHolder(view);
            x.view().inject(holder, view);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            Order order = orders.get(position);
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.tvOrderNo.setText(String.valueOf(order.getOrder_no()));
            itemHolder.tvTime.setText(DateUtils.format(new Date(order.getSend_time()), "yyyy-MM-dd HH:mm:ss"));
            switch (order.getType()) {
                case 0:
                    itemHolder.tvOrderModel.setText("普通订单");
                    break;
                case 1:
                    itemHolder.tvOrderModel.setText("售后订单");
                    break;
            }
            if (myItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myItemClickListener.onItemClick(v, position);
                    }
                });
            }
        } else if (holder instanceof FooterViewHolder) {
            if (mCallback != null) {
                if (canLoad) {
                    mCallback.onLoad();
                    ((FooterViewHolder) holder).pbLoading.setVisibility(View.VISIBLE);
                    ((FooterViewHolder) holder).tvFooter.setText("努力加载中...");
                } else {
                    ((FooterViewHolder) holder).pbLoading.setVisibility(View.GONE);
                    ((FooterViewHolder) holder).tvFooter.setText("已加载全部");
                }
            }
        }
    }

    // RecyclerView的count设置为数据总条数+ 1（footerView）
    @Override
    public int getItemCount() {
        return orders.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.tv_order_no)
        TextView tvOrderNo;
        @ViewInject(R.id.tv_time)
        TextView tvTime;
        @ViewInject(R.id.tv_order_tpye)
        TextView tvOrderModel;

        public ItemViewHolder(final View itemView) {
            super(itemView);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.pb_loading)
        ProgressBar pbLoading;
        @ViewInject(R.id.tv_footer)
        TextView tvFooter;

        public FooterViewHolder(View footerView) {
            super(footerView);
        }
    }

    //回调接口，用于回调加载数据的方法
    public interface onLoadCallback {
        void onLoad();
    }

    //对外暴露设置接口方法
    public void setLoadCallback(onLoadCallback callback) {
        this.mCallback = callback;
    }

    public void setCanLoad(boolean canLoad) {
        this.canLoad = canLoad;
    }

    public void setOnItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }
}
