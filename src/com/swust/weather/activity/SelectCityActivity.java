package com.swust.weather.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.swust.weather.R;
import com.swust.weather.adapter.SortAdapter;
import com.swust.weather.customview.ClearEditText;
import com.swust.weather.customview.SideBar;
import com.swust.weather.customview.SideBar.OnTouchingLetterChangedListener;
import com.swust.weather.database.WeatherDataBase;
import com.swust.weather.model.Basic;
import com.swust.weather.model.City;
import com.swust.weather.util.AutoLocationUtil;
import com.swust.weather.util.AutoLocationUtil.LocationData;
import com.swust.weather.util.AutoLocationUtil.LocationDataListener;
import com.swust.weather.util.HttpUtil.HttpResponseListener;
import com.swust.weather.util.HttpUtil;
import com.swust.weather.util.LogUtil;
import com.swust.weather.util.MyApplication;
import com.swust.weather.util.ParserUtil;
import com.swust.weather.util.PinyinComparator;

public class SelectCityActivity extends Activity {

    private static final int DATA_DEAL_TRUE = 1;
    private static final int DATA_DEAL_ERROR = 2;
    private static final int NOT_INTERNET = 3;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private WeatherDataBase weatherDataBase;

    private ProgressDialog progressDialog;

    private List<City> SourceDateList;

    
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_city_layout);
        weatherDataBase = WeatherDataBase.getInstance(this);
        pinyinComparator = new PinyinComparator();
        Button backButton = (Button) findViewById(R.id.select_city_back);
        Button locationButton = (Button) findViewById(R.id.select_city_location);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        locationButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCityActivity.this, MainActivity.class);
                intent.putExtra("is_auto_location", true);
                startActivity(intent);
                finish();
            }
        });
        initViews();
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DATA_DEAL_TRUE:
                closeProgress();
                SourceDateList = weatherDataBase.findAllCity();
                Collections.sort(SourceDateList, pinyinComparator);
                adapter.updateListView(SourceDateList);
                break;
            case DATA_DEAL_ERROR:
                closeProgress();
                Toast.makeText(getApplication(), "数据加载异常", Toast.LENGTH_SHORT).show();
                break;
            case NOT_INTERNET:
                closeProgress();
                Toast.makeText(getApplication(), "数据请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                break;

            }
            super.handleMessage(msg);
        }
    };

    private void initViews() {
        SourceDateList = weatherDataBase.findAllCity();
        if (SourceDateList.isEmpty()) {
            showProgress();
            HttpUtil.sendHttpRequest(HttpUtil.encapsulationUrl(), new HttpResponseListener() {

                @Override
                public void onFinish(String response) {
                    Message message = new Message();
                    if (!ParserUtil.handleCitiesResponse(weatherDataBase, response)) {
                        message.what = DATA_DEAL_ERROR;
                    } else {
                        message.what = DATA_DEAL_TRUE;
                    }
                    myHandler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {
                    Message message = new Message();
                    message.what = NOT_INTERNET;
                    myHandler.sendMessage(message);

                }
            });
        }

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

     
        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
             
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               
                Toast.makeText(getApplication(), ((City) adapter.getItem(position)).getCnName(), Toast.LENGTH_SHORT)
                        .show();

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                        MyApplication.getContext()).edit();
                editor.putString("city_id", ((City) adapter.getItem(position)).getCityId());
                editor.commit();

                Basic basic = new Basic();
                City city = (City) adapter.getItem(position);
                basic.setCityId(city.getCityId());
                basic.setCityName(city.getCnName());
                basic.setCitySource(0);
                weatherDataBase.saveBasic(basic);

                Intent intent = new Intent(SelectCityActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

     
        if (SourceDateList.size() > 1) {
            Collections.sort(SourceDateList, pinyinComparator);
        }
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

      
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    
    private void filterData(String filterStr) {
        List<City> filterDateList = new ArrayList<City>(); 

        if (TextUtils.isEmpty(filterStr)) {     
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (City sortModel : SourceDateList) {  
                if (sortModel.getCnName().indexOf(filterStr.toString()) != -1     
                        || sortModel.getEnName().indexOf(filterStr.toString()) != -1) {
                    filterDateList.add(sortModel);
                }
            }
        }
      
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }


    protected void closeProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }


    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }
}
