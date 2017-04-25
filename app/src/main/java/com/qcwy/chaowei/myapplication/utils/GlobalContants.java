package com.qcwy.chaowei.myapplication.utils;

public class GlobalContants {
    //广播
    /**
     * 程序崩溃
     */
    public static final String PROGRAM_CRASH = "PROGRAM_CRASH";
    /**
     * 主界面被销毁
     */
    public static final String MAIN_ACTIVITY_DESTROY = "MAIN_ACTIVITY_DESTROY";
    /**
     * 日志文件检测完毕
     */
    public static final String LOG_FILE_CHECKED = "LOG_FILE_CHECKED";
    /**
     * 定位完成后
     */
    public static final String LOCATION_COMPELETE = "LOCATION_COMPELETE";
    /**
     * 显示tips
     */
    public static final String SHOW_TIPS = "SHOW_TIPS";
    /**
     * 隐藏tips
     */
    public static final String HIDE_TIPS = "HIDE_TIPS";
    /**
     * 更新待抢订单列表
     */
    public static final String RECEIVER_UPDATE_RUSH_ORDERS = "RECEIVER_UPDATE_RUSH_ORDERS";
    /**
     * 更新当前订单列表
     */
    public static final String RECEIVER_UPDATE_CURRENT_ORDERS = "RECEIVER_UPDATE_CURRENT_ORDERS";
    /**
     * 接收到服务器推送的消息弹窗
     */
    public static final String RECEIVER_GET_MESSAGE = "RECEIVER_GET_MESSAGE";

    //intent
    /**
     * Extra--key  INTENT_SKIP 跳转标记
     */
    public static final String INTENT_SKIP = "INTENT_SKIP";
    /**
     * 通过PendingIntent启动activity
     * 跳转到系统消息
     */
    public static final String INTENT_SYSTEM_MESSAGE = "INTENT_SYSTEM_MESSAGE";
    /**
     * 通过PendingIntent启动activity
     * 跳转到订单消息
     */
    public static final String INTENT_ORDER_MESSAGE = "INTENT_ORDER_MESSAGE";
    /**
     * 通过Intent启动activity
     * 跳转到待抢订单
     */
    public static final String INTENT_RUSH_ORDER = "INTENT_RUSH_ORDER";
    /**
     * 通过Intent启动activity
     * 跳转到当前订单
     */
    public static final String INTENT_CURRENT_ORDER = "INTENT_CURRENT_ORDER";
    /**
     * 通过Intent启动activity
     * 跳转到历史订单
     */
    public static final String INTENT_HISTORY_ORDER = "INTENT_HISTORY_ORDER";

    //key
    /**
     * 抢单key
     */
    public static final String KEY_RUSH_ORDER = "KEY_RUSH_ORDER";

    //startActivityForResult
    /**
     * 添加零件
     */
    public static final int RESULT_CODE_ADD_PART = 100;
    /**
     * 添加零件后返回result(零件id)
     */
    public static final String KEY_ADD_PART = "KEY_ADD_PART";
    /**
     * 添加零件后返回result(零件数量)
     */
    public static final String KEY_ADD_PART_COUNT = "KEY_ADD_PART_COUNT";
}
