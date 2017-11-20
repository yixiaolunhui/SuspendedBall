package com.dl.draggable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.dalong.suspendlayout.R;

/**
 * 悬浮球父布局
 * Created by zhouweilong on 16/2/27.
 */
public class DraggableFrameLayout extends FrameLayout {

    private ViewDragHelper dragHelper;
    private int screenWidth;
    private int statusType = 0;//0左右   1左  2右
    private float showPercent = 1;

    private int finalLeft = -1;
    private int finalTop = -1;

    public DraggableFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public DraggableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DraggableFrameLayout);
        statusType = typedArray.getInt(R.styleable.DraggableFrameLayout_direction, 0);
        showPercent = typedArray.getFloat(R.styleable.DraggableFrameLayout_showPercent, 1);
        init();
    }

    private void init() {
        screenWidth = getScreenWidth(getContext());
        dragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                int viewWidth = releasedChild.getWidth();
                int viewHeight = releasedChild.getHeight();
                int curLeft = releasedChild.getLeft();
                int curTop = releasedChild.getTop();
                finalLeft = -(int) (viewWidth * (1 - showPercent));
                finalTop = curTop < 0 ? 0 : curTop;

                if ((curLeft + viewWidth / 2) > screenWidth / 2) {
                    finalLeft = screenWidth - (int) (viewWidth * showPercent);
                }

                if ((finalTop + viewHeight) > getHeight()) {
                    finalTop = getHeight() - viewHeight;
                }
                switch (statusType) {
                    case 0://左右
                        break;
                    case 1://左
                        finalLeft = -(int) (viewWidth * (1 - showPercent));
                        break;
                    case 2://右
                        finalLeft = screenWidth - (int) (viewWidth * showPercent);
                        break;
                }
                dragHelper.settleCapturedViewAt(finalLeft, finalTop);
                invalidate();
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (finalLeft == -1 && finalTop == -1) {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return isTouchChildView(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    private boolean isTouchChildView(MotionEvent ev) {
        Rect rect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            rect.set((int) view.getX(), (int) view.getY(), (int) view.getX() + view.getWidth(), (int) view.getY() + view.getHeight());
            if (rect.contains((int) ev.getX(), (int) ev.getY())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
