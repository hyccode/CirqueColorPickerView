package com.example.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class TouchRedirectLayout extends FrameLayout {
 
    private CirqueColorPickerView targetView;
 
    public TouchRedirectLayout(Context context) {
        super(context);
    }
 
    public TouchRedirectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public void setTargetView(CirqueColorPickerView targetView) {
        this.targetView = targetView;
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
////                if (isTouchArc(x, y)) {
//                targetView.setVisibility(View.VISIBLE);
//                    downOnArc = true;
//                    targetView.updateArc(x, y);
//                    return true;
////                }
////                break;
//            case MotionEvent.ACTION_MOVE:
//                if (downOnArc) {
//                    targetView.updateArc(x, y);
//                    targetView.changColor();
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                if (downOnArc){
//                    downOnArc = false;
//                    invalidate();
//                    targetView.setVisibility(View.GONE);
//
////                if (changeListener != null) {
////                    changeListener.onProgressChangeEnd(max, progress);
////                }
//                }
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


}