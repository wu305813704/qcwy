package com.qcwy.chaowei.myapplication.entity;

import java.util.List;

/**
 * Created by KouKi on 2017/1/5.
 */

public class NearPartCommision {
    private String time;
    private String Number;
    private String commission;
    private List<PartCommission> partCommissions;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public List<PartCommission> getPartCommissions() {
        return partCommissions;
    }

    public void setPartCommissions(List<PartCommission> partCommissions) {
        this.partCommissions = partCommissions;
    }
}
