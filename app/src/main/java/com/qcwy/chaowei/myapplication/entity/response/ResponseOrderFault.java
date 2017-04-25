package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.Order;
import com.qcwy.chaowei.myapplication.entity.OrderFault;

/**
 * Created by KouKi on 2017/3/15.
 */

public class ResponseOrderFault {
    private int state;
    private String message;
    private OrderFault data;

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

    public OrderFault getData() {
        return data;
    }

    public void setData(OrderFault data) {
        this.data = data;
    }
}
