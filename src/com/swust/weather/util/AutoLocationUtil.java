package com.swust.weather.util;

import java.text.DecimalFormat;

import android.os.Message;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class AutoLocationUtil {
    public LocationClient mLocationClient = null;
    private LocationData locationData;
    private LocationDataListener listener;
    public AutoLocationUtil(LocationDataListener locationDataListener){
        mLocationClient = new LocationClient(MyApplication.getContext()); 
        this.listener = locationDataListener;
        autoLocation();
    }
    public void autoLocation(){
        locationData = new LocationData();
           //声明LocationClient类
        mLocationClient.registerLocationListener( new BDLocationListener() {
            
            @Override
            public void onReceivePoi(BDLocation arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location == null) {
                    return;
                    }
                StringBuilder sb = new StringBuilder();
                DecimalFormat df = new DecimalFormat("#0.000000");
                sb.append("\nlat : ");
                String lat = df.format(location.getLatitude());
                sb.append(lat);
                sb.append("\nlon : ");
                String lon = df.format(location.getLongitude());
                sb.append(lon);
                LogUtil.v("data", "lat = "+lat);
                LogUtil.v("data", "lon = "+lon);
                
                if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                }else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                }else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                }else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                
                Toast.makeText(MyApplication.getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                mLocationClient.stop();
                
                locationData.lat = lat;
                locationData.lon = lon;
                if(listener!=null){
                    listener.onFinish(locationData);
                }
            }
        });    //注册监听函数
        mLocationClient.start();
        initLocation();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setPriority(LocationClientOption.GpsFirst);  //设置定位优先级
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mLocationClient.setLocOption(option);
    }
    
    public final class LocationData{
       public String lat;
       public String lon;
    }
    
    public static interface LocationDataListener {
        void onFinish(LocationData data);
    }
}
