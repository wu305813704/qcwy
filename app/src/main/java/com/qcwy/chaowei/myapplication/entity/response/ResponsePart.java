package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.Part;

import java.util.List;

/**
 * Created by KouKi on 2017/3/2.
 */

public class ResponsePart {
    private int state;
    private String message;
    private List<Part> data;

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

    public List<Part> getData() {
        return data;
    }

    public void setData(List<Part> data) {
        this.data = data;
    }
}
