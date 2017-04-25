package com.qcwy.chaowei.myapplication.entity;

/**
 * Created by KouKi on 2017/3/2.
 */
public class Part {
    private int part_id;
    private int classify;
    private String name;
    private String image;

    public int getPart_id() {
        return part_id;
    }

    public void setPart_id(int part_id) {
        this.part_id = part_id;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
