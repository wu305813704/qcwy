package com.qcwy.chaowei.myapplication.utils;


import com.qcwy.chaowei.myapplication.app.MyApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * 在catch中使用此类来打印错误日志
 */
public class PrintForLog {
    public static void print(String line, Exception exception, Object... params) {
        try {
            //true表示不覆盖，从后面追加内容
            FileWriter fw = MyApplication.fwCatch;
            fw.write("时间：" + DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss") + "\n错误原因：\n" + line + "\n");
            // 错误信息
            StackTraceElement[] stackTrace = exception.getStackTrace();
            fw.write(exception.getMessage() + "\n");
            for (int i = 0; i < stackTrace.length; i++) {
                fw.write("文件:" + stackTrace[i].getFileName() + " 错误类:"
                        + stackTrace[i].getClassName() + " 错误方法:"
                        + stackTrace[i].getMethodName() + " 错误行:"
                        + stackTrace[i].getLineNumber() + "\n");
            }
            fw.write("\n传入的值为:");
            for (Object obj : params) {
                fw.write("[" + obj.toString() + "] ");
            }
            fw.write("\n");
            fw.flush();
//          在service销毁的时候关闭
//          fw.close();
//          上传错误信息到服务器
//          uploadToServer();
        } catch (IOException e) {
            MyLog.e("load file failed...");
        }
        exception.printStackTrace();
    }
}