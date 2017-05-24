package com.qcwy.chaowei.myapplication.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.PowerManager;

import com.google.gson.Gson;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.ui.MainActivity;
import com.qcwy.chaowei.myapplication.ui.MessageActivity;
import com.qcwy.chaowei.myapplication.ui.OrderManagerActivity;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.MyLog;
import com.qcwy.chaowei.myapplication.utils.OrderSettingUtils;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by KouKi on 2017/2/17.
 * 极光推送的广播接收器
 */

public class JpushReceiver extends BroadcastReceiver {

    private NotificationManager nm;

    private static SoundPool soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }
        if (soundPool != null) {
            soundPool.load(context, R.raw.mysong, 1);
        }
        Bundle bundle = intent.getExtras();
        MyLog.d("onReceive - " + intent.getAction() + "extras" + bundle.toString());
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            MyLog.d("JPush用户注册成功");
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            MyLog.d("接收到推送下来的自定义消息");
            String title = intent.getStringExtra(JPushInterface.EXTRA_TITLE);
            String msg = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
            Gson gson = new Gson();
            //如果是订单
            if ("order".equals(title)) {
                Order order = gson.fromJson(msg, Order.class);
                //计算并设置订单其他属性
                OrderSettingUtils.setOrderDetail(order);
                MyApplication.getRushOrders().add(0, order);
                //去重
                removeDuplicate(MyApplication.getRushOrders());
                //发送广播更新抢单界面
                Intent updateRushOrders = new Intent();
                updateRushOrders.setAction(GlobalContants.RECEIVER_UPDATE_RUSH_ORDERS);
                context.sendBroadcast(updateRushOrders);
            } else if ("orderMsg".equals(title)) {
                //如果是订单消息
                Intent orderMsg = new Intent();
                orderMsg.setAction(GlobalContants.RECEIVER_GET_MESSAGE);
                orderMsg.putExtra("msg", msg);
                context.sendBroadcast(orderMsg);
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            MyLog.d("接受到推送下来的通知");
            soundPool.play(1,1, 1, 0, 0, 1);
            //亮屏
            MyApplication.getApp().acquireWakeLock(PowerManager.FULL_WAKE_LOCK);
            MyApplication.getApp().releaseWakeLock();
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            MyLog.d("用户点击打开了通知");
            String title = intent.getStringExtra(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            MyLog.d(title);
            //接收到order
            if ("您有新的可抢订单".equals(title)) {
                Intent i = new Intent(context, MainActivity.class);
                i.putExtras(bundle);
                i.putExtra(GlobalContants.INTENT_SKIP, GlobalContants.INTENT_RUSH_ORDER);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //如果主界面没有被销毁
                if (!MyApplication.isDestroy) {
                    i.setClass(context, OrderManagerActivity.class);
                }

                context.startActivity(i);
            } else if ("订单消息".equals(title)) {
                Intent i = new Intent(context, MainActivity.class);
                i.putExtras(bundle);
                i.putExtra(GlobalContants.INTENT_SKIP, GlobalContants.INTENT_ORDER_MESSAGE);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //如果主界面没有被销毁
                if (!MyApplication.isDestroy) {
                    i.setClass(context, MessageActivity.class);
                }
                context.startActivity(i);
            }
        } else {
            MyLog.d("Unhandled intent - " + intent.getAction());
        }
    }

    //去重
    private void removeDuplicate(List<Order> orders) {
        for (int i = 0; i < orders.size() - 1; i++) {
            for (int j = orders.size() - 1; j > i; j--) {
                if (orders.get(j).getOrder_no() == orders.get(i).getOrder_no()) {
                    orders.remove(j);
                }
            }
        }
    }

}
