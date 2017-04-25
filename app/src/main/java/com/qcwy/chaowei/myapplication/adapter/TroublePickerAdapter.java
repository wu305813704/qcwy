package com.qcwy.chaowei.myapplication.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.ui.ExceptionDialog;
import com.qcwy.chaowei.myapplication.utils.BitmapUtils;
import com.qcwy.chaowei.myapplication.utils.PrintForLog;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by KouKi on 2016/12/20.
 */

public class TroublePickerAdapter extends BaseAdapter {
    private Context context;
    private List<String> photos;

    public TroublePickerAdapter(Context context, List<String> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public String getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String path = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_image, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            FileInputStream inputStream = new FileInputStream(path);
            holder.iv.setImageBitmap(BitmapUtils.loadBitmap(inputStream,80,80));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            PrintForLog.print("找不到指定文件",e,path);
            ExceptionDialog ed = new ExceptionDialog(context);
            ed.show("图片已被删除，请重新选择");
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.iv.setImageBitmap(BitmapFactory.decodeFile(path));
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.iv)
        ImageView iv;
    }
}
