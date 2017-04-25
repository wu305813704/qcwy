package com.qcwy.chaowei.myapplication.ui;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.qcwy.chaowei.myapplication.R;

/**
 * 捕获异常的时候弹窗
 * @author KouKi
 *
 */
public class ExceptionDialog{
	private static Context context;
	public ExceptionDialog(Context context){
		ExceptionDialog.context = context;
	}
	public void show(String s){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(s)
				.setIcon(R.mipmap.ic_launcher)
				.setPositiveButton("确定", null);
		builder.create().show();
	}
}

