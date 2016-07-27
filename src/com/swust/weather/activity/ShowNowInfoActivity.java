package com.swust.weather.activity;

import com.swust.weather.R;
import com.swust.weather.util.OtherUtil;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShowNowInfoActivity extends Activity {
    private boolean dataIsNotNull;
    private String condTxt = "";
    private int fl = 0;
    private int hum = 0;
    private String pcpn = "";
    private int pres = 0;
    private int tmp = 0;
    private int vis = 0;
    private String wind = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataIsNotNull = getIntent().getBooleanExtra("data_is_not_null", false);
        if (dataIsNotNull) {
            condTxt = getIntent().getStringExtra("cond_txt");
            pcpn = getIntent().getStringExtra("pcpn");
            wind = getIntent().getStringExtra("wind");
            fl = getIntent().getIntExtra("fl", 0);
            hum = getIntent().getIntExtra("hum", 0);
            pres = getIntent().getIntExtra("pres", 0);
            tmp = getIntent().getIntExtra("tmp", 0);
            vis = getIntent().getIntExtra("vis", 0);
        }
        setContentView(R.layout.show_now_info_layout);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.show_now_info_rl);
        if (dataIsNotNull) {
            ImageView ivTemp = (ImageView) findViewById(R.id.now_temp);
            TextView tvDesp = (TextView) findViewById(R.id.now_desp);
            TextView tvFl = (TextView) findViewById(R.id.now_fl_data);
            TextView tvHum = (TextView) findViewById(R.id.now_hum_data);
            TextView tvPcpn = (TextView) findViewById(R.id.now_pcpn_data);
            TextView tvWind = (TextView) findViewById(R.id.now_wind_data);
            TextView tvPres = (TextView) findViewById(R.id.now_pres_data);
            TextView tvVis = (TextView) findViewById(R.id.now_vis_data);
            Drawable dTemp = new BitmapDrawable(OtherUtil.transformTemp(tmp));
            ivTemp.setBackgroundDrawable(dTemp);
            tvDesp.setText(condTxt);
            tvFl.setText(fl + " ℃");
            tvPcpn.setText(pcpn + " mm");
            tvWind.setText(wind + " 级");
            tvPres.setText(pres + " hPa");
            tvVis.setText(vis + " km");
            tvHum.setText(hum + " %");
        }
        rl.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

}
