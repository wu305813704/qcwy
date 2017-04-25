package com.qcwy.chaowei.myapplication.utils;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 此类可以摒弃，可在catch中使用PrintForLog类在日志中打印详情
 */
public abstract class BizStep implements Callable<Object> {
    private int no;
    private List<Object> list;
    private Exception exception;

    public abstract void flow();

    @Override
    public Object call() {
        try {
            flow();
            return true;
        } catch (Exception e) {
            setException(e);
            return false;
        }
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

}

