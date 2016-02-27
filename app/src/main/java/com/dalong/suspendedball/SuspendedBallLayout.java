package com.dalong.suspendedball;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 悬浮球父布局
 * Created by zhouweilong on 16/2/27.
 */
public class SuspendedBallLayout extends LinearLayout {

    private ViewDragHelper mDragger;

    private View mDragView;

    public SuspendedBallLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId){
                return child == mDragView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx){
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }


            /**
             * 手指释放的时候回调
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if(releasedChild == mDragView){
                    int mY=releasedChild.getTop();
                    if(releasedChild.getTop()<0){
                        mY=0;
                    }
                    if(releasedChild.getBottom()>getHeight()){
                        mY=getHeight()-releasedChild.getHeight();
                    }
                    if(releasedChild.getRight()>getWidth()/2){
                        mDragger.settleCapturedViewAt(getWidth()-mDragView.getWidth(),mY);
                    }else{
                        mDragger.settleCapturedViewAt(0, mY);
                    }
                    invalidate();
                }
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId){
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth()-child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight()-child.getMeasuredHeight();
            }
        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.DIRECTION_ALL);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mDragger.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if(mDragger.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
    }

}
