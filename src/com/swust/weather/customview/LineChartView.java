/*package com.swust.weather.customview;

import java.util.List;

import com.swust.weather.model.WeatherToDay;
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

public class LineChartView extends View {

    private float XPoint; // 重绘时的X点
    private Integer XScale; // X方向上的间隔
    private Integer XLeftConfine; // X轴的左边界
    private Integer XRightConfine; // X轴的右边界
    private float downX; // 滑动时，按下的X点

    private Integer maxTemp=0;
    private Integer minTemp=0;

    private Integer YScale;
    private int XLength = 380; // X轴的长度
    private int YLength = 240; // Y轴的长度
    private String[] XLabel; // X的刻度
    private String[] YLabel; // Y的刻度
    private List<WeatherToDay> Data; // 数据
    private String Title; // 显示的标题

    private int mLastX = 0;
    private int mLastY = 0;

    private Paint paint;

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public LineChartView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    private void init() {
        WindowManager wm = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int windowWidth = display.getWidth();
        XScale = windowWidth / 6;
        XLeftConfine = 0 - XScale * 9;
        XRightConfine = 0;
        XPoint = XRightConfine;
        YScale = XScale * 2;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);

    }

    public void SetInfo(List<WeatherToDay> AllData, Integer maxTemp, Integer minTemp) {
        this.Data = AllData;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = XScale * 15;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        int height = XScale * 6;
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
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTextAlign(Align.CENTER);
        
        String[] weeks = Times.getWeekToFifteen();
        String[] dates = Times.getDateToFifteen();
        for (int i = 0; i < Data.size(); i++) {
            paint.setStrokeWidth(1);
            canvas.drawText(weeks[i], XPoint + XScale / 2 + i * XScale, XScale / 2, paint);
            canvas.drawText(dates[i], XPoint + XScale / 2 + i * XScale, XScale / 2 + 50, paint);
            canvas.drawPoint(XPoint + XScale / 2 + i * XScale, XScale / 2 + 50 + (maxTemp - Data.get(i).getTemp1())
                    / (maxTemp - minTemp) * YScale, paint);
            canvas.drawPoint(XPoint + XScale / 2 + i * XScale, XScale / 2 + 50 + (maxTemp - Data.get(i).getTemp2())
                    / (maxTemp - minTemp) * YScale, paint);
            canvas.drawCircle(XPoint + XScale / 2 + i * XScale,
                    (float) (XScale * 2 + 50 + (maxTemp - Data.get(i).getTemp1()) * 1.0 / (maxTemp - minTemp) * YScale),
                    5, paint);
            canvas.drawText(Data.get(i).getTemp1().toString(),
                    XPoint + XScale / 2 + i * XScale,
                    (float) (XScale * 2 + 40 + (maxTemp - Data.get(i).getTemp1()) * 1.0  / (maxTemp - minTemp) * YScale),
                    paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            canvas.drawCircle(XPoint + XScale / 2 + i * XScale + 40,
                    (float) (XScale * 2 + (maxTemp - Data.get(i).getTemp1()) * 1.0  / (maxTemp - minTemp) * YScale),
                    5, paint);
            
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(1);
            canvas.drawCircle(XPoint + XScale / 2 + i * XScale, 
                    (float) (XScale * 2 + 50 + (maxTemp - Data.get(i).getTemp2()) * 1.0 / (maxTemp - minTemp) * YScale),
                    5, paint);
            canvas.drawText(Data.get(i).getTemp2().toString(),
                    XPoint + XScale / 2 + i * XScale,
                    (float) (XScale * 2 + 100 + (maxTemp - Data.get(i).getTemp2()) * 1.0 / (maxTemp - minTemp) * YScale),
                    paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            canvas.drawCircle(XPoint + XScale / 2 + i * XScale + 40, 
                    (float) (XScale * 2 + 60 + (maxTemp - Data.get(i).getTemp2()) * 1.0 / (maxTemp - minTemp) * YScale),
                    5, paint);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(2);
            if (i > 0) {
                canvas.drawLine(XPoint + XScale / 2 + (i - 1) * XScale,
                        (float) (XScale * 2 + 50 + (maxTemp - Data.get(i-1).getTemp1()) * 1.0 / (maxTemp - minTemp) * YScale),
                        XPoint + XScale / 2 + i * XScale,
                        (float) (XScale * 2 + 50 + (maxTemp - Data.get(i).getTemp1()) * 1.0 / (maxTemp - minTemp) * YScale), paint);
                canvas.drawLine(XPoint + XScale / 2 + (i - 1) * XScale,
                        (float) (XScale * 2 + 50 + (maxTemp - Data.get(i-1).getTemp2()) * 1.0 / (maxTemp - minTemp) * YScale),
                        XPoint + XScale / 2 + i * XScale, 
                        (float) (XScale * 2 + 50 + (maxTemp - Data.get(i).getTemp2()) * 1.0 / (maxTemp - minTemp) * YScale), paint);
            }
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
*/