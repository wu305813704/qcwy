package com.qcwy.chaowei.myapplication.utils;

/**
 * Created by KouKi on 2017/1/17.
 */

public class TroubleUtils {
    private static final   String ERROR_ENGINEER_NOPOWER = "车不通电";
    private static final   int ERROR_ENGINEER_NOPOWER_CODE = 11;
    private static final   String ERROR_ENGINEER_NORUN = "有电不走";
    private static final   int ERROR_ENGINEER_NORUN_CODE = 12;
    private static final   String ERROR_ENGINEER_PATCHING = "补胎";
    private static final   int ERROR_ENGINEER_PATCHING_CODE = 21;
    private static final   String ERROR_ENGINEER_PATCHING_TUBLESS = "真空胎补";
    private static final   int ERROR_ENGINEER_PATCHING_TUBLESS_CODE = 22;
    private static final   String ERROR_ENGINEER_HUB = "轮毂";
    private static final   int ERROR_ENGINEER_HUB_CODE = 23;
    private static final   String ERROR_ENGINEER_TUB_CHANGE = "换内胎";
    private static final   int ERROR_ENGINEER_TUB_CHANGE_CODE = 24;
    private static final   String ERROR_ENGINEER_TYRE_CHANGE = "换外胎";
    private static final   int ERROR_ENGINEER_TYRE_CHANGE_CODE = 25;
    private static final   String ERROR_ENGINEER_TURN = "转把故障";
    private static final   int ERROR_ENGINEER_TURN_CODE = 31;
    private static final   String ERROR_ENGINEER_BRAKE = "杀扯柄故障";
    private static final   int ERROR_ENGINEER_BRAKE_CODE = 32;
    private static final   String ERROR_ENGINEER_DRUM = "刹车鼓故障";
    private static final   int ERROR_ENGINEER_DRUM_CODE = 33;
    private static final   String ERROR_ENGINEER_DISK = "碟刹故障";
    private static final   int ERROR_ENGINEER_DISK_CODE = 34;
    private static final   String ERROR_ENGINEER_LIGHT = "灯故障";
    private static final   int ERROR_ENGINEER_LIGHT_CODE = 41;
    private static final   String ERROR_ENGINEER_HORN = "喇叭故障";
    private static final   int ERROR_ENGINEER_HORN_CODE = 42;
    private static final   String ERROR_ENGINEER_CIRCUIT = "线路问题";
    private static final   int ERROR_ENGINEER_CIRCUIT_CODE = 43;
    private static final   String ERROR_ENGINEER_LOSTKEY = "钥匙丢了";
    private static final   int ERROR_ENGINEER_LOSTKEY_CODE = 51;
    private static final   String ERROR_ENGINEER_LOCKBROKEN = "锁坏了";
    private static final   int ERROR_ENGINEER_LOCKBROKEN_CODE = 52;
    private static final   String ERROR_ENGINEER_NEW = "全新";
    private static final   int ERROR_ENGINEER_NEW_CODE = 61;
    private static final   String ERROR_ENGINEER_EXCHANGE = "以旧换新";
    private static final   int ERROR_ENGINEER_EXCHANGE_CODE = 62;
    private static final   String ERROR_ENGINEER_CHARGERTYPE = "型号";
    private static final   int ERROR_ENGINEER_CHARGERTYPE_CODE = 71;
    private static final   String ERROR_ENGINEER_TYPE = "车辆款式";
    private static final   int ERROR_ENGINEER_TYPE_CODE = 81;
    private static final   String ERROR_ENGINEER_SOUND = "异响";
    private static final   int ERROR_ENGINEER_SOUND_CODE = 91;
    private static final   String ERROR_ENGINEER_WELDING = "电焊";
    private static final   int ERROR_ENGINEER_WELDING_CODE = 101;
    private static final   String ERROR_ENGINEER_MANUAL = "零件代安装";
    private static final   int ERROR_ENGINEER_MANUAL_CODE = 102;

    public String getString(int code){
        switch (code){
            case ERROR_ENGINEER_NOPOWER_CODE:
                return ERROR_ENGINEER_NOPOWER;
            case ERROR_ENGINEER_NORUN_CODE:
                return ERROR_ENGINEER_NORUN;
            case ERROR_ENGINEER_PATCHING_CODE:
                return ERROR_ENGINEER_PATCHING;
            case ERROR_ENGINEER_PATCHING_TUBLESS_CODE:
                return ERROR_ENGINEER_PATCHING_TUBLESS;
            case ERROR_ENGINEER_HUB_CODE:
                return ERROR_ENGINEER_HUB;
            case ERROR_ENGINEER_TUB_CHANGE_CODE:
                return ERROR_ENGINEER_TUB_CHANGE;
            case ERROR_ENGINEER_TYRE_CHANGE_CODE:
                return ERROR_ENGINEER_TYRE_CHANGE;
            case ERROR_ENGINEER_TURN_CODE:
                return ERROR_ENGINEER_TURN;
            case ERROR_ENGINEER_BRAKE_CODE:
                return ERROR_ENGINEER_BRAKE;
            case ERROR_ENGINEER_DRUM_CODE:
                return ERROR_ENGINEER_DRUM;
            case ERROR_ENGINEER_DISK_CODE:
                return ERROR_ENGINEER_DISK;
            case ERROR_ENGINEER_LIGHT_CODE:
                return ERROR_ENGINEER_LIGHT;
            case ERROR_ENGINEER_HORN_CODE:
                return ERROR_ENGINEER_HORN;
            case ERROR_ENGINEER_CIRCUIT_CODE:
                return ERROR_ENGINEER_CIRCUIT;
            case ERROR_ENGINEER_LOSTKEY_CODE:
                return ERROR_ENGINEER_LOSTKEY;
            case ERROR_ENGINEER_LOCKBROKEN_CODE:
                return ERROR_ENGINEER_LOCKBROKEN;
            case ERROR_ENGINEER_NEW_CODE:
                return ERROR_ENGINEER_NEW;
            case ERROR_ENGINEER_EXCHANGE_CODE:
                return ERROR_ENGINEER_EXCHANGE;
            case ERROR_ENGINEER_CHARGERTYPE_CODE:
                return ERROR_ENGINEER_CHARGERTYPE;
            case ERROR_ENGINEER_TYPE_CODE:
                return ERROR_ENGINEER_TYPE;
            case ERROR_ENGINEER_SOUND_CODE:
                return ERROR_ENGINEER_SOUND;
            case ERROR_ENGINEER_WELDING_CODE:
                return ERROR_ENGINEER_WELDING;
            case ERROR_ENGINEER_MANUAL_CODE:
                return ERROR_ENGINEER_MANUAL;
            default:
                return null;
        }
    }
}
