package com.qcwy.chaowei.myapplication.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.MyLog;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.btn_personal_center)
    private Button btnPersonalCenter;
    @ViewInject(R.id.btn_orders_commission)
    private Button btnOrdersCommission;
    @ViewInject(R.id.btn_accessories_management)
    private Button btnAccessoriesManagement;
    @ViewInject(R.id.btn_history_orders)
    private Button btnHistoryOrders;
    @ViewInject(R.id.btn_current_order)
    private Button btnCurrentOrder;
    @ViewInject(R.id.btn_rush_orders)
    private Button btnRushOrders;
    //订单管理
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.isDestroy = false;
        //启动service
        startService(MyApplication.service);
        Intent intent = getIntent();
        String msg = intent.getStringExtra(GlobalContants.INTENT_SKIP);
        //系统消息
        if (GlobalContants.INTENT_SYSTEM_MESSAGE.equals(msg) || GlobalContants.INTENT_ORDER_MESSAGE.equals(msg)) {
            intent.setClass(this, MessageActivity.class);
            startActivity(intent);
            //待抢订单
        } else if (GlobalContants.INTENT_RUSH_ORDER.equals(msg)) {
            intent.setClass(this, OrderManagerActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void init() {
        showLogo();
        getBtnBack().setVisibility(View.GONE);
        getTvTitle().setVisibility(View.INVISIBLE);
        intent = new Intent(this, OrderManagerActivity.class);
    }

    @Override
    protected void setListeners() {
        btnPersonalCenter.setOnClickListener(this);
        btnOrdersCommission.setOnClickListener(this);
        btnAccessoriesManagement.setOnClickListener(this);
        btnHistoryOrders.setOnClickListener(this);
        btnCurrentOrder.setOnClickListener(this);
        btnRushOrders.setOnClickListener(this);
    }

    private long startTime = 0;

    //1.5秒内双击返回键退出
    @Override
    public void onBackPressed() {
        long endTime = System.currentTimeMillis();
        if (endTime - startTime >= 1500) {
            startTime = endTime;
            MyToast.show(getApplicationContext(), "再按一次退出程序");
            return;
        }
        //关闭Toast
        MyToast.cancel();
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.iv_msg:
                //跳转到Message界面
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.btn_personal_center:
                //跳转到个人中心
                startActivity(new Intent(this, PersonalCenterActivity.class));
                break;
            case R.id.btn_orders_commission:
                //跳转订单提成界面
                startActivity(new Intent(this, OrderCommissionActivity.class));
                break;
            case R.id.btn_accessories_management:
                //跳转配件管理界面
                startActivity(new Intent(this, PartManagerActivity.class));
                break;
            case R.id.btn_history_orders:
                //跳转历史订单界面
                intent.putExtra(GlobalContants.INTENT_SKIP, GlobalContants.INTENT_HISTORY_ORDER);
                startActivity(intent);
                break;
            case R.id.btn_current_order:
                //跳转当前订单界面
                intent.putExtra(GlobalContants.INTENT_SKIP, GlobalContants.INTENT_CURRENT_ORDER);
                startActivity(intent);
                break;
            case R.id.btn_rush_orders:
                //跳转我要抢单界面
                intent.putExtra(GlobalContants.INTENT_SKIP, GlobalContants.INTENT_RUSH_ORDER);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        MyLog.d("主界面被销毁");
        MyApplication.isDestroy = true;
        //发送广播(主界面被销毁)启动service
        Intent intent = new Intent();
        intent.setAction(GlobalContants.MAIN_ACTIVITY_DESTROY);
        sendBroadcast(intent);
        super.onDestroy();
    }
}
