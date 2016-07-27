package com.swust.weather.activity;

import java.util.List;

import com.swust.weather.R;
import com.swust.weather.database.WeatherDataBase;
import com.swust.weather.model.Basic;
import com.swust.weather.model.Suggestion;
import com.swust.weather.model.WeatherToDaily;
import com.swust.weather.util.MyApplication;
import com.swust.weather.util.RecommendEnum;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class RecommendInfoActivity extends Activity {
    private WeatherDataBase weatherDataBase;
    private Suggestion suggestion;
    private String title;
    private String recommendBrf;
    private String recommendTxt;
    private Basic basic;
    private WeatherToDaily weatherToDaily;
    private String tv1, tv2, tv3, tv1Data, tv2Data, tv3Data;
    private String tishiContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recommend_info_activity_layout);
        weatherDataBase = WeatherDataBase.getInstance(this);
        
        WindowManager wm = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int windowHeight = display.getHeight();
        RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.recommend_info_rl1);
        android.view.ViewGroup.LayoutParams params = rl1.getLayoutParams();
        params.height = windowHeight * 2/3-20;
        params.width = LayoutParams.MATCH_PARENT;
        rl1.setLayoutParams(params);
        RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.recommend_info_rl2);
        params = rl2.getLayoutParams();
        params.height = windowHeight /3+20;
        params.width = LayoutParams.MATCH_PARENT;
        rl2.setLayoutParams(params);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String cityId = pref.getString("city_id", " ");
        suggestion = weatherDataBase.findSuggestion(cityId);
        basic = weatherDataBase.findBasicByCityId(cityId);
        List<WeatherToDaily> list = weatherDataBase.findDailyForecastByCityId(cityId);
        weatherToDaily = list.get(0);
        int suggestionType = getIntent().getIntExtra("suggestion_type", 0);
        getDataByType(suggestionType);
        
        TextView reCity = (TextView) findViewById(R.id.recommend_city);
        TextView reDate = (TextView) findViewById(R.id.recommend_date_data);
        Button backButton = (Button) findViewById(R.id.recommend_info_activity_back);
        TextView reTitle = (TextView) findViewById(R.id.recommend_info_activity_title);
        TextView reLevel = (TextView) findViewById(R.id.recommend_level);
        TextView reInfo = (TextView) findViewById(R.id.recommend_info);
        TextView lineBelowTv1 = (TextView) findViewById(R.id.line_below_tv1);
        TextView lineBelowTv1Data = (TextView) findViewById(R.id.line_below_tv1_data);
        TextView lineBelowTv2 = (TextView) findViewById(R.id.line_below_tv2);
        TextView lineBelowTv2Data = (TextView) findViewById(R.id.line_below_tv2_data);
        TextView lineBelowTv3 = (TextView) findViewById(R.id.line_below_tv3);
        TextView lineBelowTv3Data = (TextView) findViewById(R.id.line_below_tv3_data);
        TextView tishi = (TextView) findViewById(R.id.tishi_content);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lineBelowTv1.setText(tv1);
        lineBelowTv1Data.setText(tv1Data);
        lineBelowTv2.setText(tv2);
        lineBelowTv2Data.setText(tv2Data);
        lineBelowTv3.setText(tv3);
        lineBelowTv3Data.setText(tv3Data);
        reDate.setText(weatherToDaily.getDate());
        reCity.setText(basic.getCityName());
        reTitle.setText(title);
        reLevel.setText(recommendBrf);
        reInfo.setText(recommendTxt);
        tishi.setText(tishiContent);

    }

    private void getDataByType(int type) {
        tishiContent = RecommendEnum.getText(type);

        switch (type) {
        case 1:
            title = "舒适度指数";
            recommendBrf = suggestion.getComfBrf();  //当前指数等级
            recommendTxt = suggestion.getComfTxt(); //当前指数推荐
            //指数判别依据信息
            tv1 = "温度范围：";
            tv2 = "相对湿度：";
            tv3 = "风向风力：";
            tv1Data = weatherToDaily.getTmpMin() + "~" + weatherToDaily.getTmpMax() + "℃";
            tv2Data = weatherToDaily.getHum() + "%";
            tv3Data = weatherToDaily.getWindDir() + weatherToDaily.getWindSc();
            break;
        case 2:
            title = "感冒指数";
            recommendBrf = suggestion.getFluBrf();
            recommendTxt = suggestion.getFluTxt();
            tv1 = "昼夜温差：";
            tv2 = "天气状况：";
            tv3 = "风向风力：";
            tv1Data = (weatherToDaily.getTmpMax() - weatherToDaily.getTmpMin()) + "℃";
            tv2Data = weatherToDaily.getCondTxtDay() + "转" + weatherToDaily.getCondTxtNight();
            tv3Data = weatherToDaily.getWindDir() + weatherToDaily.getWindSc();
            break;
        case 3:
            title = "穿衣指数";
            recommendBrf = suggestion.getDrsgBrf();
            recommendTxt = suggestion.getDrsgTxt();
            tv1 = "温度范围：";
            tv2 = "天气状况：";
            tv3 = "风向风力：";
            tv1Data = weatherToDaily.getTmpMin() + "~" + weatherToDaily.getTmpMax() + "℃";
            tv2Data = weatherToDaily.getCondTxtDay() + "转" + weatherToDaily.getCondTxtNight();
            tv3Data = weatherToDaily.getWindDir() + weatherToDaily.getWindSc();
            break;
        case 4:
            title = "运动指数";
            recommendBrf = suggestion.getSportBrf();
            recommendTxt = suggestion.getSportTxt();
            tv1 = "温度范围：";
            tv2 = "天气状况：";
            tv3 = "风向风力：";
            tv1Data = weatherToDaily.getTmpMin() + "~" + weatherToDaily.getTmpMax() + "℃";
            tv2Data = weatherToDaily.getCondTxtDay() + "转" + weatherToDaily.getCondTxtNight();
            tv3Data = weatherToDaily.getWindDir() + weatherToDaily.getWindSc();
            break;
        case 5:
            title = "洗车指数";
            recommendBrf = suggestion.getCwBrf();
            recommendTxt = suggestion.getCwTxt();
            tv1 = "温度范围：";
            tv2 = "天气状况：";
            tv3 = "风向风力：";
            tv1Data = weatherToDaily.getTmpMin() + "~" + weatherToDaily.getTmpMax() + "℃";
            tv2Data = weatherToDaily.getCondTxtDay() + "转" + weatherToDaily.getCondTxtNight();
            tv3Data = weatherToDaily.getWindDir() + weatherToDaily.getWindSc();
            break;
        case 6:
            title = "旅游指数";
            recommendBrf = suggestion.getTravBrf();
            recommendTxt = suggestion.getTravTxt();
            tv1 = "温度范围：";
            tv2 = "天气状况：";
            tv3 = "风向风力：";
            tv1Data = weatherToDaily.getTmpMin() + "~" + weatherToDaily.getTmpMax() + "℃";
            tv2Data = weatherToDaily.getCondTxtDay() + "转" + weatherToDaily.getCondTxtNight();
            tv3Data = weatherToDaily.getWindDir() + weatherToDaily.getWindSc();
            break;
        case 7:
            title = "紫外线指数";
            recommendBrf = suggestion.getUvBrf();
            recommendTxt = suggestion.getUvTxt();
            tv1 = "温度范围：";
            tv2 = "天气状况：";
            tv3 = "日出日落：";
            tv1Data = weatherToDaily.getTmpMin() + "~" + weatherToDaily.getTmpMax() + "℃";
            tv2Data = weatherToDaily.getCondTxtDay() + "转" + weatherToDaily.getCondTxtNight();
            tv3Data = weatherToDaily.getSr() + "/" + weatherToDaily.getSs();
            break;
        }
    }

}
