package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.OrderPart;

import java.util.List;

/**
 * Created by KouKi on 2017/3/15.
 */

public class ResponseOrderParts {
    private int state;
    private String message;
    private List<OrderPart> data;

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

    public List<OrderPart> getData() {
        return data;
    }

    public void setData(List<OrderPart> data) {
        this.data = data;
    }
}
