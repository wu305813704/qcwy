package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.HistoryOrderData;

/**
 * Created by KouKi on 2017/3/13.
 */

public class ResponseHistoryOrder {
    private int state;
    private String message;
    private HistoryOrderData data;

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

    public HistoryOrderData getData() {
        return data;
    }

    public void setData(HistoryOrderData data) {
        this.data = data;
    }
}
