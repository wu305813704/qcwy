package com.qcwy.chaowei.myapplication.entity;

/**
 * Created by KouKi on 2017/3/6.
 */
public class WarehouseEmployee {
    private int id;
    private String job_no;
    private Integer part_detail_id;
    private Integer count;
    //非数据表字段
    private String name;//零件名
    private String modle;//型号
    private String brand;//品牌

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public Integer getPart_detail_id() {
        return part_detail_id;
    }

    public void setPart_detail_id(Integer part_detail_id) {
        this.part_detail_id = part_detail_id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModle() {
        return modle;
    }

    public void setModle(String modle) {
        this.modle = modle;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
