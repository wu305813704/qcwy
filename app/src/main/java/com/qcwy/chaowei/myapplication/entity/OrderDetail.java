package com.qcwy.chaowei.myapplication.entity;

/**
 * Created by KouKi on 2017/2/18.
 */
public class OrderDetail {
    private int id;
    private String fault_id;
    private String fault_description;
    private String lon;
    private String lati;
    private String loc;
    private String send_time;
    private String appointment_time;
    private int add_price;
    private double work_time_price;
    private double total_price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFault_id() {
        return fault_id;
    }

    public void setFault_id(String fault_id) {
        this.fault_id = fault_id;
    }

    public String getFault_description() {
        return fault_description;
    }

    public void setFault_description(String fault_description) {
        this.fault_description = fault_description;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public int getAdd_price() {
        return add_price;
    }

    public void setAdd_price(int add_price) {
        this.add_price = add_price;
    }

    public double getWork_time_price() {
        return work_time_price;
    }

    public void setWork_time_price(double work_time_price) {
        this.work_time_price = work_time_price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}
