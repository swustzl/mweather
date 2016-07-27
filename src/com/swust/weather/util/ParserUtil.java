package com.swust.weather.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * import com.alibaba.fastjson.JSON; import com.alibaba.fastjson.JSONArray; import com.alibaba.fastjson.JSONObject;
 * import com.alibaba.fastjson.parser.Feature;
 */
import com.swust.weather.database.WeatherDataBase;
import com.swust.weather.model.Alarm;
import com.swust.weather.model.Aqis;
import com.swust.weather.model.Basic;
import com.swust.weather.model.City;
import com.swust.weather.model.Suggestion;
import com.swust.weather.model.WeatherToDaily;
import com.swust.weather.model.WeatherToHourly;
import com.swust.weather.model.WeatherToNow;

import android.text.TextUtils;

/**
 * 数据解析工具类
 */
public class ParserUtil {

    /**
     * 解析返回的所有城市数据
     * 
     * @throws JSONException
     */
    public static Boolean handleCitiesResponse(WeatherDataBase weatherDataBase, String response) {
        if (!TextUtils.isEmpty(response)) {
            LogUtil.v("response", response);
            long start = System.currentTimeMillis();
            // JSONObject json = JSON.parseObject(response);
            JSONObject json;
            try {
                json = new JSONObject(response);

                if (json.getString("reason").equals("Succes")) {
                    JSONArray ja = json.getJSONArray("result");
                    List<String> sqls = new ArrayList<String>();

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        String c = "insert into all_city(city_id,cn_name,en_name,area,province) values('"
                                + jo.getString("city_id") + "','" + jo.getString("cn_name") + "','"
                                + jo.getString("en_name") + "','" + jo.getString("area") + "','"
                                + jo.getString("province") + "')";
                        sqls.add(c);
                        /*
                         * City city = new City(); city.setArea(jo.getString("area"));
                         * city.setCityId(jo.getString("city_id")); city.setCnName(jo.getString("cn_name"));
                         * city.setEnName(jo.getString("en_name")); city.setProvince(jo.getString("province"));
                         * weatherDataBase.saveAllCity(city);
                         */
                    }
                    LogUtil.v("savetimes1:", (System.currentTimeMillis() - start) + "");
                    weatherDataBase.saveAllCitys(sqls);
                    long end = System.currentTimeMillis();
                    LogUtil.v("savetimes:", (end - start) + "");
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析返回的天气数据
     * 
     * @param flag 是否是定位，true 是
     * @throws JSONException
     */
    public static Boolean handleDataResponse(WeatherDataBase weatherDataBase, String response, Boolean flag) {
        if (!TextUtils.isEmpty(response)) {
            LogUtil.v("response", response);
            // JSONObject json = JSON.parseObject(response,Feature.InitStringFieldAsEmpty);
            JSONObject json;
            try {
                json = new JSONObject(response);

                if (json.getString("reason").equals("Succes")) {
                    JSONObject resultJson = json.getJSONObject("result");

                    JSONObject joBasic = resultJson.getJSONObject("basic");
                    Basic basic = new Basic();
                    basic.setCityId(joBasic.getString("id"));
                    basic.setCityName(joBasic.getString("city"));
                    if (flag) {
                        basic.setCitySource(1);
                    } else {
                        basic.setCitySource(0);
                    }
                    basic.setLat(joBasic.getString("lat"));
                    basic.setLon(joBasic.getString("lon"));
                    basic.setUpdateTime(joBasic.getJSONObject("update").getString("loc"));
                    weatherDataBase.saveBasic(basic);

                    JSONArray ja = resultJson.getJSONArray("alarms");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        Alarm alarm = new Alarm();
                        alarm.setCityId(basic.getCityId());
                        alarm.setLevel(jo.getString("level"));
                        alarm.setStat(jo.getString("stat"));
                        alarm.setTitle(jo.getString("title"));
                        alarm.setTxt(jo.getString("txt"));
                        alarm.setType(jo.getString("type"));
                        weatherDataBase.saveAlarm(alarm);
                    }

                    if (resultJson.has("aqi") && resultJson.getJSONObject("aqi").has("city")) {
                        JSONObject joAqi = resultJson.getJSONObject("aqi").getJSONObject("city");
                        Aqis aqis = new Aqis();
                        aqis.setCityId(basic.getCityId());
                        aqis.setAqi(joAqi.optInt("aqi"));
                        aqis.setCo(joAqi.optInt("co"));
                        aqis.setNo2(joAqi.optInt("no2"));
                        aqis.setO3(joAqi.optInt("o3"));
                        aqis.setPm10(joAqi.optInt("pm10"));
                        aqis.setPm25(joAqi.optInt("pm25"));
                        aqis.setQlty(joAqi.optString("qlty"));
                        aqis.setSo2(joAqi.optInt("aqi"));
                        weatherDataBase.saveAqis(aqis);
                    }

                    JSONArray jaDaily = resultJson.getJSONArray("daily_forecast");
                    for (int i = 0; i < jaDaily.length(); i++) {
                        JSONObject o = jaDaily.getJSONObject(i);
                        WeatherToDaily weatherToDaily = new WeatherToDaily();
                        weatherToDaily.setCityId(basic.getCityId());
                        weatherToDaily.setDate(o.getString("date"));
                        weatherToDaily.setHum(o.getInt("hum"));
                        weatherToDaily.setPcpn(o.getString("pcpn"));
                        weatherToDaily.setPop(o.getInt("pop"));
                        weatherToDaily.setPres(o.getInt("pres"));
                        weatherToDaily.setVis(o.getInt("vis"));
                        weatherToDaily.setSr(o.getJSONObject("astro").getString("sr"));
                        weatherToDaily.setSs(o.getJSONObject("astro").getString("ss"));
                        weatherToDaily.setCondCodeDay(o.getJSONObject("cond").getInt("code_d"));
                        weatherToDaily.setCondCodeNight(o.getJSONObject("cond").getInt("code_n"));
                        weatherToDaily.setCondTxtDay(o.getJSONObject("cond").getString("txt_d"));
                        weatherToDaily.setCondTxtNight(o.getJSONObject("cond").getString("txt_n"));
                        weatherToDaily.setTmpMax(o.getJSONObject("tmp").getInt("max"));
                        weatherToDaily.setTmpMin(o.getJSONObject("tmp").getInt("min"));
                        weatherToDaily.setWindDeg(o.getJSONObject("wind").getInt("deg"));
                        weatherToDaily.setWindDir(o.getJSONObject("wind").getString("dir"));
                        weatherToDaily.setWindSc(o.getJSONObject("wind").getString("sc"));
                        weatherToDaily.setWindSpd(o.getJSONObject("wind").getInt("spd"));
                        weatherDataBase.saveDailyForecast(weatherToDaily);
                    }

                    JSONArray jaHourly = resultJson.getJSONArray("hourly_forecast");
                    for (int i = 0; i < jaHourly.length(); i++) {
                        JSONObject o = jaHourly.getJSONObject(i);
                        WeatherToHourly weatherToHourly = new WeatherToHourly();
                        weatherToHourly.setCityId(basic.getCityId());
                        weatherToHourly.setDate(o.getString("date"));
                        weatherToHourly.setHum(o.getInt("hum"));
                        weatherToHourly.setPop(o.getInt("pop"));
                        weatherToHourly.setPres(o.getInt("pres"));
                        weatherToHourly.setTmp(o.getInt("tmp"));
                        weatherToHourly.setWindDeg(o.getJSONObject("wind").getInt("deg"));
                        weatherToHourly.setWindDir(o.getJSONObject("wind").getString("dir"));
                        weatherToHourly.setWindSc(o.getJSONObject("wind").getString("sc"));
                        weatherToHourly.setWindSpd(o.getJSONObject("wind").getInt("spd"));
                        weatherDataBase.saveHourlyForecast(weatherToHourly);
                    }

                    if (resultJson.has("now")) {
                        JSONObject jaNow = resultJson.getJSONObject("now");
                        WeatherToNow weatherToNow = new WeatherToNow();
                        weatherToNow.setCityId(basic.getCityId());
                        weatherToNow.setCondCode(jaNow.getJSONObject("cond").getInt("code"));
                        weatherToNow.setCondTxt(jaNow.getJSONObject("cond").getString("txt"));
                        weatherToNow.setFl(jaNow.getInt("fl"));
                        weatherToNow.setHum(jaNow.getInt("hum"));
                        weatherToNow.setPcpn(jaNow.getString("pcpn"));
                        weatherToNow.setPres(jaNow.getInt("pres"));
                        weatherToNow.setTmp(jaNow.getInt("tmp"));
                        weatherToNow.setVis(jaNow.getInt("vis"));
                        weatherToNow.setWindDeg(jaNow.getJSONObject("wind").getInt("deg"));
                        weatherToNow.setWindDir(jaNow.getJSONObject("wind").getString("dir"));
                        weatherToNow.setWindSc(jaNow.getJSONObject("wind").getString("sc"));
                        weatherToNow.setWindSpd(jaNow.getJSONObject("wind").getInt("spd"));
                        weatherDataBase.saveNowWeather(weatherToNow);
                    }
                    if (resultJson.has("suggestion")) {
                        JSONObject jo = resultJson.getJSONObject("suggestion");
                        Suggestion suggestion = new Suggestion();
                        suggestion.setCityId(basic.getCityId());
                        suggestion.setComfBrf(jo.getJSONObject("comf").getString("brf"));
                        suggestion.setComfTxt(jo.getJSONObject("comf").getString("txt"));
                        suggestion.setCwBrf(jo.getJSONObject("cw").getString("brf"));
                        suggestion.setCwTxt(jo.getJSONObject("cw").getString("txt"));
                        suggestion.setDrsgBrf(jo.getJSONObject("drsg").getString("brf"));
                        suggestion.setDrsgTxt(jo.getJSONObject("drsg").getString("txt"));
                        suggestion.setFluBrf(jo.getJSONObject("flu").getString("brf"));
                        suggestion.setFluTxt(jo.getJSONObject("flu").getString("txt"));
                        suggestion.setSportBrf(jo.getJSONObject("sport").getString("brf"));
                        suggestion.setSportTxt(jo.getJSONObject("sport").getString("txt"));
                        suggestion.setTravBrf(jo.getJSONObject("trav").getString("brf"));
                        suggestion.setTravTxt(jo.getJSONObject("trav").getString("txt"));
                        suggestion.setUvBrf(jo.getJSONObject("uv").getString("brf"));
                        suggestion.setUvTxt(jo.getJSONObject("uv").getString("txt"));
                        weatherDataBase.saveSuggestion(suggestion);
                    }
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }
}
