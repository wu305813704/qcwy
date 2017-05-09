package com.qcwy.chaowei.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

import com.baidu.location.LocationClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.utils.DateUtils;
import com.qcwy.chaowei.myapplication.utils.FileUtils;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.MyLog;
import com.qcwy.chaowei.myapplication.utils.Urls;

import java.io.File;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 远程主服务，用来与服务器交互
 *
 * @author KouKi
 */
public class MainService extends Service {
    private Thread thread;
    private static boolean isRunning = true;
    //定位SDK的核心类
    private LocationClient mLocationClient;

    @Override
    public void onCreate() {
        MyLog.d("onCreate");
//		setForeground();
        //开启自动检测日志文件大小的线程
        checkedLogFile();
        mLocationClient = MyApplication.getLocationClient();
        //开启线程
        startThread();
        super.onCreate();
    }

    //检测日志文件
    private void checkedLogFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //判断SD卡是否装载好
                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                    //自动检测log文件大小，大于2M移动到缓存目录
                    FileUtils.checkFile(getApplicationContext(), android.os.Environment.getExternalStorageDirectory().getPath()
                            + File.separator + "LogFile" + File.separator + "catchLog.log");
                    FileUtils.checkFile(getApplicationContext(), android.os.Environment.getExternalStorageDirectory().getPath()
                            + File.separator + "LogFile" + File.separator + "autoLog.log");
                    //检测完毕发送广播，提醒Application开启文件流
                    Intent intent = new Intent();
                    intent.setAction(GlobalContants.LOG_FILE_CHECKED);
                    sendBroadcast(intent);
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.d("onStartCommand");
        //START_STICKY----service被系统kill掉后，有空间的时候自动重启
        return super.onStartCommand(intent, flags, START_STICKY);
    }

    @Override
    public void onDestroy() {
        MyLog.d("onDestroy");
        //关闭文件流
        FileUtils.closeWriter(MyApplication.fwCatch);
        FileUtils.closeWriter(MyApplication.fwError);
        stopThread();
        MyLog.d("service线程已关闭");
        thread = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //开启线程
    public void startThread() {
        if (thread == null) {
            thread = new InnerThread();
        }
        isRunning = true;
        thread.start();
        MyLog.d("service线程已开启");
    }

    //关闭线程
    public void stopThread() {
        isRunning = false;
        thread = null;
    }

    //定时执行任务的线程
    class InnerThread extends Thread {
        @Override
        public void run() {
            while (isRunning) {
                double lati = MyApplication.getLatitude();
                double lon = MyApplication.getLongitude();
                if (lati == 0 || lon == 0) {
                    //休眠一秒后继续循环
                    try {
                        Thread.sleep(1 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                //具体业务
                mLocationClient.requestLocation();//重新获取坐标
                //30秒自动上传一次地理位置
                OkGo.get(Urls.UPLOAD_LOCATION)              // 请求方式和请求url
                        .tag(this)                          // 请求的 tag, 主要用于取消对应的请求
                        .cacheKey(Urls.UPLOAD_LOCATION)     // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                        .cacheMode(CacheMode.DEFAULT)       // 缓存模式，详细请看缓存介绍
                        .params("jobNo", MyApplication.jobNo)
                        .params("lati", String.valueOf(lati))
                        .params("lon", String.valueOf(lon))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                MyLog.d("维度：" + MyApplication.getLatitude() + "经度：" + MyApplication.getLongitude());
                                MyLog.d(DateUtils.format(new Date(), "yyyyMMdd HH:mm:ss"));
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
//                                if (response == null) {
//                                    MyToast.show(getApplicationContext(), "网络连接失败!");
//                                } else {
//                                    MyToast.show(getApplicationContext(), e);
//                                }
                                MyLog.d(MyApplication.jobNo);
                                //网络断开后，亮屏一次
                                //注册电源锁,灭屏后不被杀死
                                MyApplication.getApp().acquireWakeLock(PowerManager.FULL_WAKE_LOCK);
                                MyApplication.getApp().releaseWakeLock();
                            }
                        });
                try {
                    Thread.currentThread().sleep(MyApplication.space);//上传地理位置时间间隔
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
