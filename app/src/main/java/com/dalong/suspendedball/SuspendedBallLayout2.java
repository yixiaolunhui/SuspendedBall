package com.dalong.suspendedball;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 悬浮球父布局
 * Created by zhouweilong on 16/2/27.
 */
public class SuspendedBallLayout2 extends LinearLayout {

    private ViewDragHelper mDragger;

    private View mDragView;

    private View mDragView2;

    private Point initPointPosition = new Point();
    private Point initPointPosition2 = new Point();
    private boolean isFirst;

    public SuspendedBallLayout2(Context context, AttributeSet attrs){
        super(context, attrs);
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId){
                return child == mDragView||child == mDragView2;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx){
                //取得左边界的坐标
                final int leftBound = getPaddingLeft();
                //取得右边界的坐标
                final int rightBound = getWidth() - child.getWidth() - leftBound;
                //这个地方的含义就是 如果left的值 在leftbound和rightBound之间 那么就返回left
                //如果left的值 比 leftbound还要小 那么就说明 超过了左边界 那我们只能返回给他左边界的值
                //如果left的值 比rightbound还要大 那么就说明 超过了右边界，那我们只能返回给他右边界的值
                return Math.min(Math.max(left, leftBound), rightBound);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - child.getHeight() - topBound;
                return Math.min(Math.max(top, topBound), bottomBound);
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
                        initPointPosition.x=getWidth()-mDragView.getWidth();
                        initPointPosition.y=mY;
                    }else{
                        mDragger.settleCapturedViewAt(0, mY);

                        initPointPosition.x=0;
                        initPointPosition.y=mY;
                    }

                    invalidate();
                }
                if(releasedChild == mDragView2){
                    int mY=releasedChild.getTop();
                    if(releasedChild.getTop()<0){
                        mY=0;
                    }
                    if(releasedChild.getBottom()>getHeight()){
                        mY=getHeight()-releasedChild.getHeight();
                    }
                    if(releasedChild.getRight()>getWidth()/2){
                        mDragger.settleCapturedViewAt(getWidth()-mDragView2.getWidth(),mY);
                        initPointPosition2.x=getWidth()-mDragView2.getWidth();
                        initPointPosition2.y=mY;
                    }else{
                        mDragger.settleCapturedViewAt(0, mY);

                        initPointPosition2.x=0;
                        initPointPosition2.y=mY;
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

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
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

        if(!isFirst){
            initPointPosition.x=mDragView.getLeft();
            initPointPosition.y=mDragView.getTop();

            initPointPosition2.x=mDragView2.getLeft();
            initPointPosition2.y=mDragView2.getTop();
            isFirst=true;
        }
        mDragView.layout(initPointPosition.x,initPointPosition.y,
                initPointPosition.x+mDragView.getMeasuredWidth(),
                initPointPosition.y+mDragView.getMeasuredHeight());
        mDragView2.layout(initPointPosition2.x,initPointPosition2.y,
                initPointPosition2.x+mDragView2.getMeasuredWidth(),
                initPointPosition2.y+mDragView2.getMeasuredHeight());

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mDragView2 = getChildAt(1);
    }
}
