package com.qcwy.chaowei.myapplication.utils;

import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;

public class MyToast {
    private static Toast toast = null; //Toast的对象！
    private static String msg;

    public static synchronized void show(Context mContext, String content) {
        if (toast == null) {
            toast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static synchronized void show(Context mContext, Number info) {
        if (toast == null) {
            toast = Toast.makeText(mContext, String.valueOf(info), Toast.LENGTH_SHORT);
        } else {
            toast.setText(String.valueOf(info));
        }
        toast.show();
    }

    public static synchronized void show(Context mContext, Exception e) {
        if (e == null) {
            msg = "服务器异常";
        } else {
            msg = e.getMessage();
        }
        if (toast == null) {
            toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    //取消Toast
    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
