package com.qcwy.chaowei.myapplication.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.qcwy.chaowei.myapplication.entity.Cart;
import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.service.MainService;
import com.qcwy.chaowei.myapplication.utils.FileUtils;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.MyCrashHandler;
import com.qcwy.chaowei.myapplication.utils.MyLog;

import org.xutils.x;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static android.content.ContentValues.TAG;

public class MyApplication extends Application {
    //是否显示消息右上角红点
    public static boolean showTips = false;
    //电源锁
    private PowerManager.WakeLock mWakeLock;

    //工程师工号
    public static String jobNo;
    //上传地理位置时间间隔
    public static final int space = 60 * 1000;
    private static MyApplication app;
    public static FileWriter fwCatch;
    public static FileWriter fwError;
    public static List<Activity> activities;
    public static Intent service;
    public static boolean isServiceRunning = false;

    //百度地图定位核心类
    private static LocationClient mLocationClient;
    //地理编码
    private GeoCoder geoCoder;

    //维度
    private static double latitude;
    //经度
    private static double longitude;
    //当前城市
    private static String city;
    //当前地址
    private static String address;
    //待抢订单
    private static List<Order> rushOrders;
    //当前订单集合
    private static List<Order> currentOrders;
    //当前操作的订单
    private static Order order;
    //领料的购物车
    private Cart cart;
    //主界面是否销毁
    public static boolean isDestroy = true;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xutils
        x.Ext.init(this);
        initData();
        initLocation();
        register();
        //启动待抢订单倒计时
        countDown();
    }

    private void initData() {
        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        app = this;
        rushOrders = new ArrayList<>();
        currentOrders = new ArrayList<>();
        cart = new Cart();
        geoCoder = GeoCoder.newInstance();
        mLocationClient = new LocationClient(this);
        mLocationClient.start();
        //初始化okgo
        initOkgo();
        //读取jobNo
        SharedPreferences preferences = getSharedPreferences("qcwy", Context.MODE_PRIVATE);
        jobNo = preferences.getString("username", null);
        service = new Intent(this, MainService.class);
        activities = new ArrayList<>();
        JPushInterface.setDebugMode(true);//开启日志输出
        JPushInterface.init(this);//初始化极光推送
    }

    //初始化当前位置配置
    private void initLocation() {
        // 设置定位参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        // locationClient.requestLocation();//重新获取坐标
    }

    //注册
    private void register() {
        BroadcastReceiver receiver = new InnerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalContants.LOG_FILE_CHECKED);
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(GlobalContants.MAIN_ACTIVITY_DESTROY);
        registerReceiver(receiver, filter);
        //注册异常获取类
        MyCrashHandler myCrashHandler = MyCrashHandler.getInstance();
        myCrashHandler.init(getApplicationContext());
        // 让百度框架的接口BDLocationListener指向实现类
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                MyLog.d("百度地图重新定位");
                // bdLocation位置信息
                // 纬度
                latitude = bdLocation.getLatitude();
                // 经度
                longitude = bdLocation.getLongitude();
                city = bdLocation.getCity();
                address = bdLocation.getLocationDescribe();
//                bdLocation.getAddress();//获取位置描述
                if (latitude != 0 && longitude != 0) {
                    Intent intent = new Intent();
                    intent.setAction(GlobalContants.LOCATION_COMPELETE);
                    sendBroadcast(intent);
                }
            }
        });
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                }
                //获取地理编码结果
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                }
                //获取反向地理编码结果
            }
        });
    }

    //广播接受者
    class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //接收到文件检测完毕的广播
            if (GlobalContants.LOG_FILE_CHECKED.equals(intent.getAction())) {
                //打开文件流
                //SD卡装载完毕
                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                    //手动捕获异常
                    File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsoluteFile(),
                            File.separator + "LogFile");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    fwCatch = FileUtils.openFile(android.os.Environment.getExternalStorageDirectory().getAbsoluteFile(),
                            File.separator + "LogFile" + File.separator + "catchLog.log");
                    //自动捕获异常
                    fwError = FileUtils.openFile(android.os.Environment.getExternalStorageDirectory().getAbsoluteFile(),
                            File.separator + "LogFile" + File.separator + "autoLog.log");
                }
            } else if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                MyLog.d("接收到系统闹钟广播");
                //检查Service状态
                ActivityManager manager = (ActivityManager) MyApplication.getApp()
                        .getSystemService(Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
                    if ("com.qcwy.chaowei.myapplication.service.MainService".equals(serviceInfo.service.getClassName())) {
                        isServiceRunning = true;
                    }
                }
                if (!isServiceRunning) {
                    startService(service);
                }

                //主界面被销毁
            } else if (GlobalContants.MAIN_ACTIVITY_DESTROY.equals(intent.getAction())) {
                startService(service);
            }
        }
    }

    //倒计时
    public void countDown() {
        CountDownTimer timer = new CountDownTimer(Long.MAX_VALUE, 998) {
            @Override
            public void onTick(long millisUntilFinished) {
                for (int i = 0; i < rushOrders.size(); i++) {
                    Order order = rushOrders.get(i);
                    int time = order.getRemainTime();
                    order.setRemainTime(--time);
                }
                Intent intent = new Intent();
                intent.setAction(GlobalContants.RECEIVER_UPDATE_RUSH_ORDERS);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

    private void initOkgo() {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
//        params.put("jobNo", jobNo);     //param支持中文,直接传,不要自己编码
        //必须调用初始化
        OkGo.init(this);
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()
                    //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                    .debug("TAG")
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(1000)                        //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间
                    .setRetryCount(5)                               //全局的重试次数
                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 [url]https://github.com/jeasonlzy/[/url]
                    .setCacheMode(CacheMode.NO_CACHE)
                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                    //如果不想让框架管理cookie,以下不需要
//                .setCookieStore(new MemoryCookieStore())                //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore())          //cookie持久化存储，如果cookie不过期，则一直有效
                    //可以设置https的证书,以下几种方案根据需要自己设置,不需要不用设置
//                    .setCertificates()                                  //方法一：信任所有证书
//                    .setCertificates(getAssets().open("srca.cer"))      //方法二：也可以自己设置https证书
//                    .setCertificates(getAssets().open("aaaa.bks"), "123456", getAssets().open("srca.cer"))//方法三：传入bks证书,密码,和cer证书,支持双向加密
                    //可以添加全局拦截器,不会用的千万不要传,错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })
                    //这两行同上,不需要就不要传
                    .addCommonHeaders(headers)//设置全局公共头
                    .addCommonParams(params);//设置全局公共参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MyApplication getApp() {
        return app;
    }

    public static LocationClient getLocationClient() {
        return mLocationClient;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static List<Order> getRushOrders() {
        return rushOrders;
    }

    public static void setRushOrders(List<Order> rushOrders) {
        MyApplication.rushOrders = rushOrders;
    }

    public static List<Order> getCurrentOrders() {
        return currentOrders;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        MyApplication.order = order;
    }

    public static void setCurrentOrders(List<Order> currentOrders) {
        MyApplication.currentOrders = currentOrders;
    }

    public static String getCity() {
        return city;
    }

    public static String getAddress() {
        return address;
    }

    public Cart getCart() {
        return cart;
    }

    //设置别名
    public void setAlias() {
        JPushInterface.setAliasAndTags(getApp(),
                jobNo,
                null,
                new TagAliasCallback() {
                    @Override
                    public void gotResult(int code, String s, Set<String> set) {
                        switch (code) {
                            case 0:
                                //设置成功
                                MyLog.d("Jpush别名设置成功");
                                break;
                            case 6002:
                                //设置失败
                                // 延迟 60 秒来调用 Handler 设置别名
                                Handler handler = new Handler();
                                handler.postDelayed(new TimerTask() {
                                    @Override
                                    public void run() {
                                        setAlias();
                                    }
                                }, 60 * 1000);
                                break;
                        }
                    }
                });
    }

    //申请设备电源锁
    public void acquireWakeLock(int wakeLockMode) {
//        类型	                    CPU	    屏幕	    键盘
//        PARTIAL_WAKE_LOCK	        on	    off	        off
//        SCREEN_DIM_WAKE_LOCK	    on	    dim	        off
//        SCREEN_BRIGHT_WAKE_LOCK	on	    Bright	    off
//        FULL_WAKE_LOCK	        on  	Bright	    Bright
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(wakeLockMode | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    //释放设备电源锁
    public void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}