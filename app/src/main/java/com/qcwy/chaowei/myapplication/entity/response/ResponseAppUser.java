package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.AppUser;

/**
 * Created by KouKi on 2017/5/15.
 */

public class ResponseAppUser {
    private int state;
    private String message;
    private AppUser data;

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

    public AppUser getData() {
        return data;
    }

    public void setData(AppUser data) {
        this.data = data;
    }
}
