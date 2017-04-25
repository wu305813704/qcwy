package com.qcwy.chaowei.myapplication.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.ui.IllegalRecordActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static SimpleDateFormat sdf;
    //日历显示最小时间
    public static long startTime = System.currentTimeMillis();
    //日历显示最大时间
    public static long endTime = System.currentTimeMillis();

    public static String format(Date date, String format) {
        sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parse(String date, String format) throws ParseException {
        sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    //设置ui控件的时间
    public static void selectDate(final Context context, final TextView tv, final CommonCallback callback) {
        String date = tv.getText().toString();
        String[] dates = date.split("-");
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tv.setText(new StringBuilder()
                                .append(year)
                                .append("-")
                                .append(month < 9 ? "0" + (month + 1) : month + 1)
                                .append("-")
                                .append(dayOfMonth < 9 ? "0" + dayOfMonth : dayOfMonth));
                        callback.onSeccess();
                        try {
                            switch (tv.getId()) {
                                case R.id.tv_start_date:
                                    startTime = DateUtils.parse(tv.getText().toString(), "yyyy-MM-dd").getTime();
                                    break;
                                case R.id.tv_end_date:
                                    endTime = DateUtils.parse(tv.getText().toString(), "yyyy-MM-dd").getTime() + 24 * 60 * 1000;
                                    break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, Integer.valueOf(dates[0]), Integer.valueOf(dates[1]) - 1, Integer.valueOf(dates[2]));
        switch (tv.getId()) {
            case R.id.tv_date:
                //如果是单时间，则只设置最大时间为当前时间
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 24 * 60 * 1000);
                break;
            case R.id.tv_start_date:
                //如果是开始时间，设置最大时间为结束时间
                datePickerDialog.getDatePicker().setMaxDate(endTime);
                break;
            case R.id.tv_end_date:
                //如果是结束时间，设置最小时间为开始时间
                datePickerDialog.getDatePicker().setMinDate(startTime + 24 * 60 * 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 24 * 60 * 1000);
                break;
        }
        datePickerDialog.show();
    }

    //第三方时间选择器，时间范围不能精确到天
    public static void selectDate1(final Context context, final TextView tv) {
        //时间选择器
        TimePickerView pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH);
        //是否循环滚动
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tv.setText(format(date, "yyyy年MM月"));
            }
        });
        //设置最大日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果
        try {
            pvTime.setTime(parse(tv.getText().toString(), "yyyy年MM月"));
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            //弹出时间选择器
            pvTime.show();
        }
    }
}
