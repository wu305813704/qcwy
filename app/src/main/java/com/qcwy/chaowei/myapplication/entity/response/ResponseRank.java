package com.qcwy.chaowei.myapplication.entity.response;

import com.qcwy.chaowei.myapplication.entity.Rank;

import java.util.List;

/**
 * Created by KouKi on 2017/4/26.
 */

public class ResponseRank {
    private int state;
    private String message;
    private List<Rank> data;

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

    public List<Rank> getData() {
        return data;
    }

    public void setData(List<Rank> data) {
        this.data = data;
    }
}
