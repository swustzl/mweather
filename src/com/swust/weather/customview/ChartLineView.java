package com.swust.weather.customview;


import com.swust.weather.model.ChartDataModel;
import com.swust.weather.util.MyApplication;
import com.swust.weather.util.Times;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class ChartLineView extends View {

    private float XPoint; // 重绘时的X点
    private Integer XScale; // X方向上的间隔
    private Integer XLeftConfine; // X轴的左边界
    private Integer XRightConfine; // X轴的右边界
    private float downX; // 滑动时，按下的X点

    private Integer YScale;
    private ChartDataModel Data; // 数据
    private int dataLength;
    private String Title; // 显示的标题

    
    private Paint paint;

    public ChartLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public ChartLineView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    private void init() {
        WindowManager wm = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int windowWidth = display.getWidth();
        XScale = windowWidth / 7;
        XRightConfine = 0;
        XPoint = XRightConfine;
        YScale = XScale * 2;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);

    }

    public void SetInfo(ChartDataModel AllData) {
        this.Data = AllData;
        dataLength = AllData.hData.size();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        XLeftConfine = 0 - XScale * (dataLength - 7);
        int width = XScale * dataLength;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        int height = XScale * 7 / 2;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.drawColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTextAlign(Align.CENTER);
        paint.setARGB(127, 255, 255, 255);
        String nowTime = Times.getNowDateTime()+":00";
        for (int i = 0; i < dataLength; i++) {
            paint.setStrokeWidth(1);
            String hTime = Data.hData.get(i).substring(11);
            if (nowTime.equals(Data.hData.get(i))) {
                paint.setARGB(255, 255, 255, 255);
                hTime = "现在";
            }

            canvas.drawCircle(XPoint + XScale / 2 + i * XScale,
                    (float) (100 + (Data.hMax - Integer.parseInt(Data.vData.get(i))) * 1.0 / (Data.hMax - Data.hMin)
                            * YScale), 5, paint);
            canvas.drawText(Data.vData.get(i), XPoint + XScale / 2 + i * XScale,
                    (float) (90 + (Data.hMax - Integer.parseInt(Data.vData.get(i))) * 1.0 / (Data.hMax - Data.hMin)
                            * YScale), paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            canvas.drawCircle(XPoint + XScale / 2 + i * XScale + 40,
                    (float) (50 + (Data.hMax - Integer.parseInt(Data.vData.get(i))) * 1.0 / (Data.hMax - Data.hMin)
                            * YScale), 5, paint);

            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(2);
            if (i > 0) {
                canvas.drawLine(XPoint + XScale / 2 + (i - 1) * XScale,
                        (float) (100 + (Data.hMax - Integer.parseInt(Data.vData.get(i - 1))) * 1.0
                                / (Data.hMax - Data.hMin) * YScale), XPoint + XScale / 2 + i * XScale,
                        (float) (100 + (Data.hMax - Integer.parseInt(Data.vData.get(i))) * 1.0
                                / (Data.hMax - Data.hMin) * YScale), paint);

            }
            canvas.drawText(hTime, XPoint + XScale / 2 + i * XScale, YScale + 200, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventType = event.getAction();
        switch (eventType) {
        case MotionEvent.ACTION_DOWN:
            downX = event.getX();
            break;
        case MotionEvent.ACTION_MOVE:
            calculation(event.getX());
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            calculation(event.getX());
            invalidate();
            break;
        default:
            break;
        }
        return true;
    }

    private void calculation(float x) {
        XPoint = XPoint + x - downX;
        if (XPoint > XRightConfine) {
            XPoint = XRightConfine;
        }
        if (XPoint < XLeftConfine) {
            XPoint = XLeftConfine;
        }
        downX = x;
    }

}
