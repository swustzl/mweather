package com.swust.weather.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class Times {

    /*
     * public static void main(String[] args) {}
     */

    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    public static String[] getWeekToFifteen() {
        String[] weekOfDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        String[] weeks = new String[15];
        weeks[0] = "今天";
        getWeekOfDate(null);
        Calendar calendar = Calendar.getInstance();

        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        for (int i = 1; i < 15; i++) {
            w = w + 1;
            if (w > 6) {
                w = 0;
            }
            weeks[i] = weekOfDays[w];
        }
        return weeks;
    }

    public static String[] getDateToFifteen() {
        String[] dates = new String[15];
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sim = new SimpleDateFormat("M/d");
        for (int i = 0; i < 15; i++) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, i);
            dates[i] = sim.format(calendar.getTime());
        }
        return dates;
    }
    
    public static String getNowDateTime(){
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH");
        return sim.format(new Date());
    }
}
