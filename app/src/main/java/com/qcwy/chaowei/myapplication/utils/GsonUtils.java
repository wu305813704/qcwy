package com.qcwy.chaowei.myapplication.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by KouKi on 2017/3/14.
 */

public class GsonUtils {
    private static Gson gson;

    public synchronized static Gson getInstance() {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        return gson;
    }
}
