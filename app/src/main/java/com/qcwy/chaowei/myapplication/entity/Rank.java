package com.qcwy.chaowei.myapplication.entity;

/**
 * Created by KouKi on 2017/3/22.
 */
public class Rank {
    private String job_no;
    private String name;
    private double score;
    private int rank;

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
