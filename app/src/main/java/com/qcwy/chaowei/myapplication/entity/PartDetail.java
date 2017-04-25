package com.qcwy.chaowei.myapplication.entity;

/**
 * Created by KouKi on 2017/3/2.
 */
public class PartDetail {
    private int part_detail_id;
    private int part_no;
    private String model;
    private String unit;
    private double price;
    private double price_old;
    private double price_new;
    private int is_guarantees;
    private int guarantees_limit;
    private String remark;
    private String image;
    //非表中的字段
    private String name;
    //零件数量
    private int count = 1;
    //零件类型0-全新1-以旧换新2-以旧换旧
    private int type = 0;

    public int getPart_detail_id() {
        return part_detail_id;
    }

    public void setPart_detail_id(int part_detail_id) {
        this.part_detail_id = part_detail_id;
    }

    public int getPart_no() {
        return part_no;
    }

    public void setPart_no(int part_no) {
        this.part_no = part_no;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice_old() {
        return price_old;
    }

    public void setPrice_old(double price_old) {
        this.price_old = price_old;
    }

    public double getPrice_new() {
        return price_new;
    }

    public void setPrice_new(double price_new) {
        this.price_new = price_new;
    }

    public int getIs_guarantees() {
        return is_guarantees;
    }

    public void setIs_guarantees(int is_guarantees) {
        this.is_guarantees = is_guarantees;
    }

    public int getGuarantees_limit() {
        return guarantees_limit;
    }

    public void setGuarantees_limit(int guarantees_limit) {
        this.guarantees_limit = guarantees_limit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
