package com.yxj.yspiderview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxj on 17/7/29.
 */

public class SpiderView extends View {

    private Paint mPaint;
    private Paint mValuePaint;

    int viewWidth;
    int viewHeight;


    /**
     * 等级，默认4个
     */
    public int maxDegree;
    /**
     * 维度，可选5，6，7
     */
    int dimention;
    /**
     * 旋转角度
     */
    float rotateAngle;
    /**
     * 能力值
     */
    List<Data> datas;

    int radiu;

    float fraction;
    private Paint mRegionPaint;
    private ValueAnimator mValueAnimator;

    public SpiderView(Context context) {
        super(context);
        init(context,null);
    }

    public SpiderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SpiderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        initAttrs(context,attrs);

        Log.i("yxj","init");

        datas = new ArrayList<>();
        datas.add(new Data("力量力量力量",3.3f));
        datas.add(new Data("智力智力智力",2.6f));
        datas.add(new Data("敏捷",3.4f));
        datas.add(new Data("攻击",2.8f));
        datas.add(new Data("护甲",4f));
        datas.add(new Data("攻速",3.2f));

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);
        mPaint.setAlpha(50);

        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setStyle(Paint.Style.FILL);
        mValuePaint.setColor(Color.YELLOW);
        mValuePaint.setAlpha(80);

        mRegionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRegionPaint.setStyle(Paint.Style.STROKE);
        mRegionPaint.setStrokeWidth(3);
        mRegionPaint.setColor(Color.YELLOW);

    }

    private void initAttrs(Context context,AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.SpiderView);
        maxDegree = ta.getInteger(R.styleable.SpiderView_max_value,5);
        dimention = ta.getInteger(R.styleable.SpiderView_attr_num,6);
        rotateAngle = ta.getFloat(R.styleable.SpiderView_rotate_angle,0);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("yxj","onMeasure");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        viewWidth = w;
        viewHeight = h;

        radiu = 200;
//        radiu = Math.min(w,h)/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(viewWidth/2,viewHeight/2);
        canvas.rotate(-90+rotateAngle,0,0);// 旋转过后，坐标为数学的坐标，第一个维度线从Y轴正方向开始，顺时针排列
        drawPolygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawValues(canvas);
    }

    private void drawText(Canvas canvas) {
        for(int i=0;i<datas.size();i++){
            String text = datas.get(i).attributeName;
            float textWidth = mPaint.measureText(text);
            float textSize = mPaint.getTextSize()/2;

            float x = (float)Math.cos(2*Math.PI/dimention*i)*(radiu+20);
            float y = (float)Math.sin(2*Math.PI/dimention*i)*(radiu+20);

            canvas.rotate(90,x,y);

            canvas.drawText(datas.get(i).attributeName,x-textWidth/2,y+textSize/2,mPaint);
            canvas.rotate(-90,x,y);
        }

    }

    private void drawValues(Canvas canvas) {
//        int[] values = new int[]{4,3,3,2,4,3};

        Path path = new Path();
        for(int i=1;i<=datas.size();i++){
            float x = (float)Math.cos(2*Math.PI/dimention*i)*(datas.get(i-1).value/4f)*radiu*fraction;
            float y = (float)Math.sin(2*Math.PI/dimention*i)*(datas.get(i-1).value/4f)*radiu*fraction;
            if(i==1){
                path.moveTo(x,y);
            }else{
                path.lineTo(x,y);
            }
        }
        path.close();
        canvas.drawPath(path,mValuePaint);
        canvas.drawPath(path,mRegionPaint);
    }

    private void drawLines(Canvas canvas) {
        int currentR = radiu;
        for(int i=0;i<dimention;i++){
            float x = (float)Math.cos(2*Math.PI/dimention*i)*currentR;
            float y = (float)Math.sin(2*Math.PI/dimention*i)*currentR;
            canvas.drawLine(0,0,x,y,mPaint);
        }
    }

    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        for(int i=1;i<=maxDegree;i++){
            path.reset();
            int currentR = radiu/maxDegree*i;
            path.moveTo(currentR,0);

            for(int j=1;j<dimention;j++){
                float x = (float)Math.cos(2*Math.PI/dimention*j)*currentR;
                float y = (float)Math.sin(2*Math.PI/dimention*j)*currentR;
                path.lineTo(x,y);
            }
            path.close();
            canvas.drawPath(path,mPaint);
        }
    }

    public void setData(List<Data> datas){
        this.datas = datas;
        invalidate();
    }

    public void setSingleData(Data data){
        for(Data originData:datas){
            if(data.attributeName.equals(originData.attributeName)){
                originData.value = data.value;
                invalidate();
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("yxj","onAttachedToWindow");
        mValueAnimator = ValueAnimator.ofFloat(0,1).setDuration(1000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = animation.getAnimatedFraction();
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mValueAnimator.isRunning()){
            mValueAnimator.cancel();
        }
    }

    /**
     * 存储属性的容器
     */
    public static class Data{
        String attributeName;
        float value;

        public Data(String attributeName, float value) {
            this.attributeName = attributeName;
            this.value = value;
        }
    }
}
