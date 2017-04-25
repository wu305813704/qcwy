package com.qcwy.chaowei.myapplication.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.overlayutil.BikingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.JsonResult;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.entity.OrderDetail;
import com.qcwy.chaowei.myapplication.entity.WxUser;
import com.qcwy.chaowei.myapplication.utils.DateUtils;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.GsonUtils;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.sql.Date;

import okhttp3.Call;
import okhttp3.Response;


@ContentView(R.layout.activity_current_order_detail)
public class CurrentOrderDetailActivity extends BaseActivity {
    //一键导航
    @ViewInject(R.id.btn_navigation)
    private Button btnNavigation;
    //订单编号
    @ViewInject(R.id.tv_order_number)
    private TextView tvOrderNumber;
    //时间
    @ViewInject(R.id.tv_time)
    private TextView tvTime;
    //地点
    @ViewInject(R.id.tv_address)
    private TextView tvAddress;
    //用户名
    @ViewInject(R.id.tv_username)
    private TextView tvUsername;
    //电话
    @ViewInject(R.id.tv_mobile_no)
    private TextView tvMobileNo;
    //故障
    @ViewInject(R.id.tv_trouble)
    private TextView tvTrouble;
    //详情
    @ViewInject(R.id.tv_detail)
    private TextView tvDetail;
    //拨打电话、更多操作
    @ViewInject(R.id.btn_call)
    private Button btnCall;
    //开始订单、确认故障
    @ViewInject(R.id.btn_start_order)
    private Button btnStartOrder;
    //隐藏
    @ViewInject(R.id.btn_hidden)
    private Button btnHidden;
    //显示
    @ViewInject(R.id.btn_show)
    private Button btnShow;
    //info
    @ViewInject(R.id.ll_info)
    private LinearLayout llInfo;
    //more
    @ViewInject(R.id.ll_more)
    private LinearLayout llMore;
    //暂停订单
    @ViewInject(R.id.btn_stop_order)
    private Button btnStopOrder;
    //申请改派
    @ViewInject(R.id.btn_reassignment_order)
    private Button btnReassignmentOrder;
    //改预约单
    @ViewInject(R.id.btn_appointment_order)
    private Button btnAppointmentOrder;
    //更改订单
    @ViewInject(R.id.btn_update_order)
    private Button btnUpdateOrder;
    //取消
    @ViewInject(R.id.btn_cancel)
    private Button btnCancel;
    @ViewInject(R.id.bd_map_view)
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    //自己的位置信息
    private LatLng myLatLng;
    //客户的位置信息
    private LatLng customerLatLng;

    //路线搜索
    private RoutePlanSearch mSearch;
    //当前操作的订单
    private Order order;
    private OrderDetail orderDetail;
    private WxUser wxUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置客户坐标
        customerLatLng = new LatLng(Double.valueOf(orderDetail.getLati()), Double.valueOf(orderDetail.getLon()));
        //定位客户位置
        location(customerLatLng, R.drawable.icon_marka);
    }

    @Override
    protected void init() {
        setTitle("当前订单详情");
        //获取到order
        order = MyApplication.getApp().getOrder();
        orderDetail = order.getOrderDetail();
        wxUser = order.getWxUser();
        if (order.getState() == 2) {
            btnCall.setText("更多操作");
            btnStartOrder.setText("确认故障");
        } else if (order.getState() == 4) {
            long time = order.getAppointment_time();
            String currentDate = DateUtils.format(new Date(System.currentTimeMillis()), "yyyy-MM-dd");
            String appointmentDate = DateUtils.format(new Date(time), "yyyy-MM-dd");
            if (!currentDate.equals(appointmentDate)) {
                btnStartOrder.setText("预约中");
            }
        } else if (order.getState() == 7) {
            btnStartOrder.setText("等待用户确认故障");
        } else if (order.getState() == 6) {
            //改派中
            btnCall.setText("订单改派中");
            btnStartOrder.setText("改派人:");
            OkGo.get(Urls.GET_REASSIGNMENT_NAME)
                    .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                    .cacheKey(Urls.GET_REASSIGNMENT_NAME)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                    .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                    .params("orderNo", order.getOrder_no())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            JsonResult jsonResult = GsonUtils.getInstance().fromJson(s, JsonResult.class);
                            if (jsonResult.getState() == 0) {
                                btnStartOrder.setText("改派人:" + jsonResult.getData());
                            } else {
                                MyToast.show(getApplicationContext(), jsonResult.getMessage());
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            if (response == null) {
                                MyToast.show(getApplicationContext(), "网络连接失败!");
                            } else {
                                MyToast.show(getApplicationContext(), e);
                            }
                        }
                    });
        } else if (order.getState() == 8) {
            btnStartOrder.setText("确认维修完成");
        } else if (order.getState() == 9) {
            btnStartOrder.setText("等待客户验收");
        } else if (order.getState() == 10) {
            btnStartOrder.setText("等待客户付款");
        }
        //百度地图功能配置
        setBaiduMap();
        myLatLng = new LatLng(MyApplication.getApp().getLatitude(), MyApplication.getApp().getLongitude());
        //延迟显示info布局
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvOrderNumber.setText(String.valueOf(order.getOrder_no()));
                if (order.getState() == 4) {
                    //预约单
                    tvTime.setText(DateUtils.format(new java.util.Date(order.getAppointment_time()), "yyyy-MM-dd HH:mm:ss"));
                } else {
                    tvTime.setText(orderDetail.getSend_time());
                }
                tvAddress.setText(orderDetail.getLoc());//地点
                tvUsername.setText(wxUser.getNickname());
                tvMobileNo.setText(wxUser.getTel());
                tvTrouble.setText(order.getFaultDescription());
                showInfo(800);
            }
        }, 500);
    }

    @Override
    protected void setListeners() {
        btnNavigation.setOnClickListener(this);
        tvDetail.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnStartOrder.setOnClickListener(this);
        btnHidden.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        //防止点击穿透
        llInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        btnStopOrder.setOnClickListener(this);
        btnReassignmentOrder.setOnClickListener(this);
        btnAppointmentOrder.setOnClickListener(this);
        btnUpdateOrder.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        //地图加载完成
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //调用路线规划
                SearchButtonProcess();
            }
        });
        //搜索路线监听
        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                //步行
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                //火车
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                //驾车
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
                //室内
            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
                //骑行
                if (bikingRouteResult == null || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    MyToast.show(getApplicationContext(), "抱歉，未找到路线规划结果");
                }
                if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    MyToast.show(getApplicationContext(), "抱歉，未找到路线规划结果");
                    return;
                }
                if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    //清除地图
                    mBaiduMap.clear();
//                    route = result.getRouteLines().get(0);
                    BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaiduMap);//路线覆盖物，MyDrivingRouteOverlay代码下面给出
//                    mBaiduMap.setOnMarkerClickListener(overlay);//标记点击事件
                    overlay.setData(bikingRouteResult.getRouteLines().get(0));//获取第一个路线规划
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }
        });
    }

    /**
     * 定位、移动地图
     *
     * @param latLng 经纬度
     * @param icon   R.drawable.icon_mark
     */
    public void location(LatLng latLng, int icon) {
        // 移动地图
        // 地图显示的级别
        int zoom = 16;
        //定位客户位置
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                .newLatLngZoom(latLng, zoom);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
        // 在地图上添加图片
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(icon);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(bitmapDescriptor);
        mBaiduMap.addOverlay(markerOptions);
    }

    private void setBaiduMap() {
        //获取BaiduMap
        mBaiduMap = mMapView.getMap();
        //开启交通图
//        mBaiduMap.setTrafficEnabled(true);
        /**
         * 普通地图
         */
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 隐藏百度的LOGO
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        // 不显示地图上比例尺
        mMapView.showScaleControl(false);
        // 不显示地图缩放控件（按钮控制栏）
        mMapView.showZoomControls(false);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
    }

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
            case R.id.btn_navigation:
                // 构建 导航参数
                NaviParaOption para = new NaviParaOption()
                        .startPoint(myLatLng).endPoint(customerLatLng);
                try {
                    // 调起百度地图骑行导航
                    BaiduMapNavigation.openBaiduMapBikeNavi(para, getApplicationContext());
                } catch (BaiduMapAppNotSupportNaviException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_detail:
                //订单详情
                startActivity(new Intent(this, OrderDetailActivity.class));
                break;
            case R.id.btn_call:
                //拨打电话/
                if ("拨打电话".equals(btnCall.getText().toString())) {
                    new AlertDialog.Builder(CurrentOrderDetailActivity.this)
                            .setTitle("确定呼叫：")
                            .setMessage(wxUser.getTel())
                            .setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        MyToast.show(getApplicationContext(), "没有授权");
                                        return;
                                    }
                                    //用intent启动拨打电话
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + wxUser.getTel()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create()
                            .show();
                } else if ("更多操作".equals(btnCall.getText().toString())) {
                    //更多操作
                    //显示more
                    showMore(300);
                }
                break;
            case R.id.btn_start_order:
                //开始订单
                if ("开始订单".equals(btnStartOrder.getText().toString())) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("确定已联系上客户并开始订单?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OkGo.get(Urls.START_ORDERS)
                                            .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                                            .cacheKey(Urls.START_ORDERS)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                                            .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                                            .params("jobNo", MyApplication.jobNo)
                                            .params("orderNo", order.getOrder_no())
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onBefore(BaseRequest request) {
                                                    btnStartOrder.setEnabled(false);
                                                }

                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    JsonResult jsonResult = GsonUtils.getInstance().fromJson(s, JsonResult.class);
                                                    if (jsonResult.getState() == 0) {
                                                        btnCall.setText("更多操作");
                                                        btnStartOrder.setText("确认故障");
                                                        order.setState(2);
                                                        MyToast.show(getApplicationContext(), "订单已开始，请确认故障后开始维修");
                                                        Intent intent = new Intent();
                                                        intent.setAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
                                                        sendBroadcast(intent);
                                                    } else {
                                                        MyToast.show(getApplicationContext(), jsonResult.getMessage());
                                                    }
                                                }

                                                @Override
                                                public void onError(Call call, Response response, Exception e) {
                                                    if (response == null) {
                                                        MyToast.show(getApplicationContext(), "网络连接失败!");
                                                    } else {
                                                        MyToast.show(getApplicationContext(), e);
                                                    }
                                                }

                                                @Override
                                                public void onAfter(String s, Exception e) {
                                                    btnStartOrder.setEnabled(true);
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create()
                            .show();
                } else if ("确认故障".equals(btnStartOrder.getText().toString())) {
                    startActivity(new Intent(this, ConfirmTroubleActivity.class));
                } else if ("确认维修完成".equals(btnStartOrder.getText().toString())) {
                    //维修完成
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("确认维修完成?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    btnStartOrder.setEnabled(false);
                                    OkGo.get(Urls.COMPELETE)     // 请求方式和请求url
                                            .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                                            .cacheKey(Urls.COMPELETE)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                                            .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                                            .params("jobNo", MyApplication.jobNo)
                                            .params("orderNo", order.getOrder_no())
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    JsonResult result = GsonUtils.getInstance().fromJson(s, JsonResult.class);
                                                    if (result.getState() == 0) {
                                                        MyToast.show(getApplicationContext(), "维修完成，请等待客户验收");
                                                        btnStartOrder.setText("等待客户验收");
                                                        Intent intent = new Intent();
                                                        intent.setAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
                                                        sendBroadcast(intent);
                                                    } else {
                                                        MyToast.show(getApplicationContext(), result.getMessage());
                                                    }
                                                }

                                                @Override
                                                public void onError(Call call, Response response, Exception e) {
                                                    if (response == null) {
                                                        MyToast.show(getApplicationContext(), "网络连接失败!");
                                                    } else {
                                                        MyToast.show(getApplicationContext(), e);
                                                    }
                                                }

                                                @Override
                                                public void onAfter(String s, Exception e) {
                                                    btnStartOrder.setEnabled(true);
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create()
                            .show();
                }
                break;
            case R.id.btn_hidden:
                //隐藏info
                hiddenInfo(300, true);
                break;
            case R.id.btn_show:
                //显示info
                showInfo(300);
                break;
            case R.id.btn_stop_order:
                //暂停订单
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("您确定要暂停该订单吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OkGo.get(Urls.PAUSE_ORDER)
                                        .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                                        .cacheKey(Urls.PAUSE_ORDER)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                                        .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                                        .params("jobNo", MyApplication.jobNo)
                                        .params("orderNo", order.getOrder_no())
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onBefore(BaseRequest request) {
                                                btnStopOrder.setEnabled(false);
                                            }

                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
                                                JsonResult jsonResult = GsonUtils.getInstance().fromJson(s, JsonResult.class);
                                                if (jsonResult.getState() == 0) {
                                                    btnCall.setText("拨打电话");
                                                    btnStartOrder.setText("开始订单");
                                                    order.setState(3);
                                                    Intent intent = new Intent();
                                                    MyToast.show(getApplicationContext(), "订单已暂停，请联系客户解释原因");
                                                    intent.setAction(GlobalContants.RECEIVER_UPDATE_CURRENT_ORDERS);
                                                    sendBroadcast(intent);
                                                    finish();
                                                } else {
                                                    MyToast.show(getApplicationContext(), jsonResult.getMessage());
                                                }
                                            }

                                            @Override
                                            public void onError(Call call, Response response, Exception e) {
                                                if (response == null) {
                                                    MyToast.show(getApplicationContext(), "网络连接失败!");
                                                } else {
                                                    MyToast.show(getApplicationContext(), e);
                                                }
                                            }

                                            @Override
                                            public void onAfter(String s, Exception e) {
                                                btnStopOrder.setEnabled(true);
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                break;
            case R.id.btn_reassignment_order:
                //申请改派
                startActivity(new Intent(this, ReassignmentOrderActivity.class));
                break;
            case R.id.btn_appointment_order:
                //改预约单
                startActivity(new Intent(this, AppointmentActivity.class));
                break;
            case R.id.btn_update_order:
                //更改订单
                startActivity(new Intent(this, ConfirmTroubleActivity.class));
                break;
            case R.id.btn_cancel:
                //取消
                hiddenMore(300);
                break;
        }
    }

    /**
     * 发起路线规划搜索示例
     */
    private void SearchButtonProcess() {
        //重置浏览节点的路线数据
        //route = null;
        //途径点
//        ArrayList<PlanNode> arg0 = new ArrayList<>();
        //设置起终点、途经点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withLocation(myLatLng);
        PlanNode enNode = PlanNode.withLocation(customerLatLng);
        // 实际使用中请对起点终点城市进行正确的设定
        mSearch.bikingSearch((new BikingRoutePlanOption())
                .from(stNode)//起点
//                .passBy(arg0)//途经点
                .to(enNode));//终点
    }

    /**
     * 隐藏info布局
     *
     * @param duration 动画执行时间
     * @param showBtn  是否显示下方(显示info布局)按钮
     */
    private void hiddenInfo(long duration, boolean showBtn) {
        AnimatorSet hidden = new AnimatorSet();
        hidden.playTogether(
                ObjectAnimator.ofFloat(llInfo, "translationY", 0, llInfo.getHeight())
        );
        hidden.setDuration(duration).start();
        if (showBtn) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnShow.setVisibility(View.VISIBLE);
                }
            }, duration);
        }
    }

    //显示info布局
    private void showInfo(long duration) {
        llInfo.setVisibility(View.VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(llInfo, "translationY", llInfo.getHeight(), 0)
        );
        set.setDuration(duration).start();
        btnShow.setVisibility(View.GONE);
    }

    //隐藏more布局
    private void hiddenMore(final long duration) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(llMore, "translationY", 0, llMore.getHeight())
        );
        set.setDuration(duration).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showInfo(duration);
            }
        }, duration);
    }

    //显示more布局
    private void showMore(final long duration) {
        hiddenInfo(duration, false);
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(llMore, "translationY", llMore.getHeight(), 0)
        );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                llMore.setVisibility(View.VISIBLE);
                set.setDuration(duration).start();
            }
        }, duration);
        btnShow.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    //定制RouteOverly
    private class MyBikingRouteOverlay extends BikingRouteOverlay {
        public MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            //覆写此方法以改变默认起点图标
            return BitmapDescriptorFactory.fromResource(R.drawable.start_point);
            //这里可以使用BitmapDescriptorFactory.fromView(view)实现自定义view覆盖物，自定义覆盖物请关注以后的文章。
        }

        @Override
        public int getLineColor() {
            // TODO Auto-generated method stub
            //覆写此方法以改变默认绘制颜色
            //Returns:
            //线颜色
            return super.getLineColor();
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            //覆写此方法以改变默认终点图标
            return BitmapDescriptorFactory.fromResource(R.drawable.end_point);
        }

        @Override
        public boolean onRouteNodeClick(int i) {
            // TODO Auto-generated method stub
            //覆写此方法以改变默认点击处理
            return true;
        }
    }
}
