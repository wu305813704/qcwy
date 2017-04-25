package com.qcwy.chaowei.myapplication.entity;

/**
 * Created by KouKi on 2017/3/13.
 */
public class HistoryOrderData {
    //订单数量
    private int count;
    //订单总额
    private double totalPrice;
    //抢单数
    private int rushCount;
    //丢单数
    private int loseCount;
    //抢单率
    private double percentRush;
    //丢单率
    private double percentLose;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getRushCount() {
        return rushCount;
    }

    public void setRushCount(int rushCount) {
        this.rushCount = rushCount;
    }

    public int getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(int loseCount) {
        this.loseCount = loseCount;
    }

    public double getPercentRush() {
        return percentRush;
    }

    public void setPercentRush(double percentRush) {
        this.percentRush = percentRush;
    }

    public double getPercentLose() {
        return percentLose;
    }

    public void setPercentLose(double percentLose) {
        this.percentLose = percentLose;
    }
}
