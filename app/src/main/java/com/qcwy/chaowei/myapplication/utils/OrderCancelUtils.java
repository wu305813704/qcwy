package com.qcwy.chaowei.myapplication.utils;

/**
 * Created by KouKi on 2017/5/25.
 */

public class OrderCancelUtils {

    public static String getCause(String id) {
        if ("0".equals(id)) {
            return "故障已排除";
        } else if ("1".equals(id)) {
            return "已找到其他维修点";
        } else if ("2".equals(id)) {
            return "维修工告诉我需要等待很长时间";
        } else if ("3".equals(id)) {
            return "维修工态度恶劣、我取消服务";
        } else if ("4".equals(id)) {
            return "维修工拒绝提供服务";
        } else {
            return "";
        }
    }

    public static String getCancelStr(String cancelId) {
        String[] cancelIds = cancelId.split("-");
        StringBuilder sb = new StringBuilder();
        for (String id : cancelIds) {
            sb.append(getCause(id));
            sb.append("/");
        }
        return sb.toString();
    }
}
