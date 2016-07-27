package com.swust.weather.model;

public class WeatherToDaily {
    private int id;
    private String cityId;//城市代码
    private String date;//日期
    private int hum;//相对湿度
    private String pcpn;//降水量
    private int pop;//降水概率
    private int pres;//气压
    private int vis;//能见度
    private String sr;//日出时间
    private String ss;//日落时间
    private int condCodeDay;//白天天气状况代码
    private int condCodeNight;//晚上天气状况代码
    private String condTxtDay;//白天天气状况文字
    private String condTxtNight;//晚上天气状况文字
    private int tmpMax;//最高温度
    private int tmpMin;//最低温度
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
    public String getPcpn() {
        return pcpn;
    }
    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
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
    public int getVis() {
        return vis;
    }
    public void setVis(int vis) {
        this.vis = vis;
    }
    public String getSr() {
        return sr;
    }
    public void setSr(String sr) {
        this.sr = sr;
    }
    public String getSs() {
        return ss;
    }
    public void setSs(String ss) {
        this.ss = ss;
    }
    public int getCondCodeDay() {
        return condCodeDay;
    }
    public void setCondCodeDay(int condCodeDay) {
        this.condCodeDay = condCodeDay;
    }
    public int getCondCodeNight() {
        return condCodeNight;
    }
    public void setCondCodeNight(int condCodeNight) {
        this.condCodeNight = condCodeNight;
    }
    public String getCondTxtDay() {
        return condTxtDay;
    }
    public void setCondTxtDay(String condTxtDay) {
        this.condTxtDay = condTxtDay;
    }
    public String getCondTxtNight() {
        return condTxtNight;
    }
    public void setCondTxtNight(String condTxtNight) {
        this.condTxtNight = condTxtNight;
    }
    public int getTmpMax() {
        return tmpMax;
    }
    public void setTmpMax(int tmpMax) {
        this.tmpMax = tmpMax;
    }
    public int getTmpMin() {
        return tmpMin;
    }
    public void setTmpMin(int tmpMin) {
        this.tmpMin = tmpMin;
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
