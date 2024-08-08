package com.example.myapplication.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class TouchRedirectLayout extends FrameLayout {

    private CirqueColorPickerView targetView;
    // 记录是否正在动画
    private boolean isAnimating = false;
    // 记录是否可以拖动了
    private boolean downOnArc = false;


    public TouchRedirectLayout(Context context) {
        super(context);
    }

    public TouchRedirectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTargetView(CirqueColorPickerView targetView) {
        this.targetView = targetView;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startAnimation();
                if (downOnArc || isAnimating){
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (downOnArc) {
                    targetView.updateArc(x, y);
                    targetView.changColor();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                stopAnimation();
                if (downOnArc) {
                    downOnArc = false;
                    invalidate();
//                if (changeListener != null) {
//                    changeListener.onProgressChangeEnd(max, progress);
//                }
                    return true;
                }else{
                    // 短按事件
                    performClick(); // 触发 onClick 事件
                    return false;
                }
            case MotionEvent.ACTION_CANCEL:
                stopAnimation();
                if (downOnArc) {
                    downOnArc = false;
                    invalidate();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //解决父视图滑动冲突
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }





    private void startAnimation() {
        targetView.setVisibility(VISIBLE);
        if (!isAnimating) {
            isAnimating = true;

            // 创建透明度动画
            ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(targetView, "alpha", 0.1f, 1.0f);
            alphaAnim.setDuration(1000); // 设置动画持续时间

            // 创建缩放动画
            ObjectAnimator scaleAnimX = ObjectAnimator.ofFloat(targetView, "scaleX", 0.1f, 1.0f);
            ObjectAnimator scaleAnimY = ObjectAnimator.ofFloat(targetView, "scaleY", 0.1f, 1.0f);
            scaleAnimX.setDuration(1000);
            scaleAnimY.setDuration(1000);

            // 同时播放两个动画
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(alphaAnim, scaleAnimX, scaleAnimY);
            animatorSet.start();

            // 添加结束监听器
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;
                    downOnArc = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isAnimating = false;
                    downOnArc = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        }
    }

    private void stopAnimation() {
        targetView.setVisibility(GONE);
        if (isAnimating) {
            isAnimating = false;
            // 可以在这里取消动画，但通常情况下不需要
        }
    }


    @Override
    public boolean performClick() {
        // 执行点击操作
        return super.performClick();
    }
}