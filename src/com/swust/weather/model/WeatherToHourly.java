package com.swust.weather.model;

public class WeatherToHourly {
    private int id;
    private String cityId;
    private String date;
    private int hum;
    private int pop;
    private int pres;
    private int tmp;
    private int windDeg;
    private String windDir;
    private String windSc;
    private int windSpd;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCityId() {
        return cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getHum() {
        return hum;
    }
    public void setHum(int hum) {
        this.hum = hum;
    }
    public int getPop() {
        return pop;
    }
    public void setPop(int pop) {
        this.pop = pop;
    }
    public int getPres() {
        return pres;
    }
    public void setPres(int pres) {
        this.pres = pres;
    }
    public int getTmp() {
        return tmp;
    }
    public void setTmp(int tmp) {
        this.tmp = tmp;
    }
    public int getWindDeg() {
        return windDeg;
    }
    public void setWindDeg(int windDeg) {
        this.windDeg = windDeg;
    }
    public String getWindDir() {
        return windDir;
    }
    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }
    public String getWindSc() {
        return windSc;
    }
    public void setWindSc(String windSc) {
        this.windSc = windSc;
    }
    public int getWindSpd() {
        return windSpd;
    }
    public void setWindSpd(int windSpd) {
        this.windSpd = windSpd;
    }

}
