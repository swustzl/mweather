package com.swust.weather.adapter;

import java.util.List;

import com.swust.weather.R;
import com.swust.weather.database.WeatherDataBase;
import com.swust.weather.model.Basic;
import com.swust.weather.util.LogUtil;
import com.swust.weather.util.MyApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BasicCityAdapter extends BaseAdapter {
    private List<Basic> list = null;
    private Context mContext;
    private WeatherDataBase weatherDataBase;

    public BasicCityAdapter(Context mContext, List<Basic> list) {
        this.mContext = mContext;
        this.list = list;
        weatherDataBase = WeatherDataBase.getInstance(MyApplication.getContext());
    }

    public void updateListView() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BasicViewHolder holder = null;
        final Basic basicCity = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.basic_city_item, null);
            holder = new BasicViewHolder();
            holder.locationIV = (ImageView) convertView.findViewById(R.id.basic_city_location);
            holder.cityName = (TextView) convertView.findViewById(R.id.basic_city_name);
            holder.deleteButton = (Button) convertView.findViewById(R.id.basic_city_delete);
            holder.cityUpdateTime = (TextView) convertView.findViewById(R.id.basic_city_update_time);
            convertView.setTag(holder);
        } else {
            holder = (BasicViewHolder) convertView.getTag();
        }
        if (basicCity.getCitySource() == 1) {
            holder.locationIV.setVisibility(View.VISIBLE);
        } else {
            holder.locationIV.setVisibility(View.GONE);
        }

        holder.cityName.setText(basicCity.getCityName());
        holder.cityUpdateTime.setText(basicCity.getUpdateTime());
        holder.deleteButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LogUtil.v("delete", basicCity.getCityName());
                if (weatherDataBase.deleteBasicCity(basicCity.getCityId()) > 0) {
                    list.remove(position);
                    updateListView();
                }
            }
        });
        return convertView;
    }

    public final class BasicViewHolder {
        public ImageView locationIV;
        public TextView cityName;
        public TextView cityUpdateTime;
        public Button deleteButton;
    }
}
