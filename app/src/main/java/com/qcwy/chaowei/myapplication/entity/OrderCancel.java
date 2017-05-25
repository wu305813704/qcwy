package com.qcwy.chaowei.myapplication.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by KouKi on 2017/4/12.
 */
public class OrderCancel implements Serializable {
    private int cancel_id;
    private int order_no;
    private String cause;
    private long update_time;

    public int getCancel_id() {
        return cancel_id;
    }

    public void setCancel_id(int cancel_id) {
        this.cancel_id = cancel_id;
    }

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }
}
