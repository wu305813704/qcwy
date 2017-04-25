package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.entity.NearPartCommision;
import com.qcwy.chaowei.myapplication.entity.PartCommission;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by KouKi on 2017/1/5.
 */

public class PartCommissionAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<NearPartCommision> groupList;
    private List<List<PartCommission>> childList;
    private LayoutInflater inflater;

    public PartCommissionAdapter(Context context, List<NearPartCommision> groupList, List<List<PartCommission>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public NearPartCommision getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public PartCommission getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        NearPartCommision nearPartCommision = getGroup(groupPosition);
        GroupHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_part_commission_group, null);
            holder = new GroupHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        //判断是否已经打开列表
        if (isExpanded) {
            holder.ivStatus.setBackgroundResource(R.drawable.arrows_bottom);
        } else {
            holder.ivStatus.setBackgroundResource(R.drawable.arrows_right);
        }
        holder.tvTime.setText(nearPartCommision.getTime());
        //....
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PartCommission partCommission = getChild(groupPosition, childPosition);
        ChildHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_part_commission_child, null);
            holder = new ChildHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        holder.tvName.setText(partCommission.getName());
        //....
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        @ViewInject(R.id.tv_time)
        TextView tvTime;
        @ViewInject(R.id.tv_order_number)
        TextView tvOrderNumber;
        @ViewInject(R.id.tv_commission)
        TextView tvCommission;
        @ViewInject(R.id.iv_status)
        ImageView ivStatus;
    }

    class ChildHolder {
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_model)
        TextView tvModel;
        @ViewInject(R.id.tv_commission)
        TextView tvCommission;
    }
}
