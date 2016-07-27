package com.swust.weather.activity;

import java.util.List;

import com.swust.weather.R;
import com.swust.weather.adapter.BasicCityAdapter;
import com.swust.weather.database.WeatherDataBase;
import com.swust.weather.model.Basic;
import com.swust.weather.model.City;
import com.swust.weather.util.MyApplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BasicCityActivity extends Activity {
    private WeatherDataBase weatherDataBase;
    private List<Basic> basicList;
    private BasicCityAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.basic_city_layout);
        weatherDataBase = WeatherDataBase.getInstance(this);
        basicList = weatherDataBase.findAllBasicCity();
        ListView lvBasicCity = (ListView) findViewById(R.id.basic_city_list); 
        Button addButton = (Button) findViewById(R.id.basic_city_add);
        addButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BasicCityActivity.this, SelectCityActivity.class);
                startActivity(intent);
                
            }
        });
        Button backButton = (Button) findViewById(R.id.basic_city_back);
        backButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                onBackPressed();
                
            }
        });
        adapter =  new BasicCityAdapter(this, basicList);
        lvBasicCity.setAdapter(adapter);
        lvBasicCity.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
               
                Toast.makeText(getApplication(), ((Basic)adapter.getItem(position)).getCityName(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                editor.putString("city_id", ((Basic)adapter.getItem(position)).getCityId());
                editor.commit();
                Intent intent = new Intent(BasicCityActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
