package com.swust.weather.activity;

import java.util.ArrayList;
import java.util.List;

import com.swust.weather.R;
import com.swust.weather.database.WeatherDataBase;
import com.swust.weather.model.WeatherToDaily;
import com.swust.weather.util.LogUtil;
import com.swust.weather.util.MyApplication;
import com.swust.weather.util.Times;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class DailyActivity extends Activity {

    private TextView titleName;
    private WeatherDataBase weatherDataBase;
    private ViewPager pager = null;
    private PagerTabStrip tabStrip = null;
    private List<View> viewContainter = new ArrayList<View>();
    private List<String> titleContainer = new ArrayList<String>();
    private List<WeatherToDaily> dataList = null;
    public String TAG = "tag";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.daily_activity_layout);
        weatherDataBase = WeatherDataBase.getInstance(this);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String cityId = pref.getString("city_id", " ");
        String cityName = getIntent().getStringExtra("city_name");
        int page = getIntent().getIntExtra("page", 0);
        dataList = weatherDataBase.findDailyForecastByCityId(cityId);
        titleName = (TextView) findViewById(R.id.daily_activity_title);
        if (!TextUtils.isEmpty(cityName)) {
            titleName.setText(cityName);
        }
        Button backButton = (Button) findViewById(R.id.daily_activity_back);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DailyActivity.this.onBackPressed();
            }
        });
        pager = (ViewPager) this.findViewById(R.id.viewpager);
        tabStrip = (PagerTabStrip) this.findViewById(R.id.tabstrip);
     
        tabStrip.setDrawFullUnderline(false);
    
        tabStrip.setBackgroundColor(0);
   
        tabStrip.setTabIndicatorColor(Color.RED);

        WindowManager wm = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int windowHeight = display.getHeight();
        
        
        for (int i = 0; i < dataList.size(); i++) {
       
            View view = LayoutInflater.from(this).inflate(R.layout.day_weather_layout, null); 
     
            RelativeLayout mrl = (RelativeLayout) view.findViewById(R.id.rl_day_weather);
            android.view.ViewGroup.LayoutParams params = mrl.getLayoutParams();
            params.height = windowHeight/2;
            params.width = LayoutParams.MATCH_PARENT;
            mrl.setLayoutParams(params);
            TextView dayTmp = (TextView) view.findViewById(R.id.day_tmp);
            TextView dayCond = (TextView) view.findViewById(R.id.day_cond);
            TextView daySr= (TextView) view.findViewById(R.id.astro_sr_data);
            TextView daySs= (TextView) view.findViewById(R.id.astro_ss_data);
            TextView dayHum= (TextView) view.findViewById(R.id.day_hum_data);
            TextView dayPcpn= (TextView) view.findViewById(R.id.day_pcpn_data);
            TextView dayPop= (TextView) view.findViewById(R.id.day_pop_data);
            TextView dayPres= (TextView) view.findViewById(R.id.day_pres_data);
            TextView dayVis= (TextView) view.findViewById(R.id.day_vis_data);
            TextView dayWindDir= (TextView) view.findViewById(R.id.day_wind_dir_data);
            TextView dayWindSc= (TextView) view.findViewById(R.id.day_wind_sc_data);
            TextView dayWindSpd= (TextView) view.findViewById(R.id.day_wind_spd_data);
            daySr.setText(dataList.get(i).getSr());
            daySs.setText(dataList.get(i).getSs());
            dayHum.setText(dataList.get(i).getHum()+"%");
            dayPcpn.setText(dataList.get(i).getPcpn()+"mm");
            dayPop.setText(dataList.get(i).getPop()+"%");
            dayPres.setText(dataList.get(i).getPres()+"hPa");
            dayVis.setText(dataList.get(i).getVis()+"km");
            dayWindDir.setText(dataList.get(i).getWindDir());
            dayWindSc.setText(dataList.get(i).getWindSc());
            dayWindSpd.setText(dataList.get(i).getWindSpd()+"km/h");
            dayTmp.setText(dataList.get(i).getTmpMin() + "/" + dataList.get(i).getTmpMax() + "℃");
            if (dataList.get(i).getCondTxtDay().equals(dataList.get(i).getCondTxtNight())) {
                dayCond.setText(dataList.get(i).getCondTxtDay());
            } else {
                dayCond.setText(dataList.get(i).getCondTxtDay() + "转" + dataList.get(i).getCondTxtNight());
            }
            viewContainter.add(view);
            if (dataList.get(i).getDate().equals(Times.getNowDateTime().substring(0, 10))) {
                titleContainer.add("今 天");
            } else {
                titleContainer.add(dataList.get(i).getDate());
            }
        }

        pager.setAdapter(new PagerAdapter() {

            // viewpager中的组件数量
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            // 滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(viewContainter.get(position));
            }

            // 每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
                return viewContainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleContainer.get(position);
            }
        });

        pager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                Log.d(TAG, "--------changed:" + arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                LogUtil.d(TAG, "-------scrolled arg0:" + arg0);
                LogUtil.d(TAG, "-------scrolled arg1:" + arg1);
                LogUtil.d(TAG, "-------scrolled arg2:" + arg2);
            }

            @Override
            public void onPageSelected(int arg0) {
                LogUtil.d(TAG, "------selected:" + arg0);
            }
        });
        tabStrip.setTextSpacing(50);
        if (page < dataList.size()) {
            pager.setCurrentItem(page);
        } else {
            pager.setCurrentItem(0);
        }

    }

}
