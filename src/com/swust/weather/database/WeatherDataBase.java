package com.swust.weather.database;

import java.util.ArrayList;
import java.util.List;

import com.swust.weather.model.Alarm;
import com.swust.weather.model.Aqis;
import com.swust.weather.model.Basic;
import com.swust.weather.model.City;
import com.swust.weather.model.Suggestion;
import com.swust.weather.model.WeatherToDaily;
import com.swust.weather.model.WeatherToHourly;
import com.swust.weather.model.WeatherToNow;
import com.swust.weather.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 操作数据库的单例类
 * */
public class WeatherDataBase {
    public static final String DB_NAME = "mweather";
    public static final int VERSION = 1;
    private static WeatherDataBase weatherDataBase;
    private SQLiteDatabase sDatabase;

    private WeatherDataBase(Context context) {
        WeatherSQLiteOpenHelper dbHelper = new WeatherSQLiteOpenHelper(context, DB_NAME, null, VERSION);
        sDatabase = dbHelper.getWritableDatabase();
    }

    public synchronized static WeatherDataBase getInstance(Context context) {
        if (weatherDataBase == null) {
            weatherDataBase = new WeatherDataBase(context);
        }
        return weatherDataBase;
    }

    public void saveAllCity(City city) {
        if (city == null)
            return;
        ContentValues values = new ContentValues();
        values.put("area", city.getArea());
        values.put("city_id", city.getCityId());
        values.put("cn_name", city.getCnName());
        values.put("en_name", city.getEnName());
        values.put("province", city.getProvince());
        sDatabase.insert("all_city", null, values);
    }
    public void saveAllCitys(List<String> sqls){
        sDatabase.beginTransaction();
        for(String sql:sqls){
            sDatabase.execSQL(sql);
        }
        sDatabase.setTransactionSuccessful();
        sDatabase.endTransaction();
    }

    public List<City> findAllCity() {
        List<City> cityList = new ArrayList<City>();
        Cursor cursor = sDatabase.query("all_city", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityId(cursor.getString(cursor.getColumnIndex("city_id")));
                city.setCnName(cursor.getString(cursor.getColumnIndex("cn_name")));
                city.setEnName(cursor.getString(cursor.getColumnIndex("en_name")));
                city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
                city.setArea(cursor.getString(cursor.getColumnIndex("area")));
                cityList.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return cityList;
    }

    public void saveBasic(Basic basic) {
        if (basic == null)
            return;
        ContentValues values = new ContentValues();
        values.put("lat", basic.getLat());
        values.put("lon", basic.getLon());
        values.put("update_time", basic.getUpdateTime());
        if (isHaveBasic(basic.getCityId())) {
            sDatabase.update("basic", values, "city_id=?", new String[] { basic.getCityId() });
            deleteOtherTable(basic.getCityId());
        } else {
            values.put("city_name", basic.getCityName());
            values.put("city_id", basic.getCityId());
            values.put("city_source", basic.getCitySource());
            Long ll = sDatabase.insert("basic", null, values);
            LogUtil.v("basic", ll.toString());
        }
    }

    private boolean isHaveBasic(String cityId) {
        int size = 0;
        Cursor cursor = sDatabase.rawQuery("select count(*) as size from basic where city_id = ?",
                new String[] { cityId });
        if (cursor.moveToFirst()) {
            size = cursor.getInt(cursor.getColumnIndex("size"));
        }
        if (cursor != null) {
            cursor.close();
        }
        return size > 0 ? true : false;
    }

    public Basic findBasicByCityId(String cityId) {
        Basic basic = new Basic();
        Cursor cursor = sDatabase
                .rawQuery("select * from basic where city_id = ? limit 1", new String[] { cityId });
        if (cursor.moveToFirst()) {
            basic.setCityId(cursor.getString(cursor.getColumnIndex("city_id")));
            basic.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
            basic.setCitySource(cursor.getInt(cursor.getColumnIndex("city_source")));
            basic.setId(cursor.getInt(cursor.getColumnIndex("id")));
            basic.setLon(cursor.getString(cursor.getColumnIndex("lon")));
            basic.setLat(cursor.getString(cursor.getColumnIndex("lat")));
            basic.setUpdateTime(cursor.getString(cursor.getColumnIndex("update_time")));
        }
        if (cursor != null) {
            cursor.close();
        }
        return basic;
    }
    

    public int deleteBasicCity(String cityId) {
        deleteOtherTable(cityId);
        return sDatabase.delete("basic", "city_id = ?", new String[] { cityId });
    }
    public void deleteOtherTable(String cityId){
        this.deleteAlarmByCityId(cityId);
        this.deleteAqisByCityId(cityId);
        this.deleteDailyForecastByCityId(cityId);
        this.deleteHourlyForecastByCityId(cityId);
        this.deleteNowByCityId(cityId);
        this.deleteSuggestionByCityId(cityId);
    }

    public List<Basic> findAllBasicCity() {

        List<Basic> basicList = new ArrayList<Basic>();
        Cursor cursor = sDatabase.query("basic", null, null, null, null, null, "id desc");
        if (cursor.moveToFirst()) {
            do {
                Basic basic = new Basic();
                basic.setCityId(cursor.getString(cursor.getColumnIndex("city_id")));
                basic.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                basic.setCitySource(cursor.getInt(cursor.getColumnIndex("city_source")));
                basic.setId(cursor.getInt(cursor.getColumnIndex("id")));
                basic.setLon(cursor.getString(cursor.getColumnIndex("lon")));
                basic.setLat(cursor.getString(cursor.getColumnIndex("lat")));
                basic.setUpdateTime(cursor.getString(cursor.getColumnIndex("update_time")));
                basicList.add(basic);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return basicList;
    }

    
    public void saveAlarm(Alarm alarm) {
        if (alarm == null)
            return;
        ContentValues values = new ContentValues();
        values.put("city_id", alarm.getCityId());
        values.put("level", alarm.getLevel());
        values.put("stat", alarm.getStat());
        values.put("title", alarm.getTitle());
        values.put("txt", alarm.getTxt());
        values.put("type", alarm.getType());
        Long ll = sDatabase.insert("alarms", null, values);
        LogUtil.v("alarm", ll.toString());

    }
    public List<Alarm> findAlarmByCityId(String cityId){
        List<Alarm> dataList = new ArrayList<Alarm>();
        Cursor cursor = sDatabase.query("alarms", null, "city_id=?", new String[]{cityId}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Alarm data = new Alarm();
                data.setCityId(cursor.getString(cursor.getColumnIndex("city_id")));
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setLevel(cursor.getString(cursor.getColumnIndex("level")));
                data.setStat(cursor.getString(cursor.getColumnIndex("stat")));
                data.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                data.setTxt(cursor.getString(cursor.getColumnIndex("txt")));
                data.setType(cursor.getString(cursor.getColumnIndex("type")));
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return dataList;
    }
    public int deleteAlarmByCityId(String cityId) {
        return sDatabase.delete("alarms", "city_id = ?", new String[] { cityId });
    }

    public void saveAqis(Aqis aqis) {
        if (aqis == null)
            return;
        ContentValues values = new ContentValues();
        values.put("city_id", aqis.getCityId());
        values.put("qlty", aqis.getQlty());
        values.put("aqi", aqis.getAqi());
        values.put("co", aqis.getCo());
        values.put("no2", aqis.getNo2());
        values.put("o3", aqis.getO3());
        values.put("pm10", aqis.getPm10());
        values.put("pm25", aqis.getPm25());
        values.put("so2", aqis.getSo2());
        Long ll = sDatabase.insert("aqis", null, values);
        LogUtil.v("aqis", ll.toString());
    }
    public int deleteAqisByCityId(String cityId) {
        return sDatabase.delete("aqis", "city_id = ?", new String[] { cityId });
    }

    
    public List<WeatherToDaily> findDailyForecastByCityId(String cityId){
        List<WeatherToDaily> dailyList = new ArrayList<WeatherToDaily>();
        Cursor cursor = sDatabase.query("daily_forecast", null, "city_id=?", new String[]{cityId}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                WeatherToDaily data = new WeatherToDaily();
                data.setCityId(cursor.getString(cursor.getColumnIndex("city_id")));
                data.setDate(cursor.getString(cursor.getColumnIndex("date")));
                data.setHum(cursor.getInt(cursor.getColumnIndex("hum")));
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setPop(cursor.getInt(cursor.getColumnIndex("pop")));
                data.setPres(cursor.getInt(cursor.getColumnIndex("pres")));
                data.setTmpMax(cursor.getInt(cursor.getColumnIndex("tmp_max")));
                data.setTmpMin(cursor.getInt(cursor.getColumnIndex("tmp_min")));
                data.setWindDeg(cursor.getInt(cursor.getColumnIndex("wind_deg")));
                data.setWindDir(cursor.getString(cursor.getColumnIndex("wind_dir")));
                data.setWindSc(cursor.getString(cursor.getColumnIndex("wind_sc")));
                data.setWindSpd(cursor.getInt(cursor.getColumnIndex("wind_spd")));
                data.setCondCodeDay(cursor.getInt(cursor.getColumnIndex("cond_code_d")));
                data.setCondCodeNight(cursor.getInt(cursor.getColumnIndex("cond_code_n")));
                data.setCondTxtDay(cursor.getString(cursor.getColumnIndex("cond_txt_d")));
                data.setCondTxtNight(cursor.getString(cursor.getColumnIndex("cond_txt_n")));
                data.setPcpn(cursor.getString(cursor.getColumnIndex("pcpn")));
                data.setSr(cursor.getString(cursor.getColumnIndex("sr")));
                data.setSs(cursor.getString(cursor.getColumnIndex("ss")));
                data.setVis(cursor.getInt(cursor.getColumnIndex("vis")));
                dailyList.add(data);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return dailyList;
    }
    public void saveDailyForecast(WeatherToDaily weatherToDaily) {
        if (weatherToDaily == null)
            return;
        ContentValues values = new ContentValues();
        values.put("city_id", weatherToDaily.getCityId());
        values.put("cond_txt_d", weatherToDaily.getCondTxtDay());
        values.put("cond_txt_n", weatherToDaily.getCondTxtNight());
        values.put("date", weatherToDaily.getDate());
        values.put("pcpn", weatherToDaily.getPcpn());
        values.put("sr", weatherToDaily.getSr());
        values.put("ss", weatherToDaily.getSs());
        values.put("wind_dir", weatherToDaily.getWindDir());
        values.put("wind_sc", weatherToDaily.getWindSc());
        values.put("cond_code_d", weatherToDaily.getCondCodeDay());
        values.put("cond_code_n", weatherToDaily.getCondCodeNight());
        values.put("hum", weatherToDaily.getHum());
        values.put("pop", weatherToDaily.getPop());
        values.put("pres", weatherToDaily.getPres());
        values.put("tmp_max", weatherToDaily.getTmpMax());
        values.put("tmp_min", weatherToDaily.getTmpMin());
        values.put("vis", weatherToDaily.getVis());
        values.put("wind_deg", weatherToDaily.getWindDeg());
        values.put("wind_spd", weatherToDaily.getWindSpd());
        Long ll = sDatabase.insert("daily_forecast", null, values);
        LogUtil.v("daily", ll.toString());
    }
    public int deleteDailyForecastByCityId(String cityId) {
        return sDatabase.delete("daily_forecast", "city_id = ?", new String[] { cityId });
    }

    public List<WeatherToHourly> findHourlyForecastByCityId(String cityId){
        List<WeatherToHourly> hourlyList = new ArrayList<WeatherToHourly>();
        Cursor cursor = sDatabase.query("hourly_forecast", null, "city_id=?", new String[]{cityId}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                WeatherToHourly data = new WeatherToHourly();
                data.setCityId(cursor.getString(cursor.getColumnIndex("city_id")));
                data.setDate(cursor.getString(cursor.getColumnIndex("date")));
                data.setHum(cursor.getInt(cursor.getColumnIndex("hum")));
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setPop(cursor.getInt(cursor.getColumnIndex("pop")));
                data.setPres(cursor.getInt(cursor.getColumnIndex("pres")));
                data.setTmp(cursor.getInt(cursor.getColumnIndex("tmp")));
                data.setWindDeg(cursor.getInt(cursor.getColumnIndex("wind_deg")));
                data.setWindDir(cursor.getString(cursor.getColumnIndex("wind_dir")));
                data.setWindSc(cursor.getString(cursor.getColumnIndex("wind_sc")));
                data.setWindSpd(cursor.getInt(cursor.getColumnIndex("wind_spd")));
                hourlyList.add(data);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return hourlyList;
    }
    public void saveHourlyForecast(WeatherToHourly weatherToHourly) {
        if (weatherToHourly == null)
            return;
        ContentValues values = new ContentValues();
        values.put("city_id", weatherToHourly.getCityId());
        values.put("date", weatherToHourly.getDate());
        values.put("wind_dir", weatherToHourly.getWindDir());
        values.put("wind_sc", weatherToHourly.getWindSc());
        values.put("hum", weatherToHourly.getHum());
        values.put("pop", weatherToHourly.getPop());
        values.put("pres", weatherToHourly.getPres());
        values.put("tmp", weatherToHourly.getTmp());
        values.put("wind_deg", weatherToHourly.getWindDeg());
        values.put("wind_spd", weatherToHourly.getWindSpd());
        Long ll = sDatabase.insert("hourly_forecast", null, values);
        LogUtil.v("hourly", ll.toString());
    }
    public int deleteHourlyForecastByCityId(String cityId) {
        return sDatabase.delete("hourly_forecast", "city_id = ?", new String[] { cityId });
    }

    public void saveNowWeather(WeatherToNow weatherToNow) {
        if (weatherToNow == null)
            return;
        ContentValues values = new ContentValues();
        values.put("city_id", weatherToNow.getCityId());
        values.put("cond_txt", weatherToNow.getCondTxt());
        values.put("pcpn", weatherToNow.getPcpn());
        values.put("wind_dir", weatherToNow.getWindDir());
        values.put("wind_sc", weatherToNow.getWindSc());
        values.put("cond_code", weatherToNow.getCondCode());
        values.put("fl", weatherToNow.getFl());
        values.put("hum", weatherToNow.getHum());
        values.put("pres", weatherToNow.getPres());
        values.put("tmp", weatherToNow.getTmp());
        values.put("vis", weatherToNow.getVis());
        values.put("wind_deg", weatherToNow.getWindDeg());
        values.put("wind_spd", weatherToNow.getWindSpd());
        Long ll = sDatabase.insert("now_weather", null, values);
        LogUtil.v("now", ll.toString());
    }

    public int deleteNowByCityId(String cityId) {
        return sDatabase.delete("now_weather", "city_id = ?", new String[] { cityId });
    }
    public WeatherToNow findNowWeather(String cityId) {
        WeatherToNow weatherToNow = new WeatherToNow();
        Cursor cursor = sDatabase.rawQuery("select * from now_weather where city_id = ? limit 1",
                new String[] { cityId });
        if (cursor.moveToFirst()) {
            weatherToNow.setCityId(cursor.getString(cursor.getColumnIndex("city_id")));
            weatherToNow.setCondCode(cursor.getInt(cursor.getColumnIndex("cond_code")));
            weatherToNow.setCondTxt(cursor.getString(cursor.getColumnIndex("cond_txt")));
            weatherToNow.setFl(cursor.getInt(cursor.getColumnIndex("fl")));
            weatherToNow.setHum(cursor.getInt(cursor.getColumnIndex("hum")));
            weatherToNow.setId(cursor.getInt(cursor.getColumnIndex("id")));
            weatherToNow.setPcpn(cursor.getString(cursor.getColumnIndex("pcpn")));
            weatherToNow.setPres(cursor.getInt(cursor.getColumnIndex("pres")));
            weatherToNow.setTmp(cursor.getInt(cursor.getColumnIndex("tmp")));
            weatherToNow.setVis(cursor.getInt(cursor.getColumnIndex("vis")));
            weatherToNow.setWindDeg(cursor.getInt(cursor.getColumnIndex("wind_deg")));
            weatherToNow.setWindDir(cursor.getString(cursor.getColumnIndex("wind_dir")));
            weatherToNow.setWindSc(cursor.getString(cursor.getColumnIndex("wind_sc")));
            weatherToNow.setWindSpd(cursor.getInt(cursor.getColumnIndex("wind_spd")));
        }
        if (cursor != null) {
            cursor.close();
        }
        return weatherToNow;
    }

    public Integer findWeatherCount(String cityId) {
        LogUtil.i("findWeatherCount", cityId);
        Cursor cursor = sDatabase.rawQuery("select count(*) as size from now_weather where city_id = ?",
                new String[] { cityId });
        int size = 0;
        if (cursor.moveToFirst()) {
            size = cursor.getInt(cursor.getColumnIndex("size"));
        }
        if (cursor != null) {
            cursor.close();
        }
        LogUtil.v("size", size + "");
        return size;
    }
    
    public int deleteSuggestionByCityId(String cityId) {
        return sDatabase.delete("suggestion", "city_id = ?", new String[] { cityId });
    }
    public void saveSuggestion(Suggestion suggestion){
        if (suggestion == null)
            return;
        ContentValues values = new ContentValues();
        values.put("city_id", suggestion.getCityId());
        values.put("comf_brf", suggestion.getComfBrf());
        values.put("comf_txt", suggestion.getComfTxt());
        values.put("cw_brf", suggestion.getCwBrf());
        values.put("cw_txt", suggestion.getCwTxt());
        values.put("drsg_brf", suggestion.getDrsgBrf());
        values.put("drsg_txt", suggestion.getDrsgTxt());
        values.put("flu_brf", suggestion.getFluBrf());
        values.put("flu_txt", suggestion.getFluTxt());
        values.put("sport_brf", suggestion.getSportBrf());
        values.put("sport_txt", suggestion.getSportTxt());
        values.put("trav_brf", suggestion.getTravBrf());
        values.put("trav_txt", suggestion.getTravTxt());
        values.put("uv_brf", suggestion.getUvBrf());
        values.put("uv_txt", suggestion.getUvTxt());
        Long ll = sDatabase.insert("suggestion", null, values);
        LogUtil.v("suggestion", ll.toString());
    }
    public Suggestion findSuggestion(String cityId) {
        Suggestion suggestion = new Suggestion();
        Cursor cursor = sDatabase.rawQuery("select * from suggestion where city_id = ? limit 1",
                new String[] { cityId });
        if (cursor.moveToFirst()) {
            suggestion.setCityId(cursor.getString(cursor.getColumnIndex("city_id")));
            suggestion.setComfBrf(cursor.getString(cursor.getColumnIndex("comf_brf")));
            suggestion.setComfTxt(cursor.getString(cursor.getColumnIndex("comf_txt")));
            suggestion.setCwBrf(cursor.getString(cursor.getColumnIndex("cw_brf")));
            suggestion.setCwTxt(cursor.getString(cursor.getColumnIndex("cw_txt")));
            suggestion.setId(cursor.getInt(cursor.getColumnIndex("id")));
            suggestion.setDrsgBrf(cursor.getString(cursor.getColumnIndex("drsg_brf")));
            suggestion.setDrsgTxt(cursor.getString(cursor.getColumnIndex("drsg_txt")));
            suggestion.setFluBrf(cursor.getString(cursor.getColumnIndex("flu_brf")));
            suggestion.setFluTxt(cursor.getString(cursor.getColumnIndex("flu_txt")));
            suggestion.setSportBrf(cursor.getString(cursor.getColumnIndex("sport_brf")));
            suggestion.setSportTxt(cursor.getString(cursor.getColumnIndex("sport_txt")));
            suggestion.setTravBrf(cursor.getString(cursor.getColumnIndex("trav_brf")));
            suggestion.setTravTxt(cursor.getString(cursor.getColumnIndex("trav_txt")));
            suggestion.setUvBrf(cursor.getString(cursor.getColumnIndex("uv_brf")));
            suggestion.setUvTxt(cursor.getString(cursor.getColumnIndex("uv_txt")));
        }
        if (cursor != null) {
            cursor.close();
        }
        return suggestion;
    }
}
