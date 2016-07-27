package com.swust.weather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherSQLiteOpenHelper extends SQLiteOpenHelper{
    
    private static final String CREATE_NOW_WEATHER = "create table now_weather("
            + "id integer primary key autoincrement,"
            + "city_id text,"
            + "cond_code integer,"
            + "cond_txt text,"
            + "fl integer,"
            + "hum integer,"
            + "pcpn text,"
            + "pres integer,"
            + "tmp integer,"
            + "vis integer,"
            + "wind_deg integer,"
            + "wind_dir text,"
            + "wind_sc text,"
            + "wind_spd integer)";
    
    private static final String CREATE_ALLCITY = "create table all_city("
            + "id integer primary key autoincrement,"
            + "city_id text,"
            + "cn_name text,"
            + "en_name text,"
            + "area text,"
            + "province text)";
    private static final String CREATE_BASIC = "create table basic("
            + "id integer primary key autoincrement,"
            + "city_name text,"
            + "city_id text,"
            + "lat text,"
            + "lon text,"
            + "city_source integer,"
            + "update_time text)";
    
    private static final String CREATE_ALARMS = "create table alarms("
            + "id integer primary key autoincrement,"
            + "city_id text,"
            + "level text,"
            + "stat text,"
            + "title text,"
            + "txt text,"
            + "type text)";
    
    private static final String CREATE_AQIS = "create table aqis("
            + "id integer primary key autoincrement,"
            + "city_id text,"
            + "aqi integer,"
            + "co integer,"
            + "no2 integer,"
            + "o3 integer,"
            + "pm25 integer,"
            + "pm10 integer,"
            + "so2 integer,"
            + "qlty text)";
    private static final String CREATE_DAILY_FORECAST = "create table daily_forecast("
            + "id integer primary key autoincrement,"
            + "city_id text,"
            + "date text,"
            + "hum integer,"
            + "pcpn text,"
            + "pop integer,"
            + "pres integer,"
            + "vis integer,"
            + "sr text,"
            + "ss text,"
            + "cond_code_d integer,"
            + "cond_code_n integer,"
            + "cond_txt_d text,"
            + "cond_txt_n text,"
            + "tmp_max integer,"
            + "tmp_min integer,"
            + "wind_deg integer,"
            + "wind_dir text,"
            + "wind_sc text,"
            + "wind_spd integer)";
    private static final String CREATE_HOURLY_FORECAST = "create table hourly_forecast("
            + "id integer primary key autoincrement,"
            + "city_id text,"
            + "date text,"
            + "hum integer,"
            + "pop integer,"
            + "pres integer,"
            + "tmp integer,"
            + "wind_deg integer,"
            + "wind_dir text,"
            + "wind_sc text,"
            + "wind_spd integer)";
    private static final String CREATE_SUGGESTION = "create table suggestion("
            + "id integer primary key autoincrement,"
            + "city_id text,"
            + "comf_brf text,"
            + "comf_txt text,"
            + "cw_brf text,"
            + "cw_txt text,"
            + "drsg_brf text,"
            + "drsg_txt text,"
            + "flu_brf text,"
            + "flu_txt text,"
            + "sport_brf text,"
            + "sport_txt text,"
            + "trav_brf text,"
            + "trav_txt text,"
            + "uv_brf text,"
            + "uv_txt text)";
    public WeatherSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOW_WEATHER);
        db.execSQL(CREATE_ALLCITY);
        db.execSQL(CREATE_BASIC);
        db.execSQL(CREATE_ALARMS);
        db.execSQL(CREATE_AQIS);
        db.execSQL(CREATE_DAILY_FORECAST);
        db.execSQL(CREATE_HOURLY_FORECAST);
        db.execSQL(CREATE_SUGGESTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

}
