package com.qcwy.chaowei.myapplication.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.qcwy.chaowei.myapplication.app.MyApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by KouKi on 2016/12/14 0014.
 */

/**
 * 全局捕获导常，保存到本地错误日志。日志 路径位于app安装目录的files目录下。
 */

@SuppressLint("NewApi")
public class MyCrashHandler implements Thread.UncaughtExceptionHandler {
    private static MyCrashHandler instance;
    private Context context;

    public static synchronized MyCrashHandler getInstance() {
        if (instance == null) {
            instance = new MyCrashHandler();
        }
        return instance;
    }

    public void init(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 核心方法，当程序crash 会回调此方法， Throwable中存放着错误日志
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            FileWriter fw = MyApplication.fwError;
            fw.write("线程ID：" + thread.getId() + "\n" + "时间：" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "\n错误原因：\n");
            // 错误信息
            StackTraceElement[] stackTrace = ex.getStackTrace();
            fw.write(ex.getLocalizedMessage() + "\n");
            for (int i = 0; i < stackTrace.length; i++) {
                fw.write("文件:" + stackTrace[i].getFileName() + " 错误类:"
                        + stackTrace[i].getClassName() + " 错误方法:"
                        + stackTrace[i].getMethodName() + " 错误行:"
                        + stackTrace[i].getLineNumber() + "\n");
            }
            fw.write("\n");
            fw.flush();
            //在service销毁的时候关闭
//			fw.close();
            // 上传错误信息到服务器
            // uploadToServer();
            //关闭所有activity
            for (Activity activity : MyApplication.activities) {
                activity.finish();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ex.printStackTrace();
//        //崩溃后，进程已经被系统杀死了，无法发送广播
//        //发送程序崩溃的广播
//        Intent intent = new Intent();
//        intent.setAction(GlobalContants.PROGRAM_CRASH);
//        context.sendBroadcast(intent);
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
