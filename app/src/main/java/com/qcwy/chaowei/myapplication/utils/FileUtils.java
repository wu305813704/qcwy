package com.qcwy.chaowei.myapplication.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;

public class FileUtils {
    //获取文件输出流
    public static FileWriter openFile(File path, String name) {
        FileWriter fw = null;
        if (path != null && !StringUtils.isBlank(name)) {
            File file = new File(path, name);
            //change permission to 777 for all the users
            file.setExecutable(true, false);
            file.setReadable(true, false);
            file.setWritable(true, false);
            try {
                file.createNewFile();
                //true表示不覆盖，从后面追加
                fw = new FileWriter(file, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fw;
    }

    //关闭文件流
    public static void closeWriter(FileWriter writer) {
        try {
            writer.close();
            MyLog.d("文件流已关闭");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断文件大小到达2M就移动更名
    public static void checkFile(Context context, String filePath) {
        //判断SD卡是否装载好
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File file = new File(filePath);
            if (!file.exists()) {
                MyLog.d("文件不存在");
                return;
            }
            double size = FileSizeUtils.getFileOrFilesSize(filePath, FileSizeUtils.SIZETYPE_MB);
            if (size >= 2) {
                String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
                MyLog.d(fileName);
                File fromFile = new File(filePath);
                File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsoluteFile(),
                        File.separator + "LogCache");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File toFile = new File(android.os.Environment.getExternalStorageDirectory().getPath() + File.separator + "LogCache",
                        File.separator + DateUtils.format(new Date(), "yyyy年MM月dd日HH:mm:ss") + fileName);
                copyfile(fromFile, toFile);
                fromFile.delete();
            }
        } else {
            //TODO 提示用户装SD卡

        }
    }

    public static void copyfile(File fromFile, File toFile) {
        if (!fromFile.exists()) {
            return;
        }
        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists()) {
            toFile.delete();
        }
        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); //将内容写到新文件当中
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception ex) {
            MyLog.d(ex.getMessage());
        }
    }
}
