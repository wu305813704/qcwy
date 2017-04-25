package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.OrderReassignment;

/**
 * Created by KouKi on 2017/2/23.
 */

public class ResponseOrderReassignment {
    private int state;
    private String message;
    private OrderReassignment data;

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

    public OrderReassignment getData() {
        return data;
    }

    public void setData(OrderReassignment data) {
        this.data = data;
    }
}
