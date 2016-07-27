package com.swust.weather.util;

import java.util.Comparator;

import com.swust.weather.model.City;


public class PinyinComparator implements Comparator<City> {

    public int compare(City o1, City o2) {
        if (!o1.getEnName().isEmpty() && !o1.getEnName().isEmpty()) {
            if (o1.getEnName().equals("@") || o2.getEnName().equals("#")) { //异常城市的判断 
                return -1;
            } else if (o1.getEnName().equals("#") || o2.getEnName().equals("@")) {
                return 1;
            } else {
                return o1.getEnName().compareTo(o2.getEnName()); //通过城市英文名字母序比较
            }
        }
        return 0;
    }

}
