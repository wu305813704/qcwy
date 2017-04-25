package com.qcwy.chaowei.myapplication.entity;

/**
 * Created by KouKi on 2017/3/13.
 */
public class OrderPart {
    private int id;
    private int order_no;
    private int part_detail_id;
    private int count;
    //非表中字段
    private String partMode;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public int getPart_detail_id() {
        return part_detail_id;
    }

    public void setPart_detail_id(int part_detail_id) {
        this.part_detail_id = part_detail_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPartMode() {
        return partMode;
    }

    public void setPartMode(String partMode) {
        this.partMode = partMode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
