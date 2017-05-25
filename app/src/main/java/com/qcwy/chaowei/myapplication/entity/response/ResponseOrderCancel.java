package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.OrderCancel;
import com.qcwy.chaowei.myapplication.entity.OrderPart;

import java.util.List;

/**
 * Created by KouKi on 2017/3/15.
 */

public class ResponseOrderCancel {
    private int state;
    private String message;
    private OrderCancel data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderCancel getData() {
        return data;
    }

    public void setData(OrderCancel data) {
        this.data = data;
    }
}
