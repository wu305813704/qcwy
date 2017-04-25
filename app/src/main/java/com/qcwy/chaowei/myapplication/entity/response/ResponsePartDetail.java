package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.PartDetail;

import java.util.List;

/**
 * Created by KouKi on 2017/3/8.
 */

public class ResponsePartDetail {
    private int state;
    private String message;
    private List<PartDetail> data;

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

    public List<PartDetail> getData() {
        return data;
    }

    public void setData(List<PartDetail> data) {
        this.data = data;
    }
}
