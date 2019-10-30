package com.dqn.bottomSheet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;




/**
 * 交互的rv
 * 里面嵌套rv
 * 三段式
 */
public class MyInterRelativeLayout extends RelativeLayout {
    private final static String TAG = "MyInter";
    private RecyclerView rv;

    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mLastMotionY;
    private int mLastMotionX;

    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;

    private int peekHeight;
    private int middleOffestDis;
    private int animTime = 100;
    private boolean isIntercept;
    private MySwitchScrollLinearManager manager;
    private Handler mHandler = new Handler();

    private int yDirection;
    private int yDiff;
    private int xDiff;

    private int mStartY;
    private int mStartX;


    public MyInterRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyInterRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();

        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i) instanceof RecyclerView) {
                rv = (RecyclerView) getChildAt(i);
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (middleOffestDis <= 0 || peekHeight <= 0) {
            throw new IllegalArgumentException(" middleOffestDis&peekHeight 不能为0");
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mStartY = (int) ev.getY();
                mStartX = (int) ev.getX();

                final int y = (int) ev.getY();
                final int x = (int) ev.getX();
                mLastMotionY = y;
                mLastMotionX = x;
                mActivePointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:


                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + activePointerId
                            + " in onInterceptTouchEvent");
                    break;
                }

                final int y02 = (int) ev.getY(pointerIndex);
                final int x02 = (int) ev.getX(pointerIndex);
                yDirection = y02 - mLastMotionY;


                yDiff = Math.abs(y02 - mLastMotionY);
                xDiff = Math.abs(x02 - mLastMotionX);
                /*if (yDiff > mTouchSlop && yDiff > xDiff) {

                    mLastMotionY = y02;
                    mLastMotionX = x02;
                    int translationY = (int) rv.getTranslationY();
                    int maxTranslationY = getMaxTranslationY();


//                    final ViewParent parent = getParent();
//                    if (parent != null) {
//                        parent.requestDisallowInterceptTouchEvent(true);
//                    }

                    MyLogUtils.e(TAG, "d yDiff =" + yDiff + ",yDirection =" + yDirection);
                    MyLogUtils.e(TAG, "d rv.getTranslationY() =" + rv.getTranslationY());
                    MyLogUtils.e(TAG, "d middleOffestDis =" + middleOffestDis);
                    MyLogUtils.e(TAG, "d getHeight() - peekHeight =" + maxTranslationY);
                    MyLogUtils.e(TAG, "d  rv.getTop()=" + rv.getTop());
                    if (yDirection < 0) {

                        MyLogUtils.e(TAG, "dispatchTouchEvent 向上");
                        //向上
                        if (translationY > 0) {
                            //向上走

                            //最底层
                            if (translationY == maxTranslationY) {
                                isIntercept = true;
                                manager.setCanVerticalScroll(false);
                                startAnim(middleOffestDis);
                            }
                            //中间

                            if (translationY == middleOffestDis) {
                                isIntercept = true;
                                manager.setCanVerticalScroll(false);
                                startAnim(0);
                            }


                        }
                    } else {
                        //向下
                        MyLogUtils.e(TAG, "dispatchTouchEvent 向下");

                        if (manager.findFirstVisibleItemPosition() != 0) {
                            break;
                        }

                        if (manager.findFirstCompletelyVisibleItemPosition() != 0) {
                            break;
                        }
                        if (translationY <= 0) {
                            //最顶部
                            isIntercept = true;
                            manager.setCanVerticalScroll(false);
                            startAnim(middleOffestDis);
                        } else if (translationY > 0) {
                            //最底部
                            if (translationY == maxTranslationY) {
                                isIntercept = true;
                                manager.setCanVerticalScroll(false);
                            }
                            //中间

                            if (translationY == middleOffestDis) {

                                isIntercept = true;
                                manager.setCanVerticalScroll(false);
                                startAnim(maxTranslationY);
                            }
                        }
                    }


                    //;

                }*/


                if (yDiff > xDiff) {

                    mLastMotionY = y02;
                    mLastMotionX = x02;
                    int translationY = (int) rv.getTranslationY();
                    int maxTranslationY = getMaxTranslationY();
                    getLayoutManager();

                    //动画位置
                    float tranDiff = rv.getTranslationY() + yDirection;
                    if (yDirection < 0) {

                        //MyLogUtils.e(TAG, "dispatchTouchEvent 向上");
                        //向上
                        if (translationY > 0) {
                            //向上走
                            isIntercept = true;

                            //极限判断
                            if (tranDiff <= 0) {
                                rv.setTranslationY(0);
                            } else {
                                rv.setTranslationY(tranDiff);
                            }

                            manager.setCanVerticalScroll(false);
                            scrollFristPosition();

                        } else {

                            isIntercept = false;
                            manager.setCanVerticalScroll(true);

                        }
                    } else {
                        //向下
                        //MyLogUtils.e(TAG, "dispatchTouchEvent 向下");

                        if (manager.findFirstVisibleItemPosition() != 0) {
                            break;
                        }

                        if (manager.findFirstCompletelyVisibleItemPosition() != 0) {
                            break;
                        }
                        if (translationY < maxTranslationY) {
                            //最顶部
                            isIntercept = true;
                            manager.setCanVerticalScroll(false);

                            //极限判断
                            if (tranDiff >= maxTranslationY) {
                                rv.setTranslationY(maxTranslationY);
                            } else {
                                rv.setTranslationY(tranDiff);
                            }

                        } else {

                            isIntercept = false;
                            manager.setCanVerticalScroll(true);
                        }
                    }
                }


                break;

            case MotionEvent.ACTION_UP:

                mActivePointerId = INVALID_POINTER;
                isIntercept = false;

                MyLogUtils.e(TAG, "dispatchTouchEvent xDiff="+xDiff);
                MyLogUtils.e(TAG, "dispatchTouchEvent yDiff="+yDiff);
                MyLogUtils.e(TAG, "dispatchTouchEvent mTouchSlop="+mTouchSlop);

                //防止太灵敏
                if (xDiff > yDiff && xDiff > 100) {

                    if(yDirection<0){
                        MyLogUtils.e(TAG, "dispatchTouchEvent up 向上");
                        movePosition();
                    }else {
                        MyLogUtils.e(TAG, "dispatchTouchEvent up 向下");
                    }

                    break;
                }else {
                    movePosition();
                }


                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 移动到指定位置
     */
    private void movePosition() {

        int translationY = (int) rv.getTranslationY();
        int maxTranslationY = getMaxTranslationY();

        if (yDirection < 0) {

            MyLogUtils.e(TAG, "d up 向上");
            //向上
            if (translationY > 0) {
                //向上走
                if (translationY == maxTranslationY) {

                }
                //最底层
                if (translationY < maxTranslationY && translationY > middleOffestDis) {
                    isIntercept = true;
                    manager.setCanVerticalScroll(false);
                    startAnim(middleOffestDis);
                }
                //中间
                if (translationY == middleOffestDis) {

                }

                if (translationY < middleOffestDis) {
                    isIntercept = true;
                    manager.setCanVerticalScroll(false);
                    startAnim(0);
                }


            }
        } else {
            //向下
            //MyLogUtils.e(TAG, "d up 向下");

            if (manager.findFirstVisibleItemPosition() != 0) {
                manager.setCanVerticalScroll(true);
                return;
            }

            if (manager.findFirstCompletelyVisibleItemPosition() != 0) {
                manager.setCanVerticalScroll(true);
                return;
            }

            if (translationY == maxTranslationY) {

            }
            //最底层
            if (translationY < maxTranslationY && translationY > middleOffestDis) {
                isIntercept = true;
                manager.setCanVerticalScroll(false);
                startAnim(maxTranslationY);
            }
            //中间
            if (translationY == middleOffestDis) {

            }

            if (translationY < middleOffestDis) {
                isIntercept = true;
                manager.setCanVerticalScroll(false);
                startAnim(middleOffestDis);
            }

        }
    }

    private int getMaxTranslationY() {
        return getHeight() - peekHeight;
    }

    /**
     * 设置动画
     *
     * @param maxTranslationY
     */
    private void startAnim(int maxTranslationY) {
        rv.animate()
                .translationY(maxTranslationY)
                .setDuration(animTime)
                .setListener(animListener)
                .start();
    }

    AnimatorListenerAdapter animListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            getLayoutManager();
            manager.setCanVerticalScroll(false);
            isIntercept = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            getLayoutManager();
            manager.setCanVerticalScroll(true);
            isIntercept = false;
        }
    };

    private MySwitchScrollLinearManager getLayoutManager() {

        if (manager != null) {
            return manager;
        }

        if (rv.getLayoutManager() instanceof MySwitchScrollLinearManager) {
            manager = (MySwitchScrollLinearManager) rv.getLayoutManager();
            return manager;
        } else {
            //new Throwable("使用 MySwitchScrollLinearManager");
            throw new IllegalArgumentException("使用 MySwitchScrollLinearManager");
        }
        //return (MySwitchScrollLinearManager) rv.getLayoutManager();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isIntercept) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setDis_m_ph(int middleOffestDis, int peekHeight) {
        this.middleOffestDis = middleOffestDis;
        this.peekHeight = peekHeight;

    }

    /**
     * 中间位置
     */
    public void restoreRvMiddle() {
        scrollFristPosition();
        startAnim(middleOffestDis);
    }

    /**
     * 中间位置
     */
    public void restoreRvBottom() {

        if (rv == null) {
            mHandler.postDelayed(() -> {
                restoreRvBottom();
            }, 300);

            return;
        }

        if (rv.getHeight() > 30) {
            startAnim(getMaxTranslationY());
        } else {
            rv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    startAnim(getMaxTranslationY());

                    rv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }

    }

    /**
     * 中间位置
     */
    public void initFirstPosition() {

        if (rv == null) {
            mHandler.postDelayed(() -> {
                restoreRvBottom();
            }, 100);

            return;
        }

        rv.animate()
                .translationY(getMaxTranslationY())
                .setDuration(0)
                .setListener(animListener)
                .start();

    }

    /**
     * The bottom sheet is expanded.
     * 完全展开的状态
     */
    public static final int STATE_EXPANDED = 3;

    /**
     * The bottom sheet is collapsed.
     * 折叠关闭状态
     */
    public static final int STATE_COLLAPSED = 4;

    /**
     * The bottom sheet is hidden.
     * 隐藏状态
     */
    public static final int STATE_HIDDEN = 5;


    /**
     * 中间状态
     */
    public static final int STATE_MIDDLE_01 = 6;

    public boolean isExpanded() {
        if (rv.getTranslationY() < 0) {

            return true;
        }

        getLayoutManager();
        if(manager.findFirstVisibleItemPosition()>0){
            return true;
        }

        return false;
    }

    public void scrollFristPosition() {
        rv.scrollToPosition(0);

        getLayoutManager();

        manager.scrollToPositionWithOffset(0, 0);
        smoothMoveToPosition(rv, 0);
    }


    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }

    }

}
