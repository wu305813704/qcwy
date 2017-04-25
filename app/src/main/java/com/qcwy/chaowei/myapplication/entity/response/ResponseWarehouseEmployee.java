package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.WarehouseEmployee;

import java.util.List;

/**
 * Created by KouKi on 2017/3/6.
 */

public class ResponseWarehouseEmployee {

    private int state;
    private String message;
    private List<WarehouseEmployee> data;

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

    public List<WarehouseEmployee> getData() {
        return data;
    }

    public void setData(List<WarehouseEmployee> data) {
        this.data = data;
    }
}
