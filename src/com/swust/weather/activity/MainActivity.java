package com.swust.weather.activity;

import java.util.ArrayList;
import java.util.List;

import com.swust.weather.R;
import com.swust.weather.customview.ChartLineView;
import com.swust.weather.customview.RecommendItemView;
import com.swust.weather.database.WeatherDataBase;
import com.swust.weather.model.Alarm;
import com.swust.weather.model.Basic;
import com.swust.weather.model.ChartDataModel;
import com.swust.weather.model.Suggestion;
import com.swust.weather.model.WeatherToDaily;
import com.swust.weather.model.WeatherToHourly;
import com.swust.weather.model.WeatherToNow;
import com.swust.weather.util.HttpUtil;
import com.swust.weather.util.AutoLocationUtil.LocationData;
import com.swust.weather.util.AutoLocationUtil.LocationDataListener;
import com.swust.weather.util.HttpUtil.HttpResponseListener;
import com.swust.weather.util.AutoLocationUtil;
import com.swust.weather.util.LogUtil;
import com.swust.weather.util.MyApplication;
import com.swust.weather.util.OtherUtil;
import com.swust.weather.util.ParserUtil;
import com.swust.weather.util.Times;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener {

    private static final int M_DATA_DEAL_TRUE = 1;
    private static final int M_DATA_DEAL_ERROR = 2;
    private static final int M_NOT_INTERNET = 3;
    private static final int AUTO_LOCATION_UPDATE_DATA = 4;
    private static final int AUTO_LOCATION_UPDATE_DATA_ERROR = 5;
    private WeatherDataBase weatherDataBase;
    private ProgressDialog progressDialog;
    private WeatherToNow weatherToNow;
    private WeatherToDaily todayWeather = null;
    private WeatherToDaily tomorrowWeather = null;
    private List<WeatherToDaily> dailyList;
    private List<WeatherToHourly> hourlyList;
    private List<Basic> basicList;
    private Basic basic;
    private Message message = new Message();

    private LinearLayout alarmll;
    private Button people;
    private TextView peopleSpeak;
    private List<String> speakList = new ArrayList<String>();

    private Button selectCity;
    private TextView desp;
    private ImageView windDegIv;
    private TextView wind;
    private Button tempButton;
    private TextView titleCityName;
    private ImageView titleLocationIV;
    private ChartLineView hourlyChartView;
    private Drawable dTemp;
    private Button switchOnOff; 
    private Button tmpButton; 
    private TextView hourlyTitle;
    private Button todayButton;
    private Button tomorrowButton;
    private boolean onOffFlag = false;
    private int page = 0;
    private boolean reasonFlag = false;
    private RecommendItemView item1, item2, item3, item4, item5, item6, item7;
    private SwipeRefreshLayout swipeRefreshLayout;
    int index = 0;
    private int windowWidth,windowHeight;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity_layout);
       
        WindowManager wm = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        windowHeight = display.getHeight();
        windowWidth = display.getWidth();
        RelativeLayout mBody = (RelativeLayout) findViewById(R.id.body);
        android.view.ViewGroup.LayoutParams params = mBody.getLayoutParams();
        params.height = windowHeight * 8 / 10;
        params.width = LayoutParams.MATCH_PARENT;
        mBody.setLayoutParams(params);
      
        RelativeLayout title = (RelativeLayout) findViewById(R.id.title);
        title.getBackground().setAlpha(100);
        setViewById();
        setBasic();

        final TextView tv = (TextView) findViewById(R.id.textView1);
        final Button aqi = (Button) findViewById(R.id.aqi);

    }

    private void setBasic() {
        final Boolean isAutoLocation = getIntent().getBooleanExtra("is_auto_location", false);
        basicList = weatherDataBase.findAllBasicCity();
        if (basicList.size() == 0 || isAutoLocation) {
            showProgress();
            AutoLocationUtil util = new AutoLocationUtil(new LocationDataListener() {

                @Override
                public void onFinish(LocationData data) {
                    String url = HttpUtil.encapsulationUrl(data.lat, data.lon);
                    if (!TextUtils.isEmpty(url)) {
                        HttpUtil.sendHttpRequest(url, new HttpResponseListener() {

                            @Override
                            public void onFinish(String response) {
                                reasonFlag = ParserUtil.handleDataResponse(weatherDataBase, response, true);
                                message.what = AUTO_LOCATION_UPDATE_DATA;
                                myHandler.sendMessage(message);
                            }

                            @Override
                            public void onError(Exception e) {
                                LogUtil.e("e", e.toString());
                                message.what = AUTO_LOCATION_UPDATE_DATA_ERROR;
                                myHandler.sendMessage(message);
                            }
                        });
                    }
                }
            });
        } else {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String cityId = pref.getString("city_id", " ");
            if (cityId.equals(" ")) {
                basic = basicList.get(0);
            } else {
                basic = weatherDataBase.findBasicByCityId(cityId);
            }
            maybeUpdateWeather();
        }
    }

    private void setViewById() {
        weatherDataBase = WeatherDataBase.getInstance(this);
        alarmll = (LinearLayout) findViewById(R.id.alarm_ll);
        people = (Button) findViewById(R.id.people);
        peopleSpeak = (TextView) findViewById(R.id.people_yuyan);
        desp = (TextView) findViewById(R.id.desp);
        wind = (TextView) findViewById(R.id.wind);
        tempButton = (Button) findViewById(R.id.temp);
        titleCityName = (TextView) findViewById(R.id.city_name);
        titleLocationIV = (ImageView) findViewById(R.id.title_location);
        windDegIv = (ImageView) findViewById(R.id.wind_deg);
        hourlyChartView = (ChartLineView) findViewById(R.id.hourly_chart);
        hourlyTitle = (TextView) findViewById(R.id.hourly_title);
        switchOnOff = (Button) findViewById(R.id.swith_on_off);
        todayButton = (Button) findViewById(R.id.today_weather_info_button);
        tomorrowButton = (Button) findViewById(R.id.tomrrow_weather_info_button);
        tmpButton = (Button) findViewById(R.id.tmp_bn);
        item1 = (RecommendItemView) findViewById(R.id.item_1);
        item2 = (RecommendItemView) findViewById(R.id.item_2);
        item3 = (RecommendItemView) findViewById(R.id.item_3);
        item4 = (RecommendItemView) findViewById(R.id.item_4);
        item5 = (RecommendItemView) findViewById(R.id.item_5);
        item6 = (RecommendItemView) findViewById(R.id.item_6);
        item7 = (RecommendItemView) findViewById(R.id.item_7);
        selectCity = (Button) findViewById(R.id.title_city);
        selectCity.setOnClickListener(this);
    }

    
    private void maybeUpdateWeather() {
        titleCityName.setText(basic.getCityName());
        if (basic.getCitySource() == 1) {
            titleLocationIV.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
       
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                updateDataByCityName(basic.getCityName());
            }
        });
        if (weatherDataBase.findWeatherCount(basic.getCityId()) == 0
                || !basic.getUpdateTime().substring(0, 10).equals(Times.getNowDateTime().substring(0, 10))) {
           
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            updateDataByCityName(basic.getCityName());
        } else {
            setInfo();
        }

    }

    private void updateDataByCityName(String cityName) {
        HttpUtil.sendHttpRequest(HttpUtil.encapsulationUrl(cityName), new HttpResponseListener() {

            @Override
            public void onFinish(String response) {
                reasonFlag = ParserUtil.handleDataResponse(weatherDataBase, response, false);
                message.what = M_DATA_DEAL_TRUE;
                myHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                message.what = M_NOT_INTERNET;
                myHandler.sendMessage(message);

            }
        });
    }

    private void setInfo() {
        people.setOnClickListener(this);
        tempButton.setOnClickListener(this);
        Suggestion suggestion = weatherDataBase.findSuggestion(basic.getCityId());
        if (suggestion != null) {
            addToSpeakList(suggestion.getComfTxt());
            addToSpeakList(suggestion.getCwTxt());
            addToSpeakList(suggestion.getDrsgTxt());
            addToSpeakList(suggestion.getFluTxt());
            addToSpeakList(suggestion.getSportTxt());
            addToSpeakList(suggestion.getTravTxt());
            addToSpeakList(suggestion.getUvTxt());
        }
        
        List<Alarm> alarms = weatherDataBase.findAlarmByCityId(basic.getCityId());
        for (Alarm alarm : alarms) {
            if (!TextUtils.isEmpty(alarm.getLevel()) && !"null".equals(alarm.getLevel())) {
                Button a = new Button(this);
                a.setTextColor(Color.RED);
                a.setBackgroundResource(R.drawable.aqi_button_style);
                a.setText(alarm.getType() + alarm.getLevel() + "预警");
                a.setId(addToSpeakList(alarm.getTxt()));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 5);
                a.setLayoutParams(lp);
                a.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        LogUtil.v("speak", v.getId() + "");
                        setSpeakShow(v.getId());
                    }
                });
                alarmll.addView(a);
            }
        }
        weatherToNow = weatherDataBase.findNowWeather(basic.getCityId());
        desp.setText(weatherToNow.getCondTxt());
        wind.setText(weatherToNow.getWindDir() + " " + weatherToNow.getWindSc() + "级");
        windDegIv.setBackgroundResource(OtherUtil.transforWindDeg(weatherToNow.getWindDeg()));
        LogUtil.v("windDeg", weatherToNow.getWindDeg() + "");
        dTemp = new BitmapDrawable(OtherUtil.transformTemp(weatherToNow.getTmp()));
        dTemp.setBounds(0, 0, windowWidth*2/7, windowHeight/10);
        tempButton.setCompoundDrawables(dTemp, null, null, null);

        dailyList = weatherDataBase.findDailyForecastByCityId(basic.getCityId());
        for (int i = 0; i < dailyList.size(); i++) {
            if (dailyList.get(i).getDate().equals(Times.getNowDateTime().substring(0, 10))) {
                todayWeather = dailyList.get(i);
                if (i + 1 < dailyList.size()) {
                    tomorrowWeather = dailyList.get(i + 1);
                }
                page = i;
                break;
            }
        }

        if (todayWeather != null) {
            TextView todayTmp = (TextView) findViewById(R.id.today_temp);
            TextView todayCond = (TextView) findViewById(R.id.today_desp);
           
            TextView today = (TextView) findViewById(R.id.today);
            today.setText("今天    " + todayWeather.getDate().substring(5));
            todayTmp.setText(todayWeather.getTmpMin() + "/" + todayWeather.getTmpMax() + "℃");
            if (todayWeather.getCondTxtDay().equals(todayWeather.getCondTxtNight())) {
                todayCond.setText(todayWeather.getCondTxtDay());
            } else {
                todayCond.setText(todayWeather.getCondTxtDay() + "转" + todayWeather.getCondTxtNight());
            }
            hourlyTitle.setText("日落 " + todayWeather.getSs());
        }
        todayButton.setOnClickListener(this);
        if (tomorrowWeather != null) {
            TextView tomorrowTmp = (TextView) findViewById(R.id.tomorrow_temp);
            TextView tomorrowCond = (TextView) findViewById(R.id.tomorrow_desp);
            
            TextView tomorrow = (TextView) findViewById(R.id.tomorrow);
            tomorrow.setText("明天    " + tomorrowWeather.getDate().substring(5));
            tomorrowTmp.setText(tomorrowWeather.getTmpMin() + "/" + tomorrowWeather.getTmpMax() + "℃");
            if (tomorrowWeather.getCondTxtDay().equals(tomorrowWeather.getCondTxtNight())) {
                tomorrowCond.setText(tomorrowWeather.getCondTxtDay());
            } else {
                tomorrowCond.setText(tomorrowWeather.getCondTxtDay() + "转" + tomorrowWeather.getCondTxtNight());
            }
        }
        tomorrowButton.setOnClickListener(this);
        hourlyList = weatherDataBase.findHourlyForecastByCityId(basic.getCityId());
        ChartDataModel chartData = new ChartDataModel();
        for (int i = 0; i < hourlyList.size(); i++) {
            chartData.hData.add(hourlyList.get(i).getDate());
            chartData.vData.add(hourlyList.get(i).getTmp() + "");
            if (hourlyList.get(i).getTmp() > chartData.hMax) {
                chartData.hMax = hourlyList.get(i).getTmp();
            }
            if (hourlyList.get(i).getTmp() < chartData.hMin) {
                chartData.hMin = hourlyList.get(i).getTmp();
            }
        }
        hourlyChartView.SetInfo(chartData);

        tmpButton.setVisibility(View.GONE);
        switchOnOff.setOnClickListener(this);
       
        if (suggestion != null) {
            item1.setIndexInfoText(suggestion.getComfBrf());
            item2.setIndexInfoText(suggestion.getFluBrf());
            item3.setIndexInfoText(suggestion.getDrsgBrf());
            item4.setIndexInfoText(suggestion.getSportBrf());
            item5.setIndexInfoText(suggestion.getCwBrf());
            item6.setIndexInfoText(suggestion.getTravBrf());
            item7.setIndexInfoText(suggestion.getUvBrf());
        }
        item1.setImgById(R.drawable.index_shushidu);
        item1.setOnClickListener(this);
        item2.setImgById(R.drawable.index_ganmao);
        item2.setOnClickListener(this);
        item3.setImgById(R.drawable.index_chuanyi);
        item3.setOnClickListener(this);
        item4.setImgById(R.drawable.index_yundong);
        item4.setOnClickListener(this);
        item5.setImgById(R.drawable.index_xiche);
        item5.setOnClickListener(this);
        item6.setImgById(R.drawable.index_lvxing);
        item6.setOnClickListener(this);
        item7.setImgById(R.drawable.index_ziwaixian);
        item7.setOnClickListener(this);

    }

    private void setSpeakShow(int nowIndex) {
        if (speakList.size() == 0) {
            peopleSpeak.setVisibility(View.GONE);
        } else if (nowIndex >= speakList.size()) {
            peopleSpeak.setVisibility(View.VISIBLE);
            peopleSpeak.setText(speakList.get(0));
            index = 1;
        } else {
            peopleSpeak.setVisibility(View.VISIBLE);
            peopleSpeak.setText(speakList.get(nowIndex));
            index = nowIndex + 1;
        }
    }

    private int addToSpeakList(String txt) {
        int len = txt.length();
        int i = 1, pLen = 0;
        String str = "";
        while ((pLen = len / i) > 15) {
            i++;
        }
        for (int j = 0; j < i; j++) {
            str = str + txt.substring(j * pLen, j * pLen + pLen) + "\n";
        }
        speakList.add(str);
        return speakList.size() - 1;
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case M_DATA_DEAL_TRUE:
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                setInfo();
                break;
            case M_DATA_DEAL_ERROR:
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                Toast.makeText(getApplication(), "数据加载异常", Toast.LENGTH_SHORT).show();
                break;
            case M_NOT_INTERNET:
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                Toast.makeText(getApplication(), "数据请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                break;
            case AUTO_LOCATION_UPDATE_DATA:
                closeProgress();
                if (reasonFlag) {
                    basicList = weatherDataBase.findAllBasicCity();
                    for (Basic c : basicList) {
                        if (c.getCitySource() == 1) {
                            basic = c;
                        }
                    }
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                            MyApplication.getContext()).edit();
                    editor.putString("city_id", basic.getCityId());
                    editor.commit();
                    maybeUpdateWeather();
                } else {
                    Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
                    startActivity(intent);
                }
                break;
            case AUTO_LOCATION_UPDATE_DATA_ERROR:
                closeProgress();
                Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
                startActivity(intent);
                Toast.makeText(getApplication(), "定位更新数据失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                break;
            }
            super.handleMessage(msg);
        }
    };

 
    protected void closeProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

  
    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("定位加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
        case R.id.item_1:
            intent = new Intent(MainActivity.this, RecommendInfoActivity.class);
            intent.putExtra("suggestion_type", 1);
            startActivity(intent);
            break;
        case R.id.item_2:
            intent = new Intent(MainActivity.this, RecommendInfoActivity.class);
            intent.putExtra("suggestion_type", 2);
            startActivity(intent);
            break;
        case R.id.item_3:
            intent = new Intent(MainActivity.this, RecommendInfoActivity.class);
            intent.putExtra("suggestion_type", 3);
            startActivity(intent);
            break;
        case R.id.item_4:
            intent = new Intent(MainActivity.this, RecommendInfoActivity.class);
            intent.putExtra("suggestion_type", 4);
            startActivity(intent);
            break;
        case R.id.item_5:
            intent = new Intent(MainActivity.this, RecommendInfoActivity.class);
            intent.putExtra("suggestion_type", 5);
            startActivity(intent);
            break;
        case R.id.item_6:
            intent = new Intent(MainActivity.this, RecommendInfoActivity.class);
            intent.putExtra("suggestion_type", 6);
            startActivity(intent);
            break;
        case R.id.item_7:
            intent = new Intent(MainActivity.this, RecommendInfoActivity.class);
            intent.putExtra("suggestion_type", 7);
            startActivity(intent);
            break;
        case R.id.title_city:
            intent = new Intent(MainActivity.this, BasicCityActivity.class);
            startActivity(intent);
            break;
        case R.id.temp:
            intent = new Intent(MainActivity.this, ShowNowInfoActivity.class);
            if (weatherToNow != null) {
                intent.putExtra("data_is_not_null", true);
                intent.putExtra("city_id", weatherToNow.getCityId());
                intent.putExtra("cond_txt", weatherToNow.getCondTxt());
                intent.putExtra("fl", weatherToNow.getFl());
                intent.putExtra("hum", weatherToNow.getHum());
                intent.putExtra("pcpn", weatherToNow.getPcpn());
                intent.putExtra("pres", weatherToNow.getPres());
                intent.putExtra("tmp", weatherToNow.getTmp());
                intent.putExtra("vis", weatherToNow.getVis());
                intent.putExtra("wind", weatherToNow.getWindDir() + " " + weatherToNow.getWindSc());
            } else {
                intent.putExtra("data_is_not_null", false);
            }
            startActivity(intent);
            break;
        case R.id.today_weather_info_button:
            intent = new Intent(MainActivity.this, DailyActivity.class);
            intent.putExtra("city_name", basic.getCityName());
            intent.putExtra("page", page);
            startActivity(intent);
            break;
        case R.id.tomrrow_weather_info_button:
            intent = new Intent(MainActivity.this, DailyActivity.class);
            intent.putExtra("city_name", basic.getCityName());
            intent.putExtra("page", page + 1);
            startActivity(intent);
            break;
        case R.id.people:
            setSpeakShow(index);
            break;
        case R.id.swith_on_off:
            if (onOffFlag) {
                onOffFlag = false;
                hourlyChartView.setVisibility(View.GONE);
                tmpButton.setVisibility(View.GONE);
                hourlyTitle.setVisibility(View.VISIBLE);
                switchOnOff.setBackgroundResource(R.drawable.ic_expand_down);
            } else {
                onOffFlag = true;
                hourlyChartView.setVisibility(View.VISIBLE);
                tmpButton.setVisibility(View.VISIBLE);
                hourlyTitle.setVisibility(View.GONE);
                switchOnOff.setBackgroundResource(R.drawable.ic_expand_up);
            }
            break;

        }

    }

}
