package com.qcwy.chaowei.myapplication.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.entity.OrderDetail;

import java.util.Date;

/**
 * Created by KouKi on 2017/2/21.
 */

public class OrderSettingUtils {
    //可抢时间
    private static final int totalTime = 120;

    public static void setOrderDetail(Order order) {
        OrderDetail orderDetail = order.getOrderDetail();
        //计算剩余时间
        Date date = new Date(order.getSend_time());
        int remainTime = totalTime - (int) (System.currentTimeMillis() - date.getTime()) / 1000;
        order.setRemainTime(remainTime);
        MyLog.d(remainTime);
        //计算距离
        LatLng latLngWx = new LatLng(Double.valueOf(orderDetail.getLati()), Double.valueOf(orderDetail.getLon()));
        LatLng latLngApp = new LatLng(MyApplication.getLatitude(), MyApplication.getLongitude());
        double distance = DistanceUtil.getDistance(latLngWx, latLngApp);
//        double distance = GeoUtils.getShortDistance(latLngWx.longitude, latLngWx.latitude, latLngApp.longitude, latLngApp.latitude);
        String strDistance;
        if (distance < 1000) {
            strDistance = String.valueOf(Math.round(distance)) + "米";
        } else {
            strDistance = String.valueOf(Math.round(distance / 1000 * 100) / 100 + "公里");
        }
        order.setDistance(strDistance);
        //设置故障类型
        String[] faultIds = order.getOrderDetail().getFault_id().split("-");
        StringBuilder sb = new StringBuilder();
        switch (order.getCar_type()) {
            case 0:
                sb.append("两轮车/");
                break;
            case 1:
                sb.append("三轮车/");
                break;
        }
        for (int i = 0; i < faultIds.length; i++) {
            sb.append(FaultUtils.parseWx(Integer.valueOf(faultIds[i])) + "/");
        }
        if (!StringUtils.isEmpty(orderDetail.getFault_description())) {
            sb.append("其他描述:" + orderDetail.getFault_description());
        }
        order.setFaultDescription(sb.toString());
    }
}
