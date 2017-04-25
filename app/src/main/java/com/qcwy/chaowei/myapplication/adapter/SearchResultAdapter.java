package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.qcwy.chaowei.myapplication.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by KouKi on 2017/1/24.
 */

public class SearchResultAdapter extends BaseAdapter {
    private Context context;
    private List<PoiInfo> poiInfos;
    private LayoutInflater inflater;

    public SearchResultAdapter(Context context, List<PoiInfo> poiInfos) {
        this.context = context;
        this.poiInfos = poiInfos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return poiInfos.size();
    }

    @Override
    public PoiInfo getItem(int i) {
        return poiInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        PoiInfo poiAddrInfo = getItem(i);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_search, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(poiAddrInfo.name);
        holder.tvAddress.setText(poiAddrInfo.address);
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_address)
        TextView tvAddress;
    }
}

