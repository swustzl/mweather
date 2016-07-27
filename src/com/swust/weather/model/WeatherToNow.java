package com.swust.weather.model;

public class WeatherToNow {
    private int id;
    private String cityId;//城市代码
    private int condCode;//天气状况代码
    private String condTxt;//天气状况信息
    private int fl;//体感温度
    private int hum;//相对湿度
    private String pcpn;//降水量
    private int pres;//气压
    private int tmp;//温度
    private int vis;//能见度
    private int windDeg;//风向（360度）
    private String windDir;//风向
    private String windSc;//风力
    private int windSpd;//风速
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
    public int getCondCode() {
        return condCode;
    }
    public void setCondCode(int condCode) {
        this.condCode = condCode;
    }
    public String getCondTxt() {
        return condTxt;
    }
    public void setCondTxt(String condTxt) {
        this.condTxt = condTxt;
    }
    public int getFl() {
        return fl;
    }
    public void setFl(int fl) {
        this.fl = fl;
    }
    public int getHum() {
        return hum;
    }
    public void setHum(int hum) {
        this.hum = hum;
    }
    public String getPcpn() {
        return pcpn;
    }
    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
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
    public int getVis() {
        return vis;
    }
    public void setVis(int vis) {
        this.vis = vis;
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
