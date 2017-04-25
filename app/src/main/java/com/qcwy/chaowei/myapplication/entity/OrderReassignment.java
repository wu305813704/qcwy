package com.qcwy.chaowei.myapplication.entity;

/**
 * Created by KouKi on 2017/2/22.
 */
public class OrderReassignment {
    private int id;
    private int order_no;
    private String send_time;
    private String old_no;
    private String new_no;
    private String cause;
    private int state;
    private String handle_time;
    private String sendName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getOld_no() {
        return old_no;
    }

    public void setOld_no(String old_no) {
        this.old_no = old_no;
    }

    public String getNew_no() {
        return new_no;
    }

    public void setNew_no(String new_no) {
        this.new_no = new_no;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getHandle_time() {
        return handle_time;
    }

    public void setHandle_time(String handle_time) {
        this.handle_time = handle_time;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }
}
