package com.example.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.utils.ChartUtils;

public class CirqueColorPickerView extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 中心点坐标
     */
    private int centerX, centerY;
    /**
     * 渐变色圆环半径
     */
    private int radius;

    private int paddingOuterThumb;

    /**
     * 圆环的颜色
     */
    private int roundColor;
    /**
     * 渐变色圆环宽度
     */
    private float roundWidth;
    /**
     * 外圈圆环颜色
     */
    private int outRoundColor;

    /**
     * 内圈小圆环颜色
     */
    private int inRoundColor;
    /**
     * 连线画笔
     */
    private Paint linePaint;
    /**
     * 连线颜色
     */
    private int lineColor;
    /**
     * 连线宽度
     */
    private float lineWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 当前选中的颜色
     */
    private int currentColor = Color.RED;

    private Drawable mDragDrawable, mDragPressDrawable;

    public CirqueColorPickerView(Context context) {
        this(context, null);
    }

    public CirqueColorPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirqueColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirqueColorPickerView);

        paint = new Paint();
        linePaint = new Paint();

        //获取自定义属性和默认值
        roundColor = typedArray.getColor(R.styleable.CirqueColorPickerView_round_color, Color.RED);
        roundWidth = typedArray.getInteger(R.styleable.CirqueColorPickerView_round_width, 40);

        outRoundColor = typedArray.getColor(R.styleable.CirqueColorPickerView_out_round_color, Color.WHITE);
        inRoundColor = Color.BLACK;
        textColor = typedArray.getColor(R.styleable.CirqueColorPickerView_text_color, Color.WHITE);
        textSize = typedArray.getInteger(R.styleable.CirqueColorPickerView_text_size, 100);

        max = typedArray.getInteger(R.styleable.CirqueColorPickerView_max, 360);
        lineColor = typedArray.getColor(R.styleable.CirqueColorPickerView_line_color, Color.WHITE);
        lineWidth = typedArray.getInteger(R.styleable.CirqueColorPickerView_line_width, 10);

        // 加载拖动图标
        mDragDrawable = getResources().getDrawable(R.mipmap.ring_dot);// 圆点图片
        int thumbHalfheight = mDragDrawable.getIntrinsicHeight() / 2;
        int thumbHalfWidth = mDragDrawable.getIntrinsicWidth() / 2;
        mDragDrawable.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);

        mDragPressDrawable = getResources().getDrawable(R.mipmap.ring_dot);// 圆点图片
        thumbHalfheight = mDragPressDrawable.getIntrinsicHeight() / 2;
        thumbHalfWidth = mDragPressDrawable.getIntrinsicWidth() / 2;
        mDragPressDrawable.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);
        paddingOuterThumb = thumbHalfheight;
    }


    @Override
    public void onDraw(Canvas canvas) {
        /**
         * 画渐变色大圆环
         */
        SweepGradient sweepGradient = new SweepGradient(centerX, centerY,
                new int[]{
                        Color.rgb(107, 18, 248),
                        Color.rgb(254, 0, 198),
                        Color.rgb(255, 6, 19),
                        Color.rgb(255, 172, 40),
                        Color.rgb(146, 255, 54),
                        Color.rgb(1, 255, 53),
                        Color.rgb(0, 245, 234),
                        Color.rgb(1, 69, 249),
                        Color.rgb(107, 18, 248),
                },
                null);
        paint.setShader(sweepGradient);//设置圆环的渐变色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centerX, centerY, radius, paint); //画出渐变色圆环
        /**
         * 画渐变色大圆环的边
         */
        linePaint.setColor(outRoundColor);//设置最外圈圆环的颜色
        linePaint.setStyle(Paint.Style.STROKE); //设置空心
        linePaint.setStrokeWidth(lineWidth); //设置圆环的宽度
        linePaint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centerX, centerY, (int) (radius + roundWidth / 2), linePaint);

        /**
         * 画中间圆
         */
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(getCurrentColor());
        canvas.drawCircle(centerX, centerY, (float) (radius / 2.2), linePaint);

        /**
         * 画中间圆的边
         */
        linePaint.setColor(outRoundColor);//设置最外圈圆环的颜色
        linePaint.setStyle(Paint.Style.STROKE); //设置空心
        linePaint.setStrokeWidth(lineWidth); //设置圆环的宽度
        linePaint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centerX, centerY, (float) (radius / 2.2) + lineWidth / 2, linePaint);
        /**
         * 画内部小圆的边
         */
        RadialGradient radialGradient = new RadialGradient(
                centerX, centerY, (float) (radius / 5) + lineWidth * 4,
                new int[]{
                        0x99000000,
                        0x00ffffff,
//                        Color.rgb(0, 0, 0),
//                        Color.rgb(255, 255, 255),
                }, null, Shader.TileMode.CLAMP
        );
        linePaint.setShader(radialGradient);
//        linePaint.setColor(inRoundColor);//设置最外圈圆环的颜色
        linePaint.setStyle(Paint.Style.STROKE); //设置空心
        linePaint.setStrokeWidth(lineWidth * 4); //设置圆环的宽度
        linePaint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centerX, centerY, (float) (radius / 5) + lineWidth * 2, linePaint);
        linePaint.setShader(null);

        /**
         * 画文字
         */
        linePaint.setStrokeWidth(0);
        linePaint.setColor(textColor);
        linePaint.setTextSize(textSize);
        linePaint.setStyle(Paint.Style.FILL);
        String text = String.valueOf(progress);
        float textWidth = linePaint.measureText(text);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        canvas.drawText(text, centerX - textWidth / 2, centerY + textSize / 2, linePaint);

        linePaint.setTextSize(38);
        Path path = new Path();
        RectF oval = new RectF(centerX - (float) (radius / 3.5), centerY - (float) (radius / 3.5), centerX + (float) (radius / 3.5), centerY + (float) (radius / 3.5));  //用于定义的圆弧的形状和大小的界限
        path.addArc(oval, 220, 320);
        canvas.drawTextOnPath("ADD COLOUR", path, 0, 0, linePaint);

        /**
         * 外圈大圆环上的点
         */
        PointF progressPoint = ChartUtils.calcArcEndPointXY(centerX, centerY, radius, 360 * progress / max, 90);
        /**
         * 画外圈大圆环上的点的边
         */
        linePaint.setColor(outRoundColor);//设置最外圈圆环的颜色
        linePaint.setStyle(Paint.Style.STROKE); //设置空心
        linePaint.setStrokeWidth(lineWidth); //设置圆环的宽度
        linePaint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(progressPoint.x, progressPoint.y, (float) (roundWidth * 1.5) + lineWidth / 2, linePaint);

        /**
         * 内圈小圆环上的点
         */
        PointF proPoint = ChartUtils.calcArcEndPointXY(centerX, centerY, (float) (radius / 5), 360 * progress / max, 90);
        /**
         * 两点之间的连接线
         */
        linePaint.setColor(lineColor);  //设置连线的颜色
        linePaint.setStrokeWidth(lineWidth);//设置连线的宽度
        canvas.drawLine(proPoint.x, proPoint.y, progressPoint.x, progressPoint.y, linePaint);
        /**
         * 大圆环上的点
         */
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(progressPoint.x, progressPoint.y, (float) (roundWidth * 1.5), paint);
        canvas.save();
    }

//    private boolean downOnArc = false;
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction();
//        int x = (int) event.getX();
//        int y = (int) event.getY();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                if (isTouchArc(x, y)) {
//                    downOnArc = true;
//                    updateArc(x, y);
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (downOnArc) {
//                    updateArc(x, y);
//                    changColor();
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                downOnArc = false;
//                invalidate();
//                if (changeListener != null) {
//                    changeListener.onProgressChangeEnd(max, progress);
//                }
//
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        //解决父视图滑动冲突
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                getParent().getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_UP:
//                getParent().getParent().requestDisallowInterceptTouchEvent(false);
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        centerX = width / 2;
        centerY = height / 2;
        int minCenter = Math.min(centerX, centerY);
        //圆环的半径
        radius = (int) (minCenter - roundWidth / 2 - paddingOuterThumb);
        minValidateTouchArcRadius = (int) (radius - paddingOuterThumb * 1.5f);
        maxValidateTouchArcRadius = (int) (radius + paddingOuterThumb * 1.5f);
        super.onSizeChanged(width, height, oldw, oldh);
    }

    // 根据点的位置，更新进度
    public void updateArc(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        // 计算角度，得出（-1->1）之间的数据，等同于（-180°->180°）
        double angle = Math.atan2(cy, cx) / Math.PI;
        // 将角度转换成（0->2）之间的值，然后加上90°的偏移量
        angle = ((2 + angle) % 2 + (270 / 180f)) % 2;
        // 用（0->2）之间的角度值乘以总进度，等于当前进度
        progress = (int) (angle * max / 2);
        if (changeListener != null) {
            changeListener.onProgressChange(max, progress);
        }
        invalidate();
    }

    private int minValidateTouchArcRadius; // 最小有效点击半径
    private int maxValidateTouchArcRadius; // 最大有效点击半径

    // 判断是否按在圆边上
    private boolean isTouchArc(int x, int y) {
        double d = getTouchRadius(x, y);
        if (d >= minValidateTouchArcRadius && d <= maxValidateTouchArcRadius) {
            return true;
        }
        return false;
    }

    // 计算某点到圆点的距离
    private double getTouchRadius(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        return Math.hypot(cx, cy);
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public void changColor() {
        float colorH = (progress + 1);
        currentColor = Color.HSVToColor(new float[]{colorH, 1.0f, 1.0f});
    }

    /**
     * 获取当前颜色
     *
     * @return
     */
    private int getCurrentColor() {
        return currentColor;
    }

    /**
     * 设置当前颜色
     *
     * @param currentColor
     */
    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
        if (onColorChangeListener != null) {
            onColorChangeListener.onColorChange(currentColor);
        }
        invalidate();
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    private OnProgressChangeListener changeListener;

    public void setChangeListener(OnProgressChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface OnProgressChangeListener {
        void onProgressChange(int duration, int progress);

        void onProgressChangeEnd(int duration, int progress);
    }

    private OnColorChangeListener onColorChangeListener;

    public void setOnColorChangerListener(OnColorChangeListener onColorChangerListener) {
        this.onColorChangeListener = onColorChangerListener;
    }

    public interface OnColorChangeListener {
        void onColorChange(int color);
    }
}

