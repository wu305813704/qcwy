package com.qcwy.chaowei.myapplication.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by KouKi on 2016/12/23.
 */
@ContentView(R.layout.title)
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(R.id.iv_logo)
    private ImageView ivLogo;
    @ViewInject(R.id.btn_back)
    private Button btnBack;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_msg)
    private ImageView ivMsg;
    @ViewInject(R.id.iv_tips)
    private ImageView ivTips;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (MyApplication.showTips) {
            showTips();
        } else {
            hideTips();
        }
        receiver = new InnerReciver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalContants.SHOW_TIPS);
        filter.addAction(GlobalContants.HIDE_TIPS);
        filter.addAction(GlobalContants.RECEIVER_GET_MESSAGE);
        registerReceiver(receiver, filter);
        MyApplication.activities.add(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        init();
        setListener();
    }

    protected void showLogo() {
        ivLogo.setVisibility(View.VISIBLE);
    }

    protected void showTips() {
        ivTips.setVisibility(View.VISIBLE);
    }

    protected void hideTips() {
        ivTips.setVisibility(View.GONE);
    }

    //设置标题
    protected void setTitle(String title) {
        tvTitle.setText(title);
    }

    private void setListener() {
        btnBack.setOnClickListener(this);
        ivMsg.setOnClickListener(this);
        setListeners();
    }

    abstract protected void init();

    abstract protected void setListeners();

    //在activity启动模式为singleTask时，确保其他activity向这个activity发送数据时更新intent
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public ImageView getIvLogo() {
        return ivLogo;
    }

    public Button getBtnBack() {
        return btnBack;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageView getIvMsg() {
        return ivMsg;
    }

    public ImageView getIvTips() {
        return ivTips;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        MyApplication.activities.remove(this);
    }

    class InnerReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (GlobalContants.SHOW_TIPS.equals(intent.getAction())) {
                //显示tips
                MyApplication.showTips = true;
                showTips();
            } else if (GlobalContants.HIDE_TIPS.equals(intent.getAction())) {
                //隐藏tips
                MyApplication.showTips = false;
                hideTips();
            } else if (GlobalContants.RECEIVER_GET_MESSAGE.equals(intent.getAction())) {
                Dialog dialog;
                //收到服务器推送的消息
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("消息提示")
                                .setMessage(intent.getStringExtra("msg"))
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                //点击外部不消失
                builder.setCancelable(false);
                dialog = builder.create();
                dialog.show();
            }
        }
    }
}
