package com.swust.weather.model;

import java.util.ArrayList;
import java.util.List;

public class ChartDataModel {
    public List<String> hData;
    public List<String> vData;
    public int hMax;
    public int hMin;
    
    public ChartDataModel(){
        hData = new ArrayList<String>();
        vData = new ArrayList<String>();
        hMax = -99;
        hMin = 99;
    }
}
