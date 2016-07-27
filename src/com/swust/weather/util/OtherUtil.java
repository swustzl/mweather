package com.swust.weather.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.swust.weather.R;

public class OtherUtil {

    /**
     * 将数字温度转换成温度图片进行返回*/
    public static Bitmap transformTemp(int temp) {
        if (temp == 0) {
            return getTempBitmap( 
                    BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.t0),
                    BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.centigrade));

        }
        boolean flag = true;
        if (temp < 0) {
            temp = 0 - temp;
            flag = false;
        }
        int a[] = new int[10];
        int len = 0;
        while (temp != 0) {  
            a[len++] = temp % 10;
            temp = temp / 10;
        }
        Bitmap tempImg;
        
        if (!flag) {  
            tempImg = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.minus);
        } else {
            tempImg = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), getDrawId(a[len - 1]));
            len = len - 1;
        }
        for (int i = len - 1; i >= 0; i--) { 
            tempImg = getTempBitmap(tempImg,
                    BitmapFactory.decodeResource(MyApplication.getContext().getResources(), getDrawId(a[i])));
        }
        tempImg = getTempBitmap(tempImg,
                BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.centigrade));
        return tempImg;
    }

    private static int getDrawId(int num) {
        switch (num) {
        case 0:
            return R.drawable.t0;
        case 1:
            return R.drawable.t1;
        case 2:
            return R.drawable.t2;
        case 3:
            return R.drawable.t3;
        case 4:
            return R.drawable.t4;
        case 5:
            return R.drawable.t5;
        case 6:
            return R.drawable.t6;
        case 7:
            return R.drawable.t7;
        case 8:
            return R.drawable.t8;
        case 9:
            return R.drawable.t9;
        default:
            return -1;
        }

    }

    /**组合两张图片*/
    private static Bitmap getTempBitmap(Bitmap first, Bitmap second) {
        Bitmap tempDraw = Bitmap.createBitmap(first.getWidth() + second.getWidth(), first.getHeight(),
                first.getConfig());   
        Canvas canvas = new Canvas(tempDraw); 
        canvas.drawBitmap(first, 0, 0, null);  
        canvas.drawBitmap(second, first.getWidth(), 0, null); 
        return tempDraw;
    }
    
    public static int transforWindDeg(int windDeg){
        if(windDeg>22.5&&windDeg<67.5){
           return R.drawable.trend_wind_2;
        }
        if(windDeg>67.5&&windDeg<112.5){
            return R.drawable.trend_wind_3;
        }
        if(windDeg>112.5&&windDeg<157.5){
            return R.drawable.trend_wind_4;
        }
        if(windDeg>157.5&&windDeg<202.5){
            return R.drawable.trend_wind_5;
        }
        if(windDeg>202.5&&windDeg<247.5){
            return R.drawable.trend_wind_6;
        }
        if(windDeg>247.5&&windDeg<292.5){
            return R.drawable.trend_wind_7;
        }
        if(windDeg>292.5&&windDeg<337.5){
            return R.drawable.trend_wind_8;
        }
        if(windDeg>337.5||windDeg<22.5){
            return R.drawable.trend_wind_1;
        }
        return R.drawable.main_icon_wind_rotate;
    }
}
