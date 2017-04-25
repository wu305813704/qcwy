package com.qcwy.chaowei.myapplication.entity;

/**
 * Created by KouKi on 2017/2/18.
 */
public class OrderRecord {
    private int order_no;
    private String rush_time;
    private String start_time;
    private String pause_time;
    private String reassignment_time;
    private String appointment_time;
    private String update_time;

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public String getRush_time() {
        return rush_time;
    }

    public void setRush_time(String rush_time) {
        this.rush_time = rush_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getPause_time() {
        return pause_time;
    }

    public void setPause_time(String pause_time) {
        this.pause_time = pause_time;
    }

    public String getReassignment_time() {
        return reassignment_time;
    }

    public void setReassignment_time(String reassignment_time) {
        this.reassignment_time = reassignment_time;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
