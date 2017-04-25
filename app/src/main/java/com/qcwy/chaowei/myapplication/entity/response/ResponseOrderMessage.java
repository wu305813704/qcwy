package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.OrderMessage;

import java.util.List;

/**
 * Created by KouKi on 2017/2/23.
 */

public class ResponseOrderMessage {
    private int state;
    private String message;
    private List<OrderMessage> data;

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

    public List<OrderMessage> getData() {
        return data;
    }

    public void setData(List<OrderMessage> data) {
        this.data = data;
    }
}
