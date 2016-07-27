package com.swust.weather.customview;

import com.swust.weather.R;
import com.swust.weather.util.MyApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class RecommendItemView extends RelativeLayout {

    private RelativeLayout mrl;
    private ImageView mImage;
    private TextView indexTypeTV;
    private TextView indexInfoTV;
    public RecommendItemView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public RecommendItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.recommend_item, this, true);
        mrl = (RelativeLayout) findViewById(R.id.index_rl);
        mImage = (ImageView)findViewById(R.id.index_img);
        indexTypeTV = (TextView)findViewById(R.id.index_type);
        indexInfoTV = (TextView)findViewById(R.id.index_info);
        CharSequence text = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.swust.weather","type_text");
         if (text != null)
            indexTypeTV.setText(text);
         WindowManager wm = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
         Display display = wm.getDefaultDisplay();
         int windowWidth = display.getWidth();
         android.view.ViewGroup.LayoutParams params = mrl.getLayoutParams();
         params.height = LayoutParams.WRAP_CONTENT;
         params.width = windowWidth / 2 - 2;
         mrl.setLayoutParams(params);
    }

    public void setIndexTypeText(String text){
        indexTypeTV.setText(text);
    }
    public void setIndexInfoText(String text){
        indexInfoTV.setText(text);
    }
    public void setImgById(int resid){
        mImage.setBackgroundResource(resid);
    }
    
}
