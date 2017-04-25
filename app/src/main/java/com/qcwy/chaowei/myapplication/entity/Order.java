package com.qcwy.chaowei.myapplication.entity;

/**
 * Created by KouKi on 2017/2/13.
 */
public class Order {
    private int order_no;
    private int old_order_no;
    private String open_id;
    private String job_no;
    private int state;
    private int type;
    private long send_time;
    private long appointment_time;
    private long complete_time;
    private int car_type;
    private AppUser appUser;
    private WxUser wxUser;
    private OrderDetail orderDetail;
    private OrderRecord orderRecord;
    private int remainTime;
    private String distance;
    private String faultDescription;

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public int getOld_order_no() {
        return old_order_no;
    }

    public void setOld_order_no(int old_order_no) {
        this.old_order_no = old_order_no;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(long complete_time) {
        this.complete_time = complete_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSend_time() {
        return send_time;
    }

    public void setSend_time(long send_time) {
        this.send_time = send_time;
    }

    public int getCar_type() {
        return car_type;
    }

    public void setCar_type(int car_type) {
        this.car_type = car_type;
    }

    public long getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(long appointment_time) {
        this.appointment_time = appointment_time;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public WxUser getWxUser() {
        return wxUser;
    }

    public void setWxUser(WxUser wxUser) {
        this.wxUser = wxUser;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public OrderRecord getOrderRecord() {
        return orderRecord;
    }

    public void setOrderRecord(OrderRecord orderRecord) {
        this.orderRecord = orderRecord;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }
}
