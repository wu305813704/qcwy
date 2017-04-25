package com.qcwy.chaowei.myapplication.utils;

import android.util.Log;

public class MyLog {
	private static final String TAG = "TAG";
	private static final boolean isPublish = false;
	public static void i(Number log){
		if(!isPublish){
			Log.i(TAG, String.valueOf(log));
		}
	}
	public static void i(String log){
		if(!isPublish){
			Log.i(TAG, log);
		}
	}
	
	public static void d(Number log){
		if(!isPublish){
			Log.d(TAG, String.valueOf(log));
		}
	}
	public static void d(String log){
		if(!isPublish){
			Log.d(TAG, log);
		}
	}
	public static void v(Number log){
		if(!isPublish){
			Log.v(TAG, String.valueOf(log));
		}
	}
	public static void v(String log){
		if(!isPublish){
			Log.v(TAG, log);
		}
	}
	public static void e(Number log){
		if(!isPublish){
			Log.e(TAG, String.valueOf(log));
		}
	}
	public static void e(String log){
		if(!isPublish){
			Log.e(TAG, log);
		}
	}
	
}
