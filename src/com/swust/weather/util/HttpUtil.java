package com.swust.weather.util;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.swust.weather.R;

import android.text.TextUtils;

/**
 * http相关工具类
 * */
public class HttpUtil {
    
    /**
     * 发送http请求
     * */
    public static void sendHttpRequest(final String address, final HttpResponseListener listener){
       
        new Thread(new Runnable() {
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();  
                    connection.setRequestMethod("GET");  
                    connection.setConnectTimeout(8000); 
                    connection.setReadTimeout(8000); 
                    InputStream in = connection.getInputStream(); 
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line = reader.readLine();
                    while(line != null){  //遍历保存数据
                        response.append(line);
                        line = reader.readLine();
                    }
                    if(listener != null){
                        listener.onFinish(response.toString()); //数据成功 回调监听完成接口
                    }
                }catch(Exception e){
                    if(listener != null){
                        listener.onError(e); //失败 回调监听异常接口
                    }
                } finally {
                    if(connection != null){
                        connection.disconnect();  //断开连接
                    }
                }     
            }
        }).start();
    }
    
   
    
    /**
     * 封装URL
     * @return url 根据封装好的URL*/
    public static String  encapsulationUrl(String cityName){
        String url = null;
        if(!TextUtils.isEmpty(cityName)){
                //url = " http://api.avatardata.cn/Weather/Query?key=3e7fe00d40c149dc93683f544c3f48bb&cityname=" + cityName;
                url = "http://api.avatardata.cn/WeatherWord/City?key=055078e4b47a4164be887b7f80d3e976&city="+ cityName;
        }
        return url;
        
    }
    
    /**
     * 封装URL
     * @return url 根据封装好的URL*/
    public static String  encapsulationUrl(String lat, String lon){
        String url = null;
        if(!TextUtils.isEmpty(lat)&&!TextUtils.isEmpty(lon)){
                url = "http://api.avatardata.cn/WeatherWord/Location?key=055078e4b47a4164be887b7f80d3e976&lat=" + lat + "&lon=" + lon;
        }
        LogUtil.v("data", "url:"+url);
        return url;
    }
   
    /**
     * 封装URL
     * @return url 根据封装好的URL*/
    public static String  encapsulationUrl(){
        String url = "http://api.avatardata.cn/WeatherWord/CityList?key=055078e4b47a4164be887b7f80d3e976";
        return url;
    }

    public static interface HttpResponseListener {
        void onFinish(String response);
        void onError(Exception e);

    }

}
