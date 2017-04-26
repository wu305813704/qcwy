package com.qcwy.chaowei.myapplication.utils;

/**
 * Created by KouKi on 2017/2/19.
 */
public class Urls {
    //url
    /**
     * baseUrl
     */
    public static final String BASE = "http://1699y0i558.iask.in/qcwy/";
    /**
     * 登录url
     */
    public static final String LOGIN = BASE + "app/login";
    /**
     * 上传位置
     */
    public static final String UPLOAD_LOCATION = BASE + "app/updateLoc";
    /**
     * 抢单
     */
    public static final String RUSH_ORDER = BASE + "app/rushOrder";
    /**
     * 获取抢单列表
     */
    public static final String GET_RUSH_ORDERS = BASE + "app/getRushOrders";
    /**
     * 获取当前订单列表
     */
    public static final String GET_CURRENT_ORDERS = BASE + "app/getCurrentOrders";
    /**
     * 开始订单url
     */
    public static final String START_ORDERS = BASE + "app/startOrder";
    /**
     * 暂停订单url
     */
    public static final String PAUSE_ORDER = BASE + "app/pauseOrder";
    /**
     * 通过工号查询工程师姓名
     */
    public static final String GET_ENGINEER_NAME = BASE + "app/getUsername";
    /**
     * 改派订单给指定工程师
     */
    public static final String REASSIGNMENT_TO_ENGINEER = BASE + "app/reassignmentToEngineer";
    /**
     * 改派订单给后台
     */
    public static final String REASSIGNMENT_TO_BACKGROUND = BASE + "app/reassignmentToBackground";
    /**
     * 改预约单
     */
    public static final String APPOINTMENT_ORDER = BASE + "app/appointmentOrder";
    /**
     * 获取改派人姓名
     */
    public static final String GET_REASSIGNMENT_NAME = BASE + "app/getReassignmentName";
    /**
     * 获取工程师的订单消息
     */
    public static final String GET_ORDER_MESSAGES = BASE + "app/getOrderMessage";
    /**
     * 根据单号获取订单
     */
    public static final String GET_ORDER_BY_ORDER_NO = BASE + "app/getOrderByOrderNo";
    /**
     * 获取改派详情
     */
    public static final String GET_REASSIGNMENT_ORDER = BASE + "app/getReassignment";
    /**
     * 接受改派订单
     */
    public static final String ACCEPT_ORDER = BASE + "app/acceptOrder";
    /**
     * 拒绝改派订单
     */
    public static final String REFUSE_ORDER = BASE + "app/refuseOrder";
    /**
     * 通过分类查询零件
     */
    public static final String GET_PART_BY_CLASSIFY = BASE + "getPartsByClassify";
    /**
     * 获取员工仓所有零件
     */
    public static final String GET_PARTS = BASE + "app/getParts";
    /**
     * 根据零件id集合获取零件详情
     */
    public static final String GET_PARTS_BY_IDS = BASE + "getPartsByIds";
    /**
     * 确认故障
     */
    public static final String CONFIRM_TROUBLE = BASE + "app/confirmTrouble";
    /**
     * 维修完成
     */
    public static final String COMPELETE = BASE + "app/complete";
    /**
     * 线下付款
     */
    public static final String OFFLINE_PAY = BASE + "app/offlinePay";
    /**
     * 获取历史订单数据
     */
    public static final String GET_HISTORY_ORDERS = BASE + "app/getHistoryOrders";
    /**
     * 获取历史订单列表
     */
    public static final String GET_HISTORY_ORDER_LIST = BASE + "app/getHistoryOrderList";
    /**
     * 获取工程师确认故障
     */
    public static final String GET_REAL_ORDER_FAULT = BASE + "app/getRealOrderFault";
    /**
     * 获取订单零件
     */
    public static final String GET_ORDER_PART = BASE + "app/getOrderPart";
    /**
     * 获取预约单信息
     */
    public static final String GET_APPOINTMENT = BASE + "app/getAppointment";
    /**
     * 获取预约单信息
     */
    public static final String GET_ORDER_COUNT_RANK = BASE + "app/getOrderCountRank";
    /**
     * 获取当月订单数
     */
    public static final String GET_ORDER_COUNT = BASE + "app/getOrderCount";

}
