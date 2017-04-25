package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.OrderAppointment;

/**
 * Created by KouKi on 2017/2/21.
 */

public class ResponseOrderAppointment {
    private int state;
    private String message;
    private OrderAppointment data;

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

    public OrderAppointment getData() {
        return data;
    }

    public void setData(OrderAppointment data) {
        this.data = data;
    }
}
